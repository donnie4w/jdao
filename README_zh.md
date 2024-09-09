## JDAO —— java持久层框架  [[English](https://github.com/donnie4w/jdao/blob/master/readme.md)]

### 简介

Jdao是一个全面的go持久层解决方案。主要目的在于 减少编程量，提高生产力，提高性能，支持多数据源整合操作，支持数据读写分离，制定持久层编程规范。 
灵活运用Jdao，可以在持久层设计上，减少30%甚至50%以上的编程量，同时形成持久层的统一编程规范，减少持久层错误，同时易于维护和扩展。
Jdao的映射模块实现了SQL与程序分离的特性，映射模块与myBatis的核心功能相同。是除了mybatis和基于myBatis系列的orm外，唯一实现该特性的orm框架.

## [使用文档](https://tlnet.top/jdaodoc)

## [示例程序](https://github.com/donnie4w/jdaodemo)

------

### 主要特点

1. 轻量：Jdao没有任何依赖，所有特性均为Jdao本身实现，不会由于其他项目的更新或功能修改而受到影响，可以轻松融入各类项目中。
2. 高效：Jdao不依赖第三方库的最重要目的之一是实现高效性，没有多余的包袱。性能与直接JDBC调用极为接近，ORM封装的性能损耗非常小。
3. 灵活：Jdao支持丰富的动态SQL构建功能，包括原生动态SQL，映射动态SQL，实体类动态SQL。
4. 安全：Jdao没有SQL注入的风险。Jdao提供了Mybatis相同的映射功能，并去掉了sql注入的潜在隐患。
5. 全面：Jdao结合了Hibernate的抽象性和MyBatis的灵活性，提供规范且高效的ORM运用方案。


### 主要功能

1. 生成代码：运行jdao代码生成工具，创建数据库表的标准化实体类。类似thrift/protobuf。
2. 高效序列化：表的标准化实体类实现了高效的序列化与反序列化。比标准库序列化方法高3-12倍，而序列化体积只有其20%左右。
3. 读写分离：jdao支持绑定多数据源，并支持数据源绑定表，类，映射接口等属性。并支持数据读写分离
4. 数据缓存：jdao支持数据缓存，并支持对缓存数据时效，与数据回收等特性进行细致控制
5. 广泛兼容性：jdao理论上支持所有实现JDBC接口的数据库
6. 高级特性：支持事务，存储过程，批处理等数据库操作
7. 动态SQL：jdao实现丰富的动态SQL构建功能。支持动态SQL标签映射构建与原生SQL动态构建等多种模式。
8. 兼容myBatis：jdao在映射模块中，采用了mybatis的标签定义，并实现了相同的功能特性。

### 解决Hibernate与MyBatis痛点的新方案

#### (一)Hibernate普遍认为存在的问题

1. **过度封装**：hibernate提供了一层高级的抽象(包括HQL)，这有助于开发者专注于业务逻辑而不是底层的SQL,这是一种更高级的编程模型
   但它高度自动化虽然减少了对SQL的理解需求，同时却也掩盖底层数据库的具体行为，这可能导致性能瓶颈难以定位和优化
2. **复杂性**：配置和学习曲线陡峭，对于大型项目可能引入不必要的复杂度，尤其是当需要进行细粒度控制时。
3. **性能问题**：反射和代理机制在高并发场景下可能影响性能，尤其是在大规模数据处理时。

#### (二)MyBatis 普遍认为存在的问题

1. **SQL的维护**：MyBatis 提供了通过 XML 配置文件将 SQL 与 Java 代码分离的功能，这种方式提高了灵活性，使开发者可以直接编写和优化 SQL。然而，将所有 SQL 都与代码分离，可能会导致大量的SQL配置。这无疑增加了配置的复杂度和维护的工作量。特别是在团队协作环境中, 过多的SQL或Xml文件还可能引发版本管理和合并冲突的问题。
2. **重复工作**：在 MyBatis 中，每个 DAO 都需要编写相似的 SQL 语句和结果映射逻辑。这种重复性工作不仅增加了开发时间，还容易引入错误，导致代码维护的复杂性增加。尤其是在处理常见的 CRUD 操作时，重复编写相似的 SQL 语句显得非常低效。

#### (三)JDAO 的持久层解决方案

1. **完全表对象映射，处理单表CRUD操作**: 将所有操作都用表对象映射操作是hibernate的经典做法，它导致了一些过渡封装的问题。
   Jdao支持自动生成表的映射实体类，专门处理单表的增删改查操作。它的底层动态SQL构建，有效地解决了相似的表操作行为带来的大量相似SQL的问题。
2. **完全SQL映射对象，处理复杂SQL操作**: 在实践中发现，复杂SQL，特别是多表关联的SQL，通常需要优化，这需要对表结构，表索引性质等数据库属性有所了解。
   而将复杂SQL字符串使用java对象进行拼接，通常会增加理解上的难度。甚至，最后开发者自己都不知道对象拼接后的最终执行SQL是什么，这无疑增加了风险和维护难度。
   Jdao实现了mybatis的核心功能SQL映射特性，可以在XML配置执行SQL，并映射为Java对象或接口。
3. **`SqlBuilder`，原生SQL动态构建**：Jdao实现了原生SQL的动态构建。对于极为复杂的SQL，甚至是function或存储过程等数据库高级特性，Jdao支持使用`SqlBuilder`进行动态构建。
   它是动态标签的程序性化实现，基于java程序实现动态SQL，可以构建出任何形式的SQL。

