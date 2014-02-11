package com.jdao.action;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.jdao.base.DaoFactory;
import com.jdao.base.QueryDao;
import com.jdao.base.StoreModel;
import com.jdao.dao.Hstest;
import com.jdao.dbHandler.JdaoHandler;
import com.jdao.dbHandlerImpl.JdaoHandlerFactory;

/**
 * @Copyright 2012-2013 donnie(donnie4w@gmail.com)
 * @date 2013-1-10
 * @verion 1.0 数据增删改查 测试
 */
public class ActionTest {
	JdaoHandler jdao = JdaoHandlerFactory.getDBHandler4spring();

	@Test
	public void query1() throws Exception {
		// select value,rowname from hstest
		// where id<=10 and id>=2 and rowname like '%wuxiaodong%' group by id
		// order by id asc,rowname desc
		// limit 1,10
		Hstest t = new Hstest();
		// t.setJdaoHandler(jdao);
		t.setJdaoHandler(JdaoHandlerFactory.getDBHandler4c3p0());
		t.where(Hstest.ID.LE(10), Hstest.ID.GE(2), Hstest.ROWNAME.LIKE("wuxiaodong"));
		t.group(Hstest.ID);
		t.sort(Hstest.ID.asc(), Hstest.ROWNAME.desc());
		t.limit(1, 10);
		t.query(Hstest.VALUE, Hstest.ROWNAME);
	}

	@Test
	public void query2() throws Exception {
		// select id,rowname from hstest
		// where id between 1 and 3000
		// group by value having count(id)>3
		// limit 1,10
		Hstest t = new Hstest();
		// t.setJdaoHandler(jdao);
		t.setJdaoHandler(JdaoHandlerFactory.getDBHandler4c3p0());
		t.where(Hstest.ID.BETWEEN(1, 3000));
		t.group(Hstest.VALUE);
		t.having(Hstest.ID.count().GT(3));
		t.limit(1, 10);
		List<Hstest> list = t.query(Hstest.ID, Hstest.ROWNAME);
		for (Hstest h : list) {
			System.out.println(h.getId());
			System.out.println(h.getRowname());
		}
	}

	@Test
	public void query3() throws Exception {
		// select id,count(id) idcount from hstest where id<=10 and id>=2 group by id

		Hstest t = new Hstest();
		// t.setJdaoHandler(jdao);
		t.setJdaoHandler(JdaoHandlerFactory.getDBHandler4c3p0());
		t.where(Hstest.ID.LE(10), Hstest.ID.GE(2));
		t.group(Hstest.ID);
		t.query(Hstest.ID, Hstest.ID.count().AS("idcount"));

		DaoFactory.getCache().setDomain("aaa").setExpire(10000).setStoreModel(StoreModel.STRONG).build();
		DaoFactory.getCache().delDomain("bbb");
		DaoFactory.getCache().clearAll();

		Hstest tt = new Hstest().useCache("aaa");
		tt.where(Hstest.ID.LE(10), Hstest.ID.GE(2));
		tt.group(Hstest.ID);
		tt.query(Hstest.ID, Hstest.ID.count().AS("idcount"));

		Hstest t2 = new Hstest().useCache("aaa");
		t2.where(Hstest.ID.LE(10), Hstest.ID.GE(2));
		t2.group(Hstest.ID);
		t2.query(Hstest.ID, Hstest.ID.count().AS("idcount"));
		
	}

	/**
	 * 聚合函数即queryForInt
	 * 
	 * @throws Exception
	 */
	@Test
	public void query31() throws Exception {
		// select count(id) from hstest
		// select sum(id) from hstest
		Hstest t = new Hstest();
		// t.setJdaoHandler(jdao);
		t.setJdaoHandler(JdaoHandlerFactory.getDBHandler4c3p0());
		System.out.println(t.queryForInt(Hstest.ID.count()));
		System.out.println(t.queryForInt(Hstest.ID.sum()));
	}

	@Test
	public void query4() throws Exception {
		// select count(id) as cun from hstest where id between 2 and 10;
		Hstest t = new Hstest();
		// t.setJdaoHandler(jdao);
		t.setJdaoHandler(JdaoHandlerFactory.getDBHandler4c3p0());
		t.where(Hstest.ID.BETWEEN(2, 10));
		t.query();
	}

