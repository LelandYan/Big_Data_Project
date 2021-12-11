package com.lelandyan.consumer.dao;

import com.lelandyan.consumer.bean.Calllog;
import com.lelandyan.ct.common.bean.BaseDao;
import com.lelandyan.ct.common.constant.Names;
import com.lelandyan.ct.common.constant.ValueConstant;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;

import java.util.ArrayList;
import java.util.List;

/**
 * Hbase数据访问对象
 */
public class HbaseDao extends BaseDao {
    public void init() throws Exception {
        start();

        createNamespaceNX(Names.NAMESPACE.getValue());
        System.out.println("com.lelandyan.ct.consumer.coprocessor.InsertCalleeCoprocessor bofore");
        createTableXX(Names.TABLE.getValue(), "com.lelandyan.ct.consumer.coprocessor.InsertCalleeCoprocessor",ValueConstant.REGION_COUNT,Names.CF_CALLER.getValue(),Names.CF_CALLEE.getValue());
        System.out.println("com.lelandyan.ct.consumer.coprocessor.InsertCalleeCoprocessor end");
        end();
    }
    public void insertData(Calllog log) throws Exception{
        log.setRowKey(genRegionNum(log.getCall1(),log.getCallTime()) + "_" + log.getCall1() + "_" + log.getCallTime() + "_" + log.getCall2() + "_" + log.getDuration());
        putData(log);
    }

    public void insertData(String value) throws Exception{
        // 将童话日志保存到Hbase表中
        String[] values = value.split("\t");
        String call1 = values[0];
        String call2 = values[1];
        String callTime = values[2];
        String duration = values[3];

        String rowKey = genRegionNum(call1,callTime) + "_" + call1 + "_" + callTime + "_" + call2 + "_" + duration + "_1";
        Put put = new Put(Bytes.toBytes(rowKey));

        byte[] family = Bytes.toBytes(Names.CF_CALLER.getValue());

        put.addColumn(family,Bytes.toBytes("call1"),Bytes.toBytes(call1));
        put.addColumn(family,Bytes.toBytes("call2"),Bytes.toBytes(call2));
        put.addColumn(family,Bytes.toBytes("callTime"),Bytes.toBytes(callTime));
        put.addColumn(family,Bytes.toBytes("duration"),Bytes.toBytes(duration));
        put.addColumn(family,Bytes.toBytes("flg"),Bytes.toBytes("1"));


//        String calleerowKey = genRegionNum(call2,callTime) + "_" + call2 + "_" + callTime + "_" + call1 + "_" + duration + "_0";
//        Put calleeput = new Put(Bytes.toBytes(calleerowKey));
//
//        byte[] calleefamily = Bytes.toBytes(Names.CF_CALLEE.getValue());

//        calleeput.addColumn(calleefamily,Bytes.toBytes("call1"),Bytes.toBytes(call2));
//        calleeput.addColumn(calleefamily,Bytes.toBytes("call2"),Bytes.toBytes(call1));
//        calleeput.addColumn(calleefamily,Bytes.toBytes("callTime"),Bytes.toBytes(callTime));
//        calleeput.addColumn(calleefamily,Bytes.toBytes("duration"),Bytes.toBytes(duration));
//        calleeput.addColumn(calleefamily,Bytes.toBytes("flg"),Bytes.toBytes("0"));

        List<Put> puts = new ArrayList<>();
        puts.add(put);
        //puts.add(calleeput);
        putData(Names.TABLE.getValue(), puts);
    }
}
