package org.dante.saga.springboot.pay.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient("sc-saga-account")
public interface AccountFeignClient {

	@RequestMapping(value = "/decrease_acmout/{userId}/{amount}", method = RequestMethod.POST)
	public void decreaseAccount(@PathVariable String userId, @PathVariable double amount);

}
