package org.dante.saga.springboot.storage.controller;

import org.dante.saga.springboot.storage.dto.StorageDTO;
import org.dante.saga.springboot.storage.service.IStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StorageController {
	
	@Autowired
	private IStorageService storageService;

	/**
	 * 扣减库存
	 * 
	 * @param commodityCode
	 * @param count
	 * @return
	 */
	@PostMapping("/decrease_stock/{commodityCode}/{count}")
	public StorageDTO decreaseStock(@PathVariable String commodityCode, @PathVariable int count) {
		storageService.decreaseStock(commodityCode, count);
		return storageService.findByCommodityCode(commodityCode);
	}
	
	/**
	 * 扣减库存 - 模拟异常
	 * 
	 * @param commodityCode
	 * @param count
	 * @return
	 */
	@PostMapping("/decrease_stock_exception/{commodityCode}/{count}")
	public StorageDTO decreaseStockException(@PathVariable String commodityCode, @PathVariable int count) {
		storageService.decreaseStockException(commodityCode, count);
		return storageService.findByCommodityCode(commodityCode);
	}
	
	/**
	 * 扣减库存 - 模拟超时
	 * 
	 * @param commodityCode
	 * @param count
	 * @return
	 */
	@PostMapping("/decrease_stock_timeout/{commodityCode}/{count}")
	public StorageDTO decreaseStockTimeout(@PathVariable String commodityCode, @PathVariable int count) {
		storageService.decreaseStockTimeout(commodityCode, count);
		return storageService.findByCommodityCode(commodityCode);
	}
	
}
