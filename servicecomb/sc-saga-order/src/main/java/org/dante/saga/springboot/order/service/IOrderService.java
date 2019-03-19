package org.dante.saga.springboot.order.service;

import org.dante.saga.springboot.order.dto.OrderDTO;

public interface IOrderService {
	
	/**
	 * 创建订单
	 * 
	 * @param userId
	 * @param commodityCode
	 * @param totalCount
	 * @param amount
	 * @return
	 */
	public OrderDTO createOrder(String orderNo, String userId, String commodityCode, int totalCount, double amount);
	
}
