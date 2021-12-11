package com.lelandyan.ct.producer.bean;

import com.lelandyan.ct.common.bean.Data;

public class Contact extends Data {
    private String tel;
    private String name;

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Contact[" +tel + ", " + name +"]";
    }

    public void setValue(Object value) {
        content = (String) value;
        String[] values = content.split("\t");
        setName(values[1]);
        setTel(values[0]);
    }


}
