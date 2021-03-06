package com.jdao.action;

import java.util.List;
import com.jdao.base.DaoFactory;
import com.jdao.base.DBUtils;

/**
 * @Copyright (c) 2017, donnie4w@gmail.com All Rights Reserved.
 * @Author: dong
 * @Desc: DBUtil 测试
 */
public class RsScanTest {

	static {
		try {
			DaoFactory.setDefaultDataSource(DataSourceTest.getByDruid());
			// 可以对不同DBUtils子类 设置不同的数据源
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void testInsert() throws Exception {
		RsTest rt = new RsTest();
		System.out.println(rt.execute("insert into hstest(`value`,`rowname`)values(?,?),(?,?) ", "wu1", "66", "wu2", "77"));
	}

	/**
	 * 针对分页封装的方法
	 * 
	 * @throws Exception
	 */
	public static void testSelectListPage() throws Exception {
		RsTest rt = new RsTest();
		// 分页查询方法
		rt.selectListPage(0, 20, "select * from hstest");
		System.out.println(rt.rsList().size());
		// selectListPage 会返回 totalcount
		List<RsTest> list = rt.rsList();
		for (RsTest r : list) {
			System.out.println(r.getString("value"));
		}
	}

	/**
	 * 单行返回
	 * 
	 * @throws Exception
	 */
	public static void testSelect() throws Exception {
		RsTest rt = new RsTest();
		rt.select("select * from hstest where id=?", 1);
		System.out.println(rt.getString("value"));
		rt.select("select * from hstest where id=?", 2);
		System.out.println(rt.getString("value"));
	}

	/**
	 * 多行返回
	 * 
	 * @throws Exception
	 */
	public static void testSelectList() throws Exception {
		RsTest rt = new RsTest();
		rt.selectList("select * from hstest where id>?", 0);
		System.out.println(rt.rsList().size());
		List<RsTest> list = rt.rsList();
		for (RsTest r : list) {
			System.out.println(r.getString("value"));
		}
	}

	/**
	 * 翻页多行返回
	 * 
	 * @throws Exception
	 */
	public static void testPageTurn() throws Exception {
		Hstest ht = new Hstest();
		ht.setPageTurn(true);
		ht.where(Hstest.ID.GE(0));
		List<Hstest> list = ht.query();
		System.out.println("totalcount:" + list.get(0).getTotalcount());
		for (Hstest h : list) {
			System.out.println(h.getRowname() + " " + h.getValue());
		}
	}

	public static void main(String[] args) throws Exception {
		testSelect();
		System.out.println("=================>1");
		testSelectList();
		System.out.println("=================>2");
		testPageTurn();
	}
}

/**
 * @File:jdao: com.jdao :RsScanTest.java
 * @Copyright (c) 2017, donnie4w@gmail.com All Rights Reserved.
 * @Author: dong
 * @Desc: RsTest CRUD 操作类
 */
class RsTest extends DBUtils<RsTest> {
}