package org.dante.saga.springboot.storage;

import org.dante.saga.springboot.storage.dao.StorageDAO;
import org.dante.saga.springboot.storage.po.StoragePO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class SpiritStorageApplicationTests {

	@Autowired
	private StorageDAO storageDAO;
	
	@Test
	public void findByCommodityCode() {
		StoragePO storage = storageDAO.findByCommodityCode("C201901140001");
		log.info("Commodity ==> {}", storage);
	}

}
