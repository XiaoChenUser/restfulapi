package com.restfulapi.entity.dehumidifier;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "dehumidifier_deviceinfotable")
public class DehumidifierDevice implements Serializable {
    @Id                           //主键配置
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;


    /**
     * mac地址转long, 长度24位.
     */

    private String mac;

    /**
     * 空调的产品系列号
     */

    private String mid;


    /**
     * 故障代码 例如 F6
     */
    private String codes;

    /**
     * 事件
     */
    private String evt;

    public DehumidifierDevice() {

    }

    public DehumidifierDevice(String mac, String mid, String codes, String evt) {
        this.mac = mac;
        this.mid = mid;
        this.codes = codes;
        this.evt = evt;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public String getCodes() {
        return codes;
    }

    public void setCodes(String codes) {
        this.codes = codes;
    }

    public String getEvt() {
        return evt;
    }

    public void setEvt(String evt) {
        this.evt = evt;
    }

    @Override
    public String toString() {
        return "AirRealDevice{" +
                "id=" + id +
                ", mac='" + mac + '\'' +
                ", mid='" + mid + '\'' +
                ", codes='" + codes + '\'' +
                ", evt=" + evt +
                '}';
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}
