package org.dante.saga.springboot.pay.controller;

import org.dante.saga.springboot.pay.dto.OrderDTO;
import org.dante.saga.springboot.pay.service.IPayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class AccountPayController {

	@Autowired
	private IPayService payService;

	@PostMapping("/pay")
	public OrderDTO pay(@RequestBody OrderDTO order) {
		payService.pay(order);
		return order;
	}
	
	@PostMapping("/pay2")
	public OrderDTO pay2(@RequestBody OrderDTO order) {
		payService.payWithRestTemplate(order);
		return order;
	}

	@PostMapping("/pay_stock_exception")
	public OrderDTO payStockException(@RequestBody OrderDTO order) {
		payService.payStockException(order);
		return order;
	}

	@PostMapping("/pay_stock_timeout")
	public OrderDTO payStockTimeout(@RequestBody OrderDTO order) {
		payService.payStockTimeout(order);
		return order;
	}

}
