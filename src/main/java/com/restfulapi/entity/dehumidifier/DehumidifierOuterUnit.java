package com.restfulapi.entity.dehumidifier;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "dehumidifier_outerunittable")
public class DehumidifierOuterUnit implements Serializable{
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
     * 通信版本	0h ：Free(default)
     1h ：V1.0
     2h ：V1.1 之后以此类推

     */
    private String txbb;

    /**
     * 通信速度	0000：Free
     0001：600 bps
     0010：1200bps(default)

     */
    private String txsd;

    /**
     * 冷暖机型	0：冷暖(default)
     1：单冷

     */
    private String lnjx;

    /**
     * 变频/定频	0：变频(default)
     1：定频

     */
    private String bpdp;

    /**
     * 外机能力代码	0.1kw/Bit、
     251～255表示如下：
     251：28.0kw
     252：29.0kw
     253：30.0kw
     254：31.0kw
     255：32.0kw

     */
    private String wjnldm;

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
     * 室外机类型	0000：一拖一室外机(default)
     0001～0111：一拖二至一拖八室外机（若不区分所配内机数量，也按一拖八外机发送）

     */
    private String swjlx;

    /**
     * 压缩机型号（预留）	0000：Free(default)
     0001：凌达
     0010：三菱
     0011：日立
     0100：松下
     0101：三洋
     0110：大金

     */
    private String ysjxh;

    /**
     * 出风类型	0：侧出风(default)
     1：上出风

     */
    private String cflx;

    /**
     * 风机种类	000 ：Free
     001 ：交流电机(default)
     010 ：直流电机

     */
    private String fjzl;

    /**
     * 风机个数	0：单风机(default),
     1：双风机

     */
    private String fjgs;

    /**
     * 风机档数	0h ：Free
     1h ：1档(default)
     2h ：2档
     3 h ：3档
     4 h ：4档
     5 h ：5档
     6 h ：6档
     7h ：7档

     */
    private String fjds;

    /**
     * 底盘（冷凝器）预热带有无	0：无(default)   1：有

     */
    private String dplnqyrdyw;

    /**
     * 压缩机预热带有无	0：无(default)   1：有

     */
    private String ysjyrdyw;

    /**
     * 线圈自预热有无	0：无(default)   1：有

     */
    private String xqzyryw;

    /**
     * 电子膨胀阀有无	0：无(default)   1：有

     */
    private String dzpzfyw;

    /**
     * 机型码	29.3～29.0：0000～1111：内部控制，用于区分同一机型的不同开发方案（芯片等）
     1h ：NEC；
     2h ：飞思卡尔 ；
     3h：东芝；
     4h：TI；之后以此类推
     ……
     30.7～29.4: 11位表示整机类型
     1h：U尊；
     2h：睡3；之后以此类推
     ……
     31.7～31.0：开发年份
     0h ：2013年
     1h ：2014年
     2h ：2015年 之后以此类推
     ……

     */
    private String jxm;

    /**
     * 外风机IPM模块	0：不限制 1：东芝；2：三垦；

     */
    private String wfjipmmk;

    /**
     * 压缩机IPM模块	0：不限制 1：LS；2：三菱；3：IR
     4：三垦

     */
    private String ysjipmmk;

    /**
     * 内机程序版本号	0h：Free
     1h：V1.0
     2h：V1.1，之后以此类推

     */
    private String njcxbbh;

    /**
     * 外机程序版本号	0h：Free
     1h：V1.0
     2h：V1.1，之后以此类推

     */
    private String wjcxbbh;

    public DehumidifierOuterUnit() {

    }