	@Test
	public void query5() throws Exception {
		// select count(id) as cun from hstest where id between 2 and 10 or
		// id=20;
		Hstest t = new Hstest();
		// t.setJdaoHandler(jdao);
		t.setJdaoHandler(JdaoHandlerFactory.getDBHandler4c3p0());
		t.where(Hstest.ID.BETWEEN(2, 10).OR(Hstest.ID.EQ(20)));
		QueryDao qd = t.query(Hstest.ID.count().AS("cun"));
		System.out.println(qd.fieldValue("cun"));
		System.out.println(qd.fieldValue(1));
	}

	/**
	 * @throws Exception
	 */
	@Test
	public void query6() throws Exception {
		// select value,rowname from hstest where id<=10 and id>=2 or (id>100
		// and id <200)
		Hstest t = new Hstest();
		// t.setJdaoHandler(jdao);
		t.setJdaoHandler(JdaoHandlerFactory.getDBHandler4c3p0());
		t.where(Hstest.ID.LE(10).AND(Hstest.ID.GE(2).OR(Hstest.ID.GT(100), Hstest.ID.LT(200))));
		t.limit(0, 1);
		QueryDao qd = t.query(Hstest.ID.round(0).AS("idRound"), Hstest.ROWNAME);
		System.out.println(qd.fieldValue(1));
		System.out.println(qd.fieldValue(2));
	}

	/**
	 * 数据插入
	 * 
	 * @throws Exception
	 */
	@Test
	public void insert() throws Exception {
		// insert into hstest (id,rowname,value) values(?,?,?)
		Hstest t = new Hstest();
		// t.setJdaoHandler(jdao);
		t.setJdaoHandler(JdaoHandlerFactory.getDBHandler4c3p0());
		t.setId(1);
		t.setRowname("donnie");
		t.setValue("wuxiaodong");
		int updateCount = t.save();
		System.out.println("save===" + updateCount);
	}

	/**
	 * 批量插入
	 * 
	 * @throws Exception
	 */
	@Test
	public void insertBetch() throws Exception {
		// insert into hstest (id,rowname,value) values(?,?,?)
		Hstest t = new Hstest();
		// t.setJdaoHandler(jdao);
		t.setJdaoHandler(JdaoHandlerFactory.getDBHandler4c3p0());
		t.setId(1);
		t.setRowname("donnie1");
		t.setValue("wuxiaodong1");
		t.addBatch();
		t.setId(2);
		t.setRowname("donnie2");
		t.setValue("wuxiaodong2");
		t.addBatch();
		t.setId(2);
		t.setRowname("donnie3");
		t.setValue("wuxiaodong3");
		t.addBatch();
		t.setId(4);
		t.setRowname("donnie4");
		t.setValue("wuxiaodong4");
		t.addBatch();
		t.batchForSave();
	}

	/**
	 * 更新测试
	 * 
	 * @throws Exception
	 */
	@Test
	public void update() throws Exception {
		// update hstest set rowname=?,value=? where id=?
		Hstest t = new Hstest();
		// t.setJdaoHandler(jdao);
		t.setJdaoHandler(JdaoHandlerFactory.getDBHandler4c3p0());
		t.setRowname("wuxiaodong");
		t.setValue("wuxiaodong");
		t.where(Hstest.ID.EQ(10));
		int updateCount = t.update();
		System.out.println("update===" + updateCount);
	}

	/**
	 * 数据删除测试
	 * 
	 * @throws Exception
	 */
	@Test
	public void detele() throws Exception {
		// delete from hstest where id=?
		Hstest t = new Hstest();
		t.setLoggerOn(true);
		// t.setJdaoHandler(jdao);
		t.setJdaoHandler(JdaoHandlerFactory.getDBHandler4c3p0());
		t.where(Hstest.ID.EQ(1), Hstest.ROWNAME.EQ("donnie"));
		int updateCount = t.delete();
		System.out.println("delete===" + updateCount);
	}

