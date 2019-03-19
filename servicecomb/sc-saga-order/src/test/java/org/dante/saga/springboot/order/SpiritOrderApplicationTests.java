package org.dante.saga.springboot.order;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.dante.saga.springboot.order.dao.OrderDAO;
import org.dante.saga.springboot.order.po.OrderPO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class SpiritOrderApplicationTests {

	@Autowired
	private OrderDAO orderDAO;
	
	@Test
	@Transactional
	public void createOrder() {
		OrderPO order = new OrderPO();
		order.setUserId("dante");
		order.setCommodityCode("C201901140001");
		order.setAmount(BigDecimal.valueOf(300));
		order.setTotalCount(50);
		order.setOrderNo("BTX_".concat(DateTimeFormatter.ofPattern("yyyyMMddHHmmss").format(LocalDateTime.now())));
		OrderPO po = orderDAO.save(order);
		log.info("订单 ==> {} -- {}", order, po);
	}

}
