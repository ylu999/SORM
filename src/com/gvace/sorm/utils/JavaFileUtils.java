package com.gvace.sorm.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.gvace.sorm.bean.ColumnInfo;
import com.gvace.sorm.bean.JavaFieldGetSetSrcGenerator;
import com.gvace.sorm.bean.TableInfo;
import com.gvace.sorm.core.DBManager;
import com.gvace.sorm.core.MySQLTypeConvertor;
import com.gvace.sorm.core.TableContext;
import com.gvace.sorm.core.TypeConvertor;

/**
 * Common operation for generating java source code file 
 * @author yushan
 *
 */
public class JavaFileUtils {
	public static String packageToPath(String pack){
		return pack.replaceAll("\\.", "/");
	}
	public static void createJavaPOFile(TableInfo tableInfo, TypeConvertor convertor){
		String src = createJavaSrc(tableInfo, convertor);
		String packPath = DBManager.getConfiguration().getSrcPath()+File.separator+packageToPath(DBManager.getConfiguration().getPoPackage());
		String fileName = StringUtils.firstCharUpperCase(tableInfo.getTname())+".java";
		File dir = new File(packPath);
		if (!dir.exists()) dir.mkdir();
		File file = new File(packPath+File.separator+fileName);
		
		try(BufferedWriter bw = new BufferedWriter(new FileWriter(file))){
			bw.write(src);
			System.out.println("Created "+fileName);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * Build java src file based on table info
	 * @param tableInfo Table Info
	 * @param convertor Type Convertor
	 * @return
	 */
	public static String createJavaSrc(TableInfo tableInfo, TypeConvertor convertor){
		StringBuilder src = new StringBuilder();
		
		Map<String, ColumnInfo> columns = tableInfo.getColumns();
		List<JavaFieldGetSetSrcGenerator> generatorList = new ArrayList<JavaFieldGetSetSrcGenerator>();
		for(ColumnInfo column: columns.values()){
			generatorList.add(getSrcGenerator(column,convertor));
		}
		//package
		src.append("package "+DBManager.getConfiguration().getPoPackage()+";\n\n");
		//imports
		src.append("import java.sql.*;\n");
		src.append("import java.util.*;\n\n");
		//class defination
		src.append("public class "+StringUtils.firstCharUpperCase(tableInfo.getTname())+" {\n\n");
		//fields list
		for(JavaFieldGetSetSrcGenerator srcGenerator: generatorList){
			src.append(srcGenerator.getFieldSrcCode());
		}
		src.append("\r\n");
		//getter
		for(JavaFieldGetSetSrcGenerator srcGenerator: generatorList){
			src.append(srcGenerator.getGetterSrcCode());
		}
		//setter
		for(JavaFieldGetSetSrcGenerator srcGenerator: generatorList){
			src.append(srcGenerator.getSetterSrcCode());
		}
		//ending
		src.append("}\n");
		return src.toString();
	}
	/**
	 * Build java src generator based on column info
	 * Example: varchar username==>private String username; and getter setter
	 * @param column Column Info
	 * @param convertor Type Convertor
	 * @return JavaFieldGetSetSrcGenerator
	 */
	public static JavaFieldGetSetSrcGenerator getSrcGenerator(ColumnInfo column,TypeConvertor convertor){
		String javaFieldType = convertor.db2Java(column.getDataType());
		String javaFieldName = column.getName();
		
		String fieldSrcCode = "\tprivate "+javaFieldType+" "+column.getName()+";\n";
		
		StringBuilder getterSrcCodeBuilder = new StringBuilder();
		getterSrcCodeBuilder.append("\tpublic "+javaFieldType+" get"+StringUtils.firstCharUpperCase(javaFieldName)+"(){\n");
		getterSrcCodeBuilder.append("\t\treturn "+javaFieldName+";\n");
		getterSrcCodeBuilder.append("\t}\n");

		StringBuilder setterSrcCodeBuilder = new StringBuilder();
		getterSrcCodeBuilder.append("\tpublic void set"+StringUtils.firstCharUpperCase(javaFieldName)+"("+javaFieldType+" "+javaFieldName+"){\n");
		getterSrcCodeBuilder.append("\t\tthis."+javaFieldName+" = "+javaFieldName+";\n");
		getterSrcCodeBuilder.append("\t}\n");

		JavaFieldGetSetSrcGenerator srcGenerator = new JavaFieldGetSetSrcGenerator(fieldSrcCode, getterSrcCodeBuilder.toString(), setterSrcCodeBuilder.toString());
		return srcGenerator;
	}
	@Test
	public void test(){
		Map<String, TableInfo> map = TableContext.tables;
		for(TableInfo tableInfo: map.values()){
			createJavaPOFile(tableInfo,new MySQLTypeConvertor());
		}
	}
}
