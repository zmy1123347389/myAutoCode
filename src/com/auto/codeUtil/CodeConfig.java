package com.auto.codeUtil;

public class CodeConfig {
	public static final String version = "v1.0.0";
	private String lowerOrUpper = "0";// 0:Сд��1����д,2:���ı�
	private String encode = "UTF-8";// �ַ����
	private Boolean addIsMents = true;// �Ƿ����ע��,xml�ļ������ע��,XMXL�����
	private String paramPrefix = "";// pp_

	public String getLowerOrUpper() {
		return lowerOrUpper;
	}

	public void setLowerOrUpper(String lowerOrUpper) {
		this.lowerOrUpper = lowerOrUpper;
	}

	public String getEncode() {
		return encode;
	}

	public void setEncode(String encode) {
		this.encode = encode;
	}

	public Boolean getAddIsMents() {
		return addIsMents;
	}

	public void setAddIsMents(Boolean addIsMents) {
		this.addIsMents = addIsMents;
	}

	public String getParamPrefix() {
		return paramPrefix;
	}

	public void setParamPrefix(String paramPrefix) {
		this.paramPrefix = paramPrefix;
	}

}
