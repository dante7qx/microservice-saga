package org.dante.saga.springboot.account.service.impl;

import java.math.BigDecimal;

import org.apache.servicecomb.pack.omega.transaction.annotations.Compensable;
import org.dante.saga.springboot.account.dao.AccountDAO;
import org.dante.saga.springboot.account.dto.AccountDTO;
import org.dante.saga.springboot.account.po.AccountPO;
import org.dante.saga.springboot.account.service.IAccountService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional(readOnly = true)
public class AccountServiceImpl implements IAccountService {

	@Autowired
	private AccountDAO accountDAO;

	@Override
	@Compensable(compensationMethod = "cancelDecreaseAmount")
	@Transactional
	public void decreaseAmount(String userId, double amount) {
		log.info("========> [ 账户扣减余额操作开始 ]");
		AccountPO accountPO = accountDAO.findByUserId(userId);
		if (accountPO != null && accountPO.getAmount().doubleValue() >= amount) {
			accountPO.setAmount(accountPO.getAmount().subtract(BigDecimal.valueOf(amount)));
			accountDAO.save(accountPO);
		} else {
			throw new RuntimeException("帐户余额不足，请充值后再购买！");
		}
	}

	/**
	 * 扣减帐户补偿操作
	 * 
	 * @param userId
	 * @param amount
	 */
	@Transactional
	public void cancelDecreaseAmount(String userId, double amount) {
		log.info("[ 账户扣减余额补偿操作开始 {} - {}] <========", userId, amount);
		AccountPO accountPO = accountDAO.findByUserId(userId);
		if (accountPO != null) {
			accountPO.setAmount(accountPO.getAmount().add(BigDecimal.valueOf(amount)));
			accountDAO.save(accountPO);
		} else {
			throw new RuntimeException(
					"扣减帐户余额补偿操作失败，可记录日志进行重试，Saga框架本身提供支持！[".concat(userId).concat(",").concat(amount + "]"));
		}
	}

	@Override
	public AccountDTO fintByUserId(String userId) {
		AccountPO accountPO = accountDAO.findByUserId(userId);
		AccountDTO accountDTO = new AccountDTO();
		BeanUtils.copyProperties(accountPO, accountDTO);
		return accountDTO;
	}

}
