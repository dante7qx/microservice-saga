package org.dante.saga.springboot.order.service.impl;

import java.math.BigDecimal;

import org.apache.servicecomb.pack.omega.transaction.annotations.Compensable;
import org.dante.saga.springboot.order.dao.OrderDAO;
import org.dante.saga.springboot.order.dto.OrderDTO;
import org.dante.saga.springboot.order.enums.OrderStatusEnum;
import org.dante.saga.springboot.order.po.OrderPO;
import org.dante.saga.springboot.order.service.IOrderService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional(readOnly = true)
public class OrderServiceImpl implements IOrderService {

	@Autowired
	private OrderDAO orderDAO;

	@Override
	@Compensable(compensationMethod = "cancelCreateOrder")
	@Transactional
	public OrderDTO createOrder(String orderNo, String userId, String commodityCode, int totalCount, double amount) {
		OrderPO orderPO = buildOrder(orderNo, userId, commodityCode, totalCount, amount);
		orderPO.setStatus(OrderStatusEnum.SUCCESS.getCode());
		
		log.info("========> 创建订单开始 {}", orderPO);
		
		OrderPO order = orderDAO.save(orderPO);
		OrderDTO orderDTO = new OrderDTO();
		BeanUtils.copyProperties(order, orderDTO);
		
		return orderDTO;
	}

	@Transactional
	public void cancelCreateOrder(String orderNo, String userId, String commodityCode, int totalCount, double amount) {
		log.info("创建订单补偿操作开始 {} <========", orderNo);
		orderDAO.updateOrderStatus(orderNo, OrderStatusEnum.CANCEL.getCode());
	}


	/**
	 * 构造订单
	 * 
	 * @param userId
	 * @param commodityCode
	 * @param totalCount
	 * @param amount
	 * @return
	 */
	private OrderPO buildOrder(String orderNo, String userId, String commodityCode, int totalCount, double amount) {
		OrderPO order = new OrderPO();
		order.setOrderNo(orderNo);
		order.setUserId(userId);
		order.setCommodityCode(commodityCode);
		order.setTotalCount(totalCount);
		order.setAmount(BigDecimal.valueOf(amount));
		return order;
	}

}
