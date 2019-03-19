package org.dante.saga.springboot.account.po;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "t_account")
@Data
public class AccountPO {
	@Id
	private Long id;
	private String userId;
	private BigDecimal amount;
//	private BigDecimal frozen;
}
