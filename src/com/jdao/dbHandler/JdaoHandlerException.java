package com.jdao.dbHandler;

import java.sql.SQLException;

/**
 * @Copyright 2012-2013 donnie(donnie4w@gmail.com)
 * @date 2013-1-10
 * @verion 1.0
 */
public class JdaoHandlerException extends SQLException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Construct a new runtime exception with <code>null</code> as its detail
	 * message.
	 */
	public JdaoHandlerException() {
		super();
	}

	/**
	 * Construct a new runtime exception with the specified detail message.
	 * 
	 * @param message
	 *            The detail message for this exception
	 */
	public JdaoHandlerException(String message) {

		this(message, null);

	}

	/**
	 * Construct a new runtime exception with the specified detail message and
	 * cause.
	 * 
	 * @param message
	 *            The detail message for this exception
	 * @param cause
	 *            The root cause for this exception
	 */
	public JdaoHandlerException(String message, Throwable cause) {

		super(message);
		this.cause = cause;

	}

	/**
	 * Construct a new runtime exception with the specified cause and a detail
	 * message of <code>(cause == null &#63; null : cause&#46;toString())</code>
	 * .
	 * 
	 * @param cause
	 *            The root cause for this exception
	 */
	public JdaoHandlerException(Throwable cause) {

		super((cause == null) ? (String) null : cause.toString());
		this.cause = cause;

	}

	// ----------------------------------------------------- Instance Variables

	/**
	 * The root cause of this exception
	 */
	protected Throwable cause = null;

	// --------------------------------------------------------- Public Methods

	/**
	 * Return the root cause of this exception (if any).
	 */
	public Throwable getCause() {

		return (this.cause);

	}

}
