package com.gvace.sorm.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.gvace.sorm.bean.ColumnInfo;

/**
 * Common reflection operations 
 * @author yushan
 *
 */ 
public class ReflectUtils {

	public static Object invokeGet(Object object, String fieldName) {
		try {
			Class clazz = object.getClass();
			//load method
			Method m = clazz.getDeclaredMethod("get"+StringUtils.firstCharUpperCase(fieldName), null);
			Object priKeyValue = m.invoke(object, null); //return primaryKey getter result
			return priKeyValue;
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void invokeSet(Object obj, String columnName, Object columnValue){
		Method setMethod;
		try {
			setMethod = obj.getClass().getDeclaredMethod("set"+StringUtils.firstCharUpperCase(columnName),columnValue.getClass());
			setMethod.invoke(obj, columnValue);
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
