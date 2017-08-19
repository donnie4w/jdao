### jdao是一个java的轻量级orm工具包，根据表名可以生成与之对应的dao类，同时也支持原生sql语句操作.


**jdao 初始化：**
		JdaoHandler jdaohandler = JdaoHandlerFactory.getJdaoHandler(getDataSource());  
		DaoFactory.setJdaoHandler(jdaohandler);  
		以上两步便完成 jdao初始化
		
		<p/>
		getDataSource()获取数据源方法：
		如：ActionTest1_1_2.java 中：
		public static DataSource getDataSource() throws Exception {
			Properties p = new Properties();
			p.load(ActionTest1_1_2.class.getClassLoader().getResourceAsStream("druid.properties"));
		return DruidDataSourceFactory.createDataSource(p);}
		 

***
	
例如：对 数据库表名为 hstest的操作 
	CREATE TABLE `hstest` (
   		`id` int(10) DEFAULT NULL,
   		`value` varchar(50) DEFAULT '',
   		`rowname` varchar(50) DEFAULT ''
    )

**一.生成dao对象,生成Hstest.java**
	public void createDao() throws Exception {
		Class.forName("com.mysql.jdbc.Driver");
		String driverUrl = "jdbc:mysql://127.0.0.1:3306/test";
		String path = System.getProperty("user.dir") + "\\test\\com\\jdao\\action";
		CreateDaoUtil.createFile("com.jdao.action", "hstest",path,DriverManager.getConnection(driverUrl, "root", "123456"), "utf-8");
		//com.jdao.action 为 Hstest的包名
		//hstest为表名
	}

**二.对Hstest对象的操作**
**查询**SQL: select value,rowname from hstest where id between 2 and 10;
jdao对象操作如下：
Hstest t = new Hstest();
t.where(Hstest.ID.BETWEEN(2, 10));
t.query(Hstest.VALUE, Hstest.ROWNAME);

**插入**SQL:  insert into hstest (id,rowname,value) values(1,"donnie","wuxiaodong")
jdao对象操作如下：
Hstest t = new Hstest();
t.setId(1);
t.setRowname("donnie");
t.setValue("wuxiaodong");
t.save();

**批量插入**SQL:  insert into hstest (id,rowname,value) values(1,"donnie1","wuxiaodong1"),(2,"donnie2","wuxiaodong2"),(3,"donnie3","wuxiaodong3")
jdao对象操作如下：
Hstest t = new Hstest();
t.setId(1);
t.setRowname("donnie1");
t.setValue("wuxiaodong1");
t.addBatch();
t.setId(2);
t.setRowname("donnie2");
t.setValue("wuxiaodong2");
t.addBatch();
t.setId(3);
t.setRowname("donnie3");
t.setValue("wuxiaodong3");
t.addBatch();
t.batchForSave();

**更新**SQL:  update hstest set rowname="wuxiaodong",value="wuxiaodong" where id=10
jdao对象操作如下：
Hstest t = new Hstest();
t.setRowname("wuxiaodong");
t.setValue("wuxiaodong");
t.where(Hstest.ID.EQ(10));
t.update();

**删除**SQL:  delete from hstest where id=2
jdao对象操作如下:
Hstest t = new Hstest();
t.where(Hstest.ID.EQ(2));
t.delete();


**三.复杂SQL查询,可使用QueryDao查询数据**
	QueryDao qd = new QueryDao("select id,rowname from hstest limit ?,?", 0, 10);
	//获取数据方式一
	while (qd.hasNext()) {
 	    QueryDao q = qd.next();
	    //获取字段方式一
	    System.out.println(q.fieldValue(1) + "   " + q.fieldValue(2));
	    //获取字段方式二
	    System.out.println(q.fieldValue("id") + "   " + q.fieldValue("rowname"));
	}
	//获取数据方式二
	for(QueryDao q:qd.queryDaoList()){
    	System.out.println(q.fieldValue(1) + "   " + q.fieldValue(2));
	}

**四.对增删改SQL的操作**
		JdaoHandler jdaohandler = JdaoHandlerFactory.getJdaoHandler(getDataSource());
		jdaohandler.executeUpdate("insert into hstest (id,rowname,value) values(1,\"donnie\",\"wuxiaodong\")");
		