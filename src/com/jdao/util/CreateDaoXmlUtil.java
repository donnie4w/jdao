package com.jdao.util;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

import org.dom4j.Element;

import com.jdao.dbHandler.JdaoHandlerException;
import com.jdao.dom.Dom4jFactory;
import com.jdao.dom.Dom4jHandler;

/**
 * @Copyright 2012-2013 donnie(donnie4w@gmail.com)
 * @date 2013-1-10
 * @verion 1.0
 */
public class CreateDaoXmlUtil {
	private static final String PATH = "/jdao.xml";
	private static final String TABLE_PATH = "jdao-table/table";

	/**
	 * auther donnie wu
	 * 
	 * @param destPath
	 * @param charset
	 *            通过jdao.xml配置文件批量生成dao源文件。 如果dao文件已经存在则会在控制台提示并自动跳过该节点进行下一节点的解析。
	 */
	public static void createFile(String destPath, String charset) {
		try {
			Dom4jHandler dom4jHandler = Dom4jFactory.getDom4jHandler();
			dom4jHandler.loadbyPath(PATH);

			List<Element> list = dom4jHandler.getNodes(TABLE_PATH);
			for (Element element : list) {
				String tableName = element.attributeValue("name");
				String curdir = destPath;
				if (curdir == null || "".equals(curdir.trim()))
					curdir = System.getProperty("user.dir");
				String filePath = curdir + File.separator + Utils.upperFirstChar(Utils.delUnderline(tableName)) + ".java";
				File file = new File(filePath);
				if (file.exists()) {
					System.out.println(filePath + " is exist!");
					continue;
				}
				FileOutputStream fos = null;
				try {
					fos = new FileOutputStream(file);
					fos.write(parseSingle(element).toString().getBytes(charset));
					fos.flush();
				} catch (Exception e) {
					throw e;
				} finally {
					if (fos != null)
						fos.close();
				}
				System.out.println("create " + filePath + ".java successful");
			}
			System.out.println("finish!");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings({ "unchecked", "unused" })
	static String parseSingle(Element element) throws JdaoHandlerException {
		String tableName = element.attributeValue("name");
		List<Element> lists = element.elements("field");
		String Tablename = Utils.upperFirstChar(Utils.delUnderline(tableName));
		StringBuilder sb = new StringBuilder();
		String packageName = element.attributeValue("package");
		if (packageName != null && !"".equals(packageName)) {
			sb.append("package ").append(packageName).append(";").append("\n\n");
		}
		sb.append("import java.io.Serializable;").append("\n");
		sb.append("import com.jdao.base.Table;").append("\n");
		sb.append("import com.jdao.base.Fields;").append("\n\n");
		sb.append("/**").append("\n");
		sb.append(" * @date " + Utils.dateFormat() + "  dao for table ").append(tableName).append("\n");
		sb.append(" */").append("\n");

		sb.append("public class " + Tablename + " extends Table<" + Tablename + "> implements Serializable{").append("\n");
		sb.append("\tprivate static final long serialVersionUID = 1L;").append("\n");
		sb.append("\tprivate final static String TABLENAME_ = \"" + tableName + "\";").append("\n");

		StringBuilder sb4GetSet = new StringBuilder();
		StringBuilder sb4args = new StringBuilder();
		StringBuilder sb4FieldArgs = new StringBuilder();
		StringBuilder sb4Fields = new StringBuilder();

		for (int i = 1; i <= lists.size(); i++) {
			Element e = lists.get(i - 1);
			String columnName = e.attributeValue("name").trim();
			String type = type2String(e.attributeValue("type").trim()) + " ";
			if (type == null) {
				throw new JdaoHandlerException("field type[" + e.attributeValue("type") + "] is error!");
			}
			String fileName = Utils.delUnderline(columnName);

			sb4Fields.append("\tpublic final static Fields ").append(fileName.toUpperCase() + " = ").append("new Fields(\"`" + fileName + "`\")")
					.append(";").append("\n");
			sb4FieldArgs.append(fileName.toUpperCase());
			if (i < lists.size())
				sb4FieldArgs.append(",");
			String initField = initField(type2String(e.attributeValue("type").trim()));
			if(!Utils.isContainsLowerCase(fileName)){
				fileName = fileName.toLowerCase();
			}
			if (initField != null) {
				sb4args.append("\tprivate ").append(type).append("" + fileName + " = ").append(initField).append(";").append("\n");
			} else {
				sb4args.append("\tprivate ").append(type).append("" + fileName + "").append(";").append("\n");
			}
			sb4GetSet.append("\tpublic ").append(type).append("get").append(Utils.upperFirstChar(fileName)).append("(){").append("\n");
			sb4GetSet.append("\t\treturn this.").append(fileName).append(";").append("\n");
			sb4GetSet.append("\t}").append("\n\n");
			sb4GetSet.append("\tpublic void ").append("set").append(Utils.upperFirstChar(fileName)).append("(").append(type).append(fileName)
					.append("){").append("\n");
			sb4GetSet.append("\t\tfieldValueMap.put(" + fileName.toUpperCase() + ", " + fileName + ");").append("\n");
			sb4GetSet.append("\t\t this.").append(fileName).append("=").append(fileName).append(";").append("\n");
			sb4GetSet.append("\t}").append("\n");
		}

		sb.append(sb4Fields.toString()).append("\n");
		sb.append(sb4args.toString()).append("\n");

		sb.append("\tpublic ").append(Tablename).append("(){").append("\n");
		sb.append("\t\tsuper(TABLENAME_," + Tablename + ".class);").append("\n");
		sb.append("\t\tsuper.setFields(" + sb4FieldArgs + ");").append("\n");
		sb.append("\t}").append("\n\n");

		sb.append("\tpublic ").append(Tablename).append("(String tableName4sharding){").append("\n");
		sb.append("\t\tsuper(tableName4sharding," + Tablename + ".class);").append("\n");
		sb.append("\t\tsuper.setFields(" + sb4FieldArgs + ");").append("\n");
		sb.append("\t}").append("\n\n");

		sb.append(sb4GetSet.toString());
		sb.append("}");
		return sb.toString();
	}

	static String type2String(String value) {
		value = value.trim().toLowerCase();
		if (value.equals("int")) {
			return "int";
		} else if (value.equals("bigdecimal")) {
			return "java.math.BigDecimal";
		} else if (value.equals("date")) {
			return "java.util.Date";
		} else if (value.equals("byte[]")) {
			return "byte[]";
		} else if (value.equals("boolean")) {
			return "boolean";
		} else if (value.equals("string")) {
			return "java.lang.String";
		} else {
			return null;
		}
	}

	static String initField(String type) {
		if ("java.math.BigDecimal".equals(type)) {
			return "new java.math.BigDecimal(\"0\")";
		} else if ("int".equals(type)) {
			return "0";
		} else {
			return null;
		}
	}

	public static void main(String[] args) {
		CreateDaoXmlUtil.createFile("", "utf-8");
	}

}
