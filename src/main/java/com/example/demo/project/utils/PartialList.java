package com.example.demo.project.utils;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PartialList<T> {
	
	public PartialList(List<T> data) {
		this.data = data;
	}
	
	private List<T> data;
    private long recordsTotal;
	
}
