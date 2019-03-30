package com.auto.dbTools.core;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface IDBQuery {
	/**
	 * ���ݿ��ѯ
	 * @param sql
	 * @param map
	 * @return
	 * @throws SQLException
	 */
	public List query(String sql, Map<String,String> map) throws SQLException;

	/**
	 * ��ʼ�����ݿ����ӣ���������Ѿ��������ظ���������
	 */
	public void initDB();
	
	/**
	 * �������ݿ�����
	 */
	public void destory();
		
}
