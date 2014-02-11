package com.jdao.base;

/**
 * @Copyright 2012-2013 donnie(donnie4w@gmail.com)
 * @date 2013-2-2
 * @verion 1.0.1
 */
public abstract class Params {

	public static In IN(Object o) {
		return new In(o);
	}

	public static Out OUT(Type t) {
		return new Out(t);
	}

	public static InOut INOUT(Object o, Type t) {
		return new InOut(o, t);
	}

	public abstract Object getValue();

	public abstract int getTypes();

}
