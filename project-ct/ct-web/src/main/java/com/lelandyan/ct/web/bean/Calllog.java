package com.lelandyan.ct.web.bean;

public class Calllog {
    private Integer id;
    private Integer tel_id;
    private Integer date_id;
    private Integer call_sum;
    private Integer call_duration_sum;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTel_id() {
        return tel_id;
    }

    public void setTel_id(Integer tel_id) {
        this.tel_id = tel_id;
    }

    public Integer getDate_id() {
        return date_id;
    }

    public void setDate_id(Integer date_id) {
        this.date_id = date_id;
    }

    public Integer getCall_sum() {
        return call_sum;
    }

    public void setCall_sum(Integer call_sum) {
        this.call_sum = call_sum;
    }

    public Integer getCall_duration_sum() {
        return call_duration_sum;
    }

    public void setCall_duration_sum(Integer call_duration_sum) {
        this.call_duration_sum = call_duration_sum;
    }
}
