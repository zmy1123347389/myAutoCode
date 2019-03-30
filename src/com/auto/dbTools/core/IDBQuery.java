package com.auto.dbTools.core;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface IDBQuery {
	/**
	 * 数据库查询
	 * @param sql
	 * @param map
	 * @return
	 * @throws SQLException
	 */
	public List query(String sql, Map<String,String> map) throws SQLException;

	/**
	 * 初始化数据库连接，如果连接已经存在则不重复建立连接
	 */
	public void initDB();
	
	/**
	 * 销毁数据库连接
	 */
	public void destory();
		
}
