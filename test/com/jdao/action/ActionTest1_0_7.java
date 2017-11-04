package com.jdao.action;

import org.junit.Test;
import com.jdao.base.DaoFactory;
import com.jdao.base.StoreModel;

/**
 * @Copyright 2012-2013 donnie(donnie4w@gmail.com)
 * @verion 1.0.7 1_0_7 版本的缓存功能测试,增加缓存node节点，让操作粒度更细
 */
@Deprecated
public class ActionTest1_0_7 {
	static {
		DaoFactory.setDefaultDataSource(DataSourceTest.getByDruid());
		// 给每个dao(包括QueryDao查询类)注册不同的过滤器操作对象。
		// 新功能:设置缓存 ：域名:test 过期时间:10000毫秒 存储模式:STRONG
		DaoFactory.getCache().setDomain("test").setExpire(20000).setStoreModel(StoreModel.STRONG).build();
	}

	@Test
	static void cacheTest() throws Exception {
		String userid = "6412";
		Hstest h = new Hstest().useCache("test", userid);
		h.setLoggerOn(true);
		h.where(Hstest.VALUE.EQ("wuxiaodong"));
		h = h.queryById();
		System.out.println(h.getId() + " " + h.getRowname() + " " + h.getValue());
	}

	@Test
	static void delCacheTest(String domain, String node, Class<?> clazz) throws Exception {
		DaoFactory.getCache().delNode(domain, clazz, node);
	}

	@Test
	static void delCache2Test(String domain, String node) throws Exception {
		DaoFactory.getCache().delNode(domain, node);
	}

	public static void main(String[] args) throws Exception {
		String userid = "6412";
		for (int i = 0; i < 10; i++) {
			cacheTest();
		}
		// 删除指定 domain,node,class 的缓存,则hstest的缓存中，只有node=6412的缓存会被删除
		delCacheTest("test", userid, Hstest.class);

		// 删除指定 domain,node 的缓存
		// delCache2Test("test", userid);
	}
}
