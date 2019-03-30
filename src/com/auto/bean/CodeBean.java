package com.auto.bean;

import com.auto.codeUtil.DbType;

/**
 * 
 * @author Administrator
 *
 */
public class CodeBean {
	private String tabStr;// 待生成表名
	private String dbName;// 数据库名称
	private String path;// 路径
	private String tableName;// ��ѯ�ı���
	private String queryTableNameSql;// ��ѯ�����Ӧ��SQL��Ĭ�ϸ��ORACLE��ʵ�֣������滻���������ݿ��SQL
	private String queryColSql;// ��ѯ�����sql
	private DbType dbType;
	private String basePackage;// 实体基础包名
	private String actionBasePackage;//action路径

	private String fileauthor;// 通用注解

	private String projectName;// 项目名称版本

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		if ("".equals(projectName)) {
			this.projectName = "空项目版本";
		} else {
			this.projectName = projectName;
		}
	}

	public String getFileauthor() {
		return fileauthor;
	}

	public void setFileauthor(String fileauthor) {
		this.fileauthor = fileauthor;
	}

	public String getActionBasePackage() {
		return actionBasePackage;
	}

	public void setActionBasePackage(String actionBasePackage) {
		this.actionBasePackage = actionBasePackage;
	}

	public String getBasePackage() {
		return basePackage;
	}

	public void setBasePackage(String basePackage) {
		this.basePackage = basePackage;
	}

	public DbType getDbType() {
		return dbType;
	}

	public void setDbType(DbType dbType) {
		this.dbType = dbType;
	}

	public CodeBean() {
		super();
	}

	public String getTabStr() {
		return tabStr;
	}

	public void setTabStr(String tabStr) {
		this.tabStr = tabStr;
	}

	public String getDbName() {
		return dbName;
	}

	public void setDbName(String dbName) {
		this.dbName = dbName;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getQueryTableNameSql() {
		return queryTableNameSql;
	}

	public void setQueryTableNameSql(String queryTableNameSql) {
		this.queryTableNameSql = queryTableNameSql;
	}

	public String getQueryColSql() {
		return queryColSql;
	}

	public void setQueryColSql(String queryColSql) {
		this.queryColSql = queryColSql;
	}

}
