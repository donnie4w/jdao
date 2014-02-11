  jdao是一个java的轻量级orm工具包，通过表名便可以自动生成与之对应的dao类。

一、使用DAO方式操作数据：

查询SQL: select value,rowname from hstest where id between 2 and 10;
jdao对象操作如下：
Hstest t = new Hstest();
t.where(Hstest.ID.BETWEEN(2, 10));
t.query(Hstest.VALUE, Hstest.ROWNAME);

插入SQL:  insert into hstest (id,rowname,value) values(1,"donnie","wuxiaodong")
jdao对象操作如下：
Hstest t = new Hstest();
t.setId(1);
t.setRowname("donnie");
t.setValue("wuxiaodong");
t.save();

批量插入SQL:  insert into hstest (id,rowname,value) values(1,"donnie1","wuxiaodong1"),(2,"donnie2","wuxiaodong2"),(3,"donnie3","wuxiaodong3")
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

更新SQL:  update hstest set rowname="wuxiaodong",value="wuxiaodong" where id=10
jdao对象操作如下：
Hstest t = new Hstest();
t.setRowname("wuxiaodong");
t.setValue("wuxiaodong");
t.where(Hstest.ID.EQ(10));
t.update();

删除SQL:  delete from hstest where id=2
jdao对象操作如下:
Hstest t = new Hstest();
t.where(Hstest.ID.EQ(2));
t.delete();


二、使用QueryDao查询数据,建议用于复杂SQL查询,单表增删改查建议还是使用DAO对象操作。

QueryDao qd = new QueryDao(JdaoHandlerFactory.getDBHandler4c3p0(), "select id,rowname from hstest limit ?,?", 0, 10);
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