package com.bonc.util;

/**
 * 登陆异常管理器
 * @author zhijie.ma
 * @date 2017年9月8日
 *
 */
public class LoginException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public LoginException() {
		super();
	}

	public LoginException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
	}

	public LoginException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public LoginException(String arg0) {
		super(arg0);
	}

	public LoginException(Throwable arg0) {
		super(arg0);
	}

}
