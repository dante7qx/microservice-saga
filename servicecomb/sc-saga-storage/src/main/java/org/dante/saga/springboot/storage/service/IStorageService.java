package org.dante.saga.springboot.storage.service;

import org.dante.saga.springboot.storage.dto.StorageDTO;

public interface IStorageService {
	
	/**
	 * 扣减库存
	 * 
	 * @param commodity
	 * @param count
	 */
	public void decreaseStock(String commodityCode, int count);
	
	/**
	 * 扣减库存 - 模拟异常
	 * 
	 * @param commodityCode
	 * @param count
	 */
	public void decreaseStockException(String commodityCode, int count);
	
	/**
	 * 扣减库存 - 模拟超时
	 * 
	 * @param commodityCode
	 * @param count
	 */
	public void decreaseStockTimeout(String commodityCode, int count);
	
	/**
	 * 根据商品码获取商品
	 * 
	 * @param commodityCode
	 * @return
	 */
	public StorageDTO findByCommodityCode(String commodityCode);
	
}
