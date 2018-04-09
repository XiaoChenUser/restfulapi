package com.restfulapi.common.pojo;

public class JsonResult {
    private String respFlag;

    private Boolean TransFlag;

    private String date;

    public JsonResult() {
    }

    public JsonResult(String respFlag, Boolean transFlag, String date) {
        this.respFlag = respFlag;
        TransFlag = transFlag;
        this.date = date;
    }

    public String getRespFlag() {
        return respFlag;
    }

    public void setRespFlag(String respFlag) {
        this.respFlag = respFlag;
    }

    public Boolean getTransFlag() {
        return TransFlag;
    }

    public void setTransFlag(Boolean transFlag) {
        TransFlag = transFlag;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "JsonResult{" +
                "respFlag='" + respFlag + '\'' +
                ", TransFlag='" + TransFlag + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}