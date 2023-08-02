/**
 * https://github.com/donnie4w/jdao
 * Copyright jdao Author. All Rights Reserved.
 * Email: donnie4w@gmail.com
 */
package io.github.donnie4w.jdao.base;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import io.github.donnie4w.jdao.util.Utils;

/**
 * Copyright 2012-2013 donnie(donnie4w@gmail.com)
 * date 2013-1-10
 * verion 1.0
 */
public class JdaoUtil {
    private final static Log logger = Log.newInstance(true, JdaoUtil.class);
    public static <T extends Table<?>> List<T> selectDaos(Connection con, String sql, Class<T> classType, Object[] args) throws JException {
        List<T> retList = new ArrayList<T>();
        ResultSet rs = null;
        PreparedStatement ps = null;
        try {
            if (args == null || args.length == 0) {
                ps = con.prepareStatement(sql);
                rs = ps.executeQuery();
            } else {
                ps = con.prepareStatement(sql);
                for (int i = 1; i <= args.length; i++) {
                    ps.setObject(i, args[i - 1]);
                }
                rs = ps.executeQuery();
            }
            if (rs != null) {
                while (rs.next()) {
                    int columncount = rs.getMetaData().getColumnCount();
                    T object = classType.getConstructor().newInstance();
                    for (int i = 1; i <= columncount; i++) {
                        String columnName = Utils.delUnderline(rs.getMetaData().getColumnLabel(i));
                        if (!Utils.isContainsLowerCase(columnName)) {
                            columnName = columnName.toLowerCase();
                        }
                        String fname = null;
                        String cgn = classType.getName()+"_"+columnName;
                        if (Table.fieldnamemap.containsKey(cgn)){
                            fname = Table.fieldnamemap.get(cgn);
                        }else{
                            fname = columnName;
                        }
                        Fields f = (Fields) classType.getField(fname).get(object);
                        Object o = rs.getObject(i);
                        if (o != null)
                            try{
                                if (f.valueClass().equals(rs.getObject(i).getClass())){
                                    f._setobject(rs.getObject(i));
                                }else if (f.valueClass().equals(Long.class) || f.valueClass().equals(Integer.class)|| f.valueClass().equals(Short.class)|| f.valueClass().equals(Double.class)|| f.valueClass().equals(Float.class)|| f.valueClass().equals(BigDecimal.class)) {
                                    f._setBigDecimal(new BigDecimal(rs.getString(i)));
                                }else {
                                    f._setobject(rs.getObject(i));
                                }
                            }catch (ClassCastException e){
                                if (f.valueClass().equals(Long.class) || f.valueClass().equals(Integer.class)|| f.valueClass().equals(Short.class)|| f.valueClass().equals(Double.class)|| f.valueClass().equals(Float.class)|| f.valueClass().equals(BigDecimal.class)) {
                                    f._setBigDecimal(new BigDecimal(rs.getString(i)));
                                }else{
                                    logger.log("error:"+e.getMessage());
                                }
                            }
                    }
                    retList.add(object);
                }
            }
            return retList;
        } catch (Exception e) {
            throw new JException(e);
        } finally {
            Utils.close(rs, ps);
        }
    }

    public static <T extends Table<?>> T selectDao(Connection con, String sql, Class<T> classType, Object[] args) throws JException {
        ResultSet rs = null;
        PreparedStatement ps = null;
        T object = null;
        try {
            if (args == null || args.length == 0) {
                ps = con.prepareStatement(sql);
                rs = ps.executeQuery();
            } else {
                ps = con.prepareStatement(sql);
                for (int i = 1; i <= args.length; i++) {
                    ps.setObject(i, args[i - 1]);
                }
                rs = ps.executeQuery();
            }
            if (rs != null) {
                if (rs.next()) {
                    object = classType.getDeclaredConstructor().newInstance();
                    int columncount = rs.getMetaData().getColumnCount();
                    for (int i = 1; i <= columncount; i++) {
                        String columnName = Utils.delUnderline(rs.getMetaData().getColumnLabel(i));
                        if (!Utils.isContainsLowerCase(columnName)) {
                            columnName = columnName.toLowerCase();
                        }
                        String fname = null;
                        String cgn = classType.getName()+"_"+columnName;
                        if (Table.fieldnamemap.containsKey(cgn)){
                            fname = Table.fieldnamemap.get(cgn);
                        }else{
                            fname = columnName;
                        }
                        Fields f = (Fields) classType.getField(fname).get(object);
                        Object o = rs.getObject(i);
                        if (o != null)
                            try{
                                if (f.valueClass().equals(rs.getObject(i).getClass())){
                                    f._setobject(rs.getObject(i));
                                }else if (f.valueClass().equals(Long.class) || f.valueClass().equals(Integer.class)|| f.valueClass().equals(Short.class)|| f.valueClass().equals(Double.class)|| f.valueClass().equals(Float.class)|| f.valueClass().equals(BigDecimal.class)) {
                                    f._setBigDecimal(new BigDecimal(rs.getString(i)));
                                }else {
                                    f._setobject(rs.getObject(i));
                                }
                            }catch (ClassCastException e){
                                if (f.valueClass().equals(Long.class) || f.valueClass().equals(Integer.class)|| f.valueClass().equals(Short.class)|| f.valueClass().equals(Double.class)|| f.valueClass().equals(Float.class)|| f.valueClass().equals(BigDecimal.class)) {
                                    f._setBigDecimal(new BigDecimal(rs.getString(i)));
                                }else{
                                    logger.log("error:"+e.getMessage());
                                }
                            }
                    }
                }
            }
            return object;
        } catch (Exception e) {
            throw new JException(e);
        } finally {
            Utils.close(rs, ps);
        }
    }
}
