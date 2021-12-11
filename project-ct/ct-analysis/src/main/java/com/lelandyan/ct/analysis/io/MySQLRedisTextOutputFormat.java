package com.lelandyan.ct.analysis.io;

import com.lelandyan.ct.common.util.JDBCUtil;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.mapreduce.lib.output.FileOutputCommitter;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class MySQLRedisTextOutputFormat extends OutputFormat<Text, Text> {

    protected static class MySQLRecordWriter extends RecordWriter<Text, Text> {
        private Connection connection = null;
        private Jedis jedis = null;

        public MySQLRecordWriter() {
            connection = JDBCUtil.getConnection();
            jedis =  new Jedis("hadoop104", 6379);
        }

        @Override
        public void write(Text key, Text value) throws IOException, InterruptedException {
            String[] values = value.toString().split("_");
            String sumCall = values[0];
            String sumDuration = values[1];

            PreparedStatement pstat = null;
            try {
                String insertSQL = "insert into ct_call (tel_id,date_id,call_sum,call_duration_sum) values (?,?,?,?)";
                pstat = connection.prepareStatement(insertSQL);

                String k = key.toString();
                String[] ks = k.split("_");

                String tel = ks[0];
                String date = ks[1];

                pstat.setInt(1, Integer.parseInt(jedis.hget("ct_user",tel)));
                pstat.setInt(2, Integer.parseInt(jedis.hget("ct_date",date)));
                pstat.setInt(3, Integer.parseInt(sumCall));
                pstat.setInt(4, Integer.parseInt(sumDuration));
                pstat.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                if (pstat != null) {
                    try {
                        pstat.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        @Override
        public void close(TaskAttemptContext taskAttemptContext) throws IOException, InterruptedException {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if(jedis!=null){
                jedis.close();
            }
        }
    }

    @Override
    public RecordWriter<Text, Text> getRecordWriter(TaskAttemptContext taskAttemptContext) throws IOException, InterruptedException {
        return new MySQLRecordWriter();
    }

    @Override
    public void checkOutputSpecs(JobContext jobContext) throws IOException, InterruptedException {

    }

    private FileOutputCommitter committer = null;

    public static Path getOutputPath(JobContext job) {
        String name = job.getConfiguration().get(FileOutputFormat.OUTDIR);
        return name == null ? null : new Path(name);
    }

    @Override
    public OutputCommitter getOutputCommitter(TaskAttemptContext taskAttemptContext) throws IOException, InterruptedException {

        if (this.committer == null) {
            Path output = getOutputPath(taskAttemptContext);
            this.committer = new FileOutputCommitter(output, taskAttemptContext);
        }

        return this.committer;
    }
}
