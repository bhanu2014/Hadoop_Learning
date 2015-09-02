package com.spark.hbase;

import java.io.Serializable;
import java.util.Comparator;

import io.netty.util.internal.chmv8.ConcurrentHashMapV8.Fun;

import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;
import scala.Tuple2;

public class FUNCTIONS {
	
	public static Function<String, ApacheAccessLog> PARSE_LOG_LINE = new Function<String, ApacheAccessLog>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 588151541411367331L;

		public ApacheAccessLog call(String logline){
			return ApacheAccessLog.parseFromLogLine(logline);
		}
	};
	
	public static Function<ApacheAccessLog, Long> GET_CONTENT_SIZE= new Function<ApacheAccessLog, Long>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public Long call(ApacheAccessLog acesslog){
			return acesslog.getContentSize();
		}
	};
	
	public static Function2<Long, Long, Long> SUM_REDUCER=new Function2<Long, Long, Long>() {
		public Long call(Long a,Long b){
			return a+b;
		}
	};
	
	public static Comparator<Long> LONG_NATURAL_ORDER_COMPARATOR = new LongComparator();
	 public static class LongComparator implements Comparator<Long>, Serializable {

	   public int compare(Long a, Long b) {
	       if (a > b) return 1;
	       if (a.equals(b)) return 0;
	       return -1;
	   }
 }
	 
	 public static PairFunction<ApacheAccessLog, Integer,Long> GET_RESPONSE_CODE = new PairFunction<ApacheAccessLog, Integer,Long>() {
		 /**
		 * 
		 */
		

		public Tuple2<Integer, Long> call(ApacheAccessLog acesslog){
			 return new Tuple2<Integer,Long>(acesslog.getResponseCode(), 1L); 
		 }
		 
	};	
	
	
	public static PairFunction<ApacheAccessLog, String, Long> GET_IP_ADDRESS= new PairFunction<ApacheAccessLog, String, Long>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public Tuple2<String,Long> call(ApacheAccessLog accesslog){
			return new Tuple2<String, Long>(accesslog.getIpAddress(), 1L);
		}
	};
	
	public static Function<Tuple2<String,Long>,Boolean> GREATER_THAN_10 =new Function<Tuple2<String,Long>,Boolean>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public Boolean call(Tuple2<String, Long> tuple){
			return tuple._2() >10;
		}
	};
	
	public static Function<Tuple2<String,Long>,String> GET_TUPLE_FIRST=new Function<Tuple2<String,Long>, String>() {
		public String call(Tuple2<String, Long> tuple){
			return tuple._1();
		}
	};
	
	
	public static PairFunction<ApacheAccessLog, String, Long> GET_END_POINT=new PairFunction<ApacheAccessLog, String, Long>() {
		public Tuple2<String, Long> call(ApacheAccessLog log){
			return new Tuple2<String, Long>(log.getEndpoint(), 1L);
		}
 	};
 	
 	public static class ValueComparator<K, V> implements Comparator<Tuple2<K, V>>, Serializable {
		  private Comparator<V> comparator;
		
		  public ValueComparator(Comparator<V> comparator) {
		    this.comparator = comparator;
		  }
		
		  public int compare(Tuple2<K, V> o1, Tuple2<K, V> o2) {
		    return comparator.compare(o1._2(), o2._2());
		  }
 	}
 	
 	public static Function<ApacheAccessLog, Boolean> EXCEPTION_CATCH=new Function<ApacheAccessLog, Boolean>() {
		private static final long serialVersionUID = 1L;
		public Boolean call(ApacheAccessLog bean) throws Exception {
			if(bean == null) {
				return false;
			}
			return true;
		}
 	};
}

