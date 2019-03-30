package com.auto.dbTools;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.auto.dbTools.core.DBConnectionManager;
import com.auto.dbTools.core.IDBQuery;

public class DBQueryBase implements IDBQuery {
	private Connection conn = null;
	private boolean connIsInit = false;

	/**
	 * mysql�Ĳ�ѯ��������ѯ����ơ�������
	 */
	public List query(String sql, Map<String,String> map) throws SQLException {
		PreparedStatement pstmt = null;
		ResultSet res = null;
		List resultList = null;
		try {
			initDB();
//			sql = "describe t_goods";
			pstmt = this.conn.prepareStatement(sql);//"show columns from "+"CRAWLDB.T_ACCOUNT");//sql);
			System.out.println(sql);
//			Iterator it = map.keySet().iterator();
//			int k=1;
//			while (it.hasNext()) {
//				String key = (String) it.next();
//				System.out.println("vv="+map.get(key).toString());
//				pstmt.setString(k, map.get(key).toString());
//				k++;
//			}
//			res = pstmt.executeQuery();
//			Map<String, Object> rowMap = null;
//			resultList = new ArrayList();
//			while (res.next()) {
//				ResultSetMetaData resMeta = res.getMetaData();
//				rowMap = new HashMap<String, Object>();
//				for (int i = 0; i < resMeta.getColumnCount(); i++) {
////					System.out.println("��ͷ��"+resMeta.getColumnName(i+1)+",����ݣ�"+res.getObject(resMeta.getColumnName(i+1)));
//					rowMap.put(resMeta.getColumnName(i+1), res.getObject(resMeta.getColumnName(i+1)));
//				}
//				resultList.add(rowMap);
//			}
			
			
			boolean c = pstmt.execute();  //����ر�Ҫע��:�����Oracle�����mysql���Բ��ü�.
//			ResultSetMetaData rsmd = (ResultSetMetaData) pstmt.getMetaData();
			
			Map<String, Object> rowMap = null;
			resultList = new ArrayList();
			ResultSet e = pstmt.getResultSet();
			while(e.next()){
					rowMap = new HashMap<String, Object>();
					rowMap.put("COLUMNNAME",e.getString(1));
					rowMap.put("DATATYPE",e.getString(2));
					rowMap.put("KEY",e.getString(5));
					rowMap.put("COMMENT",e.getString(9));//表字段注释 add by xueyb
					System.out.println(rowMap);
					resultList.add(rowMap);
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(" com.auto.dbTools.DBQueryBase��ѯִ��ʧ��!");
		}finally{
			DBConnectionManager.close(pstmt, res);
		}
		return resultList;
	}
	public List query1(String sql, Map<String,String> map) throws SQLException {
		PreparedStatement pstmt = null;
		ResultSet res = null;
		List resultList = null;
		try {
			initDB();
//			sql = "describe t_goods";
			pstmt = this.conn.prepareStatement(sql);//"show columns from "+"CRAWLDB.T_ACCOUNT");//sql);
			System.out.println(sql);
			boolean c = pstmt.execute();  //����ر�Ҫע��:�����Oracle�����mysql���Բ��ü�.
			
			Map<String, Object> rowMap = null;
			resultList = new ArrayList();
			ResultSet e = pstmt.getResultSet();
			while(e.next()){
				rowMap = new HashMap<String, Object>();
				rowMap.put("TABLENAME",e.getString(1));
				rowMap.put("TABLECOMMENT",e.getString(2));//表注释 add by xueyb
				System.out.println(rowMap);
				resultList.add(rowMap);
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(" com.auto.dbTools.DBQueryBase��ѯִ��ʧ��!");
		}finally{
			DBConnectionManager.close(pstmt, res);
		}
		return resultList;
	}
	
	/**
	 * oracle�Ĳ�ѯ��������ѯ����ơ�������
	 * @param sql
	 * @param map
	 * @return
	 * @throws SQLException
	 */
	public List queryOracle(String sql, Map<String,String> map) throws SQLException {
		PreparedStatement pstmt = null;
		ResultSet res = null;
		List resultList = null;
		try {
			initDB();
//			sql = "describe t_goods";
			pstmt = this.conn.prepareStatement(sql);//"show columns from   crawldb.t_account");//sql);
			System.out.println(sql);
			Iterator it = map.keySet().iterator();
			int k=1;
			while (it.hasNext()) {
				String key = (String) it.next();
				pstmt.setString(k, map.get(key).toString());
				k++;
			}
			res = pstmt.executeQuery();
			Map<String, Object> rowMap = null;
			resultList = new ArrayList();
			while (res.next()) {
				ResultSetMetaData resMeta = res.getMetaData();
				rowMap = new HashMap<String, Object>();
				for (int i = 0; i < resMeta.getColumnCount(); i++) {
//					System.out.println("��ͷ��"+resMeta.getColumnName(i+1)+",����ݣ�"+res.getObject(resMeta.getColumnName(i+1)));
					rowMap.put(resMeta.getColumnName(i+1), res.getObject(resMeta.getColumnName(i+1)));
				}
				resultList.add(rowMap);
			}
			
			
//			boolean c = pstmt.execute();  //����ر�Ҫע��:�����Oracle�����mysql���Բ��ü�.
//			ResultSetMetaData rsmd = (ResultSetMetaData) pstmt.getMetaData();
////           className="User";
////           tableName="user";
//            for (int i = 1; i < rsmd.getColumnCount() + 1; i++) {
////                Table table=new Table();
////                table.setColumnName(rsmd.getColumnName(i));
////                table.setColumnType(rsmd.getColumnClassName(i).substring(rsmd.getColumnClassName
////
////(i).lastIndexOf(".")+1));
////                tables.add(table);
//                System.out.println(rsmd.getColumnName(i)+ "  " +rsmd.getColumnTypeName(i)
//                        +"  " +rsmd.getColumnClassName(i)+ "  "+rsmd.getTableName(i));
//            }
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(" com.auto.dbTools.DBQueryBase��ѯִ��ʧ��!");
		}finally{
			DBConnectionManager.close(pstmt, res);
		}
		return resultList;
	}

	public void initDB() {
		if (!connIsInit) {
			if (this.conn == null) {
				this.conn = DBConnectionManager.getConnection();
				connIsInit = true;
			}
		}
	}

	public void destory() {
		DBConnectionManager.closeConn(conn, null, null);
	}
}
