package org.dante.saga.springboot.order.enums;

public enum OrderStatusEnum {
	CREATING("CREATING", "创建中"),
	PAYING("PAYING", "支付中"),
	CANCEL("CANCEL", "取消"),
	SUCCESS("SUCCESS", "成功"),
	FAILURE("FAILURE", "失败");
	
	private String code;
	private String message;
	
	OrderStatusEnum(String code, String message) {
		this.code = code;
		this.message = message;
	}
	
	public String getCode() {
		return code;
	}
	
	public String getMessage() {
		return message;
	}
}
