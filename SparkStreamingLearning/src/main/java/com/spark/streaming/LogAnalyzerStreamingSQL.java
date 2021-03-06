package com.spark.streaming;

import java.io.Serializable;
import java.util.List;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.streaming.Duration;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaReceiverInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;

//import com.streaming.cummulativeSum.CountryBean;

import scala.Tuple2;



public class LogAnalyzerStreamingSQL implements Serializable {
	private static final long serialVersionUID = 5221730303761091083L;

	private static final Duration WINDOW_LENGTH = new Duration(30 * 1000);
	// Stats will be computed every slide interval time.
	private static final Duration SLIDE_INTERVAL = new Duration(10 * 1000);
	@SuppressWarnings({ "resource", "serial" })
	public static void main(String[] args){
		System.out.println("streaming started...............................");
		SparkConf conf=new SparkConf().setAppName("Spark Streaming learning").setMaster("local[4]");
		JavaSparkContext spctx=new JavaSparkContext(conf);
		JavaStreamingContext jssc=new JavaStreamingContext(spctx,SLIDE_INTERVAL);
		JavaReceiverInputDStream<String> logDataStream=jssc.socketTextStream("localhost",9999);
		JavaDStream<CountryBean> accesslogDstream=logDataStream.map(new Function<String, CountryBean>() {

			private static final long serialVersionUID = 1L;

			public CountryBean call(String logline){
				return CountryBean.parseStreamLine(logline);
			}
		}).filter(new Function<CountryBean, Boolean>() {
			private static final long serialVersionUID = 1L;
			public Boolean call(CountryBean bean) throws Exception {
				if(bean == null) {
					return false;
				}
				return true;
			}
		});
		accesslogDstream.print();
		System.out.println("streaming to be started...............................");
		JavaDStream<CountryBean> windowDstream=accesslogDstream.window(WINDOW_LENGTH,SLIDE_INTERVAL);
		
		windowDstream.print();
		System.out.println("streaming starting..................................");
		windowDstream.foreachRDD(new Function<JavaRDD<CountryBean>, Void>() {

			@Override
			public Void call(JavaRDD<CountryBean> accessLogs) {
				System.out.println("in eachRDD...............................");
				
				testMethod(accessLogs);
				
				if (accessLogs.count() == 0) {
					System.out.println("No access logs in this time interval");
					//return null;
				}
				else{
					System.out.println("its working: Count:"+accessLogs.count());
				}
				accessLogs.saveAsTextFile("/home/bhanu/spark/projects/data/");
				List<Tuple2<String,Long>> LeadersToCount=accessLogs.mapToPair(new PairFunction<CountryBean, String,Long>() {

					private static final long serialVersionUID = -5180343107682307962L;

					public Tuple2<String, Long> call(CountryBean acesslog){
						return new Tuple2<String,Long>(acesslog.getLeader(), 1L); 
					}

				})
				.reduceByKey(new Function2<Long, Long, Long>() {
					public Long call(Long a,Long b){
						return a+b;
					}
				}).collect();

				System.out.println("Leaders count: "+LeadersToCount.toString());
				
			
				//return null;
				return null;
			}
		});
		System.out.println("streaming ending...............................");
		windowDstream.count();
		
		System.out.println("------ windowDstream count: "+windowDstream.count());
		jssc.start();
		jssc.awaitTermination();
		System.out.println("streaming to be ending...............................");
	}
	
	static int i = 0;
	private static void testMethod(JavaRDD<CountryBean> rdd) {
		i++;
		System.out.println("---- testMethod() called for "+i+" time");
		if(rdd.count() == 0) {
			System.out.println("---- This RDD has no values");
		}
	}
}
