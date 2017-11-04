package com.jdao.action;

import java.util.List;

import com.jdao.base.DaoFactory;
import com.jdao.base.PageDao;
import com.jdao.base.Transaction;
import com.jdao.dbHandler.JdaoHandler;

//生产dao类的各项测试
public class DaoTest {
	static {
		System.out.println("init");
		// DaoFactory.setJdaoHandler(jdao);
		DaoFactory.setDefaultDataSource(DataSourceTest.getByDruid());
	}

	public static void query() throws Exception {
		// select value,rowname from hstest
		// where id<=10 and id>=0 and rowname like '%wuxiaodong%' group by id
		// order by id asc,rowname desc
		// limit 1,10
		Hstest t = new Hstest();
		t.where(Hstest.ID.LE(10), Hstest.ID.GE(0), Hstest.ROWNAME.LIKE("wuxiaodong"));
		t.group(Hstest.ID);
		t.sort(Hstest.ID.asc(), Hstest.ROWNAME.desc());
		t.limit(1, 10);
		List<Hstest> list = t.query(Hstest.ID, Hstest.VALUE, Hstest.ROWNAME);
		for (Hstest h : list) {
			System.out.println(h.getRowname());
		}
	}

	public static void transaction() throws Exception {
		Transaction t = new Transaction(DataSourceTest.getByDruid());
		Hstest hstest = new Hstest();
		hstest.setTransaction(t);
		hstest.setRowname("wu");
		hstest.setValue("dong");
		hstest.save();
		Hstest hstest2 = new Hstest();
		hstest2.setTransaction(t);
		hstest2.setRowname("wu2");
		hstest2.setValue("dong2");
		hstest2.save();
		RsTest rt = new RsTest();
		rt.setTransaction(t);
		rt.execute("insert into hstest(`rowname`,`value`)values(?,?)", 1, 2);
		t.commit();
		t.rollBackAndClose();
	}

	/**
	 * 翻页多行返回
	 * 
	 * @throws Exception
	 */
	public static void testPageDao() throws Exception {
		Hstest ht = new Hstest();
		ht.where(Hstest.ID.GE(1));
		PageDao<Hstest> pd = ht.selectListPage();
		System.out.println("totalcount:" + pd.getTotalcount());
		List<Hstest> list = pd.getList();
		for (Hstest h : list) {
			System.out.println(h.getRowname() + " " + h.getValue());
		}
	}

	public static void main(String[] args) throws Exception {
		// query();
		// testPageDao();
		transaction();
	}

}
