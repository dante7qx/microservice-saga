package org.dante.saga.springboot.account.controller;

import org.dante.saga.springboot.account.dto.AccountDTO;
import org.dante.saga.springboot.account.service.IAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AccountController {
	
	@Autowired
	private IAccountService accountService;
	
	@PostMapping("/decrease_acmout/{userId}/{amount}")
	public AccountDTO decreaseAccount(@PathVariable String userId, @PathVariable double amount) {
		accountService.decreaseAmount(userId, amount);
		return accountService.fintByUserId(userId);
	}
	
}
