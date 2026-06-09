package com.example.demo.project.utils;

import java.util.List;

import org.springframework.data.domain.Page;

public class PartialListUtil {

    public <T> PartialList<T> createPartialList(Page<T> page) {
        PartialList<T> partialList = new PartialList<>();
        partialList.setData(page.toList());
        partialList.setRecordsTotal(page.getTotalElements());
        return partialList;
    }
    
    public <T> PartialList<T> createPartialList(List<T> list,int count) {
        PartialList<T> partialList = new PartialList<>();
        partialList.setData(list);
        partialList.setRecordsTotal(count);
        return partialList;
    }
    
}