package com.restfulapi.common.pojo;

import java.util.ArrayList;

public class ArrayRequest {
    public ArrayList Mac;
    public ArrayList Mid;
    public Boolean TransFlag;
    public String AskFlag;
    public String BeginTime;
    public String EndTime;

    public Boolean getTransFlag() {
        return TransFlag;
    }

    public ArrayList getMac() {
        return Mac;
    }

    public ArrayList getMid() {
        return Mid;
    }

    public String getAskFlag() {
        return AskFlag;
    }

    //@NotBlank(message = "时间不能为空")
    public String getBeginTime() {
        return BeginTime;
    }

    public String getEndTime() {
        return EndTime;
    }

}