	/**
	 * 针对复杂SQL，建议直接编程人员自己抒写sql QueryDao封装了查询结果集，方便数据读取
	 * 
	 * @throws Exception
	 */
	@Test
	public void QueryDao() throws Exception {

		// QueryDao qd = new QueryDao(jdao,
		// "select id,rowname,value from hstest limit ?,?", 0, 10);
		QueryDao qd = new QueryDao(JdaoHandlerFactory.getDBHandler4c3p0(), "select id,rowname,value from hstest limit ?,?", 0, 10);
		System.out.println(qd.fieldType("rowname"));
		System.out.println(qd.fieldValue("rowname"));
		System.out.println(qd.field2Int("id"));
		System.out.println(qd.field2String("rowname"));
		System.out.println(qd.field2BigDecimal("id"));
		System.out.println("qd.size===" + qd.size());
		System.out.println("qd.hasNext()===" + qd.hasNext());
		System.out.println("-------------------------------------------");
		while (qd.hasNext()) {
			QueryDao q = qd.next();
			System.out.println(q.fieldValue("id") + "   " + q.fieldValue("rowname") + "   " + q.fieldValue("value"));
		}

	}

	/**
	 * QueryDao接受Connection参数
	 * 
	 * @throws Exception
	 */
	@Test
	public void QueryDao2() throws Exception {
		Class.forName("com.mysql.jdbc.Driver");
		Connection conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/wuxiaodong", "root", "123456");
		String sql = "select id,rowname,value from hstest limit ?,?";
		QueryDao qd = new QueryDao(conn, sql, 0, 10);
		System.out.println(qd.fieldType("rowname"));
		System.out.println(qd.fieldValue("rowname"));
		System.out.println(qd.field2Int("id"));
		System.out.println(qd.field2String("rowname"));
		System.out.println(qd.field2BigDecimal("id"));
		System.out.println("qd.size===" + qd.size());
		System.out.println("qd.hasNext()===" + qd.hasNext());
		System.out.println("-------------------------------------------");
		while (qd.hasNext()) {
			QueryDao q = qd.next();
			System.out.println(q.fieldValue("id") + "   " + q.fieldValue("rowname") + "   " + q.fieldValue("value"));
		}
	}

	@Test
	public void QueryDao4Been() throws Exception {
		List<Hstest> list = QueryDao.queryForBeens(JdaoHandlerFactory.getDBHandler4c3p0(), Hstest.class,
				"select id,rowname,value from hstest limit ?,?", 0, 10);
		for (Hstest h : list) {
			System.out.println(h.getId() + " " + h.getRowname() + " " + h.getValue());
		}
	}

	@Test
	public void QueryDao4Map() throws Exception {
		List<Map<String, Object>> list = QueryDao.queryForMaps(JdaoHandlerFactory.getDBHandler4c3p0(),
				"select id,rowname,value from hstest limit ?,?", 0, 10);
		for (Map<String, Object> m : list) {
			System.out.println(m.get("id") + " " + m.get("rowname") + " " + m.get("value"));
		}
	}

	public static void main(String[] args) throws Exception {
		// JdaoHandler jdao = JdaoHandlerFactory.getDBHandler4spring();
		JdaoHandler jdao = JdaoHandlerFactory.getDBHandler4c3p0SingleTon();
		DaoFactory.jdaoMap.put(Hstest.class, jdao);
		// jdao.setAutoCommit(true);
		if (true) {
			Hstest tt = new Hstest();
			// tt.setLoggerOn(true);
			// 使用jdbcTemplate
			// tt.setJdaoHandler(jdao);

			// 使用 jdaoHandlerImpl 基于c3p0
			// tt.setJdaoHandler(JdaoHandlerFactory.getDBHandler4c3p0());

			tt.where(Hstest.ID.NOTIN(1, 100, 111), Hstest.VALUE.LIKE("wuxiaodong"));
			tt.group(Hstest.ID);
			tt.sort(Hstest.ID.desc(), Hstest.ROWNAME.desc());
			tt.limit(0, 10);
			List<Hstest> list = tt.query(Hstest.ID, Hstest.VALUE, Hstest.ROWNAME);
			for (Hstest h : list) {
				System.out.println(h.getId() + "   " + h.getValue() + "   " + h.getRowname());
			}
			Thread.sleep(100);
		}

		ActionTest at = new ActionTest();
		at.detele();

		// ActionTest at = new ActionTest();
		// System.out.println("QueryDao4Been");
		// at.QueryDao4Been();
		// System.out.println("QueryDao4Map");
		// at.QueryDao4Map();
	}
}