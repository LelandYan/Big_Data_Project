package com.lelandyan.ct.web.service.impl;

import com.lelandyan.ct.web.bean.Calllog;
import com.lelandyan.ct.web.dao.CalllogDao;
import com.lelandyan.ct.web.service.CalllogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CalllogServiceImpl implements CalllogService {
    @Autowired
    private CalllogDao calllogDao;

    @Override
    public List<Calllog> queryMonthDatas(String tel, String calltime) {
        System.out.println("begin CalllogServiceImpl");
        Map<String, Object> paraMap = new HashMap<>();
        paraMap.put("tel", tel);
        if (calltime.length() > 4) {
            calltime = calltime.substring(0, 4);
        }
        paraMap.put("year", calltime);
        System.out.println(paraMap);
        List<Calllog> res = calllogDao.queryMonthDatas(paraMap);
        System.out.println(res.toString());
        return res;
    }
}
