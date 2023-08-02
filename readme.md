### Jdao  基于Java的持久层框架，零配置，零SQL操作持久层

------------

- 用最简单的方式操作数据库
- 支持全对象方式读写数据；
- 支持事务，缓存，批处理等操作；
- 支持原生sql 操作;
- 支持注册多数据源，设置对象，类，包名对应不同数据源；
- 支持自动生成数据表对应的java Bean对象；
- jdao使用非常简单方便，可用于快速构建持久层封装服务；

------------

# 基本使用

#### Jdao 数据表对应的javaBean：

	import io.github.donnie4w.jdao.base.DefName;
	import io.github.donnie4w.jdao.base.Table;
	import io.github.donnie4w.jdao.type.*;
	 public class Hstest2 extends Table<Hstest2> {
			public LONG id;
			public STRING name;
			public SHORT age;
			public DATE createtime;
			public DOUBLE money;
			public BINARY binary;
			public FLOAT real;
			//当变量(或类名)与表不对应时，用注解DefName修正
			@DefName(name = "level") 
			public INT level2;
	}

#### 与数据表 hstest2  表名列名一一对应

| Field  | Type  |   |
| ------------ | ------------ | ------------ |
| id  | bigint  | NOT NULL AUTO_INCREMENT  |
|  name | varchar(100)  |  NULL |
|  age |  tinyint | NULL  |
|  createtime | timestamp  |  NULL|
|  money |  double | NULL  |
|  binary |  binary(100) |  NULL |
|  real |  float | NULL  |
|  level |  int | NULL  |

####  hstest2 可直接操作数据：
	
	设置数据源(一切操作从设置数据源开始)
	这里使用druid 演示：
	Properties p = new Properties()
	p.load(new FileReader("druid.properties"));
	DataSource ds = DruidDataSourceFactory.createDataSource(p);
	DaoFactory.registDefaultDataSource(ds);
	以上完成jdao 数据源的设置操作，该数据源默认适用全部持久层对象

##### 插入数据
	Hstest2 h = new Hstest2();
	h.money.setValue(11.2);
	h.age.setValue((short) 22);
	h.name.setValue("tom");
	h.binary.setValue("hello".getBytes(StandardCharsets.UTF_8));
	h.createtime.setValue(new Date());
	h.real.setValue(33.3f);
	h.level2.setValue(33);
	h.insert();
	以上完成插入数据的操作

##### 查询数据

	Hstest2 h = new Hstest2();
	h.where(h.id.GE(1),h.name.LIKE("tom"));
	List<Hstest2> list = h.select(h.id,h.name);
	for (Hstest2 ht : list) {
				System.out.println(ht.id.getValue());
	}
	以上对应查询sql：
	select id,name from hstest2 where `id`>=1 and `name` like %'tom'%

##### 事务操作
	Transaction t = new Transaction(DataSourceTest.getDataSourceByDruid());
	Hstest hstest = new Hstest();
	hstest.setTransaction(t);  //使用事务t
	hstest.rowname.setValue("wu");
	hstest.value.setValue("dong");
	hstest.insert();
	Hstest hstest2 = new Hstest();
	hstest2.setTransaction(t); //使用事务t
	hstest2.rowname.setValue("wu2");
	hstest2.value.setValue("dong2");
	hstest2.insert();
	t.commit();   //提交
	//t.rollBack(); //回滚
	//t.close();    //关闭事务

##### 批处理

	Hstest ht = new Hstest();
	ht.rowname.setValue("1111");
	ht.value.setValue("2222");
	ht.addBatch();  //加入批处理
	ht.rowname.setValue("3333");
	ht.value.setValue("4444");
	ht.addBatch(); //加入批处理
	ht.endBatch(); //批处理结束并执行

##### java 支持对象对数据库 增删改查的全部操作，返回相应的对象
##### 数据表操作全部映射为简单的对象操作

------------


#### 复杂的SQL操作，jdao支持原生sql操作，使用DBUtil
	第一步，设置数据源，如上面数据源设置即可
	例如：
	DBUtil dt = new DBUtil()
	int id = dt.execute("insert into hstest(`value`,`rowname`)values(?,?)", "11", "aa");
	增删改 使用 execute方法
	————————————————————————
	DBUtil dt = new DBUtil()
	dt.selects("select * from hstest where id>?", 0)
	List<DBUtil> list = dt.rsList();
	for (DBUtil r : list) {
	      System.out.println(r.getString("value"));
	}
	查询数据使用 select,  返回一行使用selectSingle
	


------------

##### maven 配置，jdao本身无其他的依赖

			<dependency>
				<groupId>io.github.donnie4w</groupId>
				<artifactId>jdao</artifactId>
				<version>2.0.0</version>
			</dependency>

------------



[jdao项目地址：https://github.com/donnie4w/jdao](https://github.com/donnie4w/jdao "jdao项目地址：https://github.com/donnie4w/jdao")

[jdao使用demo地址：https://github.com/donnie4w/jdaodemo](https://github.com/donnie4w/jdaodemo "jdao使用demo地址：https://github.com/donnie4w/jdaodemo")


------------














