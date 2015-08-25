import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapreduce.lib.db.DBWritable;

public class DBOutputWritable implements Writable, DBWritable
{
   private String year;
   private int max_temperature;

   public DBOutputWritable(String year, int max_temperature)
   {
     this.year = year;
     this.max_temperature = max_temperature;
   }

   public void readFields(DataInput in) throws IOException {   }

   public void readFields(ResultSet rs) throws SQLException
   {
     year = rs.getString(1);
     max_temperature = rs.getInt(2);
   }

   public void write(DataOutput out) throws IOException {    }

   public void write(PreparedStatement ps) throws SQLException
   {
     ps.setString(1, year);
     ps.setInt(2, max_temperature);
   }
}
