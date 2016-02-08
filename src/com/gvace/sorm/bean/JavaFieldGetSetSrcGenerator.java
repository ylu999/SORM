package com.gvace.sorm.bean;

/**
 * Java field, getter, setter source code generator 
 * @author Yushan Lu gvace.blogspot.com Feb 5, 2016
 *
 */
public class JavaFieldGetSetSrcGenerator {
	/**
	 * Field src code
	 */
	private String fieldSrcCode;
	/**
	 * getter src code
	 */
	private String getterSrcCode;
	/**
	 * setter src code
	 */
	private String setterSrcCode;
	
	public String getFieldSrcCode() {
		return fieldSrcCode;
	}
	public void setFieldSrcCode(String fieldSrcCode) {
		this.fieldSrcCode = fieldSrcCode;
	}
	public String getGetterSrcCode() {
		return getterSrcCode;
	}
	public void setGetterSrcCode(String getterSrcCode) {
		this.getterSrcCode = getterSrcCode;
	}
	public String getSetterSrcCode() {
		return setterSrcCode;
	}
	public void setSetterSrcCode(String setterSrcCode) {
		this.setterSrcCode = setterSrcCode;
	}
	public JavaFieldGetSetSrcGenerator(String fieldInfo, String getInfo,
			String setInfo) {
		super();
		this.fieldSrcCode = fieldInfo;
		this.getterSrcCode = getInfo;
		this.setterSrcCode = setInfo;
	}
	public JavaFieldGetSetSrcGenerator() {
	}
	public String toString(){
		return fieldSrcCode+getterSrcCode+setterSrcCode;
	}
}
