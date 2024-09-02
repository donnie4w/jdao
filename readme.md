## JDAO —— Java Persistence Framework   [[中文](https://github.com/donnie4w/jdao/blob/master/README_zh.md)]

### Introduction

JDAO is an innovative persistence layer solution designed to reduce coding effort, improve productivity, enhance performance, support multi-data source integration, and enforce coding standards in the persistence layer. By leveraging JDAO, developers can reduce their codebase by 30% to 50% or more in the persistence layer design while maintaining consistent coding standards, reducing errors, and making maintenance and expansion easier.
- JDAO's design is both simple and rigorous, with interfaces and function names that align with JDBC conventions, making all operations intuitive.
- Even if you have never worked with JDAO, you can immediately understand the intent of the code and the corresponding data behavior upon seeing JDAO-based persistence code.
- JDAO’s simplicity allows you to master its usage within minutes, even if you are unfamiliar with it.

## [User Documentation](https://tlnet.top/jdaodoc)

## [Example Programs](https://github.com/donnie4w/jdaodemo)

------

### Key Features

1. **Lightweight**: JDAO has no dependencies; all features are implemented within JDAO itself, ensuring it is not affected by updates or changes in other projects. It can be easily integrated into various projects.
2. **Efficient**: JDAO is designed with efficiency in mind. Its performance is close to direct JDBC calls, and the performance overhead of its ORM encapsulation is minimal.
3. **Flexible**: JDAO supports rich dynamic SQL construction, including native dynamic SQL, mapped dynamic SQL, and dynamic SQL for entity classes.
4. **Secure**: JDAO eliminates the risk of SQL injection. It offers similar mapping functionality to MyBatis but without the potential SQL injection vulnerabilities.
5. **Comprehensive**: JDAO combines the abstraction of Hibernate and the flexibility of MyBatis, providing a standardized and efficient ORM solution.

### Core Features

1. **Code Generation**: Run the JDAO code generation tool to create standardized entity classes for your database tables, similar to Thrift/Protobuf.
2. **Efficient Serialization**: The standardized entity classes of the tables implement efficient serialization and deserialization, which is 3-12 times faster than the standard library methods, while the serialized size is only about 20% of theirs.
3. **Supports Read-Write Separation**: JDAO supports binding to multiple data sources and allows data source binding to tables, classes, mapping interfaces, etc., while supporting read-write separation.
4. **Data Caching Support**: JDAO supports data caching and allows fine control over cache data expiration and recovery features.
5. **Broad Compatibility**: JDAO theoretically supports all databases that implement the JDBC interface.
6. **Advanced Features**: JDAO supports transactions, stored procedures, batch processing, and other database operations.
7. **Dynamic SQL Support**: JDAO implements MyBatis-style dynamic SQL functionality while removing security risks and unnecessary features. It also provides native SQL with dynamic SQL construction capabilities.

### A New Solution to Address the Pain Points of Hibernate and MyBatis

#### Common Issues with Hibernate

1. **Over-Encapsulation**: Hibernate offers a high-level abstraction (including HQL) that helps developers focus on business logic rather than low-level SQL, making it a higher-level programming model. However, its high degree of automation reduces the need to understand SQL but can obscure underlying database behavior, making performance bottlenecks harder to identify and optimize.
2. **Complexity**: The steep learning curve and complex configuration can introduce unnecessary complexity in large projects, especially when fine-grained control is required.
3. **Performance Issues**: Reflection and proxy mechanisms can impact performance in high-concurrency scenarios, particularly when dealing with large-scale data processing.

#### Common Issues with MyBatis

1. **SQL Maintenance**: MyBatis allows SQL to be separated from Java code via XML configuration files, which improves flexibility by enabling developers to directly write and optimize SQL. However, separating all SQL from the code can result in a large volume of SQL configurations, increasing complexity and maintenance workload. In a team environment, too much SQL or XML configuration can also lead to version control and merge conflict issues.
2. **Repetitive Work**: In MyBatis, each DAO often requires writing similar SQL statements and result mapping logic. This repetitive work increases development time and can introduce errors, making code maintenance more complex. This inefficiency is especially pronounced when dealing with common CRUD operations, where writing similar SQL statements repeatedly is very inefficient.

#### JDAO's Innovative Solution

The JDAO framework combines Hibernate's abstraction level with MyBatis's flexibility to provide a robust and intuitive persistence layer solution.

