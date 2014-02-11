package com.jdao.action;

import java.util.List;

import org.junit.Test;

import com.jdao.base.DaoFactory;
import com.jdao.base.QueryDao;
import com.jdao.base.StoreModel;
import com.jdao.dao.Hstest;
import com.jdao.dbHandler.JdaoHandler;
import com.jdao.dbHandlerImpl.JdaoHandlerFactory;

/**
 * @Copyright 2012-2013 donnie(donnie4w@gmail.com)
 * @date 2013-6-22
 * @verion 1.0.6 1_0_6 版本的缓存功能测试
 */
public class ActionTest1_0_6 {
	static JdaoHandler jdao = JdaoHandlerFactory.getDBHandler4c3p0SingleTon();
	static {
		// 给每个dao(包括QueryDao查询类)注册不同的过滤器操作对象。
		DaoFactory.dataSourceRegister(Hstest.class, jdao);
		// 新功能:设置缓存 ：域名:test 过期时间:10000毫秒 存储模式:STRONG
		DaoFactory.getCache().setDomain("test").setExpire(10000).setStoreModel(StoreModel.STRONG).build();
	}

	@Test
	static void cacheTest() throws Exception {
		Hstest h = new Hstest().useCache("test");
		h.setLoggerOn(true);
		h.where(Hstest.ID.EQ(6412));
		h = h.queryById();
		System.out.println(h.getId() + " " + h.getRowname() + " " + h.getValue());
	}

	@Test
	static void cacheTest2() throws Exception {
		Hstest hstest = new Hstest().useCache("test");
		hstest.setLoggerOn(true);
		hstest.where(Hstest.ID.IN(6412, 6413, 6414));
		List<Hstest> list = hstest.query();
		for (Hstest h : list) {
			System.out.println(h.getId() + " " + h.getRowname() + " " + h.getValue());
		}
	}

	@Test
	static void cacheTest3() throws Exception {
		Hstest hstest = new Hstest().useCache("test");
		// hstest.setLoggerOn(true);
		hstest.where(Hstest.ID.IN(6412, 6413, 6414, 6415));
		QueryDao qd = hstest.query(Hstest.ID.count());
		System.out.println("size=" + qd.size());

		// 这里要注意：由于用了缓存，过期前每次都返回同一个QueryDao对象，而在while中由于调用了next(),
		// 使得游标每次都下移，所以在之后调用next会出现问题，甚至没有数据。这里调用了flip()，让游标回复位
		qd.flip();

		while (qd.hasNext()) {
			System.out.println("count()=" + qd.next().fieldValue(1));
		}
	}

	@Test
	static void cacheTest4() throws Exception {
		Hstest hstest = new Hstest().useCache("test");
		List<Hstest> list = hstest.query();
		for (Hstest h : list) {
			System.out.println(h.getId() + " " + h.getRowname() + " " + h.getValue());
		}
	}

	public static void main(String[] args) throws Exception {
		for (int i = 1; i < 1000; i++) {
			// log中有打印[USE CACHE]时，表示使用了缓存
			System.out.println("i=" + i);
			if (i < 10) {
				ActionTest1_0_6.cacheTest();
			}
			if (i > 10 && i < 20) {
				ActionTest1_0_6.cacheTest2();
			}
			if (i == 5) {
				// 删除域名即去除相应域名的所有缓存
				// 可以看到 i在[6,10]的log 中没有打印出USE CACHE
				DaoFactory.getCache().delDomain("test");
			}
			if (i == 10) {
				// 重新设置缓存时间，缓存模式
				DaoFactory.getCache().setDomain("test").setExpire(12000).setStoreModel(StoreModel.SOFT).build();
			}

			if (i == 15) {
				// 垃圾回收 ,会影响缓存模式WEAK，SOFT
				// 可以看到WEAK模式的缓存被回收，i在[16 ,20]中有不打印USE CACHE的log
				System.gc();
			}

			// Thread.sleep(2000);
			if (i > 20 && i < 30) {
				cacheTest3();
			}
			cacheTest4();
		}
	}

}
