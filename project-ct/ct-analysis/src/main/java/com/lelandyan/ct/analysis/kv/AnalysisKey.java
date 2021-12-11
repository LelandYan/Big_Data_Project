package com.lelandyan.ct.analysis.kv;

import org.apache.hadoop.io.WritableComparable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class AnalysisKey implements WritableComparable<AnalysisKey>{
    public AnalysisKey(){

    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public AnalysisKey(String tel, String date){
        this.tel = tel;
        this.date = date;
    }
    private String tel;
    private String date;

    @Override
    public void write(DataOutput dataOutput) throws IOException {
        dataOutput.writeUTF(tel);
        dataOutput.writeUTF(date);
    }

    @Override
    public void readFields(DataInput dataInput) throws IOException {
        tel = dataInput.readUTF();
        date = dataInput.readUTF();
    }

    @Override
    public int compareTo(AnalysisKey o) {
        int result =  tel.compareTo(o.getTel());
        if(result == 0){
            result = date.compareTo(o.getDate());
        }
        return result;
    }
}
