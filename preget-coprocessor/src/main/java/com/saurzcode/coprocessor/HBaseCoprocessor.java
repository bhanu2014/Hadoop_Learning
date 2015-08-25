package com.saurzcode.coprocessor;

import java.io.IOException;
import java.util.List;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.coprocessor.BaseRegionObserver;
import org.apache.hadoop.hbase.coprocessor.ObserverContext;
import org.apache.hadoop.hbase.coprocessor.RegionCoprocessorEnvironment;
import org.apache.hadoop.hbase.util.Bytes;
public class HBaseCoprocessor extends BaseRegionObserver {
	  @Override
	  public void preGetOp(final ObserverContext<RegionCoprocessorEnvironment> e,
	      final Get get, final List<Cell> results) throws IOException {
		  if(Bytes.equals(get.getRow(), Bytes.toBytes("TEST_COPROCESSOR"))){
			  KeyValue kv = new KeyValue(get.getRow(),Bytes.toBytes("name"),Bytes.toBytes("first"),Bytes.toBytes("saurzcode"));
			  results.add(kv);
		  }
	  }
}