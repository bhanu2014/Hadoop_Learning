package com.hbase.practice;

import java.io.IOException;

import org.apache.zookeeper.Op.Create;

public class HadoopIntialize {
	public static void main(String[] args) throws IOException{
		//CreateTable createTable= new CreateTable();
		ListTables listTables = new ListTables();
		DeleteTable deleteTable= new DeleteTable();
		CRUD_Operations crud= new CRUD_Operations();
		
		//createTable.createTable("student","student_info","student_marks");
		listTables.listTables();
		deleteTable.deleteTable("student");
		crud.insertData();
		crud.readData();
		
	}
}
