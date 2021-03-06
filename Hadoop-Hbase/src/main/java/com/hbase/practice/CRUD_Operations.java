package com.hbase.practice;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.MasterNotRunningException;
import org.apache.hadoop.hbase.ZooKeeperConnectionException;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;

public class CRUD_Operations {
	
	
	
	
	Configuration conf= HBaseConfiguration.create();
		
	
	public void insertData() throws IOException{
		HTable htable =new HTable(conf,"emp");
		Put p = new Put(Bytes.toBytes("row1"));
		
		p.add(Bytes.toBytes("personal"),Bytes.toBytes("name"),Bytes.toBytes("raju"));
		p.add(Bytes.toBytes("personal"),Bytes.toBytes("city"),Bytes.toBytes("hyderabad"));
		p.add(Bytes.toBytes("professional"),Bytes.toBytes("designation"),Bytes.toBytes("manager"));
		p.add(Bytes.toBytes("professional"),Bytes.toBytes("salary"),Bytes.toBytes("5000"));
		
		htable.put(p);
		htable.close();
	}
	
	
	public void readData() throws IOException{
		HTable htable= new HTable(conf, "emp");
		Get g=new Get(Bytes.toBytes("row1"));
		
		Result result=htable.get(g);
		
		byte[] value=result.getValue(Bytes.toBytes("personal"),Bytes.toBytes("name"));
		byte[] value1=result.getValue(Bytes.toBytes("personal"),Bytes.toBytes("city"));
		
		String name=Bytes.toString(value);
		String city=Bytes.toString(value1);
		
		System.out.println("name: "+name+" city: "+city);
		
	}
}
