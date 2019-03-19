package org.dante.saga.springboot.pay.service;

import org.dante.saga.springboot.pay.dto.OrderDTO;

public interface IPayService {

	/**
	 * 支付、创建订单 - 使用 Feign
	 * 
	 * @param order
	 */
	public void pay(OrderDTO order);
	
	/**
	 * 支付、创建订单 - 使用 RestTemplate
	 * 
	 * @param order
	 */
	public void payWithRestTemplate(OrderDTO order);
	
	
	/**
	 * 支付、创建订单 (扣减库存异常)
	 * 
	 * @param order
	 */
	public void payStockException(OrderDTO order);
	
	/**
	 * 支付、创建订单 (扣减库存超时)
	 * 
	 * @param order
	 */
	public void payStockTimeout(OrderDTO order);

}
