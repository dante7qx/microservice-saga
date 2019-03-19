package org.dante.saga.springboot.pay;

import java.math.BigDecimal;

import org.dante.saga.springboot.pay.client.OrderFeignClient;
import org.dante.saga.springboot.pay.dto.OrderDTO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpiritPayApplicationTests {
	
	@Autowired
	private RestTemplate restTemplate;
	@Autowired
	private OrderFeignClient orderFeignClient;
	
	@Test
	public void createOrder() {
		try {
			OrderDTO order = new OrderDTO();
			order.setOrderNo("1111");
			order.setCommodityCode("test_shangpin");
			order.setUserId("test");
			order.setTotalCount(222);
			order.setAmount(BigDecimal.valueOf(500));
			order.setStatus("test...");
			HttpEntity<OrderDTO> request = new HttpEntity<OrderDTO>(order);
			restTemplate.postForEntity("http://peer2:8803/create_order", request, OrderDTO.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void createOrderWithFeign() {
		try {
			OrderDTO order = new OrderDTO();
			order.setOrderNo("1111");
			order.setCommodityCode("test_shangpin");
			order.setUserId("test");
			order.setTotalCount(222);
			order.setAmount(BigDecimal.valueOf(500));
			order.setStatus("test...");
			Thread.sleep(5000L);
			orderFeignClient.createOrder(order);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void decreaseAccount() {
		try {
			restTemplate.postForEntity("http://peer2:8801/decrease_acmout/dante/400", null, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
