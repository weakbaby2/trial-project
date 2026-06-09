package com.example.demo.project.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class PagingRequest {

	private Integer pageNo;
    private Integer pageSize;
    private String orderBy;
    private String directive;
    Map<String, Object> filterParam;
	public Date getDateValue(String dateString, String dateFormat) {
	    try {
	        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
	        return sdf.parse(dateString); 
	    } catch (Exception e) {
	        e.printStackTrace();
	        return null; 
	    }
	}
    
}
