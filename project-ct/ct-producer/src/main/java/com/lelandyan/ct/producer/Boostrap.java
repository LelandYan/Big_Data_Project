package com.lelandyan.ct.producer;

import com.lelandyan.ct.common.bean.Producer;
import com.lelandyan.ct.producer.bean.LocalFileProducer;
import com.lelandyan.ct.producer.io.LocalFileDataIn;
import com.lelandyan.ct.producer.io.LocalFileDataOut;

import java.io.IOException;

/**
 * 启动对象
 */
public class Boostrap {
    public static void main(String[] args) throws IOException {
        if(args.length < 2){
            System.out.println("系统参数不正确，请按照执行格式传递: java -jar Produce.jar path1 path2");
            System.exit(1);
        }
        // 构建生产者对象
        Producer producer = new LocalFileProducer();

//        producer.setIn(new LocalFileDataIn("D:\\contact.log"));
//        producer.setOut(new LocalFileDataOut("D:\\call.log"));

        producer.setIn(new LocalFileDataIn(args[0]));
        producer.setOut(new LocalFileDataOut(args[1]));
        // 生产数据
        producer.produce();

        // 关闭生产者对象
        producer.close();
    }
}
