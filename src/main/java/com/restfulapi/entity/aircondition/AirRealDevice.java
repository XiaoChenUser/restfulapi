package com.restfulapi.entity.aircondition;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "aircondition_historydeviceinfotable")
public class AirRealDevice implements Serializable {
    @Id                           //主键配置
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    /**
     *  数据库函数转换： mysql的inet_aton:ip->num,inet_ntoa:number->ip
     */
    private String ip;

    /**
     * mac地址转long, 长度24位.
     */

    private String mac;

    /**
     * 空调的产品系列号
     */

    private String mid;

    /**
     * 国家
     */
    private String country;

    /**
     * 省份
     */
    private String province;

    /**
     * 城市
     */
    private String city;

    /**
     * 故障代码 例如 F6
     */
    private String codes;

    /**
     * 事件
     */
    private String evt;

    public AirRealDevice() {

    }

    public AirRealDevice(String ip, String mac, String mid, String country, String province, String city, String codes, String evt) {
        this.ip = ip;
        this.mac = mac;
        this.mid = mid;
        this.country = country;
        this.province = province;
        this.city = city;
        this.codes = codes;
        this.evt = evt;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
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

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
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
                ", ip='" + ip + '\'' +
                ", mac='" + mac + '\'' +
                ", mid='" + mid + '\'' +
                ", country='" + country + '\'' +
                ", province='" + province + '\'' +
                ", city='" + city + '\'' +
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
