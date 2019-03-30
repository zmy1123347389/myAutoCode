package com.auto.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesUtil {
	private static PropertiesUtil instance = new PropertiesUtil();
	private static Properties p = new Properties();

	private PropertiesUtil() {
	}

	public static PropertiesUtil getInstance() {
		InputStream io = PropertiesUtil.class
				.getResourceAsStream("/jdbc.properties");
		try {
			p.load(io);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return instance;
	}

	public Object get(Object key) {
		return p.get(key);
	}

	public static void main(String[] args) {
		System.out.println("=="+PropertiesUtil.getInstance().get("query_table_sql"));;
	}
}
