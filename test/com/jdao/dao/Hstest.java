package com.jdao.dao;

import java.io.Serializable;
import com.jdao.base.Table;
import com.jdao.base.Fields;

/**
 * @date 2013-07-01 22:41:52  dao for table Hstest
 */
public class Hstest extends Table<Hstest> implements Serializable{
	private static final long serialVersionUID = 1L;
	private final static String TABLENAME_ = "Hstest";
	public final static Fields ID = new Fields("id");
	public final static Fields ROWNAME = new Fields("rowname");
	public final static Fields VALUE = new Fields("value");
	public final static Fields AGE = new Fields("age");
	public final static Fields CTIME = new Fields("ctime");

	private int id = 0;
	private java.lang.String rowname;
	private java.lang.String value;
	private java.lang.String age;
	private java.util.Date ctime;

	public Hstest(){
		super(TABLENAME_,Hstest.class);
		super.setFields(ID,ROWNAME,VALUE,AGE,CTIME);
	}

	public Hstest(String tableName4sharding){
		super(tableName4sharding,Hstest.class);
		super.setFields(ID,ROWNAME,VALUE,AGE,CTIME);
	}

	public int getId(){
		return this.id;
	}

	public void setId(int id){
		fieldValueMap.put(ID, id);
		 this.id=id;
	}
	public java.lang.String getRowname(){
		return this.rowname;
	}

	public void setRowname(java.lang.String rowname){
		fieldValueMap.put(ROWNAME, rowname);
		 this.rowname=rowname;
	}
	public java.lang.String getValue(){
		return this.value;
	}

	public void setValue(java.lang.String value){
		fieldValueMap.put(VALUE, value);
		 this.value=value;
	}
	public java.lang.String getAge(){
		return this.age;
	}

	public void setAge(java.lang.String age){
		fieldValueMap.put(AGE, age);
		 this.age=age;
	}
	public java.util.Date getCtime(){
		return this.ctime;
	}

	public void setCtime(java.util.Date ctime){
		fieldValueMap.put(CTIME, ctime);
		 this.ctime=ctime;
	}
}