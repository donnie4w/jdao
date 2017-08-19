### jdao是一个java的轻量级orm工具包，根据表名可以生成与之对应的dao类，同时也支持原生sql语句操作.


**jdao 初始化：**
<br/>		JdaoHandler jdaohandler = JdaoHandlerFactory.getJdaoHandler(getDataSource());  
<br/>		DaoFactory.setJdaoHandler(jdaohandler);  
<br/>		以上两步便完成 jdao初始化
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
<br/>		String path = System.getProperty("user.dir") + "\\test\\com\\jdao\\action";
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
**三.复杂SQL查询,可使用QueryDao查询数据**
<br/>	QueryDao qd = new QueryDao("select id,rowname from hstest limit ?,?", 0, 10);
<br/>	//获取数据方式一
<br/>	while (qd.hasNext()) {
<br/> 	    QueryDao q = qd.next();
<br/>	    //获取字段方式一
<br/>	    System.out.println(q.fieldValue(1) + "   " + q.fieldValue(2));
<br/>	    //获取字段方式二
<br/>	    System.out.println(q.fieldValue("id") + "   " + q.fieldValue("rowname"));
<br/>	}
<br/>	//获取数据方式二
<br/>	for(QueryDao q:qd.queryDaoList()){
<br/>    	System.out.println(q.fieldValue(1) + "   " + q.fieldValue(2));
<br/>	}
<br/>
**四.对增删改SQL的操作**
<br/>		JdaoHandler jdaohandler = JdaoHandlerFactory.getJdaoHandler(getDataSource());
<br/>		jdaohandler.executeUpdate("insert into hstest (id,rowname,value) values(1,\"donnie\",\"wuxiaodong\")");
		