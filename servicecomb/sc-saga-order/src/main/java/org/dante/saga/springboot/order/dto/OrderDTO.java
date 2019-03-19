package org.dante.saga.springboot.order.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Data;

@Data
public class OrderDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private String orderNo;
	private String userId;
	private String commodityCode;
	private int totalCount;
	private BigDecimal amount;
	private String status;
}
