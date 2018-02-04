package com.yuantek.myblog.vo;

/**
 * 统一处理返回对象 restful
 * 
 * @author mi.zhe
 *
 */
public class Response {

	private boolean success; ///** 响应处理是否成功 */
	private String message; // /** 响应处理的消息提示 */
	private Object body; // /** 响应处理的返回内容 */

	
	public Response(boolean success, String message) {
		super();
		this.success = success;
		this.message = message;
	}

	
	public Response(boolean success, String message, Object body) {
		super();
		this.success = success;
		this.message = message;
		this.body = body;
	}


	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Object getBody() {
		return body;
	}

	public void setBody(Object body) {
		this.body = body;
	}

}
