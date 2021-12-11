package com.lelandyan.ct.common.bean;

/**
 * 数据对象
 */
public abstract class Data implements Val{
    public String content;

    @Override
    public void setValue(Object value) {
        content = (String) value;
    }

    @Override
    public Object getValue() {
        return content;
    }
}
