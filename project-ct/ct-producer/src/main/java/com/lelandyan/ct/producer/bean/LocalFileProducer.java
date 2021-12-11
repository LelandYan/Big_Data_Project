package com.lelandyan.ct.producer.bean;

import com.lelandyan.ct.common.bean.DataIn;
import com.lelandyan.ct.common.bean.DataOut;
import com.lelandyan.ct.common.bean.Producer;
import com.lelandyan.ct.common.util.DateUtil;
import com.lelandyan.ct.common.util.NumberUtil;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.TreeMap;

/**
 * 本地数据文件生产者
 */
public class LocalFileProducer implements Producer {
    private DataIn in;
    private DataOut out;
    private volatile boolean flg = true;

    @Override
    public void setIn(DataIn in) {
        this.in = in;
    }

    @Override
    public void setOut(DataOut out) {
        this.out = out;
    }

    @Override
    public void produce() {
        try {
            List<Contact> contacts = in.read(Contact.class);
            while (flg) {
                int call1Index = new Random().nextInt(contacts.size());
                int call2Index;
                while (true) {
                    call2Index = new Random().nextInt(contacts.size());
                    if (call2Index != call1Index) break;
                }
                Contact call1 = contacts.get(call1Index);
                Contact call2 = contacts.get(call2Index);

                String startDate = "20210101000000";
                String endDate = "20220101000000";
                long startTime = DateUtil.parse(startDate,"yyyyMMddHHmmss").getTime();
                long endTime = DateUtil.parse(endDate,"yyyyMMddHHmmss").getTime();
                long callTime = startTime + (long)((endTime-startTime)*Math.random());
                String callTimeString = DateUtil.format(new Date(callTime),"yyyyMMddHHmmss");

                String duration = NumberUtil.format(new Random().nextInt(3000),4);

                CallLog log = new CallLog(call1.getTel(),call2.getTel(),callTimeString,duration);

                System.out.println(log);
                out.write(log);
                Thread.sleep(500);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void close() throws IOException {
        if (in != null) {
            in.close();
        }
        if (out != null) {
            out.close();
        }
    }
}
