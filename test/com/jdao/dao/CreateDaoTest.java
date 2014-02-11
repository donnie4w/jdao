package com.jdao.dao;
import java.sql.Connection;
import java.sql.DriverManager;

import org.junit.Test;

import com.jdao.util.CreateDaoUtil;
import com.jdao.util.CreateDaoXmlUtil;

/**
 * @Copyright 2012-2013 donnie(donnie4w@gmail.com)
 * @date 2013-1-25
 * @verion 1.0 测试如何生成DAO类
 */
public class CreateDaoTest {

	/***
	 * @auther donnie wu
	 * @throws Exception
	 * 
	 *             通过jdbc连接数据库，根据参数 tableName(表名) 生成相应的 java源文件。
	 *             生成的java文件没有package ，所以 实际使用时，需在生成的文件中自行加上相应的包名。 descFile
	 *             为java文件的目标地址。如果空值则生成的文件在根目录下(文件创建完成后会在控制台提示生成文件的绝对地址)。
	 * 
	 */
	@Test
	public void createDao4jdbc() throws Exception {
		Class.forName("com.mysql.jdbc.Driver");
		String driverUrl = "jdbc:mysql://127.0.0.1:3306/wuxiaodong";
		String username = "root";
		String password = "123456";
		Connection conn = DriverManager.getConnection(driverUrl, username, password);
		String tableName = "Hstest";
		String descFile = "";
		CreateDaoUtil.createFile(tableName, descFile, conn, "utf-8");
	}

	/**
	 * @auther donnie wu 通过jdao.xml配置文件批量生成dao源文件。
	 *         如果dao文件已经存在则会在控制台提示并自动跳过该节点进行下一节点的解析。
	 * 
	 */
	@Test
	public void createDao4XML() {
		String descFile = "";
		CreateDaoXmlUtil.createFile(descFile, "utf-8");
	}

	public static void main(String[] args) throws Exception {
		new CreateDaoTest().createDao4jdbc();
	}

}
