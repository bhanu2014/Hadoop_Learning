package com.spark.learning;

import java.util.List;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.sql.DataFrame;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SQLContext;
import scala.Tuple2;
import scala.Tuple4;

public class SqlLogAnalyser {

	@SuppressWarnings({ "unused", "serial" })
	public static void main(String[] args) {
		SparkConf conf = new SparkConf().setAppName("Basic log analyser").setMaster("local");
		JavaSparkContext contx=new JavaSparkContext(conf);

		String logFile="input_data/input.txt";

		JavaRDD<String> logRdd=contx.textFile(logFile).cache();

		SQLContext sqlctx=new SQLContext(contx);


		//converting raw RDD to RDD of ApacheAcessLog objects
		//			JavaRDD<ApacheAccessLog> acesslog =loRdd.map(FUNCTIONS.PARSE_LOG_LINE)
		//												.filter(FUNCTIONS.EXCEPTION_CATCH);
		//			
		JavaRDD<ApacheAccessLog> acesslog=logRdd.map(new Function<String, ApacheAccessLog>() {

			private static final long serialVersionUID = 1L;

			public ApacheAccessLog call(String logline){
				return ApacheAccessLog.parseFromLogLine(logline);
			}
		}).filter(new Function<ApacheAccessLog, Boolean>() {
			private static final long serialVersionUID = 1L;
			public Boolean call(ApacheAccessLog bean) throws Exception {
				if(bean == null) {
					return false;
				}
				return true;
			}
		});

		DataFrame sqlDataFrame=sqlctx.createDataFrame(acesslog, ApacheAccessLog.class);
		sqlDataFrame.registerTempTable("logs");
		sqlctx.cacheTable("logs");

		Row contentSizes =sqlctx.sql("SELECT SUM(contentSize),COUNT(*),MIN(contentSize),MAX(contentSize) FROM logs")
				.javaRDD()
				.collect()
				.get(0);
		System.out.println(String.format("ContentSize Avg:%s , Min:%s, Max:%s", contentSizes.getLong(0)/contentSizes.getLong(1),contentSizes.getLong(2),contentSizes.getLong(3)));


		//response code count

		List<Tuple2<Integer,Long>> responseToCodeCount=sqlctx.sql("SELECT responseCode,COUNT(*) FROM logs GROUP BY responseCode LIMIT 1000")
				.javaRDD()
				.mapToPair(new PairFunction<Row, Integer, Long>() {
					public Tuple2<Integer, Long> call(Row row){
						return new Tuple2<Integer,Long>(row.getInt(0),row.getLong(1));
					}
				})
				.collect();
		System.out.println(String.format("Response code counts:%s", responseToCodeCount));

		//Ip address count

		List<String> ipAddress =sqlctx.sql("SELECT ipAddress,COUNT(*) AS total FROM logs GROUP BY ipAddress HAVING total> 10 LIMIT 100")
				.javaRDD()
				.map(new Function<Row,String>(){
					public String call(Row row){
						return row.getString(0);
					}
				})
				.collect();
		System.out.println(String.format("List of IpAddress:%s", ipAddress));


		//endpoint count
		List<Tuple2<String,Long>> topEndpoints=sqlctx.sql("SELECT endpoint,COUNT(*) AS total FROM logs GROUP BY endpoint ORDER BY total DESC LIMIT 10")
				.javaRDD()
				.map(new Function<Row,Tuple2<String,Long>>(){
					public Tuple2<String,Long> call(Row row){
						return new Tuple2<String,Long>(row.getString(0),row.getLong(1));
					}
				}).collect();


		System.out.println(String.format("EndPoints:%s", topEndpoints));









		contx.stop();







	}

}
