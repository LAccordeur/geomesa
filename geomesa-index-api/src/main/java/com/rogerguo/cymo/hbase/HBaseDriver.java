package com.rogerguo.cymo.hbase;


import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description Some common HBase operations
 * @Date 2019/11/7 16:23
 * @Created by GUO Yang
 */
public class HBaseDriver {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private Configuration configuration;

    private Connection connection;


    public HBaseDriver(String zookeeperUrl) {
        try {
            this.configuration = HBaseConfiguration.create();
            this.configuration.set("hbase.zookeeper.quorum", zookeeperUrl);
            this.connection = ConnectionFactory.createConnection(configuration);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




    public void put(String tableName, Put put) throws IOException {
        try (Table hTable = connection.getTable(TableName.valueOf(tableName))) {
            hTable.put(put);
        }

    }

    public void batchPut(String tableName, List<Put> putList) throws IOException {
        try (Table hTable = connection.getTable(TableName.valueOf(tableName))) {
            hTable.put(putList);
        }
    }

    public Result get(String tableName, byte[] rowKey) throws IOException {
        try (Table hTable = connection.getTable(TableName.valueOf(tableName))) {
            Get get = new Get(rowKey);
            Result result = hTable.get(get);

            return result;
        }
    }

    public Result[] batchGet(String tableName, List<Get> getList) throws IOException {
        try (Table hTable = connection.getTable(TableName.valueOf(tableName))) {
            return hTable.get(getList);
        }
    }


    public Result get(String tableName, byte[] rowKey, byte[] columnFamily, byte[] qualifier) throws IOException {
        try (Table hTable = connection.getTable(TableName.valueOf(tableName))) {
            Get get = new Get(rowKey);
            get.addColumn(columnFamily, qualifier);
            Result result = hTable.get(get);

            return result;
        }
    }

    public List<Result> scan(String tableName, byte[] startKey, byte[] stopKey) throws IOException {
        List<Result> resultList = new ArrayList<>();

        try (Table hTable = connection.getTable(TableName.valueOf(tableName))) {

            Scan scan = new Scan();
            scan.withStartRow(startKey, true);
            scan.withStopRow(stopKey, true);
            ResultScanner results = hTable.getScanner(scan);

            for (Result result : results) {
                resultList.add(result);
            }

            return resultList;
        }
    }

}
