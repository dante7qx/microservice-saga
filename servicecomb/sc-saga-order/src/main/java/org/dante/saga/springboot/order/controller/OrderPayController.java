package org.dante.saga.springboot.order.controller;

import org.dante.saga.springboot.order.dto.OrderDTO;
import org.dante.saga.springboot.order.service.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderPayController {

	@Autowired
	private IOrderService orderService;

	@PostMapping("/create_order")
	public OrderDTO pay(@RequestBody OrderDTO reqOrder) {
		OrderDTO orderDTO = orderService.createOrder(reqOrder.getOrderNo(), reqOrder.getUserId(), reqOrder.getCommodityCode(), reqOrder.getTotalCount(),
				reqOrder.getAmount().doubleValue());
		return orderDTO;
	}
	

}
