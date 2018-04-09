package com.restfulapi.entity.dehumidifier;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "dehumidifier_innerunittable")
public class DehumidifierInnerUnit implements Serializable{
    @Id                           //主键配置
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    /**
     * mac地址转long, 长度24位.
     */

    private String mac;

    /**
     * 创建时间
     */

    private Date ctime;

    /**
     * 通讯版本
     0h ：Free(default)
     1h ：V1.0
     2h ：V1.1之后以此类推

     */
    private String txbb;

    /**
     * 通讯速度
     0000：Free
     0001：600 bps
     0010：1200bps(default)

     */
    private String txsd;

    /**
     * 冷暖机型
     0：冷暖(default)
     1：单冷

     */
    private String lnjx;

    /**
     * 变频/定频
     0：变频(default)
     1：定频

     */
    private String dpbp;

    private String njnldm;

    /**
     * 电源频率
     0：50Hz(default)
     1：60Hz

     */
    private String dypl;

    /**
     * 供电方式	0：内机供电(default)
     1：外机供电

     */
    private String gdfs;

    /**
     * 电源种类	000 ：Free
     001 ：100～115V
     010 ：200～240V(default)

     */
    private String dyzl;

    /**
     * 冷媒种类	0000：Free
     0001：R22
     0010：R407C
     0011：R410A(default)

     */
    private String lmzl;

    /**
     * 类型代码	0h：Free(default)
     1h：壁挂     2h：柜机    3h：吊顶机
     4h：风管机   5h：天井式  6h：嵌入式

     */
    private String lxdm;

    /**
     * 环境感温包	0：未安装   1：已安装

     */
    private String hjgwb;

    /**
     * 内管中间感温包	0：未安装   1：已安装

     */
    private String ngzjgwb;

    /**
     * 湿度传感器	0：未安装   1：已安装

     */
    private String sdcgq;

    /**
     * 风机种类	000 ：Free
     001 ：PG电机(default)
     010 ：无刷直流电机
     011 ：抽头电机

     */
    private String fjzl;

    /**
     * 风机档数	0h：Free1h：1档
     2h：2档3h：3档
     4h：4档5h：5档(default)
     6h：6档7h：7档

     */
    private String fjds;

    /**
     * 记忆芯片获取数据标识	0：无记忆芯片(default)
     1：通过记忆芯片获取数据

     */
    private String jyxphqsjbz;

    /**
     * 静电除尘功能	0：无(default)   1：有

     */
    private String jdccgn;

    /**
     * 辅热功能	0：无          1：有(default)

     */
    private String frgn;

    /**
     * 记忆功能	0：无(default)   1：有

     */
    private String jygn;

    /**
     * 健康功能	0：无(default)   1：有

     */
    private String jkgn;

    /**
     * 换气功能	0：无(default)   1：有

     */
    private String hqgn;

    /**
     * 随身感功能	0：无(default)   1：有

     */
    private String ssggn;

    /**
     * 定时方式选择	0：时间定时(default)      1：时刻定时

     */
    private String dsfxxz;

    /**
     * 跳线帽号	00001～11111：1～31号帽子

     */
    private String txmh;

    /**
     * 机型	17.3～17.0：0000～1111：内部控制，用于区分同一机型的不同开发方案（芯片等）
     1h ：NEC；
     2h ：飞思卡尔 ；
     3h：东芝；
     4h：TI；之后以此类推
     ……
     18.7～17.4: 11位表示整机类型
     1h：U尊；
     2h：睡3；之后以此类推
     ……
     19.7～19.0：开发年份
     0h ：2013年
     1h ：2014年
     2h ：2015年 之后以此类推
     ……

     */
    private String jx;

    public DehumidifierInnerUnit() {
    }

