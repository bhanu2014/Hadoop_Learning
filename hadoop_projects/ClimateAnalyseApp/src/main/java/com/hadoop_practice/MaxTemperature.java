import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.mapreduce.lib.db.DBConfiguration;
import org.apache.hadoop.mapreduce.lib.db.DBInputFormat;
import org.apache.hadoop.mapreduce.lib.db.DBOutputFormat;
import org.apache.hadoop.io.NullWritable;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class MaxTemperature {

  public static void main(String[] args) throws Exception {
    if (args.length != 1) {
      System.err.println("Usage: MaxTemperature <input path> <output path>");
      System.exit(-1);
    }
    Configuration conf = new Configuration();
     DBConfiguration.configureDB(conf,
     "com.mysql.jdbc.Driver", 
     "jdbc:mysql://localhost:3306/climate_db",
     "year",   
     "max_temperature");
    Job job = new Job();
    job.setJarByClass(MaxTemperature.class);
    job.setJobName("Max temperature");

    FileInputFormat.addInputPath(job, new Path(args[0]));
    
    job.setMapperClass(MaxTemperatureMapper.class);
    job.setReducerClass(MaxTemperatureReduce.class);
    job.setOutputKeyClass(DBOutputWritable.class);
    	job.setOutputValueClass(NullWritable.class);
	job.setOutputFormatClass(DBOutputFormat.class);
	DBOutputFormat.setOutput(job,"climate_info ",new String[] { "year", "max_temperature" } );

    System.exit(job.waitForCompletion(true) ? 0 : 1);
  }
}

