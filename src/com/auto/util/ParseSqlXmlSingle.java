package com.auto.util;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 * Ω‚Œˆsrc/querySql.xml
 *
 */
public class ParseSqlXmlSingle {
	private static Map map = new HashMap();
	private static volatile boolean isParse = false;
	private static ParseSqlXmlSingle instance = new ParseSqlXmlSingle();
	private ParseSqlXmlSingle() {
	}

	public static ParseSqlXmlSingle getInstance() {
		if (!isParse) {
			parseXml();
		}
		return instance;
	}

	public static String get(String key) {
		return map.get(key).toString();
	}

	public static synchronized void parseXml() {
		File xmlFile = new File(PropertiesUtil.getInstance().get("sqlXml").toString());
		if(xmlFile==null && !xmlFile.exists())
			throw new NullPointerException("ªÒ»°sqlXml ß∞‹!");
		SAXReader reader = new SAXReader();
		Document document = null;
		try {
			document = reader.read(xmlFile);
		} catch (DocumentException e1) {
			e1.printStackTrace();
		}
		Element root = document.getRootElement();
		for (Iterator i = root.elementIterator(); i.hasNext();) {
			Element e = (Element) i.next();
			map.put(e.attributeValue("id"), e.getText());
		}
		isParse = true;
	}

//	public static void main(String[] args) throws DocumentException {
//		parseXml();
//	}
}
