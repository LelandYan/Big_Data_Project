package com.lelandyan.ct.consumer.coprocessor;

import com.lelandyan.ct.common.bean.BaseDao;
import com.lelandyan.ct.common.constant.Names;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Durability;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.coprocessor.ObserverContext;
import org.apache.hadoop.hbase.coprocessor.RegionCoprocessor;
import org.apache.hadoop.hbase.coprocessor.RegionCoprocessorEnvironment;
import org.apache.hadoop.hbase.coprocessor.RegionObserver;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.hbase.wal.WALEdit;

import java.io.IOException;
import java.util.Optional;


public class InsertCalleeCoprocessor implements RegionObserver, RegionCoprocessor {
    @Override
    public Optional<RegionObserver> getRegionObserver() {
        return Optional.of(this);
    }

    @Override
    public void postPut(ObserverContext<RegionCoprocessorEnvironment> c, Put put, WALEdit edit, Durability durability) throws IOException {
        //Table table = c.getEnvironment().getTable(TableName.valueOf(Names.TABLE.getValue()));
        //Table table = c.getEnvironment().createConnection(c.getEnvironment().getConfiguration()).getTable(TableName.valueOf(Names.TABLE.getValue()));
//        Table table = c.getEnvironment().getConnection().getTable(TableName.valueOf(Names.TABLE.getValue()));
        TableName tn = TableName.valueOf(Names.TABLE.getValue());
        Table table = c.getEnvironment().createConnection(c.getEnvironment().getConfiguration()).getTable(tn);


        String rowKey = Bytes.toString(put.getRow());
        String[] values = rowKey.split("_");

        CoprocessorDao dao = new CoprocessorDao();
        String call1 = values[1];
        String call2 = values[3];
        String callTime = values[2];
        String duration = values[4];
        String flg = values[5];

//        System.out.println("corprocessor go.........................");
        System.out.println(flg);
        if ("1".equals(flg)) {
//            System.out.println("corprocessor begin......................");
            String calleerowKey = dao.getRegionNum(call2, callTime) + "_" + call2 + "_" + callTime + "_" + call1 + "_" + duration + "_0";

            Put calleeput = new Put(Bytes.toBytes(calleerowKey));
            byte[] calleefamily = Bytes.toBytes(Names.CF_CALLEE.getValue());
            calleeput.addColumn(calleefamily, Bytes.toBytes("call1"), Bytes.toBytes(call2));
            calleeput.addColumn(calleefamily, Bytes.toBytes("call2"), Bytes.toBytes(call1));
            calleeput.addColumn(calleefamily, Bytes.toBytes("callTime"), Bytes.toBytes(callTime));
            calleeput.addColumn(calleefamily, Bytes.toBytes("duration"), Bytes.toBytes(duration));
            calleeput.addColumn(calleefamily, Bytes.toBytes("flg"), Bytes.toBytes("0"));
            table.put(calleeput);

//            System.out.println("corprocessor end......................");

        }
        table.close();

    }

    private class CoprocessorDao extends BaseDao {
        public int getRegionNum(String tel, String time) {
            return genRegionNum(tel, time);
        }
    }
}
