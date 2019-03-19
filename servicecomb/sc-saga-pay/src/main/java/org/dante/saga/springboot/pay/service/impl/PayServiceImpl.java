package org.dante.saga.springboot.pay.service.impl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.apache.servicecomb.pack.omega.context.annotations.SagaStart;
import org.dante.saga.springboot.pay.client.AccountFeignClient;
import org.dante.saga.springboot.pay.client.OrderFeignClient;
import org.dante.saga.springboot.pay.client.StorageFeignClient;
import org.dante.saga.springboot.pay.dto.OrderDTO;
import org.dante.saga.springboot.pay.service.IPayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PayServiceImpl implements IPayService {

	@Autowired
	private OrderFeignClient orderFeignClient;
	@Autowired
	private AccountFeignClient accountFeignClient;
	@Autowired
	private StorageFeignClient storageFeignClient;
	@Autowired
	private RestTemplate restTemplate;

	@Override
	@SagaStart
	public void pay(OrderDTO order) {
		log.info("========> 用户购买支付 {},", order);

		// 创建订单
		order.setOrderNo("SAGA_".concat(DateTimeFormatter.ofPattern("yyyyMMddHHmmss").format(LocalDateTime.now())));
		orderFeignClient.createOrder(order);

		// 扣减账户
		accountFeignClient.decreaseAccount(order.getUserId(), order.getAmount().doubleValue());

		// 扣减库存
		storageFeignClient.decreaseStorage(order.getCommodityCode(), order.getTotalCount());
	}

	@Override
	@SagaStart
	public void payStockException(OrderDTO order) {
		log.info("========> 用户购买支付, 库存异常 {},", order);
		// 创建订单
		order.setOrderNo("SAGA_".concat(DateTimeFormatter.ofPattern("yyyyMMddHHmmss").format(LocalDateTime.now())));
		orderFeignClient.createOrder(order);

		// 扣减账户
		accountFeignClient.decreaseAccount(order.getUserId(), order.getAmount().doubleValue());

		// 扣减库存
		storageFeignClient.decreaseStorageException(order.getCommodityCode(), order.getTotalCount());
	}

	@Override
	@SagaStart
	public void payStockTimeout(OrderDTO order) {
		log.info("========> 用户购买支付, 交易超时 {},", order);

		// 创建订单
		order.setOrderNo("SAGA_".concat(DateTimeFormatter.ofPattern("yyyyMMddHHmmss").format(LocalDateTime.now())));
		orderFeignClient.createOrder(order);

		// 扣减账户
		accountFeignClient.decreaseAccount(order.getUserId(), order.getAmount().doubleValue());

		// 扣减库存
		storageFeignClient.decreaseStorageTimeout(order.getCommodityCode(), order.getTotalCount());
	}

	@Override
	@SagaStart
	public void payWithRestTemplate(OrderDTO order) {
		order.setOrderNo("SAGA_".concat(DateTimeFormatter.ofPattern("yyyyMMddHHmmss").format(LocalDateTime.now())));
		log.info("========> 用户购买支付 {},", order);

		// 创建订单
		HttpEntity<OrderDTO> request = new HttpEntity<OrderDTO>(order);
		restTemplate.postForEntity("http://peer2:8803/create_order", request, OrderDTO.class);
		
		// 扣减帐户
		restTemplate.postForEntity("http://peer2:8801/decrease_acmout/"+order.getUserId()+"/"+order.getAmount(), null, null);
		
		// 扣减库存
		restTemplate.postForEntity("http://peer2:8802/decrease_stock/"+order.getCommodityCode()+"/"+order.getTotalCount(), null, null);
	}

}
