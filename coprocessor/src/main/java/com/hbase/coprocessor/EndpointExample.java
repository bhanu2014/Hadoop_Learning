package com.hbase.coprocessor;

import java.io.IOException;
import java.util.Map;
 

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.coprocessor.Batch;
import org.apache.hadoop.hbase.util.Bytes;
 
public class EndpointExample {
    public static void main(String[] args) throws IOException {
        Configuration conf = HBaseConfiguration.create();
        @SuppressWarnings("resource")
		HTable table = new HTable(conf, "testtable");
        try {
            Map<byte[], Long> results = table.coprocessorExec(RowCountProtocol.class, null, null,
            			new Batch.Call<RowCountProtocol, Long>() {
                        @Override
                        public Long call(RowCountProtocol counter)
                                throws IOException {
                            return counter.getRowCount();
                        }
                    });
 
            long total = 0;
            for (Map.Entry<byte[], Long> entry : results.entrySet()) {
                total += entry.getValue().longValue();
                System.out.println("Region: " + Bytes.toString(entry.getKey())+ ", Count: " + entry.getValue());
            }
            System.out.println("Total Count: " + total);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }
}
