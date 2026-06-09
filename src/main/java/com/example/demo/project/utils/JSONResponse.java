package com.example.demo.project.utils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JSONResponse <T> {

	private T data;
	private String message;
	private String rc;
	
}
