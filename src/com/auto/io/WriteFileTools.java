package com.auto.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.List;

import com.auto.bean.CodeBean;

public class WriteFileTools {
	public void createFile(String path) throws IOException {
		File f = new File(path);
		f.delete();
		if (!f.exists()) {
			f.mkdirs();
		}
		if (!f.exists()) {
			throw new FileNotFoundException("�����ļ�ʧ��,�ļ���" + path);
		}
	}

	public void write(CodeBean param, List list) throws IOException {
		// ����һ���µ��ļ�������ļ����������ݻᱻ��д
		Writer out = null;
		try {
			out = new FileWriter(param.getPath() + File.separator + param.getTableName() + ".xml", false);
			for (int i = 0; i < list.size(); i++) {
				out.write(list.get(i).toString() + "\r");
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Exception:" + e.getMessage());
		} finally {
			if (out != null) {
				out.flush();
				out.close();
			}
			list = null;
		}
	}

	public void writeJava(String f, String list) throws IOException {
		// ����һ���µ��ļ�������ļ����������ݻᱻ��д
		Writer out = null;
		try {
			File dir = new File(f);
			new File(dir.getParent()).mkdirs();
			out = new FileWriter(f, false);
			out.write(list);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Exception:" + e.getMessage());
		} finally {
			if (out != null) {
				out.flush();
				out.close();
			}
			list = null;
		}
	}
}
