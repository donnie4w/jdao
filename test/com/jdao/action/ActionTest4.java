package com.jdao.action;

import org.junit.Test;
import com.jdao.base.Params;
import com.jdao.base.ProcedureCall;
import com.jdao.base.Type;

/**
 * @Copyright 2012-2013 donnie(donnie4w@gmail.com)
 * @date 2013-2-2
 * @verion 1.0.1 存储过程测试
 */
@Deprecated
public class ActionTest4 {

	@Test
	public void call1() throws Exception {
		// call proc_test2()
		new ProcedureCall(DataSourceTest.getByDruid().getConnection(), "proc_test2");
	}

	@Test
	public void call2() throws Exception {
		// call proc_test(?,?,?)
		ProcedureCall pc = new ProcedureCall(DataSourceTest.getByDruid().getConnection(), "proc_test", Params.IN(30), Params.OUT(Type.VARCHAR),
				Params.INOUT("wuxiaodong", Type.VARCHAR));

		System.out.println(pc.fieldIndex(2));
		System.out.println(pc.fieldIndex(3));
	}
	// Params.IN(30) 入参
	// Params.OUT(Type.VARCHAR) 出参
	// Params.INOUT("wuxiaodong", Type.VARCHAR) 出入参

}