1. **Standardized Mapping Entity Classes for Single Table CRUD Operations**: Over 90% of single-table database operations can be handled via entity class operations. These CRUD operations, which typically do not involve complex SQL optimization, are encapsulated by entity classes, reducing error rates and making maintenance easier. By utilizing caching and read-write separation mechanisms, optimizing the persistence layer becomes more efficient and convenient. The standardized entity class data operation format is not simply a concatenation of Java object functions but rather more like object-oriented SQL operations, making the operations easier to understand.
2. **Executing Complex SQL**: In practice, complex SQL, especially multi-table joins, often requires optimization. This requires knowledge of the table structure, index properties, and other database attributes. Composing complex SQL using Java objects typically adds to the cognitive load. Developers may not even know what the final SQL will look like after the object concatenation, increasing the risk and maintenance difficulty. Therefore, for complex SQL, JDAO recommends using its CRUD interface to execute SQL. JDAO provides flexible data conversion and efficient JavaBean mapping, avoiding the overuse of time-consuming operations like reflection.
3. **Compatibility with MyBatis Mapping Files**: For complex SQL operations, JDAO offers corresponding CRUD interfaces and supports XML-configured SQL interface mapping, similar to MyBatis. However, while MyBatis requires mapping for all SQL operations, JDAO recommends only mapping complex SQL or CURD operations that cannot be handled by standard entity classes. JDAO's SQL configuration files reference MyBatis's format but implement a new parser, allowing for greater flexibility and tolerance for configuration parameters (see documentation for details).

### The Strength of JDAO

JDAO provides diverse solutions to suit the needs of different projects. In the same project, the complexity of persistence layer requirements can vary between different business modules, requiring tailored solutions. For example, if the project involves a large number of simple single-table CRUD operations, using SQL mapping will result in a significant amount of repetitive SQL and XML configuration files. In this case, using JDAO's standardized entity classes is the best solution. Conversely, JDAO offers tools like SqlBuilder and Mapper to tackle more complex data scenarios. The strength of JDAO lies in its ability to consider the various situations that can arise in a project and offer appropriate solutions for each one, rather than trying to solve all problems with a single approach, which can lead to issues like those encountered with Hibernate or MyBatis. As a result, JDAO is well-suited for both small and large projects, as well as for small and large teams.

![](https://tlnet.top/statics/tlnet/5165.png)

------

# Quick Start with JDAO

#### Install the JDAO Dependency

```xml
<!-- Install via Maven -->
<dependency>
	<groupId>io.github.donnie4w</groupId>
	<artifactId>jdao</artifactId>
	<version>2.1.0</version>
</dependency>
```

### [Basic JDAO Usage Examples](https://github.com/donnie4w/jdaodemo)

#### Data Source Initialization

```java
static {
    Jdao.init(DataSourceFactory.getDataSourceBySqlite(), DBType.SQLITE);  // Initialize the data source, the first parameter is the DataSource, the second parameter is the database type
    Jdao.setLogger(true);  // Enable JDAO logging in a test environment to print executed SQL and parameters
}
```

#### CRUD Operations with Standardized Entity Classes

```java
// Query
Hstest t = new Hstest();
t.where(Hstest.ID.GT(1));
t.limit(20, 10);
Hstest rs = t.select();
```
```java
// Update
Hstest t = new Hstest();
t.setValue("hello world");
t.where(Hstest.ID.EQ(1));
t.update();
```
```java
// Delete
Hstest t = new Hstest();
t.where(Hstest.ID.EQ(1));
t.delete();
```
```java
// Insert
Hstest hs = new Hstest();
hs.setRowname("hello world");
hs.setValue("123456789");
hs.insert();
```

#### Native SQL Operations

```java
// Query
Hstest hs = Jdao.executeQuery(Hstest.class, "select * from Hstest order by id desc limit ?", 1);
System.out.println(hs);
```
```java
// Insert
int i = Jdao.executeUpdate("insert into hstest2(rowname, value) values(?, ?)", "helloWorld", "123456789");
```
```java
// Update
int i = Jdao.executeUpdate("update hstest set value=? where id=?", "hello", 1);
```
```java
// Delete
int i = Jdao.executeUpdate("delete from hstest where id = ?", 1);
```

#### Cache Operations

```java
// Bind Hstest.class and enable caching
JdaoCache.bindClass(Hstest.class);
```

#### Read-Write Separation

```java
// Bind for read-write separation
JdaoSlave.bindClass(Hstest.class, DataSourceFactory.getDataSourceByPostgreSql(), DBType.POSTGRESQL);
```

#### SQL Mapping Operations

```xml
<select id="selectHstestById" resultType="io.github.donnie4w.hstest">
    select * from Hstest where id=#{id}
</select>
```
```java
Hstest hs = MapperFactory.of(HstestMapper.class).selectHstestById(1);
System.out.println(hs);
```

##### Dynamic SQL mapping operations

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

------------

#### Usage documentation interface of Jdao

![jdao](https://tlnet.top/statics/tlnet/606.jpg)