package com.lelandyan.ct.common.bean;

import com.lelandyan.ct.common.api.Column;
import com.lelandyan.ct.common.api.Rowkey;
import com.lelandyan.ct.common.api.TableRef;
import com.lelandyan.ct.common.constant.Names;
import com.lelandyan.ct.common.constant.ValueConstant;
import com.lelandyan.ct.common.util.DateUtil;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

/**
 * 基础数据访问对象
 */
public abstract class BaseDao {
    private ThreadLocal<Connection> connHolder = new ThreadLocal<Connection>();
    private ThreadLocal<Admin> adminHolder = new ThreadLocal<Admin>();

    protected void start() throws Exception {
        getConnection();
        getAdmin();
    }

    protected void end() throws Exception {
        Admin admin = getAdmin();
        if (admin != null) {
            admin.close();
            adminHolder.remove();
        }

        Connection conn = getConnection();
        if (conn != null) {
            conn.close();
            connHolder.remove();
        }
    }

    /**
     * 获取连接对象
     *
     * @return
     */
    protected synchronized Connection getConnection() throws Exception {
        Connection conn = connHolder.get();
        if (conn == null) {
            Configuration conf = HBaseConfiguration.create();
            conn = ConnectionFactory.createConnection();
            connHolder.set(conn);
        }
        return conn;
    }

    protected synchronized Admin getAdmin() throws Exception {
        Admin admin = adminHolder.get();
        if (admin == null) {
            admin = getConnection().getAdmin();
            adminHolder.set(admin);
        }
        return admin;
    }

    protected void createNamespaceNX(String namespace) throws Exception {
        Admin admin = getAdmin();
        try {
            admin.getNamespaceDescriptor(namespace);
        } catch (NamespaceNotFoundException e) {
            NamespaceDescriptor namespaceDescriptor = NamespaceDescriptor.create(namespace).build();

            admin.createNamespace(namespaceDescriptor);
        }
    }

    protected void createTableXX(String name, String... families) throws Exception {
        createTableXX(name, null,null, families);
    }

    protected void createTableXX(String name, String coprocessorClass,Integer regionCount, String... families) throws Exception {
        Admin admin = getAdmin();

        TableName tableName = TableName.valueOf(name);
        if (admin.tableExists(tableName)) {
            deleteTable(name);
        }

        createTable(name, coprocessorClass,regionCount, families);
    }

    private void createTable(String name, String coprocessorClass, Integer regionCount, String... families) throws Exception {
        Admin admin = getAdmin();
        TableName tableName = TableName.valueOf(name);
        HTableDescriptor tableDescriptor = new HTableDescriptor(tableName);
        if (families == null || families.length == 0) {
            families = new String[1];
            families[0] = Names.CF_INFO.getValue();
        }
        for (String family : families) {
            HColumnDescriptor columnDescriptor = new HColumnDescriptor(family);
            tableDescriptor.addFamily(columnDescriptor);
        }
        if(coprocessorClass != null && !"".equals(coprocessorClass)){
            tableDescriptor.addCoprocessor(coprocessorClass);
        }
        // 增加预分区
        if (regionCount == null || regionCount <= 0) {
            admin.createTable(tableDescriptor);
        } else {
            byte[][] splitKeys = genSplitKeys(regionCount);
            admin.createTable(tableDescriptor, splitKeys);
        }

    }

    protected List<String[]> getStartStorRowkeys(String tel, String start, String end){
        List<String[]> rowkeyss = new ArrayList<>();
        String startTime = start.substring(0,6);
        String endTime = end.substring(0,6);


        Calendar startCal = Calendar.getInstance();
        startCal.setTime(DateUtil.parse(startTime,"yyyyMM"));

        Calendar endCal = Calendar.getInstance();
        endCal.setTime(DateUtil.parse(endTime,"yyyyMM"));

        while(startCal.getTimeInMillis()<=endCal.getTimeInMillis()){
            String nowTime = DateUtil.format(startCal.getTime(),"yyyyMM");

            int regionNum = genRegionNum(tel,nowTime);

            String startRow = regionNum + "_" + tel + "_" + nowTime;
            String stopRow = startRow+"|";

            String[] rowkeys = {startRow,stopRow};
            rowkeyss.add(rowkeys);

            startCal.add(Calendar.MONTH,1);
        }
        return rowkeyss;
    }

    protected int genRegionNum(String tel, String date) {
        String userCode = tel.substring(tel.length() - 4);
        String yearMonth = date.substring(0, 6);
        int userCodeHash = userCode.hashCode();
        int yearMonthHash = yearMonth.hashCode();
        int crc = Math.abs(userCodeHash ^ yearMonthHash);
        int regionNum = crc % ValueConstant.REGION_COUNT;

        return regionNum;
    }

    private byte[][] genSplitKeys(int regionCount) {
        int splitKeyCount = regionCount - 1;
        byte[][] bs = new byte[splitKeyCount][];
        List<byte[]> bsList = new ArrayList<byte[]>();
        for (int i = 0; i < splitKeyCount; i++) {
            String splitKey = i + "|";
            bsList.add(Bytes.toBytes(splitKey));
        }
        bsList.toArray(bs);
        return bs;
    }

    protected void deleteTable(String name) throws Exception {
        TableName tableName = TableName.valueOf(name);
        Admin admin = getAdmin();
        admin.disableTable(tableName);
        admin.deleteTable(tableName);
    }

    protected void putData(Object obj) throws Exception {
        Class clazz = obj.getClass();
        TableRef tableRef = (TableRef)clazz.getAnnotation(TableRef.class);
        String tableName = tableRef.value();
        String stringrowKey = "";
        Field[] fs = clazz.getDeclaredFields();
        for (Field f : fs) {
            Rowkey rowkey = f.getAnnotation(Rowkey.class);
            if(rowkey!=null){
                f.setAccessible(true);
                stringrowKey = (String) f.get(obj);
                break;
            }
        }

        Connection conn = getConnection();
        Table table = conn.getTable(TableName.valueOf(tableName));
        Put put = new Put(Bytes.toBytes(stringrowKey));

        for (Field f : fs) {
            Column column = f.getAnnotation(Column.class);
            if(column!=null){
                f.setAccessible(true);
                String family = column.family();
                String colName = column.column();
                if(colName == null || "".equals(colName)){
                    colName = f.getName();
                }
                f.setAccessible(true);
                String value = (String) f.get(obj);

                put.addColumn(Bytes.toBytes(family),Bytes.toBytes(colName),Bytes.toBytes(value));
            }
        }

        table.put(put);

        table.close();
    }

    protected void putData(String name, Put put) throws Exception {
        Connection conn = getConnection();
        Table table = conn.getTable(TableName.valueOf(name));

        table.put(put);

        table.close();
    }

    protected void putData(String name, List<Put> puts) throws Exception {
        Connection conn = getConnection();
        Table table = conn.getTable(TableName.valueOf(name));

        table.put(puts);

        table.close();
    }
}
