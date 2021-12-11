package com.lelandyan.ct.common.constant;

import com.lelandyan.ct.common.bean.Val;

/**
 * 常量枚举类
 */
public enum Names implements Val {
    NAMESPACE("ct"),TOPIC("ct"),TABLE("ct:calllog"),CF_CALLEE("callee"),CF_CALLER("caller"),CF_INFO("info");
    private String name;

    private Names(String name){
        this.name = name;
    }

    @Override
    public void setValue(Object value) {
        this.name = (String)value;
    }

    @Override
    public String getValue() {
        return name;
    }
}
