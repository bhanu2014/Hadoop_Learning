import java.io.IOException;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class MaxTemperatureReduce
  extends Reducer<Text, IntWritable, DBOutputWritable, NullWritable> {

  @Override
  public void reduce(Text key, Iterable<IntWritable> values,Context context)
      throws IOException, InterruptedException {

    int maxValue = Integer.MIN_VALUE;
    for (IntWritable value : values) {
      maxValue = Math.max(maxValue, value.get());
    }
    //context.write(key, new IntWritable(maxValue));
    	context.write(new DBOutputWritable(key.toString(),maxValue), NullWritable.get());
    	  }
     }
    
