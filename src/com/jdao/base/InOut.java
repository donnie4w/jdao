package com.jdao.base;

/**
 * @Copyright 2012-2013 donnie(donnie4w@gmail.com)
 * @date 2013-2-2
 * @verion 1.0.1 出入参
 */
public class InOut extends com.jdao.base.Params {
	private Object o;
	private Type t;

	public InOut(Object o, Type t) {
		this.o = o;
		this.t = t;
	}

	public Object getValue() {
		return o;
	}

	public int getTypes() {
		return Out.types2java(t);
	}
}
