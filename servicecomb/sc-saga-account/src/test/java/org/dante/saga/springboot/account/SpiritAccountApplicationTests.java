package org.dante.saga.springboot.account;

import java.util.List;

import org.dante.saga.springboot.account.dao.AccountDAO;
import org.dante.saga.springboot.account.po.AccountPO;
import org.dante.saga.springboot.account.service.IAccountService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class SpiritAccountApplicationTests {
	
	@Autowired
	private AccountDAO accountDAO;
	
	@Autowired
	private IAccountService accountService;

	@Test
	public void loadAccount() {
		List<AccountPO> accounts = accountDAO.findAll();
		log.info("Accounts => {}", accounts);
	}
	
	@Test
	public void findByUserId() {
		AccountPO account =accountDAO.findByUserId("dante");
		log.info("Account => {}", account);
	}
	
	@Test
	public void decreaseAmount() {
		accountService.decreaseAmount("1", 50);
	}

}
