package com.restfulapi.hbase.utils;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

public class HbaseUtils {

    private static Logger LOG = LoggerFactory.getLogger(HbaseUtils.class);

    /**
     * get connection
     */
    public static Connection getConnection(){
        Connection conn = null;
        try {
            System.setProperty("HADOOP_USER_NAME", "greejsj");
            Configuration config = HBaseConfiguration.create();
            config.set("hbase.zookeeper.quorum", "Master01,Master02,Slave01");
            config.set("hbase.zookeeper.property.clientPort", "2181");
            config.setLong("hbase.client.scanner.timeout.period", 86400000);

            conn = ConnectionFactory.createConnection(config);
            return conn;
        }catch (IOException e){
            throw new RuntimeException("create connection failed"+e);
        }
    }

    /**
     * close connection
     * @param connection
     */
    public static void closeConnection(Connection connection){
        if(connection!=null){
            try {
                connection.close();
            } catch (IOException e) {
                //do nothing
            }
        }
    }

    /**
     * get a specific field value
     * @param connection
     * @param tableNameStr
     * @param rowName
     * @param columnFamily
     * @param columnQualifier
     * @return
     */
    public static String get(Connection connection, String tableNameStr,String rowName, String columnFamily, String columnQualifier){
        if(connection == null){
            LOG.error("connection == null");
            return null;
        }
        TableName tableName = TableName.valueOf(tableNameStr);
        try {
            Table table = connection.getTable(tableName);
            Get get = new Get(Bytes.toBytes(rowName));
            get.addColumn(Bytes.toBytes(columnFamily),Bytes.toBytes(columnQualifier));
            Result rs = table.get(get);
            List<Cell> cellList = rs.listCells();
            if(cellList==null) {
                return null;
            }

            String value = Bytes.toString(CellUtil.cloneValue(rs.listCells().get(0)));
            return value;
        } catch (IOException e) {
            e.printStackTrace();
            LOG.error("get field value failed. "+e);
            return null;
        }
    }

    /**
     * put the value to a specific column
     * @param connection
     * @param tableNameStr
     * @param rowKey
     * @param columnFamily
     * @param columnQualifier
     * @param value
     */
    public static void put(Connection connection, String tableNameStr, String rowKey, String columnFamily, String columnQualifier,String value){
        Table table = null;
        try{
            table = connection.getTable(TableName.valueOf(tableNameStr));
            Put put = new Put(Bytes.toBytes(rowKey));
            put.addColumn(Bytes.toBytes(columnFamily),Bytes.toBytes(columnQualifier),Bytes.toBytes(value));
            table.put(put);
        } catch (IOException e) {
            e.printStackTrace();
            LOG.error("put value failed. "+e);
        }
    }
}
