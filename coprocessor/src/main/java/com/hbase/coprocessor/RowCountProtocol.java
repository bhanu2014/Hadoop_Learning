package com.hbase.coprocessor;

import java.io.IOException;

import org.apache.hadoop.hbase.filter.Filter;
import org.apache.hadoop.hbase.ipc.CoprocessorProtocol;

public interface RowCountProtocol extends CoprocessorProtocol {
    
    long getRowCount() throws IOException;
 
    long getRowCount(Filter filter) throws IOException;
 
    long getKeyValueCount() throws IOException;
 
}