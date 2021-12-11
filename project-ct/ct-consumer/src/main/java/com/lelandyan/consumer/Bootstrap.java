package com.lelandyan.consumer;

import com.lelandyan.consumer.bean.CallLogConsumer;

import java.io.IOException;

/**
 * 启动消费者
 * // 使用kafka的消费者获取flume采集的数据
 *
 * // 将数据存储到Hbase中去
 */
public class Bootstrap {
    public static void main(String[] args) throws IOException {
        //创建消费者
        CallLogConsumer consumer = new CallLogConsumer();

        //消费数据
        consumer.consume();

        // 关闭资源
        consumer.close();
    }
}
