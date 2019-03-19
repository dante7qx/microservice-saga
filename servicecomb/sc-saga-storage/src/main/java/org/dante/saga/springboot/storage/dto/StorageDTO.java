package org.dante.saga.springboot.storage.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class StorageDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Long id;
	private String commodityCode;
	private String name;
	private int totalCount;
	private int lockCount;
}
