package com.fwcd.timetable.model.utils;

public class HttpException extends RuntimeException {
	private static final long serialVersionUID = -8333346330649063821L;
	
	public HttpException(String msg) { super(msg); }
	
	public HttpException(Throwable cause) { super(cause); }
	
	public HttpException(String msg, Throwable cause) { super(msg, cause); }
}
