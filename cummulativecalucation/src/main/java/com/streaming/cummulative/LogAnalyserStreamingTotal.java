package com.streaming.cummulative;

import java.io.Serializable;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.streaming.Duration;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaInputDStream;
import org.apache.spark.streaming.api.java.JavaPairDStream;
import org.apache.spark.streaming.api.java.JavaReceiverInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;

import scala.Tuple2;



import com.google.common.base.Optional;


//import com.spark.streaming.CountryBean;

@SuppressWarnings("serial")
public class LogAnalyserStreamingTotal implements Serializable{
	
	public static final AtomicLong runningCount=new AtomicLong(0);
	public static final AtomicLong runningSum=new AtomicLong(0);
	public static final AtomicLong runningMin=new AtomicLong(Long.MIN_VALUE);
	public static final AtomicLong runningMax=new AtomicLong(Long.MAX_VALUE);
	
	 private static Function2<List<Long>, Optional<Long>, Optional<Long>>
     COMPUTE_RUNNING_SUM =
     new Function2<List<Long>, Optional<Long>, Optional<Long>>() {
       public Optional<Long> call(List<Long> nums, Optional<Long> current)
           throws Exception {
         long sum = current.or(0L);
         for (long i : nums) {
           sum += i;
         }
         return Optional.of(sum);
       }
     };
	
	public static Function2<Long,Long,Long> SUM_REDUCER=new Function2<Long, Long, Long>() {
		public Long call(Long a ,Long b){
			return a+b;
		}
	};
	
	public static class LongComparator
	implements Comparator<Long>, Serializable {

		public int compare(Long a, Long b) {
			if (a > b) return 1;
			if (a.equals(b)) return 0;
			return -1;
		}
	}
	  public static Comparator<Long> LONG_NATURAL_ORDER_COMPARATOR =
		      new LongComparator();
	@SuppressWarnings({ "resource" })
	public static void main(String[] args){
		System.out.println("----------------------Spark Execution Started ----------------------------------");
		SparkConf conf=new SparkConf().setAppName("Log Analyser Streaming Total").setMaster("local[4]");
		JavaSparkContext cntx=new JavaSparkContext(conf);
		JavaStreamingContext jssc=new JavaStreamingContext(cntx, new Duration(10000));

		jssc.checkpoint("hdfs://user/root/bhanu/");
		System.out.println("--------------before JavaInputDStream -------------------------");
		JavaReceiverInputDStream<String> logDataStream =jssc.socketTextStream("localhost",9999);
		System.out.println("------------after JavaInputDStream-----------------------------");

		System.out.println("------ Log Data Stream count: "+(logDataStream.count()));
		
		JavaDStream<CountryBean> countryLogStream =logDataStream.map(new Function<String, CountryBean>() {
			
			public CountryBean call(String line) throws Exception{
				System.out.println("-------------------line is:"+line	);
				return CountryBean.parseStreamLine(line);
			}
		}).filter(new Function<CountryBean, Boolean>() {
		
			private static final long serialVersionUID = 1L;
			public Boolean call(CountryBean bean) throws Exception {
				System.out.println("----------------------------in exception block parsin-----------------------");
				if(bean == null) {
					return false;
				}
				return true;
			}
		});
		System.out.println("-------------------------------------countryLogStreamCount----------------------------"+countryLogStream.count());
		countryLogStream.print();
		JavaDStream<Long> GDPStream=countryLogStream.map(new Function<CountryBean, Long>() {
			public Long call(CountryBean cbean){
				System.out.println("---------------------countryLogStream+"+cbean.toString()+"+---------------");
				return cbean.getGDP();
			}
		});


		GDPStream.foreach(new Function<JavaRDD<Long>, Void>() {
			public Void call(JavaRDD<Long> rdd)throws Exception{
				System.out.println("	"+rdd.count()+"---------------------------------------");
				if(rdd.count()>0){
					runningSum.getAndAdd(rdd.reduce(SUM_REDUCER));
					runningCount.getAndAdd(rdd.count());
					runningMin.set(Math.min(runningMin.get(), rdd.min(LONG_NATURAL_ORDER_COMPARATOR)));
					runningMax.set(Math.max(runningMax.get(),rdd.min(LONG_NATURAL_ORDER_COMPARATOR)));
					
					System.out.println("GDP Avg is: "+runningSum.get()/runningCount.get());
					System.out.println("GDP Min is: "+runningMin.get());
					System.out.println("GDP Max is: "+runningMax.get());
				}
				else{
					System.out.println("--------------------------rdd count less than zero---------------------");
				}
				return null;
			}
		});

		JavaPairDStream<String,Long> leaderCountStream=countryLogStream.mapToPair(new PairFunction<CountryBean, String, Long>() {
			public Tuple2<String, Long> call(CountryBean cbean){
				return new Tuple2<String, Long>(cbean.getLeader(),1L);
			}
		}).reduceByKey(SUM_REDUCER)
		.updateStateByKey(COMPUTE_RUNNING_SUM);
		
		leaderCountStream.foreach(new Function<JavaPairRDD<String,Long>, Void>() {
			public Void call(JavaPairRDD<String,Long> rdd){
				//rdd.take(100);
				System.out.println("-------------------------leaders count: "+rdd.take(100));
				return null;
			}
		});


		JavaDStream<String> ipAddressStream=countryLogStream.mapToPair(new PairFunction<CountryBean,String,Long>() {
			public Tuple2<String,Long> call(CountryBean cbean)throws Exception{
				return new Tuple2<String,Long>(cbean.getIp(),1L);
			}
		}).reduceByKey(SUM_REDUCER)
		.updateStateByKey(COMPUTE_RUNNING_SUM)
		.filter(new Function<Tuple2<String,Long>, Boolean>() {
			public Boolean call(Tuple2<String,Long> tuple){
				return tuple._2()>10;
			}
		}).map(new Function<Tuple2<String,Long>, String>() {
			public String call(Tuple2<String,Long> tuple){
				return tuple._1();
			}
		});

		ipAddressStream.foreach(new Function<JavaRDD<String>, Void>() {
			public Void call(JavaRDD<String> rdd){
				List<String>  ipAdress= rdd.take(100);
				System.out.println("Ip address greater than 50 times:"+ipAdress);
				return null;
			}
		});

		
		 jssc.start();              // Start the computation
		 jssc.awaitTermination();  


	}	
}
