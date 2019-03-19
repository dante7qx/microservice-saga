package org.dante.saga.springboot.account.service;

import org.dante.saga.springboot.account.dto.AccountDTO;

public interface IAccountService {
	
	/**
	 * 扣款支付
	 * 
	 * @param userId
	 * @param amount
	 */
	public void decreaseAmount(String userId, double amount);
	
	public AccountDTO fintByUserId(String userId);
}
