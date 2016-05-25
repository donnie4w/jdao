package com.jdao.exception;

public class JdaoException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 0;
	private Throwable cause;

	/**
	 * Constructs a JSONException with an explanatory message.
	 * 
	 * @param message
	 *            Detail about the reason for the exception.
	 */
	public JdaoException(String message) {
		super(message);
	}

	public JdaoException(Throwable t) {
		super(t.getMessage());
		this.cause = t;
	}

	public Throwable getCause() {
		return this.cause;
	}
}
