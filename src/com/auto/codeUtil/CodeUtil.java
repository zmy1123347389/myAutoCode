package com.auto.codeUtil;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.auto.bean.CodeBean;
import com.auto.dbTools.DBQueryBase;
import com.auto.dbTools.DBQueryUtil;
import com.auto.io.WriteFileTools;
import com.auto.util.CodeChar;

@SuppressWarnings("all")
public class CodeUtil {
	private DBQueryUtil dbutil = new DBQueryUtil();
	private WriteFileTools fileUtil = new WriteFileTools();
	private CodeConfig codeConfig = new CodeConfig();
	private String beanStr;
	private String tableName;

	// 获取格式化当前时间
	Date date = new Date();

	SimpleDateFormat sdformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 24小时制

	String LgTime = sdformat.format(date);

	public void writeCodeToFile(CodeBean param) {
		String[] tabArr = check(param);
		try {
			fileUtil.createFile(param.getPath());
			for (int i = 0; i < tabArr.length; i++) {
				List listTabCol = dbutil.queryColsByTableName(tabArr[i].toUpperCase(), param.getQueryTableNameSql(),
						param.getQueryColSql(), param);
				// 表注释
				String sql = "select table_name,table_comment from information_schema.tables where table_name ='"
						+ tabArr[i] + "'";
				List listTable = dbutil.query1(sql, null);
				String tableComment = "";
				if (listTable != null && listTable.size() > 0) {
					tableComment = ((Map) listTable.get(0)).get("TABLECOMMENT").toString();
				}

				if (listTabCol == null || listTabCol.size() == 0) {
					throw new NullPointerException("锟�" + tabArr[i] + ",锟斤拷锟轿猴拷锟斤拷!");
				}
				String table = tabArr[i];
				String tableName2 = "";
				if (table.contains("t_")) {
					//去除前端标识
					tableName = tabArr[i].substring(2);
					String replace = tableName.replace("_", " ");
					tableName2 = CodeUtil.method1(replace).replace(" ", "");
				} else {
					String replace = table.replace("_", " ");
					tableName2 = CodeUtil.method1(replace).replace(" ", "");

				}
				param.setTableName(tableName2);
				String basePackage = param.getBasePackage();
				String fileauthor = param.getFileauthor();
				String projectName = param.getProjectName();
				// basePackage += "." + tableName;// net.eshop.services.lable
				String actionBasePackage = param.getActionBasePackage();// net.eshop.web.action.store
				String basePackageFilepath = param.getPath() + File.separator + basePackage.replaceAll("\\.", "\\\\");
				String actionBasePackageFilepath = param.getPath() + File.separator
						+ actionBasePackage.replaceAll("\\.", "\\\\");
				String beanPackage = basePackage + ".entity;";
				String daoPackage = basePackage + ".dao;";
				String implPackage = basePackage + ".service;";
				String serviceimplPackage = basePackage + ".service.impl;";
				String controllerPackage = basePackage + ".controller;";
				String java = ".java";
				String _r = "\r";

				/**
				 * 写entity
				 */
				StringBuilder listFtlBuff = new StringBuilder();
				String _t = CodeChar.space1;
				String beanFilepath = basePackageFilepath + File.separator + File.separator + tableName2
						+ java;// bean锟斤拷全路锟斤拷
				StringBuilder beanBuff = new StringBuilder();
				beanBuff.append("package " + beanPackage + _r);
				beanBuff.append("import java.io.Serializable;" + _r);
				beanBuff.append("import java.util.Date;" + _r);
				if (tableComment != "") {
					beanBuff.append("//" + tableComment + _r);
				}
				beanBuff.append("public class " + tableName2 + " implements Serializable {" + _r + _r);
				beanBuff.append(_t + "private static final long serialVersionUID = 1L;" + _r + _r);
				StringBuilder beanSubBuff = new StringBuilder();
				for (int j = 0; j < listTabCol.size(); j++) {
					Object e = listTabCol.get(j);
					String colName = ((Map) e).get("columnName".toUpperCase()).toString();
					colName = colName.replace("_", " ");
					colName = CodeUtil.method1(colName).replace(" ", "");
					colName = colName.substring(0, 1).toLowerCase() + colName.substring(1);
					String dataType = ((Map) e).get("DATATYPE").toString();
					String comment = ((Map) e).get("COMMENT").toString();
					// 转换类型
					dataType = dataType.contains("time") ? "Date"
							: ("date".equals(dataType) ? "Date" : (dataType.contains("int") ? "String" : "String"));
					if (comment != "" && comment != null) {
						beanBuff.append(_t + "private " + dataType + " " + colName + ";//" + comment + _r);
					} else {
						beanBuff.append(_t + "private  " + dataType + " " + colName + ";" + _r);
					}
					if (colName.length() > 4) {
						String isEnum = colName.substring(colName.length() - 4);
						if (isEnum.equals("Enum")) {
							beanBuff.append(_t + "private Enumeration " + colName + "Object;" + _r);
						}
					}
				}
				// 无参构造器
				beanBuff.append(_t + "public " + tableName2 + "() {}" + _r + _r);
				for (int j = 0; j < listTabCol.size(); j++) {
					Object e = listTabCol.get(j);
					String colName = ((Map) e).get("columnName".toUpperCase()).toString();
					String colName2 = colName.substring(0, 1).toUpperCase() + colName.substring(1);
					colName2 = colName.replace("_", " ");
					colName2 = CodeUtil.method1(colName2).replace(" ", "");
					String colName3 = colName2.substring(0, 1).toLowerCase() + colName2.substring(1);
					String dataType = ((Map) e).get("DATATYPE").toString();
					String date = dataType;
					dataType = dataType.contains("time") ? "Date"
							: ("date".equals(dataType) ? "Date" : (dataType.contains("int") ? "String" : "String"));
					beanBuff.append(_t + "public " + dataType + " get" + colName2 + "() {" + _r);
					beanBuff.append(_t + _t + "return " + colName3 + ";" + _r);
					beanBuff.append(_t + "}" + _r + _r);
					beanBuff.append(_t + "public void set" + colName2 + "(" + dataType + " " + colName3 + ") {" + _r);
					beanBuff.append(_t + _t + "this." + colName3 + " = " + colName3 + ";" + _r);
					beanBuff.append(_t + "}" + _r + _r);
					if (colName3.length() > 4) {
						String isEnum = colName3.substring(colName3.length() - 4);
						if (isEnum.equals("Enum")) {
							beanBuff.append(_t + "public Enumeration get" + colName2 + "Object() {" + _r);
							beanBuff.append(_t + _t + "return " + colName3 + "Object;" + _r);
							beanBuff.append(_t + "}" + _r + _r);
							beanBuff.append(_t + "public void set" + colName2 + "Object(Enumeration " + colName3
									+ "Object) {" + _r);
							beanBuff.append(_t + _t + "this." + colName3 + "Object = " + colName3 + "Object;" + _r);
							beanBuff.append(_t + "}" + _r + _r);
						}
					}
				}
				beanBuff.append("}" + _r);
				fileUtil.writeJava(beanFilepath, beanBuff.toString());
				String tableName2s = tableName2;
				String substring1 = tableName2s.substring(0,1);
				String tableNameed = substring1.toLowerCase() + tableName2s.substring(1);
				/**
				 * 写Mapper
				 */
				StringBuilder listFtlBuffDao = new StringBuilder();
				String daoFilepath = basePackageFilepath + File.separator + File.separator + tableName2 +"Mapper"
						+ java;// bean锟斤拷全路锟斤拷
				StringBuilder daoBuff = new StringBuilder();
				daoBuff.append("package " + daoPackage + _r+ _r);
				daoBuff.append("import java.util.List;"+_r + _r);
				daoBuff.append("import com.article.entity."+param.getTableName()+ ";"+_r + _r);
				if (tableComment != "") {
					daoBuff.append("//" + tableComment + _r);
				}
				daoBuff.append("public interface " + tableName2 +"Mapper {" + _r + _r);
				daoBuff.append(_t + tableName2+" getBy"+tableName2+"("+tableName2+" "+tableNameed+");"+ _r+ _r);
				daoBuff.append(_t + "List<"+tableName2+">"+" listPage"+tableName2+"("+tableName2+" "+tableNameed+");"+ _r+ _r);
				daoBuff.append(_t + "int insert"+tableName2+"("+tableName2+" "+tableNameed+");" + _r+ _r);
				daoBuff.append(_t + "int delete"+tableName2+"(String id);" + _r+ _r);
				daoBuff.append(_t + "int update"+tableName2+"("+tableName2+" "+tableNameed+");" + _r+ _r);
				daoBuff.append("}" + _r);
				fileUtil.writeJava(daoFilepath, daoBuff.toString());
				
				/**
				 * 写servicePackage
				 */
				StringBuilder listFtlBuffService = new StringBuilder();
				String serviceFilepath = basePackageFilepath + File.separator + File.separator + tableName2 +"Service"
						+ java;// bean锟斤拷全路锟斤拷
				StringBuilder serviceBuff = new StringBuilder();
				serviceBuff.append("package " + implPackage + _r+ _r);
				serviceBuff.append("import java.util.List;" + _r+ _r);
				serviceBuff.append("import com.article.entity."+param.getTableName()+ ";"+_r+_r);
				if (tableComment != "") {
					serviceBuff.append("//" + tableComment + _r);
				}
				serviceBuff.append("public interface " + tableName2 +"Service{" + _r + _r);
				serviceBuff.append(_t + tableName2+" getBy"+tableName2+"("+tableName2+" "+tableNameed+");"+ _r+ _r);
				serviceBuff.append(_t + "List<"+tableName2+">"+" listPage"+tableName2+"("+tableName2+" "+tableNameed+");"+ _r+ _r);
				serviceBuff.append(_t + "int insert"+tableName2+"("+tableName2+" "+tableNameed+");" + _r+ _r);
				serviceBuff.append(_t + "int delete"+tableName2+"(String id);" + _r+ _r);
				serviceBuff.append(_t + "int update"+tableName2+"("+tableName2+" "+tableNameed+");" + _r+ _r);
				serviceBuff.append("}" + _r);
				fileUtil.writeJava(serviceFilepath, serviceBuff.toString());
				
				
				/**
				 * 写serviceimplPackage
				 */
				StringBuilder listFtlBuffServiceimpl = new StringBuilder();
				String serviceimplFilepath = basePackageFilepath + File.separator + File.separator + tableName2 +"ServiceImpl"
						+ java;// bean锟斤拷全路锟斤拷
				StringBuilder serviceimplBuff = new StringBuilder();
				serviceimplBuff.append("package " + serviceimplPackage + _r+ _r);
				serviceimplBuff.append("import java.util.List;" + _r+ _r);
				serviceimplBuff.append("import javax.annotation.Resource;"+_r+_r);
				serviceimplBuff.append("import org.springframework.stereotype.Service;"+_r+_r);
				serviceimplBuff.append("import com.article.dao."+param.getTableName()+ "Mapper;"+_r);
				serviceimplBuff.append("import com.article.entity."+param.getTableName()+ ";"+_r);
				serviceimplBuff.append("import com.article.service."+param.getTableName()+ "Service;"+_r+_r);
				if (tableComment != "") {
					serviceimplBuff.append("//" + tableComment + _r);
				}
				serviceimplBuff.append("@Service" + _r);
				serviceimplBuff.append("public class " + tableName2 +"ServiceImpl implements "+tableName2+"Service {" + _r + _r);
				serviceimplBuff.append(_t + "@Resource" + _r);
				serviceimplBuff.append(_t + "private "+tableName2+"Mapper "+tableNameed+"Mapper;" + _r + _r);
				serviceimplBuff.append(_t + "@Override" + _r);
				serviceimplBuff.append(_t + "public "+ tableName2+" getBy"+tableName2+"("+tableName2+" "+tableNameed+"){"+ _r);
				serviceimplBuff.append(_t + _t + "return "+tableNameed+"Mapper.getBy"+tableName2+"("+tableNameed+");"+ _r);
				serviceimplBuff.append(_t + "}"+ _r+ _r);
				serviceimplBuff.append(_t + "@Override" + _r);
				serviceimplBuff.append(_t + "public "+"List<"+tableName2+">"+" listPage"+tableName2+"("+tableName2+" "+tableNameed+"){"+ _r);
				serviceimplBuff.append(_t + _t + "return "+tableNameed+"Mapper.listPage"+tableName2+"("+tableNameed+");"+ _r);
				serviceimplBuff.append(_t + "}"+ _r+ _r);
				serviceimplBuff.append(_t + "@Override" + _r );
				serviceimplBuff.append(_t + "public int insert"+tableName2+"("+tableName2+" "+tableNameed+"){" + _r);
				serviceimplBuff.append(_t + _t + "return "+tableNameed+"Mapper.insert"+tableName2+"("+tableNameed+");"+ _r);
				serviceimplBuff.append(_t + "}" + _r+ _r);
				serviceimplBuff.append(_t + "@Override" + _r);
				serviceimplBuff.append(_t + "public int delete"+tableName2+"(String id){" + _r);
				serviceimplBuff.append(_t + _t + "return "+tableNameed+"Mapper.delete"+tableName2+"(id);"+ _r);
				serviceimplBuff.append(_t + "}" + _r+ _r);
				serviceimplBuff.append(_t + "@Override" + _r);
				serviceimplBuff.append(_t + "public int update"+tableName2+"("+tableName2+" "+tableNameed+"){" + _r);
				serviceimplBuff.append(_t + _t + "return "+tableNameed+"Mapper.update"+tableName2+"("+tableNameed+");"+ _r);
				serviceimplBuff.append(_t + "}" + _r);
				serviceimplBuff.append("}" + _r);
				fileUtil.writeJava(serviceimplFilepath, serviceimplBuff.toString());
				
				/**
				 * 写controller
				 */
				StringBuilder listFtlBuffController = new StringBuilder();
				String controllerFilepath = basePackageFilepath + File.separator + File.separator + tableName2 +"Controller"
						+ java;// bean锟斤拷全路锟斤拷
				StringBuilder controllerBuff = new StringBuilder();
				controllerBuff.append("package " + controllerPackage + _r+ _r);
				controllerBuff.append("import java.util.List;" + _r+ _r);
				controllerBuff.append("import javax.annotation.Resource;" + _r+ _r);
				controllerBuff.append("import org.springframework.stereotype.Controller;" + _r);
				controllerBuff.append("import org.springframework.web.bind.annotation.RequestMapping;" + _r);
				controllerBuff.append("import org.springframework.web.bind.annotation.RequestMethod;" + _r);
				controllerBuff.append("import org.springframework.web.bind.annotation.ResponseBody;"+_r + _r);
				
				controllerBuff.append("import com.article.entity."+param.getTableName()+ ";"+_r);
				controllerBuff.append("import com.article.service."+param.getTableName()+ "Service;"+_r + _r);
				if (tableComment != "") {
					controllerBuff.append("//" + tableComment + _r);
				}
				controllerBuff.append("@Controller" + _r);
				controllerBuff.append("@RequestMapping(\""+tableNameed+"\")" + _r);
				controllerBuff.append("public class " + tableName2 +"Controller{" + _r);
				controllerBuff.append(_t + "@Resource" + _r);
				controllerBuff.append(_t + "private "+tableName2+"Service "+tableNameed+"Service;" + _r + _r);
				
				controllerBuff.append(_t + "@RequestMapping(value = \"getBy"+tableName2+"\", method = RequestMethod.POST, produces = \"text/html;charset=UTF-8\")" + _r );
				controllerBuff.append(_t + "@ResponseBody" + _r);
				controllerBuff.append(_t + "public "+ tableName2+" getBy"+tableName2+"("+tableName2+" "+tableNameed+"){"+ _r);
				controllerBuff.append(_t + _t + "return "+tableNameed+"Service.getBy"+tableName2+"("+tableNameed+");"+ _r);
				controllerBuff.append(_t + "}"+ _r+ _r);
				controllerBuff.append(_t + "@RequestMapping(value = \"listPage"+tableName2+"\", method = RequestMethod.POST, produces = \"text/html;charset=UTF-8\")" + _r );
				controllerBuff.append(_t + "@ResponseBody" + _r);
				controllerBuff.append(_t + "public List<"+tableName2+">"+" listPage"+tableName2+"("+tableName2+" "+tableNameed+"){"+ _r);
				controllerBuff.append(_t + _t + "return "+tableNameed+"Service.listPage"+tableName2+"("+tableNameed+");"+ _r);
				controllerBuff.append(_t + "}"+ _r+ _r);
				controllerBuff.append(_t + "@RequestMapping(value = \"insert"+tableName2+"\", method = RequestMethod.POST, produces = \"text/html;charset=UTF-8\")" + _r);
				controllerBuff.append(_t + "@ResponseBody" + _r);
				controllerBuff.append(_t + "public int insert"+tableName2+"("+tableName2+" "+tableNameed+"){" + _r);
				controllerBuff.append(_t + _t + "return "+tableNameed+"Service.insert"+tableName2+"("+tableNameed+");"+ _r);
				controllerBuff.append(_t + "}" + _r+ _r);
				controllerBuff.append(_t + "@RequestMapping(value = \"delete"+tableName2+"\", method = RequestMethod.POST, produces = \"text/html;charset=UTF-8\")" + _r);
				controllerBuff.append(_t + "@ResponseBody" + _r);
				controllerBuff.append(_t + "public int delete"+tableName2+"(String id){" + _r);
				controllerBuff.append(_t + _t + "return "+tableNameed+"Service.delete"+tableName2+"(id);"+ _r);
				controllerBuff.append(_t + "}" + _r+ _r);
				controllerBuff.append(_t + "@RequestMapping(value = \"update"+tableName2+"\", method = RequestMethod.POST, produces = \"text/html;charset=UTF-8\")" + _r);
				controllerBuff.append(_t + "@ResponseBody" + _r);
				controllerBuff.append(_t + "public int update"+tableName2+"("+tableName2+" "+tableNameed+"){" + _r);
				controllerBuff.append(_t + _t + "return "+tableNameed+"Service.update"+tableName2+"("+tableNameed+");"+ _r);
				controllerBuff.append(_t + "}" + _r+ _r);
				controllerBuff.append("}" + _r);
				fileUtil.writeJava(controllerFilepath, controllerBuff.toString());
				
				
				
				fileUtil.write(param, convertToXml(param, listTabCol));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			dbutil.destory();
		}
	}

	private List convertToXml(CodeBean param, List listTabCol) {
		List list = new ArrayList();
		list.addAll(myBatisXmlHead(param));
		list.addAll(queryByColumn(param, listTabCol));
		list.addAll(Base_Column_List(param, listTabCol));
		list.addAll(insertSql(param, listTabCol));
		list.addAll(insertListSql(param, listTabCol));
		list.addAll(insertStr(param, listTabCol));
		list.addAll(updateStr(param, listTabCol));
		list.addAll(insertSqlWhere(param, listTabCol));
		list.addAll(deleteSql(param, listTabCol));
		list.addAll(myBatisXmlTail());
		return list;
	}

	private List myBatisXmlTail() {
		List tailList = new ArrayList();
		tailList.add("</mapper>");
		return tailList;
	}

	private List myBatisXmlHead(CodeBean param) {
		List headList = new ArrayList();
		headList.add("<?xml version=\"1.0\" encoding=\"" + codeConfig.getEncode().trim() + "\"?>");
		headList.add(
				"<!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\" \"http://mybatis.org/dtd/mybatis-3-mapper.dtd\">");
		headList.add("<!-- create by autoCode," + CodeConfig.version + " -->");
		headList.add("<mapper namespace=\"com.article.dao."+param.getTableName() +"Mapper\">");
		return headList;
	}

	public List queryByColumn(CodeBean param, List listTabCol) {
		String _t = CodeChar.space1;
		String _r = "\r";
		List headList = new ArrayList();
		headList.add("<resultMap type=\"com.article.entity."+param.getTableName()+"\"" + " id=\"BaseResultMap \">");
		for (int j = 0; j < listTabCol.size(); j++) {
			String sss = "";
			Object e = listTabCol.get(j);
			String colName = ((Map) e).get("columnName".toUpperCase()).toString();
			sss = colName;
			colName = colName.replace("_", " ");
			colName = CodeUtil.method1(colName).replace(" ", "");
			colName = colName.substring(0, 1).toLowerCase() + colName.substring(1);
			if (j == 0) {
				headList.add(_t + "<id column= \"" + sss + "\" property=\"" + colName + "\"/>");
			} else {
				headList.add(_t + "<result column= \"" + sss + "\" property=\"" + colName + "\"/>");
			}

			// }
		}
		headList.add("</resultMap>");
		return headList;
	}

	private List Base_Column_List(CodeBean param, List listTabCol) {
		List sqlList = new ArrayList();
		sqlList.add(CodeChar.space1 + "<sql id=\"Base_Column_List\">");
		boolean isFirst = true;
		String colName = "";
		for (int i = 0; i < listTabCol.size(); i++) {
			String sss = "";
			Object e = listTabCol.get(i);
			colName = ((Map) e).get("columnName".toUpperCase()).toString();
			if(i!=0){
				sqlList.add(CodeChar.space1 +"," + colName);
			}else{
				sqlList.add(CodeChar.space1 +colName);
			}
		}
		sqlList.add(CodeChar.space1 + "</sql>");
		return sqlList;
	}
	
	
	private List insertSql(CodeBean param, List listTabCol) {
		List sqlList = new ArrayList();
		// id=锟斤拷锟斤拷.insert
		sqlList.add(CodeChar.space1 + "<select id=\"getBy"+param.getTableName()+"\" parameterType=\"com.article.entity."+param.getTableName()+"\" resultMap=\"baseResultMap\" >");
		sqlList.add(CodeChar.space1 + "select");
		sqlList.add(CodeChar.space1 + "<include refid=\"Base_Column_List\" />");
		sqlList.add(CodeChar.space1 + "from "+param.getTabStr());
		sqlList.add(CodeChar.space1 + "<where>");
		sqlList.add(CodeChar.space1 + "<include refid=\"condition_sql\" />");
		sqlList.add(CodeChar.space1 + "</where>");
		sqlList.add(CodeChar.space1 + "</select>");
		return sqlList;
	}
	private List insertListSql(CodeBean param, List listTabCol) {
		List sqlList = new ArrayList();
		// id=锟斤拷锟斤拷.insert
		sqlList.add(CodeChar.space1 + "<select id=\"listPage"+param.getTableName()+"\" parameterType=\"com.article.entity."+param.getTableName()+ "\" resultMap=\"BaseResultMap\">");
		sqlList.add(CodeChar.space1 + "select");
		sqlList.add(CodeChar.space1 + "<include refid=\"Base_Column_List\" />");
		sqlList.add(CodeChar.space1 + "from "+param.getTabStr());
		sqlList.add(CodeChar.space1 + "<where>");
		sqlList.add(CodeChar.space1 + "<include refid=\"condition_sql\" />");
		sqlList.add(CodeChar.space1 + "</where>");
		sqlList.add(CodeChar.space1 + "</select>");
		return sqlList;
	}
	private List deleteSql(CodeBean param, List listTabCol) {
		List sqlList = new ArrayList();
		// id=锟斤拷锟斤拷.insert
		sqlList.add(CodeChar.space1 + "<select id=\"delete"+param.getTableName()+"\"  parameterType=\"java.lang.String\">");
		sqlList.add(CodeChar.space1 + "delete from "+ param.getTabStr() +" where id = #{id}");
		sqlList.add(CodeChar.space1 + "</select>");
		return sqlList;
	}
	
	private List insertSqlWhere(CodeBean param, List listTabCol) {
		List sqlList = new ArrayList();
		sqlList.add(CodeChar.space1 + "<sql id=\"condition_sql\">");
		boolean isFirst = true;
		String colName = "";
		for (int i = 0; i < listTabCol.size(); i++) {
			String sss = "";
			Object e = listTabCol.get(i);
			colName = ((Map) e).get("columnName".toUpperCase()).toString();
			sss = colName;
			colName = colName.replace("_", " ");
			colName = CodeUtil.method1(colName).replace(" ", "");
			colName = colName.substring(0, 1).toLowerCase() + colName.substring(1);
			String dataType = ((Map) listTabCol.get(i)).get("DATATYPE").toString();
			sqlList.add(CodeChar.space2 + "<if test=\"" + colName + " != null and " + colName + " !=''\">");
			sqlList.add(CodeChar.space2 + "and "+sss+" = #{"+colName+",jdbcType=VARCHAR}");
			sqlList.add(CodeChar.space2 + "</if>");
		}
		sqlList.add(CodeChar.space1 + "</sql>");
		return sqlList;
	}
	
	private List insertStr(CodeBean param, List listTabCol) {
		List insertList = new ArrayList();
		// id=锟斤拷锟斤拷.insert
		insertList.add(CodeChar.space1 + "<insert id=\"insert"+param.getTableName()+"\" parameterType=\"com.article.entity."+param.getTableName()
				+ "\" useGeneratedKeys=\"true\" keyProperty=\"id\">");
		insertList.add(CodeChar.space2 + "insert into " + param.getTabStr());
		insertList.add(CodeChar.space2 + "(");
		boolean isFirst = true;
		String colName = "";
		for (int i = 0; i < listTabCol.size(); i++) {
			String sss = "";
			Object e = listTabCol.get(i);
			colName = ((Map) e).get("columnName".toUpperCase()).toString();
			sss = colName;
			colName = colName.replace("_", " ");
			colName = CodeUtil.method1(colName).replace(" ", "");
			colName = colName.substring(0, 1).toLowerCase() + colName.substring(1);
			String dataType = ((Map) listTabCol.get(i)).get("DATATYPE").toString();
			if (isFirst) {
				insertList.add(CodeChar.space2 + colName);
				isFirst = false;
			} else {
				insertList.add(CodeChar.space2 + "<if test=\"" + colName + " !=null and " + colName + " !=''\">");
				insertList.add(CodeChar.space2 + "," + sss);
				insertList.add(CodeChar.space2 + "</if>");
			}
		}
		insertList.add(CodeChar.space2 + ")");
		insertList.add(CodeChar.space2 + "values");
		insertList.add(CodeChar.space2 + "(");
		isFirst = true;
		for (int i = 0; i < listTabCol.size(); i++) {
			colName = ((Map) listTabCol.get(i)).get("columnName".toUpperCase()).toString();
			colName = colName.replace("_", " ");
			colName = CodeUtil.method1(colName).replace(" ", "");
			colName = colName.substring(0, 1).toLowerCase() + colName.substring(1);
			if (isFirst) {
				insertList.add(CodeChar.space2 + colName);
				isFirst = false;
			} else {
				String dataType = ((Map) listTabCol.get(i)).get("DATATYPE").toString();
				if (dataType.indexOf("int") == -1) {
					insertList.add(CodeChar.space2 + "<if test=\"" + colName + " !=null and " + colName + " !=''\">");
				} else {
					insertList.add(CodeChar.space2 + "<if test=\"" + colName + "!=0 and " + colName + "!=null and "
							+ colName + "!=''\">");
				}
				if (dataType.equals("DATE")) {
					insertList.add(CodeChar.space2 + ",to_date(#{" + colName + "},'yyyy-mm-dd hh24:mi:ss')");
				} else {
					insertList.add(CodeChar.space2 + ",#{" + colName + "}");
				}
				insertList.add(CodeChar.space2 + "</if>");
			}
		}
		insertList.add(CodeChar.space1 + ")</insert>");
		return insertList;
	}

	/**
	 * @param i
	 * @param listTabCol
	 * @param tableName2
	 * @param basePackage
	 * @param beanPackage
	 * @param java
	 * @param _r
	 * @throws IOException
	 */
	private void writeBean(int i, List listTabCol, String tableName2, String basePackageFilepath, String beanPackage,
			String java, String _r) throws IOException {

	}

	private String[] check(CodeBean param) {
		if (param.getPath() == null || param.getPath().equals("") || param.getQueryTableNameSql() == null
				|| param.getQueryTableNameSql().equals("") || param.getQueryColSql() == null
				|| param.getQueryColSql().equals("") || param.getTabStr() == null || param.getTabStr().equals("")) {
			throw new IllegalArgumentException("锟斤拷锟捷的诧拷锟斤拷锟斤拷确!");
		}
		String[] tabArr = param.getTabStr().split(",");
		if (tabArr == null || tabArr.length == 0) {
			throw new IllegalArgumentException("没锟斤拷锟轿何憋拷锟斤拷锟�");
		}
		return tabArr;
	}

	private List updateStr(CodeBean param, List listTabCol) {
		List updateList = new ArrayList();
		// id=锟斤拷锟斤拷.update
		updateList.add(CodeChar.space1 + "<update id=\"update" + param.getTableName() +"\" parameterType=\"com.article.entity."+param.getTableName() + "\">");
		updateList.add(CodeChar.space2 + "update " + param.getTabStr());
		boolean isFirst = true;
		String colName = "", firstColName = "";
		for (int i = 0; i < listTabCol.size(); i++) {
			String sss = "";
			colName = ((Map) listTabCol.get(i)).get("columnName".toUpperCase()).toString();
			sss = colName;
			colName = ((Map) listTabCol.get(i)).get("columnName".toUpperCase()).toString();
			colName = colName.replace("_", " ");
			colName = CodeUtil.method1(colName).replace(" ", "");
			colName = colName.substring(0, 1).toLowerCase() + colName.substring(1);
			if (isFirst) {
				firstColName = colName;
				updateList.add(CodeChar.space2 + "  set " + colName + "=#{" + colName + "}");
				isFirst = false;
			} else {
				String dataType = ((Map) listTabCol.get(i)).get("DATATYPE").toString();

				if (dataType.indexOf("int") == -1) {
					updateList.add(CodeChar.space2 + "<if test=\"" + colName + " !=null and " + colName + " !=''\">");
				} else {
					updateList.add(CodeChar.space2 + "<if test=\"" + colName + "!=0 and " + colName + "!=null and "
							+ colName + "!=''\">");
				}

				if (dataType.equals("DATE")) {
					updateList
							.add(CodeChar.space2 + "," + sss + "=to_date(#{" + colName + "},'yyyy-mm-dd hh24:mi:ss')");
				} else {
					updateList.add(CodeChar.space2 + "," + sss + "=#{" + colName + "}");
				}
				updateList.add(CodeChar.space2 + "</if>");
			}
		}
		updateList.add(CodeChar.space2 + " where 1=1 and " + firstColName + "=#{" + firstColName + "}");
		updateList.add(CodeChar.space1 + "</update>");
		return updateList;
	}

	// 锟窖诧拷锟斤拷锟斤拷锟絁SON锟斤拷式
	private List JSON(CodeBean param, List listTabCol) {
		List list = new ArrayList();
		list.add("");
		list.add("");
		list.add("[一锟斤拷锟斤拷insert or update锟侥诧拷锟斤拷锟斤拷锟絔锟斤拷");
		list.add("");
		String comments = "";
		list.add("{");
		for (int i = 0; i < listTabCol.size(); i++) {
			String colName = ((Map) listTabCol.get(i)).get("columnName".toUpperCase()).toString().toLowerCase();

			if (listTabCol.get(i) != null && !listTabCol.get(i).toString().equals("")
					&& ((Map) listTabCol.get(i)).get("comments".toUpperCase()) != null) {
				comments = ((Map) listTabCol.get(i)).get("comments".toUpperCase()).toString();
				// System.out.println(comments);
			}
			list.add(colName + " : " + codeConfig.getParamPrefix() + colName + ".text,"
					+ (codeConfig.getAddIsMents() ? ("//" + comments) : ""));
			comments = "";
		}
		String lastStr = list.get(list.size() - 1).toString();
		int i = lastStr.lastIndexOf(",");
		lastStr = lastStr.substring(0, i) + lastStr.substring(i + 1);
		list.set(list.size() - 1, lastStr);
		list.add("}");
		return list;
	}

	// 通锟斤拷JSON去锟斤拷锟截硷拷
	private List setValueByJson(CodeBean param, List listTabCol) {
		List list = new ArrayList();
		list.add("");
		list.add("");
		list.add("[一锟斤拷锟斤拷setValueByJson锟侥诧拷锟斤拷锟斤拷锟絔锟斤拷");
		list.add("");
		String comments = "";
		for (int i = 0; i < listTabCol.size(); i++) {
			String colName = ((Map) listTabCol.get(i)).get("columnName".toUpperCase()).toString().toLowerCase();

			if (listTabCol.get(i) != null && !listTabCol.get(i).toString().equals("")
					&& ((Map) listTabCol.get(i)).get("comments".toUpperCase()) != null) {
				comments = ((Map) listTabCol.get(i)).get("comments".toUpperCase()).toString();
				// System.out.println(comments);
			}
			list.add(codeConfig.getParamPrefix() + colName + ".text = obj[\"" + colName + "\"];"
					+ (codeConfig.getAddIsMents() ? ("//" + comments) : ""));
			comments = "";
		}
		return list;
	}

	public static void main(String[] args) {
		System.out.println("aaa.b".replaceAll("\\.", "}"));
	}

	// 字符串全盘大写
	public static String method1(String str) {
		String finalstr = "";
		String[] ele = str.split(" ");
		for (int i = 0; i < ele.length; i++) {
			ele[i] = ele[i].substring(0, 1).toUpperCase() + ele[i].substring(1);
			finalstr = finalstr + ele[i] + " ";
		}
		finalstr = finalstr.substring(0, finalstr.length() - 1);
		return finalstr;
	}
}
