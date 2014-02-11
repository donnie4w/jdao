package com.jdao.dbHandlerImpl;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.transaction.PlatformTransactionManager;

import com.jdao.dbHandler.JdaoHandler;

public class JdaoHandlerFactory {

	private static BeanFactory BeanFactory = new ClassPathXmlApplicationContext("jdao-*");

	public static JdaoHandler getDBHandler4spring() {
		JdaoHandler jdaoHandler = (JdaoHandler) BeanFactory.getBean("jdaoImpl4JdbcTemplate");
		return jdaoHandler;
	}

	public static PlatformTransactionManager getTransactionManager() {
		PlatformTransactionManager ptm = (PlatformTransactionManager) BeanFactory.getBean("transactionManager");
		return ptm;
	}

	public static JdaoHandler getDBHandler4c3p0() {
		JdaoHandler jdaoHandler = new JdaoHandlerImpl("database");
		return jdaoHandler;
	}

	public static JdaoHandler getDBHandler4c3p0SingleTon() {
		JdaoHandler jdaoHandler = new JdaoHandlerImplSingleTon("database");
		return jdaoHandler;
	}
}
