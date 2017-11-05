### jdao是一个java的轻量级orm工具包，根据表名可以生成与之对应的dao类，同时也支持原生sql语句操作.

**v1.1.6**
**jdao 初始化：**
<br/>		DaoFactory.setDefaultDataSource(getDataSource()); 
<br/>		jdao初始化 设置数据源，一步完成。
<br/>		

<br/>		getDataSource()获取数据源方法：
<br/>		如：ActionTest1_1_2.java 中：
<br/>		public static DataSource getDataSource() throws Exception {
<br/>			Properties p = new Properties();
<br/>			p.load(ActionTest1_1_2.class.getClassLoader().getResourceAsStream("druid.properties"));
<br/>		return DruidDataSourceFactory.createDataSource(p);}
		 

***
	
例如：对 数据库表名为 hstest的操作 
<br/>	CREATE TABLE `hstest` (
<br/>   		`id` int(10) DEFAULT NULL,
<br/>   		`value` varchar(50) DEFAULT '',
<br/>   		`rowname` varchar(50) DEFAULT ''
<br/>    )

**一.生成dao对象,生成Hstest.java**
<br/>	public void createDao() throws Exception {
<br/>		Class.forName("com.mysql.jdbc.Driver");
<br/>		String driverUrl = "jdbc:mysql://127.0.0.1:3306/test";
<br/>		String path = System.getProperty("user.dir") + "/test/com/jdao/action";
<br/>		CreateDaoUtil.createFile("com.jdao.action", "hstest",path,DriverManager.getConnection(driverUrl, "root", "123456"), "utf-8");
<br/>		//com.jdao.action 为 Hstest的包名
<br/>		//hstest为表名
<br/>	}

**二.对Hstest对象的操作**
**查询**SQL: select value,rowname from hstest where id between 2 and 10;
<br/>jdao对象操作如下：
<br/>Hstest t = new Hstest();
<br/>t.where(Hstest.ID.BETWEEN(2, 10));
<br/>t.query(Hstest.VALUE, Hstest.ROWNAME);

**插入**SQL:  insert into hstest (id,rowname,value) values(1,"donnie","wuxiaodong")
<br/>jdao对象操作如下：
<br/>Hstest t = new Hstest();
<br/>t.setId(1);
<br/>t.setRowname("donnie");
<br/>t.setValue("wuxiaodong");
<br/>t.save();

**批量插入**SQL:  insert into hstest (id,rowname,value) values(1,"donnie1","wuxiaodong1"),(2,"donnie2","wuxiaodong2"),(3,"donnie3","wuxiaodong3")
<br/>jdao对象操作如下：
<br/>Hstest t = new Hstest();
<br/>t.setId(1);
<br/>t.setRowname("donnie1");
<br/>t.setValue("wuxiaodong1");
<br/>t.addBatch();
<br/>t.setId(2);
<br/>t.setRowname("donnie2");
<br/>t.setValue("wuxiaodong2");
<br/>t.addBatch();
<br/>t.setId(3);
<br/>t.setRowname("donnie3");
<br/>t.setValue("wuxiaodong3");
<br/>t.addBatch();
<br/>t.batchForSave();

**更新**SQL:  update hstest set rowname="wuxiaodong",value="wuxiaodong" where id=10
<br/>jdao对象操作如下：
<br/>Hstest t = new Hstest();
<br/>t.setRowname("wuxiaodong");
<br/>t.setValue("wuxiaodong");
<br/>t.where(Hstest.ID.EQ(10));
<br/>t.update();
<br/>
**删除**SQL:  delete from hstest where id=2
<br/>jdao对象操作如下:
<br/>Hstest t = new Hstest();
<br/>t.where(Hstest.ID.EQ(2));
<br/>t.delete();
<br/>
**三.支持SQL操作 DBUtils**
<br/>		DBUtils<?> db=new DBUtils();
<br/>		db.select("select * from hstest where id=? limit 1",1);
<br/>		System.out.println(db.getString("value"));
<br/>		int i = db.select("insert into hstest(`rowname`,`value`)values(?,?)",1,2);
**四.自定义类继承 DBUtils**
<br/>  任何子类继承自DBUtils 都可以设置与其对应的数据源，同时支持sql编写，支持翻页
<br/>  如：class RsTest extends DBUtils<RsTest> {}
<br/>    //翻页
<br/>	public static void testSelectListPage() throws Exception {
<br/>		RsTest rt = new RsTest();
<br/>		// 分页查询方法
<br/>		rt.selectListPage(0, 20, "select * from hstest");
<br/>		System.out.println(rt.rsList().size());
<br/>		// selectListPage 会返回 totalcount
<br/>		List<RsTest> list = rt.rsList();
<br/>		for (RsTest r : list) {
<br/>			System.out.println(r.getString("value"));
<br/>		}
<br/>	}
<br/>
<br/>	//单行返回
<br/>	public static void testSelect() throws Exception {
<br/>		RsTest rt = new RsTest();
<br/>		rt.select("select * from hstest where id=?", 1);
<br/>		System.out.println(rt.getString("value"));
<br/>		rt.select("select * from hstest where id=?", 2);
<br/>		System.out.println(rt.getString("value"));
<br/>	}
<br/>
<br/>	//插入
<br/>	public static void testInsert() throws Exception {
<br/>		RsTest rt = new RsTest();
<br/>		System.out.println(rt.execute("insert into hstest(`value`,`rowname`)values(?,?),(?,?) ", "wu1", "11", "wu2", "22"));
<br/>	}
<br/>
<br/>	//生成dao 的翻页测试类
<br/>	public static void testPageTurn() throws Exception {
<br/>		Hstest ht = new Hstest();
<br/>		ht.setPageTurn(true);   //翻页
<br/>		ht.where(Hstest.ID.GE(0));
<br/>		List<Hstest> list = ht.query();
<br/>		System.out.println("totalcount:" + list.get(0).getTotalcount());
<br/>		for (Hstest h : list) {
<br/>			System.out.println(h.getRowname() + " " + h.getValue());
<br/>		}
<br/>	}
<br/>
<br/>	//PageDao类测试
<br/>	public static void testPageDao() throws Exception {
<br/>		Hstest ht = new Hstest();
<br/>		ht.where(Hstest.ID.GE(1));
<br/>		PageDao<Hstest> pd = ht.selectListPage();
<br/>		System.out.println("totalcount:" + pd.getTotalcount());
<br/>		List<Hstest> list = pd.getList();
<br/>		for (Hstest h : list) {
<br/>			System.out.println(h.getRowname() + " " + h.getValue());
<br/>		}
<br/>	}
**五.事务**
<br/>		Transaction t = new Transaction(DataSourceTest.getByDruid());
<br/>		Hstest hstest = new Hstest();
<br/>		hstest.setTransaction(t);
<br/>		hstest.setRowname("wu");
<br/>		hstest.setValue("dong");
<br/>		hstest.save();
<br/>		Hstest hstest2 = new Hstest();
<br/>		hstest2.setTransaction(t);
<br/>		hstest2.setRowname("wu2");
<br/>		hstest2.setValue("dong2");
<br/>		hstest2.save();
<br/>		DBUtils rt = new DBUtils();
<br/>		rt.setTransaction(t);
<br/>		rt.execute("insert into hstest(`rowname`,`value`)values(?,?)", 1, 2);
<br/>		t.rollBackAndClose();