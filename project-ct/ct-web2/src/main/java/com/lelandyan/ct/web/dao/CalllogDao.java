package com.lelandyan.ct.web.dao;

import com.lelandyan.ct.web.bean.Calllog;

import java.util.List;
import java.util.Map;

public interface CalllogDao {
    List<Calllog> queryMonthDatas(Map<String, Object> paraMap);
}
