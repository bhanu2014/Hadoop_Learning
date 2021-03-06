package com.spark.loganalyser.com;

import java.util.List;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.sql.SQLContext;
import org.apache.spark.sql.api.java.JavaSQLContext;
import org.apache.spark.sql.api.java.JavaSchemaRDD;

import scala.Tuple2;
import scala.Tuple4;

public class LogAnalyserSql {
	public static void main(String[] arg){
		SparkConf conf = new SparkConf().setAppName("LogAnalyserSql").setMaster("local");
		JavaSparkContext sc= new JavaSparkContext(conf);
		JavaSQLContext sqlsc= new JavaSQLContext(sc);
		
		String logfile="C:/Users/Vostro/Desktop/input/input.txt";
		
		JavaRDD<String> logfile_rdd=sc.textFile(logfile);
		JavaRDD<ApacheAccessLog>  acesslogs=logfile_rdd.map(ApacheAccessLog::parseFromLogLine).filter(new Function<ApacheAccessLog, Boolean>() {
			private static final long serialVersionUID = 1L;
			@Override
			public Boolean call(ApacheAccessLog bean) throws Exception {
				if(bean == null) {
					return false;
				}
				return true;
			}
		});
		
		
		
		
		JavaSchemaRDD schemaRdd=sqlsc.applySchema(acesslogs, ApacheAccessLog.class);
		schemaRdd.registerTempTable("logs");
		sqlsc.sqlContext().cacheTable("logs");
		
		Tuple4<Long, Long, Long, Long> contentSizeStats = sqlsc.sql("SELECT SUM(contentSize),COUNT(*),MIN(contentSize),MAX(contentSize) FROM logs")
															.map(row -> new Tuple4<>(row.getLong(0),row.getLong(1),row.getLong(2),row.getLong(3)))
															.first();
		System.out.println(String.format("Content size AVG: %s,MIN:%s,Max:%s", contentSizeStats._1()/contentSizeStats._2(),contentSizeStats._3(),contentSizeStats._4()));
		
		
		//compute response code to count
		
		List<Tuple2<Integer,Long>> responseCodeToCount =  sqlsc.sql("SELECT responseCode,COUNT(*) FROM logs GROUP BY responseCode LIMIT 1000")
															.map(row -> new Tuple2<>(row.getInt(0),row.getLong(1)))
															.collect();
		System.out.println(String.format("Response code counts: %s", responseCodeToCount));
		
		//ip address  that has accessed from the server more than 10 times
		
		List<String> ipAddress = sqlsc.sql("SELECT ipAddress,COUNT(*) AS total FROM logs GROUP BY ipAddress HAVING total> 10 LIMIT 100")
								.map(row -> row.getString(0))
								.collect();
		
		System.out.println(String.format("IpAddress > 10 times:%s",ipAddress));
		
		//Top endpoints
		
		List<Tuple2<String, Long>> topEndpoints = sqlsc.sql("SELECT endpoint,COUNT(*) AS total FROM logs GROUP BY endpoint ORDER BY total DESC LIMIT 10")
														.map(row  -> new Tuple2<>(row.getString(0),row.getLong(1)))
														.collect();
		
		System.out.println(String.format("TopEndpoints: %s", topEndpoints));
		
		
		
		
		
		sc.stop();
	}
}
