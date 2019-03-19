package org.dante.saga.springboot.pay.client;

import org.dante.saga.springboot.pay.dto.OrderDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient("sc-saga-order")
public interface OrderFeignClient {

	@RequestMapping(value = "/create_order", method = RequestMethod.POST)
	public void createOrder(@RequestBody OrderDTO orderDTO);

}