### Jdao的定位

* Jdao将定位为灵活且可扩展的基础框架。与hibernate，mybatis相似。在功能上与数据库CRUD操作做基础性对应。
* Jdao与MyBatis-Plus类型框架的区别：
  * MyBatis-Plus等类型框架属于扩展性框架，扩展性框架通常更多为了满足寻求更高生产力和更快速开发的需求而内置大量高级功能。对于需要敏捷开发需求来说，它是方便的，但它增加的功能同时也并不是所有项目都需要的。特别那些已经有成熟架构和复杂业务逻辑的应用来说，可能更倾向于自己控制每一个细节。
  * Jdao属于基础性框架，不内置过多的高级功能，保持了较好的可扩展性。与Hibernate和MyBatis类似，设计的目的是为了解决不同场景下的数据访问问题。但完全的表映射与完全的SQL映射都存在一定的问题，Jdao提供了多种场景下的对象封装策略，以解决大小场景中的持久层解决方案。
* Jdao的基础性定位与简洁的接口，使得Jdao即适合小型项目，同时也适合大型项目，即适合小团队，也适合大团队。

![](https://tlnet.top/statics/tlnet/5165.png)

------

# Jdao 快速使用

#### 安装 jdao依赖包

```xml
<!--使用 Maven 安装-->
<dependency>
	<groupId>io.github.donnie4w</groupId>
	<artifactId>jdao</artifactId>
	<version>2.1.0</version>
</dependency>
```

### [Jdao 普通使用示例](https://github.com/donnie4w/jdaodemo)

#### 数据源初始化

```java
static {
    Jdao.init(DataSourceFactory.getDataSourceBySqlite(), DBType.SQLITE);  //初始化数据源，第一个参数是DataSource，第二是参数是数据库类型
    Jdao.setLogger(true);  // 测试环境打开Jdao日志打印，将输出执行的SQL即参数等信息
}
```

#### 标准化实体类增删改查

```java
Hstest t = new Hstest();
t.where(Hstest.ID.GT(1));
t.limit(20,10);
Hstest rs = t.select(); // 查询
```
```java
Hstest t = new Hstest();
t.SetValue("hello world")
t.where(Hstest.ID.EQ(1));
t.update(); // 更新
```
```java
Hstest t = new Hstest();
t.where(Hstest.ID.EQ(1));
t.delete(); // 删除
```
```java
Hstest hs = new Hstest();
hs.setRowname("hello world");
hs.setValue("123456789");
hs.insert(); //新增
```

#### 原生SQL操作

```java
//查询
Hstest hs =  Jdao.executeQuery(Hstest.class,"select * from Hstest order by id desc limit ?",1);
System.out.println(hs);
```
```java
//新增
int  i = Jdao.executeUpdate("insert into hstest2(rowname,value) values(?,?)", "helloWorld", "123456789");
```
```java
//更新
int  i = Jdao.executeUpdate("update hstest set value=? where id=?", "hello",1);
```
```java
//插入
int  i = Jdao.executeUpdate("delete from hstest where id = ?", 1);
```

#### 缓存操作

```java
// 绑定Hstest.class  启用缓存
JdaoCache.bindClass(Hstest.class);
```

#### 读写分离

```java
//读写分离绑定
JdaoSlave.bindClass(Hstest.class, DataSourceFactory.getDataSourceByPostgreSql(), DBType.POSTGRESQL);
```

#### SQL映射操作

```xml
 <select id="selectHstestById" resultType="io.github.donnie4w.jdao.dao.Hstest">
      SELECT * FROM hstest WHERE id &lt; #{id}  and age &lt; #{age}
 </select>
```

```java
 @Test
 public void selectOne() throws JdaoException, JdaoClassException, SQLException {
     Hstest hs = jdaoMapper.selectOne("io.github.donnie4w.jdao.action.Mapperface.selectHstestById", 2, 26);
     System.out.println(hs);
 }
```

##### 动态SQL映射操作

```xml
 <select id="demo1" resultType="io.github.donnie4w.jdao.dao.Hstest">
     SELECT * FROM hstest
     <where>
         <if test="rowname!= 'hello'">
             and rowname = #{rowname}
         </if>
         <if test="id >0">
             AND id = #{id}
         </if>
     </where>
 </select>
```

```java
 @Test
 public void demo1() throws JdaoException, JdaoClassException, SQLException {
     Hstest hs = new Hstest();
     hs.setId(31);
     hs.setRowname("hello");
     List<Hstest> listSlave = jdaoMapper.selectList("io.github.donnie4w.jdao.action.Dynamic.demo1", hs);
     for (Hstest hstest : listSlave) {
         System.out.println(hstest);
     }
 }
```

#### SqlBuilder动态SQL

```java
import io.github.donnie4w.jdao.handle.SqlBuilder;

@Test
public void testAppendIf() throws JdaoException, SQLException {
    Map<String, Object> context = new HashMap<>();
    context.put("id", 31);

    SqlBuilder builder = SqlBuilder.newInstance();
    builder.append("SELECT * FROM HSTEST where 1=1")
            .appendIf("id>0", context, "and id=?", context.get("id"))
            .append("ORDER BY id ASC");

    List<DataBean> list = builder.selectList();
    for (DataBean bean : list) {
        System.out.println(bean);
    }
}
```


------------

#### [Jdao 使用文档界面](https://tlnet.top/jdaodoc)

![jdao](https://tlnet.top/statics/tlnet/21691.jpg)