package com.qrmarketlist.market.core;


/**
 * <p>Class that extends {@link Exception}.
 * This should be throw when a business logic
 * error occurs or when a validation exception
 * occurs.</br>
 * The current database transaction will be
 * rolled back.</p>
 * 
 * {@link BusinessException}
 */
public class BusinessException extends Exception {

	private static final long serialVersionUID = 3785782248769803001L;
	private Object param;
	
	/**
	 * <p>Constructs a new exception with the 
	 * specified detail message.</p>
	 * <p>The detail message could be a key
	 * for the internacionalization bundle.
	 * 
	 * @see {@link Exception}</p>
	 */
	public BusinessException(String message){
		super(message);
	}
	
	/**
	 * <p>Constructs a new exception with the 
	 * specified detail message and throwable.
	 * <p>The detail message could be a key
	 * for the internacionalization bundle.
	 * 
	 * @see {@link Exception}</p>
	 */
	public BusinessException(String message, Throwable throwable){
		super(message, throwable);
	}
	
	/**
	 * <p>
	 * Constructs a new exception with the specified detail message and params
	 * for that message.
	 * <p>
	 * The detail message could be a key for the internacionalization bundle.
	 * 
	 * @see {@link Exception}
	 *      </p>
	 */
	public BusinessException(String message, Object param){
		super(message);
		this.param = param;
	}
	
	/**
	 * <p>Constructs a new exception with the 
	 * specified detail message with parameter 
	 * and throwable.
	 * <p>The detail message could be a key
	 * for the internacionalization bundle.
	 * 
	 * @see {@link Exception}</p>
	 */
	public BusinessException(String message, Object param, Throwable throwable){
		super(message, throwable);
		this.param = param;
	}
	
	public Object getParam() {
		return this.param;
	}
}