    public DehumidifierInnerUnit(String mac, Date ctime, String txbb, String txsd, String lnjx, String dpbp, String njnldm, String dypl, String gdfs, String dyzl, String lmzl, String lxdm, String hjgwb, String ngzjgwb, String sdcgq, String fjzl, String fjds, String jyxphqsjbz, String jdccgn, String frgn, String jygn, String jkgn, String hqgn, String ssggn, String dsfxxz, String txmh, String jx) {
        this.mac = mac;
        this.ctime = ctime;
        this.txbb = txbb;
        this.txsd = txsd;
        this.lnjx = lnjx;
        this.dpbp = dpbp;
        this.njnldm = njnldm;
        this.dypl = dypl;
        this.gdfs = gdfs;
        this.dyzl = dyzl;
        this.lmzl = lmzl;
        this.lxdm = lxdm;
        this.hjgwb = hjgwb;
        this.ngzjgwb = ngzjgwb;
        this.sdcgq = sdcgq;
        this.fjzl = fjzl;
        this.fjds = fjds;
        this.jyxphqsjbz = jyxphqsjbz;
        this.jdccgn = jdccgn;
        this.frgn = frgn;
        this.jygn = jygn;
        this.jkgn = jkgn;
        this.hqgn = hqgn;
        this.ssggn = ssggn;
        this.dsfxxz = dsfxxz;
        this.txmh = txmh;
        this.jx = jx;
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

    public Date getCtime() {
        return ctime;
    }

    public void setCtime(Date ctime) {
        this.ctime = ctime;
    }

    public String getTxbb() {
        return txbb;
    }

    public void setTxbb(String txbb) {
        this.txbb = txbb;
    }

    public String getTxsd() {
        return txsd;
    }

    public void setTxsd(String txsd) {
        this.txsd = txsd;
    }

    public String getLnjx() {
        return lnjx;
    }

    public void setLnjx(String lnjx) {
        this.lnjx = lnjx;
    }

    public String getDpbp() {
        return dpbp;
    }

    public void setDpbp(String dpbp) {
        this.dpbp = dpbp;
    }

    public String getNjnldm() {
        return njnldm;
    }

    public void setNjnldm(String njnldm) {
        this.njnldm = njnldm;
    }

    public String getDypl() {
        return dypl;
    }

    public void setDypl(String dypl) {
        this.dypl = dypl;
    }

    public String getGdfs() {
        return gdfs;
    }

    public void setGdfs(String gdfs) {
        this.gdfs = gdfs;
    }

    public String getDyzl() {
        return dyzl;
    }

    public void setDyzl(String dyzl) {
        this.dyzl = dyzl;
    }

    public String getLmzl() {
        return lmzl;
    }

    public void setLmzl(String lmzl) {
        this.lmzl = lmzl;
    }

    public String getLxdm() {
        return lxdm;
    }

    public void setLxdm(String lxdm) {
        this.lxdm = lxdm;
    }

    public String getHjgwb() {
        return hjgwb;
    }

    public void setHjgwb(String hjgwb) {
        this.hjgwb = hjgwb;
    }

    public String getNgzjgwb() {
        return ngzjgwb;
    }

    public void setNgzjgwb(String ngzjgwb) {
        this.ngzjgwb = ngzjgwb;
    }

    public String getSdcgq() {
        return sdcgq;
    }

    public void setSdcgq(String sdcgq) {
        this.sdcgq = sdcgq;
    }

    public String getFjzl() {
        return fjzl;
    }

    public void setFjzl(String fjzl) {
        this.fjzl = fjzl;
    }

    public String getFjds() {
        return fjds;
    }

    public void setFjds(String fjds) {
        this.fjds = fjds;
    }

    public String getJyxphqsjbz() {
        return jyxphqsjbz;
    }

    public void setJyxphqsjbz(String jyxphqsjbz) {
        this.jyxphqsjbz = jyxphqsjbz;
    }

    public String getJdccgn() {
        return jdccgn;
    }

    public void setJdccgn(String jdccgn) {
        this.jdccgn = jdccgn;
    }

    public String getFrgn() {
        return frgn;
    }

    public void setFrgn(String frgn) {
        this.frgn = frgn;
    }

    public String getJygn() {
        return jygn;
    }

    public void setJygn(String jygn) {
        this.jygn = jygn;
    }

    public String getJkgn() {
        return jkgn;
    }

    public void setJkgn(String jkgn) {
        this.jkgn = jkgn;
    }

    public String getHqgn() {
        return hqgn;
    }

    public void setHqgn(String hqgn) {
        this.hqgn = hqgn;
    }

    public String getSsggn() {
        return ssggn;
    }

    public void setSsggn(String ssggn) {
        this.ssggn = ssggn;
    }

    public String getDsfxxz() {
        return dsfxxz;
    }

    public void setDsfxxz(String dsfxxz) {
        this.dsfxxz = dsfxxz;
    }

    public String getTxmh() {
        return txmh;
    }

    public void setTxmh(String txmh) {
        this.txmh = txmh;
    }

    public String getJx() {
        return jx;
    }

    public void setJx(String jx) {
        this.jx = jx;
    }

    @Override
    public String toString() {
        return "AirRealInnerUnit{" +
                "id=" + id +
                ", mac='" + mac + '\'' +
                ", ctime=" + ctime +
                ", txbb='" + txbb + '\'' +
                ", txsd='" + txsd + '\'' +
                ", lnjx=" + lnjx +
                ", dpbp=" + dpbp +
                ", njnldm='" + njnldm + '\'' +
                ", dypl=" + dypl +
                ", gdfs=" + gdfs +
                ", dyzl='" + dyzl + '\'' +
                ", lmzl='" + lmzl + '\'' +
                ", lxdm='" + lxdm + '\'' +
                ", hjgwb=" + hjgwb +
                ", ngzjgwb=" + ngzjgwb +
                ", sdcgq=" + sdcgq +
                ", fjzl='" + fjzl + '\'' +
                ", fjds='" + fjds + '\'' +
                ", jyxphqsjbz=" + jyxphqsjbz +
                ", jdccgn=" + jdccgn +
                ", frgn=" + frgn +
                ", jygn=" + jygn +
                ", jkgn=" + jkgn +
                ", hqgn=" + hqgn +
                ", ssggn=" + ssggn +
                ", dsfxxz=" + dsfxxz +
                ", txmh='" + txmh + '\'' +
                ", jx=" + jx +
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
