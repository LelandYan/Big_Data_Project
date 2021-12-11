package com.lelandyan.ct.cache;

import com.lelandyan.ct.common.util.JDBCUtil;
import redis.clients.jedis.Jedis;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Bootstrap {
    public static void main(String[] args) {
        Map<String, Integer> userMap = new HashMap<>();
        Map<String, Integer> dateMap = new HashMap<>();

        Connection connection = null;
        String queryUserSql = "select id,tel from ct_user";
        String queryDateSql = "select id,year,month,day from ct_date";
        PreparedStatement pstat = null;
        ResultSet res = null;
        try {
            connection = JDBCUtil.getConnection();

            pstat = connection.prepareStatement(queryUserSql);
            res = pstat.executeQuery();
            while (res.next()) {
                Integer id = res.getInt(1);
                String tel = res.getString(2);
                userMap.put(tel, id);
            }
            res.close();
            pstat = connection.prepareStatement(queryDateSql);
            res = pstat.executeQuery();
            while (res.next()) {
                Integer id = res.getInt(1);
                String year = res.getString(2);
                String month = res.getObject(3) == null ? "" : res.getString(3);
                if (month.length() == 1) {
                    month = "0" + month;
                }
                String day = res.getObject(4) == null ? "" : res.getString(4);
                if (day.length() == 0) {
                    day = "0" + day;
                }
                dateMap.put(year + month + day, id);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (res != null) {
                try {
                    res.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (pstat != null) {
                try {
                    pstat.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
//        System.out.println(userMap.size());
//        System.out.println(dateMap.size());
        Jedis jedis = new Jedis("hadoop104", 6379);
        Iterator<String> keyIterator = userMap.keySet().iterator();
        while (keyIterator.hasNext()) {
            String key = keyIterator.next();
            Integer value = userMap.get(key);
            jedis.hset("ct_user", key, "" + value);
        }

        keyIterator = dateMap.keySet().iterator();
        while (keyIterator.hasNext()) {
            String key = keyIterator.next();
            Integer value = dateMap.get(key);
            jedis.hset("ct_date", key, "" + value);
        }
    }
}
