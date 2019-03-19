package org.dante.saga.springboot.account.dao;

import org.dante.saga.springboot.account.po.AccountPO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountDAO extends JpaRepository<AccountPO, Long> {
	
	public AccountPO findByUserId(String userId);
}
