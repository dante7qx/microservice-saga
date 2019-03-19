package org.dante.saga.springboot.storage.dao;

import org.dante.saga.springboot.storage.po.StoragePO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StorageDAO extends JpaRepository<StoragePO, Long> {
	
	public StoragePO findByCommodityCode(String commodityCode);
	
}
