package com.hbase.practice;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.MasterNotRunningException;
import org.apache.hadoop.hbase.ZooKeeperConnectionException;
import org.apache.hadoop.hbase.client.HBaseAdmin;

public class ListTables {
	public void listTables() throws MasterNotRunningException, ZooKeeperConnectionException, IOException{
		Configuration conf =  HBaseConfiguration.create();
		
		HBaseAdmin admin =new HBaseAdmin(conf);
		
		HTableDescriptor[] tableDescriptor= admin.listTables();
		
		for(HTableDescriptor tbDescp:tableDescriptor){
			System.out.println(tbDescp.getNameAsString());
		}
	}
}
