package com.hbase.practice;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.MasterNotRunningException;
import org.apache.hadoop.hbase.ZooKeeperConnectionException;
import org.apache.hadoop.hbase.client.HBaseAdmin;

public class DeleteTable {
	public void deleteTable(String table) throws MasterNotRunningException, ZooKeeperConnectionException, IOException{
		Configuration conf =HBaseConfiguration.create();
		HBaseAdmin admin=new HBaseAdmin(conf);
		
		admin.disableTable(table);
		admin.deleteTable(table);
		System.out.println("stuent table deleted");
		
		
	}
}
