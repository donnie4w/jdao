package com.jdao.action;

import java.io.Serializable;
import com.jdao.base.Table;
import com.jdao.base.Fields;

/**
 * @date 2017-08-19 08:49:56  dao for table hstest
 */
public class Hstest extends Table<Hstest> implements Serializable{
	private static final long serialVersionUID = 1L;
	private final static String TABLENAME_ = "hstest";
	/**id*/
	public final static Fields ID = new Fields("`id`");
	/**value*/
	public final static Fields VALUE = new Fields("`value`");
	/**rowname*/
	public final static Fields ROWNAME = new Fields("`rowname`");

	private int id = 0;
	private java.lang.String value;
	private java.lang.String rowname;

	public Hstest(){
		super(TABLENAME_,Hstest.class);
		super.setFields(ID,VALUE,ROWNAME);
	}

	public Hstest(String tableName4sharding){
		super(tableName4sharding,Hstest.class);
		super.setFields(ID,VALUE,ROWNAME);
	}

	public int getId(){
		return this.id;
	}

	public void setId(int id){
		fieldValueMap.put(ID, id);
		 this.id=id;
	}
	public java.lang.String getValue(){
		return this.value;
	}

	public void setValue(java.lang.String value){
		fieldValueMap.put(VALUE, value);
		 this.value=value;
	}
	public java.lang.String getRowname(){
		return this.rowname;
	}

	public void setRowname(java.lang.String rowname){
		fieldValueMap.put(ROWNAME, rowname);
		 this.rowname=rowname;
	}
}