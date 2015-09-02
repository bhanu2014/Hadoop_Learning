package com.spark.hbase;


import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Put;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.hadoop.hbase.util.Bytes;
import com.cloudera.spark.hbase.JavaHBaseContext;


//import com.spark.learning.ApacheAccessLog;

public class SparkHbaseBulkInsert {
	
	@SuppressWarnings({ "serial", "unused" })
	public static void main(String[] args){
		SparkConf conf=new SparkConf().setAppName("spark hbase bulk insert").setMaster("local[4]");
		JavaSparkContext ctx=new JavaSparkContext(conf);
		
		JavaRDD<String> logData=ctx.textFile("hdfs://hadoop1.test.com/user/root/bhanu/countryLog.txt");
		
		//System.out.println("log data count"+logData.count());

		
	
		Configuration hconf = HBaseConfiguration.create();
		hconf.addResource(new Path("/etc/hbase/conf.cloudera.yarn/core-site.xml"));
		hconf.addResource(new Path("/etc/hbase/conf.cloudera.hbase/hbase-site.xml"));
		
		JavaHBaseContext hbaseContext =new JavaHBaseContext(ctx,hconf);
		
		hbaseContext.bulkPut(logData,"countryLog",new PutFunction(), true);
		}

	@SuppressWarnings("serial")
		public static class PutFunction implements Function<String, Put>{
			int rowkey=1;
			@Override
			public Put call(String logline) throws Exception {
				String[] cell=logline.split(",");
				 Put put = new Put(Bytes.toBytes(rowkey));
				 put.add((Bytes.toBytes("logFields")),(Bytes.toBytes("ip")),(Bytes.toBytes( cell[0])));
				 put.add((Bytes.toBytes("logFields")),(Bytes.toBytes("country")),(Bytes.toBytes( cell[1])));
				 put.add((Bytes.toBytes("logFields")),(Bytes.toBytes("descrip")),(Bytes.toBytes( cell[2])));
				 put.add((Bytes.toBytes("logFields")),(Bytes.toBytes("leader")),(Bytes.toBytes( cell[3])));
				 put.add((Bytes.toBytes("logFields")),(Bytes.toBytes("GDP")),(Bytes.toBytes( cell[4])));
				rowkey++;
				return put;
			}
			
		}
}
