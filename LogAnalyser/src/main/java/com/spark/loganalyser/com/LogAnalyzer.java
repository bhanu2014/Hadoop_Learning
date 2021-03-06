package com.spark.loganalyser.com;




import java.util.Comparator;
import java.util.List;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.Function2;

import scala.Tuple2;
public class LogAnalyzer {
	private static Function2<Long,Long,Long> SUM_REDUCER=(a,b)->a+b;
	
	public static void main(String[] args){
		SparkConf conf=new SparkConf().setAppName("Log Analyzer").setMaster("local");
		JavaSparkContext sc=new JavaSparkContext(conf);
		
/*		if(args.length==0){
			System.out.println("Must specify an input log file");
			System.exit(-1);
		}
*/		

//		String logFile=args[0];
		String logFile = "C:/Users/Vostro/Desktop/input/input.txt";
		
		JavaRDD<String> logLines=sc.textFile(logFile);
		
		JavaRDD<ApacheAccessLog> acesslog=logLines.map(ApacheAccessLog::parseFromLogLine).filter(new Function<ApacheAccessLog, Boolean>() {
			private static final long serialVersionUID = 1L;
			@Override
			public Boolean call(ApacheAccessLog bean) throws Exception {
				if(bean == null) {
					return false;
				}
				return true;
			}
		});
		
		JavaRDD<Long> contentSizes=acesslog.map(ApacheAccessLog::getContentSize);
		System.out.println("dataset count: "+acesslog.count());
		
		//average, min, max content size of logs
		System.out.println(String.format("Content size avg: %s,Min: %s,Max: %s",contentSizes.reduce(SUM_REDUCER)/contentSizes.count(),
											+contentSizes.min(Comparator.naturalOrder()),contentSizes.max(Comparator.naturalOrder())));
		
		//counting response codes
		List<Tuple2<Integer, Long>> resposeCodeToCount=acesslog.mapToPair(line -> new Tuple2<>(line.getResponseCode(), 1L))
														.reduceByKey(SUM_REDUCER)
														.take(100);
		
		System.out.println(String.format("Response code counts: %s", resposeCodeToCount));
		
		//counting ip address which request to server more than 10 times
		List<String> ipAdress = acesslog.mapToPair(line -> new Tuple2<>(line.getIpAddress(), 1L))
															.reduceByKey(SUM_REDUCER)
															.filter(tuple -> tuple._2>10)
															.map(Tuple2::_1)
															.take(100);
		
		System.out.println(String.format("ipadress > 10 times: %s",ipAdress));
		
	}
	

}