    public DehumidifierOuterUnit(String mac, Date ctime, String txbb, String txsd, String lnjx, String bpdp, String wjnldm, String gdfs, String dyzl, String swjlx, String ysjxh, String cflx, String fjzl, String fjgs, String fjds, String dplnqyrdyw, String ysjyrdyw, String xqzyryw, String dzpzfyw, String jxm, String wfjipmmk, String ysjipmmk, String njcxbbh, String wjcxbbh) {
        this.mac = mac;
        this.ctime = ctime;
        this.txbb = txbb;
        this.txsd = txsd;
        this.lnjx = lnjx;
        this.bpdp = bpdp;
        this.wjnldm = wjnldm;
        this.gdfs = gdfs;
        this.dyzl = dyzl;
        this.swjlx = swjlx;
        this.ysjxh = ysjxh;
        this.cflx = cflx;
        this.fjzl = fjzl;
        this.fjgs = fjgs;
        this.fjds = fjds;
        this.dplnqyrdyw = dplnqyrdyw;
        this.ysjyrdyw = ysjyrdyw;
        this.xqzyryw = xqzyryw;
        this.dzpzfyw = dzpzfyw;
        this.jxm = jxm;
        this.wfjipmmk = wfjipmmk;
        this.ysjipmmk = ysjipmmk;
        this.njcxbbh = njcxbbh;
        this.wjcxbbh = wjcxbbh;
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

    public String getBpdp() {
        return bpdp;
    }

    public void setBpdp(String bpdp) {
        this.bpdp = bpdp;
    }

    public String getWjnldm() {
        return wjnldm;
    }

    public void setWjnldm(String wjnldm) {
        this.wjnldm = wjnldm;
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

    public String getSwjlx() {
        return swjlx;
    }

    public void setSwjlx(String swjlx) {
        this.swjlx = swjlx;
    }

    public String getYsjxh() {
        return ysjxh;
    }

    public void setYsjxh(String ysjxh) {
        this.ysjxh = ysjxh;
    }

    public String getCflx() {
        return cflx;
    }

    public void setCflx(String cflx) {
        this.cflx = cflx;
    }

    public String getFjzl() {
        return fjzl;
    }

    public void setFjzl(String fjzl) {
        this.fjzl = fjzl;
    }

    public String getFjgs() {
        return fjgs;
    }

    public void setFjgs(String fjgs) {
        this.fjgs = fjgs;
    }

    public String getFjds() {
        return fjds;
    }

    public void setFjds(String fjds) {
        this.fjds = fjds;
    }

    public String getDplnqyrdyw() {
        return dplnqyrdyw;
    }

    public void setDplnqyrdyw(String dplnqyrdyw) {
        this.dplnqyrdyw = dplnqyrdyw;
    }

    public String getYsjyrdyw() {
        return ysjyrdyw;
    }

    public void setYsjyrdyw(String ysjyrdyw) {
        this.ysjyrdyw = ysjyrdyw;
    }

    public String getXqzyryw() {
        return xqzyryw;
    }

    public void setXqzyryw(String xqzyryw) {
        this.xqzyryw = xqzyryw;
    }

    public String getDzpzfyw() {
        return dzpzfyw;
    }

    public void setDzpzfyw(String dzpzfyw) {
        this.dzpzfyw = dzpzfyw;
    }

    public String getJxm() {
        return jxm;
    }

    public void setJxm(String jxm) {
        this.jxm = jxm;
    }

    public String getWfjipmmk() {
        return wfjipmmk;
    }

    public void setWfjipmmk(String wfjipmmk) {
        this.wfjipmmk = wfjipmmk;
    }

    public String getYsjipmmk() {
        return ysjipmmk;
    }

    public void setYsjipmmk(String ysjipmmk) {
        this.ysjipmmk = ysjipmmk;
    }

    public String getNjcxbbh() {
        return njcxbbh;
    }

    public void setNjcxbbh(String njcxbbh) {
        this.njcxbbh = njcxbbh;
    }

    public String getWjcxbbh() {
        return wjcxbbh;
    }

    public void setWjcxbbh(String wjcxbbh) {
        this.wjcxbbh = wjcxbbh;
    }

    @Override
    public String toString() {
        return "AirRealOuterUnit{" +
                "id=" + id +
                ", mac='" + mac + '\'' +
                ", ctime=" + ctime +
                ", txbb='" + txbb + '\'' +
                ", txsd='" + txsd + '\'' +
                ", lnjx=" + lnjx +
                ", bpdp=" + bpdp +
                ", wjnldm='" + wjnldm + '\'' +
                ", gdfs='" + gdfs + '\'' +
                ", dyzl='" + dyzl + '\'' +
                ", swjlx='" + swjlx + '\'' +
                ", ysjxh='" + ysjxh + '\'' +
                ", cflx=" + cflx +
                ", fjzl='" + fjzl + '\'' +
                ", fjgs=" + fjgs +
                ", fjds='" + fjds + '\'' +
                ", dplnqyrdyw=" + dplnqyrdyw +
                ", ysjyrdyw=" + ysjyrdyw +
                ", xqzyryw=" + xqzyryw +
                ", dzpzfyw=" + dzpzfyw +
                ", jxm='" + jxm + '\'' +
                ", wfjipmmk='" + wfjipmmk + '\'' +
                ", ysjipmmk='" + ysjipmmk + '\'' +
                ", njcxbbh='" + njcxbbh + '\'' +
                ", wjcxbbh='" + wjcxbbh + '\'' +
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
