## JDAO —— Java Persistence Framework   [[中文](https://github.com/donnie4w/jdao/blob/master/README_zh.md)]

### Introduction

JDAO is an innovative persistence layer solution designed to reduce programming workload, enhance productivity, improve performance, support integration of multiple data sources, enable read-write separation, and establish standards for persistence layer programming. By leveraging JDAO in your persistence layer design, you can reduce coding by up to 30%, sometimes even 50% or more, while establishing a unified programming standard that reduces errors and facilitates maintenance and expansion.

The mapping module of Jdao realizes the separation of SQL and program, and the mapping module has the same core function as myBatis. In addition to mybatis and ORMs based on the myBatis family, it is the only orm framework that implements this feature.

[Usage Documentation](https://tlnet.top/jdaodoc)

[Sample Programs](https://github.com/donnie4w/jdaodemo)

---

### Key Features

1. **Lightweight:** JDAO has no dependencies. All features are implemented within JDAO itself, ensuring it integrates smoothly into various projects without being affected by updates or changes in other projects.
2. **Efficiency:** One of the main goals of JDAO is efficiency, avoiding unnecessary overhead. Its performance is very close to direct JDBC calls, with minimal performance loss due to ORM encapsulation.
3. **Flexibility:** JDAO supports rich dynamic SQL construction capabilities, including native dynamic SQL, mapping dynamic SQL, and entity class dynamic SQL.
4. **Security:** JDAO eliminates the risk of SQL injection. It offers similar mapping functionality to MyBatis but removes potential security vulnerabilities.
5. **Comprehensive:** Combining the abstraction of Hibernate and the flexibility of MyBatis, JDAO provides a standardized and efficient ORM application solution.

### Main Functions

1. **Code Generation:** Run the JDAO code generation tool to create standardized entity classes for database tables, similar to Thrift/Protocol Buffers.
2. **Efficient Serialization:** The standardized entity classes for tables implement efficient serialization and deserialization, which is 3-12 times faster than standard library serialization methods and has a serialized size about 20% of the standard method.
3. **Read-Write Separation:** JDAO supports binding multiple data sources and supports data source binding to tables, classes, mapping interfaces, etc., as well as read-write separation.
4. **Data Caching:** JDAO supports data caching and allows detailed control over cache expiration and data eviction.
5. **Wide Compatibility:** JDAO theoretically supports all databases that implement the JDBC interface.
6. **Advanced Features:** Supports transactions, stored procedures, batch processing, and other database operations.
7. **Dynamic SQL:** JDAO implements rich dynamic SQL construction capabilities. It supports dynamic SQL tag mapping construction and native SQL dynamic construction.
8. **MyBatis Compatibility:** In the mapping module, JDAO uses MyBatis tag definitions and implements the same functional characteristics.

### New Solution Addressing Pain Points of Hibernate and MyBatis

#### **Common Issues with Hibernate**

1. **Over-Abstraction:** While Hibernate's high-level abstraction (including HQL) helps developers focus on business logic rather than underlying SQL, this abstraction can mask the specific behaviors of the underlying database, leading to performance bottlenecks that are hard to identify and optimize.
2. **Complexity:** The configuration and learning curve are steep, which may introduce unnecessary complexity for large projects, especially when fine-grained control is required.
3. **Performance Issues:** Reflection and proxy mechanisms can affect performance in high-concurrency scenarios, particularly during large-scale data processing.

#### **Common Issues with MyBatis**

1. **SQL Maintenance:** MyBatis separates SQL from Java code using XML configuration files, increasing flexibility and allowing direct writing and optimization of SQL. However, this can lead to a large number of SQL configurations, increasing complexity and maintenance workloads.
2. **Redundant Work:** Each DAO in MyBatis requires writing similar SQL statements and result mapping logic, which not only increases development time but also introduces errors and increases the complexity of code maintenance.

#### **JDAO Persistence Layer Solution**

1. **Complete Table Object Mapping for Single Table CRUD Operations:**
   Mapping all operations to table objects is a classic approach used by Hibernate, which can lead to over-encapsulation issues. JDAO supports auto-generating mapping entity classes dedicated to CRUD operations on single tables. Its underlying dynamic SQL construction effectively addresses the problem of generating a large amount of similar SQL for similar table operations.
2. **Complete SQL Mapping Objects for Complex SQL Operations:**
   In practice, complex SQL, especially multi-table joins, often require optimization, which necessitates a good understanding of table structures, index properties, and other database attributes. Concatenating complex SQL strings using Java objects typically increases the difficulty of comprehension. Sometimes, developers might not even know what the final SQL statement looks like after concatenation, which undoubtedly increases risk and maintenance difficulty. JDAO implements the core feature of MyBatis — the SQL mapping capability, allowing the definition of SQL in XML configuration files and mapping it to Java objects or interfaces.
3. **`SqlBuilder` for Native SQL Dynamic Construction:**
   JDAO supports dynamic construction of native SQL. For extremely complex SQL, including database advanced features such as functions or stored procedures, JDAO supports dynamic construction using `SqlBuilder`. This is a procedural implementation of dynamic tags, based on Java programs to construct dynamic SQL, enabling the creation of any form of SQL.

### Positioning of JDAO

* JDAO is positioned as a flexible and extensible foundational framework. Similar to Hibernate and MyBatis, its functionalities primarily correspond to basic CRUD operations on databases.

* Differences Between JDAO and MyBatis-Plus Type Frameworks:
    * Frameworks like MyBatis-Plus are extension frameworks, typically designed with a plethora of advanced features built-in to meet the needs of higher productivity and faster development. While this is convenient for agile development requirements, the added features are not necessary for all projects. Especially for applications with mature architectures and complex business logic, there might be a preference for controlling every detail.
    * JDAO is a foundational framework that does not come with a surplus of advanced features, maintaining good extensibility. Like Hibernate and MyBatis, its design aims to solve data access problems across different scenarios. However, both complete table mapping and complete SQL mapping have their issues; JDAO provides various object encapsulation strategies to address persistence layer solutions for both small and large scenarios.

* The foundational positioning and clean interfaces of JDAO make it suitable for both small and large projects, as well as for small and large teams.
---

### Quick Start Guide

#### Install JDAO Dependency Package

```xml
<!-- Using Maven -->
<dependency>
    <groupId>io.github.donnie4w</groupId>
    <artifactId>jdao</artifactId>
    <version>2.1.0</version>
</dependency>
```

#### Initialize Data Source

```java
static {
    Jdao.init(DataSourceFactory.getDataSourceBySqlite(), DBType.SQLITE);  // Initialize data source, first parameter is DataSource, second parameter is database type
    Jdao.setLogger(true);  // Enable logging in the test environment to output executed SQL and parameters
}
```

#### CRUD Operations with Standard Entity Classes

```java
Hstest t = new Hstest();
t.where(Hstest.ID.GT(1));
t.limit(20, 10);
Hstest rs = t.select();  // Query
```

```java
Hstest t = new Hstest();
t.setValue("hello world");
t.where(Hstest.ID.EQ(1));
t.update();  // Update
```

```java
Hstest t = new Hstest();
t.where(Hstest.ID.EQ(1));
t.delete();  // Delete
```

```java
Hstest hs = new Hstest();
hs.setRowname("hello world");
hs.setValue("123456789");
hs.insert();  // Insert
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
int i = Jdao.executeUpdate("delete from hstest where id=?", 1);
```

#### Cache Operations

```java
// Bind Hstest.class to enable caching
JdaoCache.bindClass(Hstest.class);
```

#### Read-Write Separation

```java
// Read-Write Separation Binding
JdaoSlave.bindClass(Hstest.class, DataSourceFactory.getDataSourceByPostgreSql(), DBType.POSTGRESQL);
```

#### SQL Mapping Operations

```xml
<select id="selectHstestById" resultType="io.github.donnie4w.jdao.dao.Hstest">
    SELECT * FROM hstest WHERE id &lt; #{id} and age &lt; #{age}
</select>
```

```java
@Test
public void selectOne() throws JdaoException, JdaoClassException, SQLException {
    Hstest hs = jdaoMapper.selectOne("io.github.donnie4w.jdao.action.Mapperface.selectHstestById", 2, 26);
    System.out.println(hs);
}
```

#### Dynamic SQL Mapping Operations

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

#### SqlBuilder Dynamic SQL

```java
@Test
public void testAppendIf() throws JdaoException, SQLException {
    Map<String, Object> context = new HashMap<>();
    context.put("id", 31);

    SqlBuilder builder = new SqlBuilder();
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

#### [JDAO Usage Documentation Interface](https://tlnet.top/jdaodoc)

![jdao](https://tlnet.top/statics/tlnet/606.jpg)