package com.spark.hbase;

import java.io.Serializable;
import java.util.Comparator;
import java.util.List;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;
import scala.Tuple2;

public class BasicLogAnalyser {

	public static void main(String[] args) {
		SparkConf conf = new SparkConf().setAppName("Basic log analyser").setMaster("local");
		JavaSparkContext contx=new JavaSparkContext(conf);

		String logFile="input_data/input.txt";

		JavaRDD<String> logRdd=contx.textFile(logFile).cache();
		//converting raw RDD to RDD of ApacheAcessLog objects
		//			JavaRDD<ApacheAccessLog> acesslog =logRdd.map(FUNCTIONS.PARSE_LOG_LINE)
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

		//Computing avaerage,min,max content sizes

		JavaRDD<Long> contentSize=acesslog.map(new Function<ApacheAccessLog, Long>() {

			private static final long serialVersionUID = 1L;

			public Long call(ApacheAccessLog acesslog){
				return acesslog.getContentSize();
			}
		});

		System.out.println("Average Content Size: "+contentSize.reduce(new Function2<Long, Long, Long>() {
			private static final long serialVersionUID = 8162313538292993582L;

			public Long call(Long a,Long b){
				return a+b;
			}
		})/contentSize.count());
		System.out.println("Minimun Content Size: "+contentSize.min(new LongComparator()));
		System.out.println("Maximum ContentSize: "+contentSize.max(new LongComparator()));

		//counting response count

		List<Tuple2<Integer,Long>> responseCodeToCount=acesslog.mapToPair(new PairFunction<ApacheAccessLog, Integer,Long>() {

			private static final long serialVersionUID = -5180343107682307962L;

			public Tuple2<Integer, Long> call(ApacheAccessLog acesslog){
				return new Tuple2<Integer,Long>(acesslog.getResponseCode(), 1L); 
			}

		})
		.reduceByKey(new Function2<Long, Long, Long>() {
			public Long call(Long a,Long b){
				return a+b;
			}
		}).collect();

		System.out.println("Response codes count: "+responseCodeToCount);




		//Computing top 10 ip address

		List<String> ipAddress =acesslog.mapToPair(new PairFunction<ApacheAccessLog, String, Long>() {
			private static final long serialVersionUID = 1L;

			public Tuple2<String,Long> call(ApacheAccessLog accesslog){
				return new Tuple2<String, Long>(accesslog.getIpAddress(), 1L);
			}
		})
		.reduceByKey(new Function2<Long, Long, Long>() {
			public Long call(Long a,Long b){
				return a+b;
			}
		})
		.filter(new Function<Tuple2<String,Long>,Boolean>() {
			private static final long serialVersionUID = 1L;

			public Boolean call(Tuple2<String, Long> tuple){
				return tuple._2() >10;
			}
		})
		.map(new Function<Tuple2<String,Long>, String>() {
			public String call(Tuple2<String, Long> tuple){
				return tuple._1();
			}
		})
		.take(100);

		System.out.println("Ip addresses: "+ipAddress);
		//Top end points

		List<Tuple2<String,Long>> topEndPoints=acesslog.mapToPair(new PairFunction<ApacheAccessLog, String, Long>() {
			public Tuple2<String, Long> call(ApacheAccessLog log){
				return new Tuple2<String, Long>(log.getEndpoint(), 1L);
			}
		})
		.reduceByKey(new Function2<Long, Long, Long>() {
			public Long call(Long a,Long b){
				return a+b;
			}
		})
		.top(10, new FUNCTIONS.ValueComparator<String, Long> ( new LongComparator()));


		System.out.println("Top end points: "+topEndPoints);




		contx.stop();
		contx.close();
	}
	@SuppressWarnings("serial")
	public static class LongComparator implements Comparator<Long>, Serializable {

		public int compare(Long a, Long b) {
			if (a > b) return 1;
			if (a.equals(b)) return 0;
			return -1;
		}
	}

	@SuppressWarnings("serial")
	public static class ValueComparator<K, V> implements Comparator<Tuple2<K, V>>, Serializable {
		private Comparator<V> comparator;

		public ValueComparator(Comparator<V> comparator) {
			this.comparator = comparator;
		}

		public int compare(Tuple2<K, V> o1, Tuple2<K, V> o2) {
			return comparator.compare(o1._2(), o2._2());
		}
	}
}
