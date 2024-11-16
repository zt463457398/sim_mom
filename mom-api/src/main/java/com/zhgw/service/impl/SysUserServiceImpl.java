package com.zhgw.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhgw.common.exception.BusinessException;
import com.zhgw.entity.SysUser;
import com.zhgw.mapper.SysUserMapper;
import com.zhgw.model.dto.ProfileUpdateDTO;
import com.zhgw.model.dto.UserDTO;
import com.zhgw.model.vo.UserInfoVO;
import com.zhgw.service.SysUserService;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

import com.zhgw.config.FileUploadConfig;

@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private FileUploadConfig fileUploadConfig;

    // 手机号正则表达式
    private static final Pattern PHONE_PATTERN = Pattern.compile("^1[3-9]\\d{9}$");
    
    // 邮箱正则表达式
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$");
    
    // 用户名正则表达式（字母开头，允许字母数字下划线，4-16位）
    private static final Pattern USERNAME_PATTERN = Pattern.compile("^[a-zA-Z][a-zA-Z0-9_]{3,15}$");

    @Override
    public SysUser findByUsername(String username) {
        return baseMapper.selectByUsername(username);
    }

    @Override
    public boolean checkUsernameExists(String username) {
        return this.count(new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getUsername, username)) > 0;
    }

    @Override
    public boolean checkPhoneExists(String phone, Long excludeUserId) {
        return this.count(new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getPhone, phone)
                .ne(excludeUserId != null, SysUser::getId, excludeUserId)) > 0;
    }

    @Override
    public boolean checkEmailExists(String email, Long excludeUserId) {
        return this.count(new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getEmail, email)
                .ne(excludeUserId != null, SysUser::getId, excludeUserId)) > 0;
    }

    @Override
    public void exportUser(List<SysUser> userList, HttpServletResponse response) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("用户数据");
            
            // 创建标题行
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("用户名");
            headerRow.createCell(1).setCellValue("真实姓名");
            headerRow.createCell(2).setCellValue("手机号");
            headerRow.createCell(3).setCellValue("邮箱");
            headerRow.createCell(4).setCellValue("状态");
            
            // 填充数据
            int rowNum = 1;
            for (SysUser user : userList) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(user.getUsername());
                row.createCell(1).setCellValue(user.getRealName());
                row.createCell(2).setCellValue(user.getPhone());
                row.createCell(3).setCellValue(user.getEmail());
                row.createCell(4).setCellValue(user.getStatus() == 1 ? "正常" : "停用");
            }
            
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment; filename=users.xlsx");
            workbook.write(response.getOutputStream());
        } catch (IOException e) {
            throw new BusinessException("导出用户数据失败");
        }
    }

    @Override
    public void importUser(MultipartFile file, boolean updateSupport) throws Exception {
        try (Workbook workbook = WorkbookFactory.create(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);
            List<SysUser> userList = new ArrayList<>();
            
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) {
                    continue;
                }
                
                // 获取单元格数据
                String username = getCellValue(row.getCell(0));
                String realName = getCellValue(row.getCell(1));
                String phone = getCellValue(row.getCell(2));
                String email = getCellValue(row.getCell(3));
                
                // 数据校验
                validateUsername(username);
                validatePhone(phone);
                validateEmail(email);
                
                SysUser user = new SysUser();
                user.setUsername(username);
                user.setRealName(realName);
                user.setPhone(phone);
                user.setEmail(email);
                user.setPassword(passwordEncoder.encode("123456")); // 默认密码
                user.setStatus(1); // 默认状态为正常
                
                // 检查用户名是否存在
                if (checkUsernameExists(username)) {
                    if (!updateSupport) {
                        throw new BusinessException(String.format("第%d行：用户名[%s]已存在", i + 1, username));
                    }
                    // 更新用户信息
                    SysUser existUser = findByUsername(username);
                    user.setId(existUser.getId());
                    this.updateById(user);
                } else {
                    userList.add(user);
                }
            }
            
            if (!userList.isEmpty()) {
                this.saveBatch(userList);
            }
        }
    }

    /**
     * 校验用户名
     */
    private void validateUsername(String username) {
        if (!StringUtils.hasText(username)) {
            throw new BusinessException("用户名不能为空");
        }
        if (!USERNAME_PATTERN.matcher(username).matches()) {
            throw new BusinessException("用户名必须以字母开头，只能包含字母、数字和下划线，长度4-16位");
        }
    }

    /**
     * 校验手机号
     */
    private void validatePhone(String phone) {
        if (StringUtils.hasText(phone) && !PHONE_PATTERN.matcher(phone).matches()) {
            throw new BusinessException("手机号格式不正确");
        }
    }

    /**
     * 校验邮箱
     */
    private void validateEmail(String email) {
        if (StringUtils.hasText(email) && !EMAIL_PATTERN.matcher(email).matches()) {
            throw new BusinessException("邮箱格式不正确");
        }
    }

    @Override
    public void exportTemplate(HttpServletResponse response) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("用户导入模板");
            
            // 创建标题行
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("用户名");
            headerRow.createCell(1).setCellValue("真实姓名");
            headerRow.createCell(2).setCellValue("手机号");
            headerRow.createCell(3).setCellValue("邮箱");
            
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment; filename=user_template.xlsx");
            workbook.write(response.getOutputStream());
        } catch (IOException e) {
            throw new BusinessException("导出模板失败");
        }
    }

    private String getCellValue(Cell cell) {
        if (cell == null) {
            return "";
        }
        
        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue();
            case NUMERIC -> {
                if (DateUtil.isCellDateFormatted(cell)) {
                    // 将Excel日期转换为LocalDateTime
                    yield LocalDateTime.ofInstant(cell.getDateCellValue().toInstant(), 
                            ZoneId.systemDefault()).toString();
                }
                // 处理数值，避免科学计数法
                double value = cell.getNumericCellValue();
                if (value == (long) value) {
                    yield String.valueOf((long) value);
                }
                yield String.valueOf(value);
            }
            case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());
            case FORMULA -> {
                try {
                    yield cell.getStringCellValue();
                } catch (IllegalStateException e) {
                    double value = cell.getNumericCellValue();
                    if (value == (long) value) {
                        yield String.valueOf((long) value);
                    }
                    yield String.valueOf(value);
                }
            }
            default -> "";
        };
    }

    @Override
    public UserInfoVO getCurrentUserInfo() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        SysUser user = this.findByUsername(username);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }

        UserInfoVO userInfo = new UserInfoVO();
        BeanUtils.copyProperties(user, userInfo);
        // TODO: 设置部门和角色信息
        return userInfo;
    }

    @Override
    public void updateProfile(ProfileUpdateDTO profileDTO) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        SysUser user = this.findByUsername(username);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }

        // 检查手机号是否被其他用户使用
        if (checkPhoneExists(profileDTO.getPhone(), user.getId())) {
            throw new BusinessException("手机号已被使用");
        }

        // 检查邮箱是否被其他用户使用
        if (checkEmailExists(profileDTO.getEmail(), user.getId())) {
            throw new BusinessException("邮箱已被使用");
        }

        user.setRealName(profileDTO.getRealName());
        user.setPhone(profileDTO.getPhone());
        user.setEmail(profileDTO.getEmail());
        this.updateById(user);
    }

    @Override
    public String uploadAvatar(MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || originalFilename.isEmpty()) {
            throw new BusinessException("文件名不能为空");
        }

        // 获取文件扩展名
        int lastDotIndex = originalFilename.lastIndexOf(".");
        String extension = (lastDotIndex > 0) ? 
            originalFilename.substring(lastDotIndex).toLowerCase() : "";

        // 生成新的文件名
        String fileName = UUID.randomUUID().toString() + extension;

        // 确保上传目录存在
        File uploadPath = new File(fileUploadConfig.getFormattedPath());
        if (!uploadPath.exists()) {
            if (!uploadPath.mkdirs()) {
                throw new BusinessException("创建上传目录失败");
            }
        }

        // 保存文件
        File destFile = new File(uploadPath, fileName);
        file.transferTo(destFile);

        // 更新用户头像
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        SysUser user = this.findByUsername(username);
        if (user != null) {
            // 删除旧头像
            if (StringUtils.hasLength(user.getAvatar())) {
                String oldFileName = user.getAvatar().replace(fileUploadConfig.getAccessUrl(), "");
                File oldAvatar = new File(uploadPath, oldFileName);
                if (oldAvatar.exists()) {
                    oldAvatar.delete();
                }
            }
            // 保存包含访问路径的文件名
            String avatarUrl = fileUploadConfig.getAccessUrl() + fileName;
            user.setAvatar(avatarUrl);
            this.updateById(user);
        }

        return fileUploadConfig.getAccessUrl() + fileName;
    }

    @Override
    public void updatePassword(String oldPassword, String newPassword) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        SysUser user = this.findByUsername(username);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }

        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new BusinessException("原密码错误");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        this.updateById(user);
    }

    @Override
    public Page<UserInfoVO> getUserPage(Integer pageNum, Integer pageSize, UserDTO userDTO) {
        Page<SysUser> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<SysUser>()
                .like(userDTO.getUsername() != null, SysUser::getUsername, userDTO.getUsername())
                .eq(userDTO.getStatus() != null, SysUser::getStatus, userDTO.getStatus())
                .eq(userDTO.getDeptId() != null, SysUser::getDeptId, userDTO.getDeptId())
                .orderByDesc(SysUser::getCreateTime);

        Page<SysUser> userPage = this.page(page, queryWrapper);
        
        // 转换为VO对象
        Page<UserInfoVO> voPage = new Page<>();
        BeanUtils.copyProperties(userPage, voPage, "records");
        
        List<UserInfoVO> voList = new ArrayList<>();
        for (SysUser user : userPage.getRecords()) {
            UserInfoVO vo = new UserInfoVO();
            BeanUtils.copyProperties(user, vo);
            voList.add(vo);
        }
        voPage.setRecords(voList);
        
        return voPage;
    }
} 