package com.zhgw.model.vo;

import lombok.Data;
import java.util.List;

@Data
public class PageVO<T> {
    private List<T> records;
    private long total;
    private long size;
    private long current;

    public static <T> PageVO<T> build(List<T> records, long total, long size, long current) {
        PageVO<T> pageVO = new PageVO<>();
        pageVO.setRecords(records);
        pageVO.setTotal(total);
        pageVO.setSize(size);
        pageVO.setCurrent(current);
        return pageVO;
    }
} 