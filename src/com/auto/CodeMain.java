package com.auto;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;

import com.auto.bean.CodeBean;
import com.auto.codeUtil.CodeUtil;
import com.auto.codeUtil.DbType;
import com.auto.util.PropertiesUtil;

/**
 * mybaties生成文件
 * 
 * @author zmy 日期 2018年8月13日
 */
public class CodeMain {
	public static void main(String[] args) throws SQLException, IOException {
		System.out.println("columnName".toUpperCase());
		PropertiesUtil.getInstance();
		CodeBean param = new CodeBean();
		// 数据库类型
		param.setDbType(DbType.mysql);
		// 基础包名
		param.setBasePackage("com.article");//
		// 生成类路径
		param.setActionBasePackage("com.article");
		// 文件保存路径
		param.setPath("D:/WorkSpace/auto");
		// 待生成表
		param.setTabStr("user");

		param.setQueryTableNameSql("queryTableSql");
		param.setQueryColSql("queryTableInfoSql");

		new CodeUtil().writeCodeToFile(param);
		System.out.println("生成文件路径" + param.getPath());
	}
}
