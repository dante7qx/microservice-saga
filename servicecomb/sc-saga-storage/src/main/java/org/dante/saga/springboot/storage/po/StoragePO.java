package org.dante.saga.springboot.storage.po;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "t_storage")
public class StoragePO {
	@Id
	private Long id;
	private String commodityCode;
	private String name;
	private int totalCount;
//	private int lockCount;
}
