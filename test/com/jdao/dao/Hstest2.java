package com.jdao.dao;

import java.io.Serializable;
import com.jdao.base.Table;
import com.jdao.base.Fields;

/**
 * @date 2013-03-04 12:51:19  dao for table hstest2
 */
public class Hstest2 extends Table<Hstest2> implements Serializable{
	private static final long serialVersionUID = 1L;
	private final static String TABLENAME_ = "hstest2";
	public final static Fields ID = new Fields("id");
	public final static Fields ROWNAME = new Fields("rowname");
	public final static Fields INCOME = new Fields("income");
	public final static Fields DAY = new Fields("day");

	private int id = 0;
	private java.lang.String rowname;
	private java.math.BigDecimal income = new java.math.BigDecimal("0");
	private java.util.Date day;

	public Hstest2(){
		super(TABLENAME_,Hstest2.class);
		super.setFields(ID,ROWNAME,INCOME,DAY);
	}

	public Hstest2(String tableName4sharding){
		super(tableName4sharding,Hstest2.class);
		super.setFields(ID,ROWNAME,INCOME,DAY);
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
	public java.math.BigDecimal getIncome(){
		return this.income;
	}

	public void setIncome(java.math.BigDecimal income){
		fieldValueMap.put(INCOME, income);
		 this.income=income;
	}
	public java.util.Date getDay(){
		return this.day;
	}

	public void setDay(java.util.Date day){
		fieldValueMap.put(DAY, day);
		 this.day=day;
	}
}