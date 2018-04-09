package com.restfulapi.common.pojo;

public class ReqEntity {
    public String Mac;
    public String Mid;
    public Boolean TransFlag;
    public String AskFlag;
    public String BeginTime;
    public String EndTime;

    public Boolean getTransFlag() {
        return TransFlag;
    }

    public String getMac() {
        return Mac;
    }

    public String getMid() {
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
