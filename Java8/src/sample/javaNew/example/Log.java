package sample.javaNew.example;



import java.io.Serializable;
import java.util.Comparator;
import java.util.List;

/**
 * The LogAnalyzerStreaming illustrates how to use logs with Spark Streaming to
 *   compute statistics every slide_interval for the last window length of time.
 *
 * To feed the new lines of some logfile into a socket, run this command:
 *   % tail -f [[YOUR_LOG_FILE]] | nc -lk 9999
 *
 * If you don't have a live log file that is being written to, you can add test lines using this command:
 *   % cat ../../data/apache.access.log >> [[YOUR_LOG_FILE]]
 *
 * Example command to run:
 * %  ${YOUR_SPARK_HOME}/bin/spark-submit
 *     --class "com.databricks.apps.logs.chapter1.LogsAnalyzerStreaming"
 *     --master local[4]
 *     target/log-analyzer-1.0.jar
 */
public class Log {
//	private static Function2<Long, Long, Long> SUM_REDUCER = (a, b) -> a + b;
//
//	private static class ValueComparator<K, V> implements Comparator<Tuple2<K, V>>, Serializable {
//		private Comparator<V> comparator;
//
//		public ValueComparator(Comparator<V> comparator) {
//			this.comparator = comparator;
//		}
//
//		@Override
//		public int compare(Tuple2<K, V> o1, Tuple2<K, V> o2) {
//			return comparator.compare(o1._2(), o2._2());
//		}
//	}
//
//	// Stats will be computed for the last window length of time.
//	private static final Duration WINDOW_LENGTH = new Duration(30 * 1000);
//	// Stats will be computed every slide interval time.
//	private static final Duration SLIDE_INTERVAL = new Duration(10 * 1000);
//
//	public static void main(String[] args) {
//		SparkConf conf = new SparkConf().setAppName("Log Analyzer Streaming");
//		JavaSparkContext sc = new JavaSparkContext(conf);
//
//		JavaStreamingContext jssc = new JavaStreamingContext(sc,
//				SLIDE_INTERVAL);  // This sets the update window to be every 10 seconds.
//
//		JavaReceiverInputDStream<String> logDataDStream =
//				jssc.socketTextStream("localhost", 9999);
//
//		// A DStream of Apache Access Logs.
//		JavaDStream<ApacheAccessLog> accessLogDStream =
//				logDataDStream.map(ApacheAccessLog::parseFromLogLine);
//
//		// Splits the accessLogDStream into a dstream of time windowed rdd's of apache access logs.
//		JavaDStream<ApacheAccessLog> windowDStream =
//				accessLogDStream.window(WINDOW_LENGTH, SLIDE_INTERVAL);
//
//		windowDStream.foreachRDD(accessLogs -> {
//			if (accessLogs.count() == 0) {
//				System.out.println("No access logs in this time interval");
//				return null;
//			}
//
//			// *** Note that this is code copied verbatim from LogAnalyzer.java.
//
//			// Calculate statistics based on the content size.
//			JavaRDD<Long> contentSizes =
//					accessLogs.map(ApacheAccessLog::getContentSize).cache();
//			System.out.println(String.format("Content Size Avg: %s, Min: %s, Max: %s",
//					contentSizes.reduce(SUM_REDUCER) / contentSizes.count(),
//					contentSizes.min(Comparator.naturalOrder()),
//					contentSizes.max(Comparator.naturalOrder())));
//
//			return null;
//		});
//
//		// Start the streaming server.
//		jssc.start();              // Start the computation
//		jssc.awaitTermination();   // Wait for the computation to terminate
//	}
}
