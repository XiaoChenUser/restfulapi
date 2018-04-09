package com.restfulapi.entity.aircondition;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "aircondition_outerstatustable")
public class AirOuterStatus implements Serializable{
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
     * 压缩机开/关状态	0：压缩机关
     1：压缩机开

     */
    private String ysjkgzt;

    /**
     * 回送模式	0000：关机
     0001：制冷
     0010：抽湿
     0011：送风
     0100：制热
     0101：强制制冷
     0110：强制制热
     0111：强制除霜
     1000：制冷能力测试
     1001：制热能力测试
     1010：收氟模式
     1011：试运行

     */
    private String hsms;

    /**
     * 压缩机运行转速	实际运行转速 单位：rps

     */
    private String ysjyxzs;

    /**
     * 外风机1转速	实际转速/10  单位：rpm

     */
    private String wfj1zs;

    /**
     * 外风机2转速	实际转速/10  单位：rpm

     */
    private String wfj2zs;

    /**
     * 压缩机运行功率	实际运行功率/32 单位：W

     */
    private String ysjyxgl;

    /**
     * 电子膨胀阀开度	实际电子膨胀阀步数（D[6]为低位）
     单位：step

     */
    private String dzpzfkd;

    /**
     * 直流母线电压	实际直流母线电压值/4
     单位：V

     */
    private String zlmxdy;

    /**
     * 室外排气温度小数标志	00： 0℃标志
     01： 0.25℃标志
     10： 0.5℃标志
     11： 0.75℃标志

     */
    private String swpqwdxsbz;

    /**
     * 室外冷凝器温度小数标志	00： 0℃标志
     01： 0.25℃标志
     10： 0.5℃标志
     11： 0.75℃标志

     */
    private String swlnqwdxsbz;

    /**
     * 室外环境温度小数标志	00： 0℃标志
     01： 0.25℃标志
     10： 0.5℃标志
     11： 0.75℃标志

     */
    private String swlhjwdxsbz;

    /**
     * 室外环境温度	0h：-40℃，数据每增加1则温度增加1℃

     */
    private String swhjwd;

    /**
     * 室外冷凝器中间温度	0h：-40℃，数据每增加1则温度增加1℃

     */
    private String swlnqzjwd;

    /**
     * 室外排气温度	0h：-40℃，数据每增加1则温度增加1℃

     */
    private String swpqwd;

    /**
     * SAVE状态	0：通常状态(default)
     1：SAVE状态运行中

     */
    private String savezt;

    /**
     * 外机防凝露状态	0：通常状态(default)
     1：外机防凝露中

     */
    private String wjfnlzt;

    /**
     * 外机静音状态	0：通常状态(default)
     1：外机静音中

     */
    private String wjjyzt;

    /**
     * 电子膨胀阀状态	0：通常状态(default)
     1：复位中

     */
    private String dzpzfzt;

    /**
     * 3分钟待机状态	0：通常状态(default)
     1：3分钟待机中

     */
    private String sfzdjzt;

    /**
     * 回油状态	0：无需停内风机的制热回油或非回油状态
     1：制冷回油 或 需要停内风机的制热回油

     */
    private String hyzt;

    /**
     * 普通除霜状态	0：通常状态(default) 或特殊除霜状态
     1：普通除霜中

     */
    private String ptcszt;

    /**
     * 除霜要求	0：无要求(default)
     1：除霜要求

     */
    private String csyq;

    /**
     * 特殊除霜状态	0：通常状态(default) 或普通除霜状态
     1：特殊除霜中

     */
    private String tscszt;

    /**
     * 外机任意停机故障	0：正常
     1：故障 oE
     (外机出现任意需要停机的故障该位置1)

     */
    private String wjrytjgz;

    /**
     * 回送自测状态	00：无自测
     01：自测1
     10：自测2；
     11：自测3

     */
    private String hszczt;

    /**
     * 外机AC电流值	实际电流，单位0.5A

     */
    private String wjacdlz;

    /**
     * 试运行	0：未执行；1：执行
     */
    private String syx;

    /**
     * 收氟模式	0：未执行；1：执行
     */
    private String sfms;

    /**
     * 强制除霜	0：未执行；1：执行
     */
    private String qzcs;

    /**
     * 强制制热	0：未执行；1：执行
     */
    private String qzzr;

    /**
     * 强制制冷	0：未执行；1：执行

     */
    private String qzzl;

    /**
     * DRED模式运行状态	00-无DRED模式运行
     01-DRED 1模式运行
     10-DRED 2模式运行
     11-DRED 3模式运行

     */
    private String dredmsyxzt;

    /**
     * 功率过高保护限/降频	0：正常            1：限/降频
     */
    private String glggbhxjp;

    /**
     * 模块电流（压缩机相电流）保护限/降频	0：正常            1：限/降频
     */
    private String mkdlbhxjp;

    /**
     * 模块温度保护限/降频	0：正常            1：限/降频
     */
    private String mkwdbhxjp;

    /**
     * 直流母线电压保护限/降频	0：正常            1：限/降频

     */
    private String zlmxdybhxjp;

    /**
     * 过负荷保护限/降频	0：正常            1：限/降频


     */
    private String ghbhxjp;

    /**
     * 防冻结保护限/降频	0：正常            1：限/降频

     */
    private String fjdbhxjp;

    /**
     * 排气保护限/降频	0：正常            1：限/降频

     */
    private String pqbhxjp;

    /**
     * 外机AC电流保护限/降频	0：正常            1：限/降频

     */
    private String wjacdlbhxjp;

    /**
     * 过载感温包故障	0：正常            1：故障

     */
    private String gzgwbgz;

    /**
     * 排气感温包故障	0：正常            1：故障

     */
    private String pqgwbgz;

    /**
     * 环境感温包故障	0：正常            1：故障

     */
    private String hjgwbgz;

    /**
     * 室外冷凝器中间管感温包故障	0：正常            1：故障

     */
    private String swlnqzjgwbgz;

    /**
     * 模块感温包电路故障	0：正常            1：故障

     */
    private String mkgwbdlgz;

    /**
     * 压缩机热过载保护	0：正常            1：故障

     */
    private String ysjrgzbh;

    /**
     * 排气保护	0：正常            1：故障

     */
    private String pqbh;

    /**
     * 过负荷保护	0：正常            1：故障

     */
    private String gfhbh;

    /**
     * 外机AC电流保护	0：正常            1：故障

     */
    private String wjacdlbh;

    /**
     * 模块电流（Fo）保护	0：正常            1：故障

     */
    private String mkdlfobh;

    /**
     * 模块温度保护	0：正常            1：故障

     */
    private String mkwdbh;

    /**
     * 防冻结保护	0：正常            1：故障

     */
    private String fjdbh;

    /**
     * 功率过高保护	0：正常            1：故障

     */
    private String glggbh;

    /**
     * 压缩机缺/逆相保护欠相，脱调（缺相）	0：正常        1：故障


     */
    private String ysjqnxbhqxttqx;

    /**
     * PFC过流故障	0：正常        1：故障

     */
    private String pfcglgz;

    /**
     * 直流母线电压过高保护	0：正常        1：故障

     */
    private String zlmxdyggbh;

    /**
     * 直流母线电压过低保护	0：正常        1：故障

     */
    private String zlmxdygdbh;

    /**
     * 缺氟保护	0：正常        1：故障

     */
    private String qfbh;

    /**
     * 模式冲突	0：正常        1：故障

     */
    private String msct;

    /**
     * 室内外机型不匹配	0：正常        1：故障

     */
    private String snwjxbpp;

    /**
     * 一拖多内外机管路连接与通讯连接不匹配	0：正常        1：故障

     */
    private String ytdnwjglljytxljbpp;

    /**
     * 记忆芯片读写故障	0：正常        1：故障

     */
    private String jyxpdxgz;

    /**
     * 过零信号异常	0：正常        1：故障

     */
    private String glxhyc;

    /**
     * 四通阀换向异常	0：正常        1：故障

     */
    private String stfhxyc;

    /**
     * 选择口电平异常	0：正常        1：故障

     */
    private String xzkdpyc;

    /**
     * 室外风机2故障	0：正常        1：故障

     */
    private String swfj2gz;

    /**
     * 室外风机1故障	0：正常        1：故障

     */
    private String swfj1gz;

    /**
     * 高温保护停外风机（定频零火线通讯机型使用）	0：正常        1：高温保护停外风机

     */
    private String gwbhswfj;

    /**
     * 系统低压保护	0：正常        1：故障

     */
    private String xtdybh;

    /**
     * 系统高压保护	0：正常        1：故障

     */
    private String xtgybh;

    /**
     * 直流母线电压跌落故障	0：正常        1：故障

     */
    private String zlmxdydlgz;

    /**
     * 整机电流检测故障	0：正常        1：故障

     */
    private String zjdljcgz;

    /**
     * 电容充电故障	0：正常        1：故障

     */
    private String drcdgz;

    /**
     * 压缩机相电流电路检测故障	0：正常        1：故障

     */
    private String ysjxdldljcgz;

    /**
     * 压缩机失步	0：正常        1：故障

     */
    private String ysjsb;

    /**
     * 压缩机退磁保护	0：正常        1：故障

     */
    private String ysjtcbh;

    /**
     * 压缩机堵转	0：正常       1：故障

     */
    private String ysjdz;

    /**
     * 启动失败	0：正常       1：故障

     */
    private String qdsb;

    /**
     * 驱动模块复位	0：正常       1：故障

     */
    private String qdmkfw;

    /**
     * 超速	0：正常       1：故障

     */
    private String sc;

    /**
     * 压缩机拨码异常	0：正常       1：故障

     */
    private String ysjbmyc;

    /**
     * 驱动板环境感温包故障	0：正常       1：故障

     */
    private String qdbhjgwbgz;

    /**
     * 交流接触器保护	0：正常       1：故障

     */
    private String jljcqbh;

    /**
     * 温漂保护	0：正常       1：故障

     */
    private String wpbh;

    /**
     * 传感器连接保护（电流传感器没有接到对应的U相或V相）	0：正常       1：故障

     */
    private String cgqljbh;

    /**
     * 驱动板通讯故障	0：正常       1：故障

     */
    private String qdbtxgz;

    /**
     * 压缩机相电流过流	0：正常       1：故障

     */
    private String ysjxdlgl;

    /**
     * 交流输入电压异常	0：正常       1：故障

     */
    private String jlsrdyyc;

    /**
     * 风机调速板通讯故障	0：正常       1：故障

     */
    private String fjtsbtxgz;

    /**
     * 液阀感温包故障	0：正常       1：故障

     */
    private String yfgwbgz;

    /**
     * 气阀感温包故障	0：正常       1：故障

     */
    private String qfgwbgz;

    /**
     * 室外冷凝器入管感温包故障	0：正常       1：故障

     */
    private String swlnqrggwbgz;

    /**
     * 室外冷凝器出管感温包故障	0：正常       1：故障

     */
    private String swlnqcggwbgz;

    /**
     * 冷媒温度感温包故障	0：正常   1：故障

     */
    private String lmwdgwbgz;

    /**
     * 室外机冷媒加热器失效故障	0：正常   1：故障

     */
    private String swjlmjrqsxgz;

    /**
     * 室外机冷媒加热器继电器粘连故障	0：正常   1：故障

     */
    private String swjlmjrqjdqzlgz;

    /**
     * IPM模块温度	0h：-40℃，数据每增加1则温度增加1℃

     */
    private String ipmmkwd;

    /**
     * 电压状态位	000：正常
     001：启动前过压
     010：启动前欠压
     011：运行过程过压
     100：运行过程欠压

     */
    private String dyztw;

    /**
     * 电流状态位	00：正常
     01：启动前过流
     10：运行过程过流
     11：保留

     */
    private String dlztw;

    /**
     * 液阀感温包温度	0h：-40℃，数据每增加1则温度增加1℃

     */
    private String yfgwbwd;

    /**
     * 气阀感温包温度	0h：-40℃，数据每增加1则温度增加1℃

     */
    private String qfgwbwd;

    /**
     * 室外冷凝器入管温度	0h：-40℃，数据每增加1则温度增加1℃

     */
    private String swlnqrgwd;

    /**
     * 室外冷凝器出管温度	0h：-40℃，数据每增加1则温度增加1℃

     */
    private String swlnqcgwd;

    /**
     * 压缩机排气高温保护	0：正常       1：故障

     */
    private String ysjpqgwbh;

    /**
     * 交流过流保护	0：正常       1：故障

     */
    private String jlglbh;

    /**
     * 防高温保护	0：正常       1：故障

     */
    private String ggwbh;

    /**
     * 读EEPROM故障	0：正常       1：故障

     */
    private String deepromgz;

    /**
     * 外环温度异常	0：正常       1：故障

     */
    private String whwdyc;

    /**
     * 制热防高温降频	0：正常       1：限/降频

     */
    private String zrggwjp;

    /**
     * 系统异常
     */
    private String xtyc;

    public AirOuterStatus() {

    }

    public AirOuterStatus(String mac, Date ctime, String ysjkgzt, String hsms, String ysjyxzs, String wfj1zs, String wfj2zs, String ysjyxgl, String dzpzfkd, String zlmxdy, String swpqwdxsbz, String swlnqwdxsbz, String swlhjwdxsbz, String swhjwd, String swlnqzjwd, String swpqwd, String savezt, String wjfnlzt, String wjjyzt, String dzpzfzt, String sfzdjzt, String hyzt, String ptcszt, String csyq, String tscszt, String wjrytjgz, String hszczt, String wjacdlz, String syx, String sfms, String qzcs, String qzzr, String qzzl, String dredmsyxzt, String glggbhxjp, String mkdlbhxjp, String mkwdbhxjp, String zlmxdybhxjp, String ghbhxjp, String fjdbhxjp, String pqbhxjp, String wjacdlbhxjp, String gzgwbgz, String pqgwbgz, String hjgwbgz, String swlnqzjgwbgz, String mkgwbdlgz, String ysjrgzbh, String pqbh, String gfhbh, String wjacdlbh, String mkdlfobh, String mkwdbh, String fjdbh, String glggbh, String ysjqnxbhqxttqx, String pfcglgz, String zlmxdyggbh, String zlmxdygdbh, String qfbh, String msct, String snwjxbpp, String ytdnwjglljytxljbpp, String jyxpdxgz, String glxhyc, String stfhxyc, String xzkdpyc, String swfj2gz, String swfj1gz, String gwbhswfj, String xtdybh, String xtgybh, String zlmxdydlgz, String zjdljcgz, String drcdgz, String ysjxdldljcgz, String ysjsb, String ysjtcbh, String ysjdz, String qdsb, String qdmkfw, String sc, String ysjbmyc, String qdbhjgwbgz, String jljcqbh, String wpbh, String cgqljbh, String qdbtxgz, String ysjxdlgl, String jlsrdyyc, String fjtsbtxgz, String yfgwbgz, String qfgwbgz, String swlnqrggwbgz, String swlnqcggwbgz, String lmwdgwbgz, String swjlmjrqsxgz, String swjlmjrqjdqzlgz, String ipmmkwd, String dyztw, String dlztw, String yfgwbwd, String qfgwbwd, String swlnqrgwd, String swlnqcgwd, String ysjpqgwbh, String jlglbh, String ggwbh, String deepromgz, String whwdyc, String zrggwjp, String xtyc) {
        this.mac = mac;
        this.ctime = ctime;
        this.ysjkgzt = ysjkgzt;
        this.hsms = hsms;
        this.ysjyxzs = ysjyxzs;
        this.wfj1zs = wfj1zs;
        this.wfj2zs = wfj2zs;
        this.ysjyxgl = ysjyxgl;
        this.dzpzfkd = dzpzfkd;
        this.zlmxdy = zlmxdy;
        this.swpqwdxsbz = swpqwdxsbz;
        this.swlnqwdxsbz = swlnqwdxsbz;
        this.swlhjwdxsbz = swlhjwdxsbz;
        this.swhjwd = swhjwd;
        this.swlnqzjwd = swlnqzjwd;
        this.swpqwd = swpqwd;
        this.savezt = savezt;
        this.wjfnlzt = wjfnlzt;
        this.wjjyzt = wjjyzt;
        this.dzpzfzt = dzpzfzt;
        this.sfzdjzt = sfzdjzt;
        this.hyzt = hyzt;
        this.ptcszt = ptcszt;
        this.csyq = csyq;
        this.tscszt = tscszt;
        this.wjrytjgz = wjrytjgz;
        this.hszczt = hszczt;
        this.wjacdlz = wjacdlz;
        this.syx = syx;
        this.sfms = sfms;
        this.qzcs = qzcs;
        this.qzzr = qzzr;
        this.qzzl = qzzl;
        this.dredmsyxzt = dredmsyxzt;
        this.glggbhxjp = glggbhxjp;
        this.mkdlbhxjp = mkdlbhxjp;
        this.mkwdbhxjp = mkwdbhxjp;
        this.zlmxdybhxjp = zlmxdybhxjp;
        this.ghbhxjp = ghbhxjp;
        this.fjdbhxjp = fjdbhxjp;
        this.pqbhxjp = pqbhxjp;
        this.wjacdlbhxjp = wjacdlbhxjp;
        this.gzgwbgz = gzgwbgz;
        this.pqgwbgz = pqgwbgz;
        this.hjgwbgz = hjgwbgz;
        this.swlnqzjgwbgz = swlnqzjgwbgz;
        this.mkgwbdlgz = mkgwbdlgz;
        this.ysjrgzbh = ysjrgzbh;
        this.pqbh = pqbh;
        this.gfhbh = gfhbh;
        this.wjacdlbh = wjacdlbh;
        this.mkdlfobh = mkdlfobh;
        this.mkwdbh = mkwdbh;
        this.fjdbh = fjdbh;
        this.glggbh = glggbh;
        this.ysjqnxbhqxttqx = ysjqnxbhqxttqx;
        this.pfcglgz = pfcglgz;
        this.zlmxdyggbh = zlmxdyggbh;
        this.zlmxdygdbh = zlmxdygdbh;
        this.qfbh = qfbh;
        this.msct = msct;
        this.snwjxbpp = snwjxbpp;
        this.ytdnwjglljytxljbpp = ytdnwjglljytxljbpp;
        this.jyxpdxgz = jyxpdxgz;
        this.glxhyc = glxhyc;
        this.stfhxyc = stfhxyc;
        this.xzkdpyc = xzkdpyc;
        this.swfj2gz = swfj2gz;
        this.swfj1gz = swfj1gz;
        this.gwbhswfj = gwbhswfj;
        this.xtdybh = xtdybh;
        this.xtgybh = xtgybh;
        this.zlmxdydlgz = zlmxdydlgz;
        this.zjdljcgz = zjdljcgz;
        this.drcdgz = drcdgz;
        this.ysjxdldljcgz = ysjxdldljcgz;
        this.ysjsb = ysjsb;
        this.ysjtcbh = ysjtcbh;
        this.ysjdz = ysjdz;
        this.qdsb = qdsb;
        this.qdmkfw = qdmkfw;
        this.sc = sc;
        this.ysjbmyc = ysjbmyc;
        this.qdbhjgwbgz = qdbhjgwbgz;
        this.jljcqbh = jljcqbh;
        this.wpbh = wpbh;
        this.cgqljbh = cgqljbh;
        this.qdbtxgz = qdbtxgz;
        this.ysjxdlgl = ysjxdlgl;
        this.jlsrdyyc = jlsrdyyc;
        this.fjtsbtxgz = fjtsbtxgz;
        this.yfgwbgz = yfgwbgz;
        this.qfgwbgz = qfgwbgz;
        this.swlnqrggwbgz = swlnqrggwbgz;
        this.swlnqcggwbgz = swlnqcggwbgz;
        this.lmwdgwbgz = lmwdgwbgz;
        this.swjlmjrqsxgz = swjlmjrqsxgz;
        this.swjlmjrqjdqzlgz = swjlmjrqjdqzlgz;
        this.ipmmkwd = ipmmkwd;
        this.dyztw = dyztw;
        this.dlztw = dlztw;
        this.yfgwbwd = yfgwbwd;
        this.qfgwbwd = qfgwbwd;
        this.swlnqrgwd = swlnqrgwd;
        this.swlnqcgwd = swlnqcgwd;
        this.ysjpqgwbh = ysjpqgwbh;
        this.jlglbh = jlglbh;
        this.ggwbh = ggwbh;
        this.deepromgz = deepromgz;
        this.whwdyc = whwdyc;
        this.zrggwjp = zrggwjp;
        this.xtyc = xtyc;
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

    public String getYsjkgzt() {
        return ysjkgzt;
    }

    public void setYsjkgzt(String ysjkgzt) {
        this.ysjkgzt = ysjkgzt;
    }

    public String getHsms() {
        return hsms;
    }

    public void setHsms(String hsms) {
        this.hsms = hsms;
    }

    public String getYsjyxzs() {
        return ysjyxzs;
    }

    public void setYsjyxzs(String ysjyxzs) {
        this.ysjyxzs = ysjyxzs;
    }

    public String getWfj1zs() {
        return wfj1zs;
    }

    public void setWfj1zs(String wfj1zs) {
        this.wfj1zs = wfj1zs;
    }

    public String getWfj2zs() {
        return wfj2zs;
    }

    public void setWfj2zs(String wfj2zs) {
        this.wfj2zs = wfj2zs;
    }

    public String getYsjyxgl() {
        return ysjyxgl;
    }

    public void setYsjyxgl(String ysjyxgl) {
        this.ysjyxgl = ysjyxgl;
    }

    public String getDzpzfkd() {
        return dzpzfkd;
    }

    public void setDzpzfkd(String dzpzfkd) {
        this.dzpzfkd = dzpzfkd;
    }

    public String getZlmxdy() {
        return zlmxdy;
    }

    public void setZlmxdy(String zlmxdy) {
        this.zlmxdy = zlmxdy;
    }

    public String getSwpqwdxsbz() {
        return swpqwdxsbz;
    }

    public void setSwpqwdxsbz(String swpqwdxsbz) {
        this.swpqwdxsbz = swpqwdxsbz;
    }

    public String getSwlnqwdxsbz() {
        return swlnqwdxsbz;
    }

    public void setSwlnqwdxsbz(String swlnqwdxsbz) {
        this.swlnqwdxsbz = swlnqwdxsbz;
    }

    public String getSwlhjwdxsbz() {
        return swlhjwdxsbz;
    }

    public void setSwlhjwdxsbz(String swlhjwdxsbz) {
        this.swlhjwdxsbz = swlhjwdxsbz;
    }

    public String getSwhjwd() {
        return swhjwd;
    }

    public void setSwhjwd(String swhjwd) {
        this.swhjwd = swhjwd;
    }

    public String getSwlnqzjwd() {
        return swlnqzjwd;
    }

    public void setSwlnqzjwd(String swlnqzjwd) {
        this.swlnqzjwd = swlnqzjwd;
    }

    public String getSwpqwd() {
        return swpqwd;
    }

    public void setSwpqwd(String swpqwd) {
        this.swpqwd = swpqwd;
    }

    public String getSavezt() {
        return savezt;
    }

    public void setSavezt(String savezt) {
        this.savezt = savezt;
    }

    public String getWjfnlzt() {
        return wjfnlzt;
    }

    public void setWjfnlzt(String wjfnlzt) {
        this.wjfnlzt = wjfnlzt;
    }

    public String getWjjyzt() {
        return wjjyzt;
    }

    public void setWjjyzt(String wjjyzt) {
        this.wjjyzt = wjjyzt;
    }

    public String getDzpzfzt() {
        return dzpzfzt;
    }

    public void setDzpzfzt(String dzpzfzt) {
        this.dzpzfzt = dzpzfzt;
    }

    public String getSfzdjzt() {
        return sfzdjzt;
    }

    public void setSfzdjzt(String sfzdjzt) {
        this.sfzdjzt = sfzdjzt;
    }

    public String getHyzt() {
        return hyzt;
    }

    public void setHyzt(String hyzt) {
        this.hyzt = hyzt;
    }

    public String getPtcszt() {
        return ptcszt;
    }

    public void setPtcszt(String ptcszt) {
        this.ptcszt = ptcszt;
    }

    public String getCsyq() {
        return csyq;
    }

    public void setCsyq(String csyq) {
        this.csyq = csyq;
    }

    public String getTscszt() {
        return tscszt;
    }

    public void setTscszt(String tscszt) {
        this.tscszt = tscszt;
    }

    public String getWjrytjgz() {
        return wjrytjgz;
    }

    public void setWjrytjgz(String wjrytjgz) {
        this.wjrytjgz = wjrytjgz;
    }

    public String getHszczt() {
        return hszczt;
    }

    public void setHszczt(String hszczt) {
        this.hszczt = hszczt;
    }

    public String getWjacdlz() {
        return wjacdlz;
    }

    public void setWjacdlz(String wjacdlz) {
        this.wjacdlz = wjacdlz;
    }

    public String getSyx() {
        return syx;
    }

    public void setSyx(String syx) {
        this.syx = syx;
    }

    public String getSfms() {
        return sfms;
    }

    public void setSfms(String sfms) {
        this.sfms = sfms;
    }

    public String getQzcs() {
        return qzcs;
    }

    public void setQzcs(String qzcs) {
        this.qzcs = qzcs;
    }

    public String getQzzr() {
        return qzzr;
    }

    public void setQzzr(String qzzr) {
        this.qzzr = qzzr;
    }

    public String getQzzl() {
        return qzzl;
    }

    public void setQzzl(String qzzl) {
        this.qzzl = qzzl;
    }

    public String getDredmsyxzt() {
        return dredmsyxzt;
    }

    public void setDredmsyxzt(String dredmsyxzt) {
        this.dredmsyxzt = dredmsyxzt;
    }

    public String getGlggbhxjp() {
        return glggbhxjp;
    }

    public void setGlggbhxjp(String glggbhxjp) {
        this.glggbhxjp = glggbhxjp;
    }

    public String getMkdlbhxjp() {
        return mkdlbhxjp;
    }

    public void setMkdlbhxjp(String mkdlbhxjp) {
        this.mkdlbhxjp = mkdlbhxjp;
    }

    public String getMkwdbhxjp() {
        return mkwdbhxjp;
    }

    public void setMkwdbhxjp(String mkwdbhxjp) {
        this.mkwdbhxjp = mkwdbhxjp;
    }

    public String getZlmxdybhxjp() {
        return zlmxdybhxjp;
    }

    public void setZlmxdybhxjp(String zlmxdybhxjp) {
        this.zlmxdybhxjp = zlmxdybhxjp;
    }

    public String getGhbhxjp() {
        return ghbhxjp;
    }

    public void setGhbhxjp(String ghbhxjp) {
        this.ghbhxjp = ghbhxjp;
    }

    public String getFjdbhxjp() {
        return fjdbhxjp;
    }

    public void setFjdbhxjp(String fjdbhxjp) {
        this.fjdbhxjp = fjdbhxjp;
    }

    public String getPqbhxjp() {
        return pqbhxjp;
    }

    public void setPqbhxjp(String pqbhxjp) {
        this.pqbhxjp = pqbhxjp;
    }

    public String getWjacdlbhxjp() {
        return wjacdlbhxjp;
    }

    public void setWjacdlbhxjp(String wjacdlbhxjp) {
        this.wjacdlbhxjp = wjacdlbhxjp;
    }

    public String getGzgwbgz() {
        return gzgwbgz;
    }

    public void setGzgwbgz(String gzgwbgz) {
        this.gzgwbgz = gzgwbgz;
    }

    public String getPqgwbgz() {
        return pqgwbgz;
    }

    public void setPqgwbgz(String pqgwbgz) {
        this.pqgwbgz = pqgwbgz;
    }

    public String getHjgwbgz() {
        return hjgwbgz;
    }

    public void setHjgwbgz(String hjgwbgz) {
        this.hjgwbgz = hjgwbgz;
    }

    public String getSwlnqzjgwbgz() {
        return swlnqzjgwbgz;
    }

    public void setSwlnqzjgwbgz(String swlnqzjgwbgz) {
        this.swlnqzjgwbgz = swlnqzjgwbgz;
    }

    public String getMkgwbdlgz() {
        return mkgwbdlgz;
    }

    public void setMkgwbdlgz(String mkgwbdlgz) {
        this.mkgwbdlgz = mkgwbdlgz;
    }

    public String getYsjrgzbh() {
        return ysjrgzbh;
    }

    public void setYsjrgzbh(String ysjrgzbh) {
        this.ysjrgzbh = ysjrgzbh;
    }

    public String getPqbh() {
        return pqbh;
    }

    public void setPqbh(String pqbh) {
        this.pqbh = pqbh;
    }

    public String getGfhbh() {
        return gfhbh;
    }

    public void setGfhbh(String gfhbh) {
        this.gfhbh = gfhbh;
    }

    public String getWjacdlbh() {
        return wjacdlbh;
    }

    public void setWjacdlbh(String wjacdlbh) {
        this.wjacdlbh = wjacdlbh;
    }

    public String getMkdlfobh() {
        return mkdlfobh;
    }

    public void setMkdlfobh(String mkdlfobh) {
        this.mkdlfobh = mkdlfobh;
    }

    public String getMkwdbh() {
        return mkwdbh;
    }

    public void setMkwdbh(String mkwdbh) {
        this.mkwdbh = mkwdbh;
    }

    public String getFjdbh() {
        return fjdbh;
    }

    public void setFjdbh(String fjdbh) {
        this.fjdbh = fjdbh;
    }

    public String getGlggbh() {
        return glggbh;
    }

    public void setGlggbh(String glggbh) {
        this.glggbh = glggbh;
    }

    public String getYsjqnxbhqxttqx() {
        return ysjqnxbhqxttqx;
    }

    public void setYsjqnxbhqxttqx(String ysjqnxbhqxttqx) {
        this.ysjqnxbhqxttqx = ysjqnxbhqxttqx;
    }

    public String getPfcglgz() {
        return pfcglgz;
    }

    public void setPfcglgz(String pfcglgz) {
        this.pfcglgz = pfcglgz;
    }

    public String getZlmxdyggbh() {
        return zlmxdyggbh;
    }

    public void setZlmxdyggbh(String zlmxdyggbh) {
        this.zlmxdyggbh = zlmxdyggbh;
    }

    public String getZlmxdygdbh() {
        return zlmxdygdbh;
    }

    public void setZlmxdygdbh(String zlmxdygdbh) {
        this.zlmxdygdbh = zlmxdygdbh;
    }

    public String getQfbh() {
        return qfbh;
    }

    public void setQfbh(String qfbh) {
        this.qfbh = qfbh;
    }

    public String getMsct() {
        return msct;
    }

    public void setMsct(String msct) {
        this.msct = msct;
    }

    public String getSnwjxbpp() {
        return snwjxbpp;
    }

    public void setSnwjxbpp(String snwjxbpp) {
        this.snwjxbpp = snwjxbpp;
    }

    public String getYtdnwjglljytxljbpp() {
        return ytdnwjglljytxljbpp;
    }

    public void setYtdnwjglljytxljbpp(String ytdnwjglljytxljbpp) {
        this.ytdnwjglljytxljbpp = ytdnwjglljytxljbpp;
    }

    public String getJyxpdxgz() {
        return jyxpdxgz;
    }

    public void setJyxpdxgz(String jyxpdxgz) {
        this.jyxpdxgz = jyxpdxgz;
    }

    public String getGlxhyc() {
        return glxhyc;
    }

    public void setGlxhyc(String glxhyc) {
        this.glxhyc = glxhyc;
    }

    public String getStfhxyc() {
        return stfhxyc;
    }

    public void setStfhxyc(String stfhxyc) {
        this.stfhxyc = stfhxyc;
    }

    public String getXzkdpyc() {
        return xzkdpyc;
    }

    public void setXzkdpyc(String xzkdpyc) {
        this.xzkdpyc = xzkdpyc;
    }

    public String getSwfj2gz() {
        return swfj2gz;
    }

    public void setSwfj2gz(String swfj2gz) {
        this.swfj2gz = swfj2gz;
    }

    public String getSwfj1gz() {
        return swfj1gz;
    }

    public void setSwfj1gz(String swfj1gz) {
        this.swfj1gz = swfj1gz;
    }

    public String getGwbhswfj() {
        return gwbhswfj;
    }

    public void setGwbhswfj(String gwbhswfj) {
        this.gwbhswfj = gwbhswfj;
    }

    public String getXtdybh() {
        return xtdybh;
    }

    public void setXtdybh(String xtdybh) {
        this.xtdybh = xtdybh;
    }

    public String getXtgybh() {
        return xtgybh;
    }

    public void setXtgybh(String xtgybh) {
        this.xtgybh = xtgybh;
    }

    public String getZlmxdydlgz() {
        return zlmxdydlgz;
    }

    public void setZlmxdydlgz(String zlmxdydlgz) {
        this.zlmxdydlgz = zlmxdydlgz;
    }

    public String getZjdljcgz() {
        return zjdljcgz;
    }

    public void setZjdljcgz(String zjdljcgz) {
        this.zjdljcgz = zjdljcgz;
    }

    public String getDrcdgz() {
        return drcdgz;
    }

    public void setDrcdgz(String drcdgz) {
        this.drcdgz = drcdgz;
    }

    public String getYsjxdldljcgz() {
        return ysjxdldljcgz;
    }

    public void setYsjxdldljcgz(String ysjxdldljcgz) {
        this.ysjxdldljcgz = ysjxdldljcgz;
    }

    public String getYsjsb() {
        return ysjsb;
    }

    public void setYsjsb(String ysjsb) {
        this.ysjsb = ysjsb;
    }

    public String getYsjtcbh() {
        return ysjtcbh;
    }

    public void setYsjtcbh(String ysjtcbh) {
        this.ysjtcbh = ysjtcbh;
    }

    public String getYsjdz() {
        return ysjdz;
    }

    public void setYsjdz(String ysjdz) {
        this.ysjdz = ysjdz;
    }

    public String getQdsb() {
        return qdsb;
    }

    public void setQdsb(String qdsb) {
        this.qdsb = qdsb;
    }

    public String getQdmkfw() {
        return qdmkfw;
    }

    public void setQdmkfw(String qdmkfw) {
        this.qdmkfw = qdmkfw;
    }

    public String getSc() {
        return sc;
    }

    public void setSc(String sc) {
        this.sc = sc;
    }

    public String getYsjbmyc() {
        return ysjbmyc;
    }

    public void setYsjbmyc(String ysjbmyc) {
        this.ysjbmyc = ysjbmyc;
    }

    public String getQdbhjgwbgz() {
        return qdbhjgwbgz;
    }

    public void setQdbhjgwbgz(String qdbhjgwbgz) {
        this.qdbhjgwbgz = qdbhjgwbgz;
    }

    public String getJljcqbh() {
        return jljcqbh;
    }

    public void setJljcqbh(String jljcqbh) {
        this.jljcqbh = jljcqbh;
    }

    public String getWpbh() {
        return wpbh;
    }

    public void setWpbh(String wpbh) {
        this.wpbh = wpbh;
    }

    public String getCgqljbh() {
        return cgqljbh;
    }

    public void setCgqljbh(String cgqljbh) {
        this.cgqljbh = cgqljbh;
    }

    public String getQdbtxgz() {
        return qdbtxgz;
    }

    public void setQdbtxgz(String qdbtxgz) {
        this.qdbtxgz = qdbtxgz;
    }

    public String getYsjxdlgl() {
        return ysjxdlgl;
    }

    public void setYsjxdlgl(String ysjxdlgl) {
        this.ysjxdlgl = ysjxdlgl;
    }

    public String getJlsrdyyc() {
        return jlsrdyyc;
    }

    public void setJlsrdyyc(String jlsrdyyc) {
        this.jlsrdyyc = jlsrdyyc;
    }

    public String getFjtsbtxgz() {
        return fjtsbtxgz;
    }

    public void setFjtsbtxgz(String fjtsbtxgz) {
        this.fjtsbtxgz = fjtsbtxgz;
    }

    public String getYfgwbgz() {
        return yfgwbgz;
    }

    public void setYfgwbgz(String yfgwbgz) {
        this.yfgwbgz = yfgwbgz;
    }

    public String getQfgwbgz() {
        return qfgwbgz;
    }

    public void setQfgwbgz(String qfgwbgz) {
        this.qfgwbgz = qfgwbgz;
    }

    public String getSwlnqrggwbgz() {
        return swlnqrggwbgz;
    }

    public void setSwlnqrggwbgz(String swlnqrggwbgz) {
        this.swlnqrggwbgz = swlnqrggwbgz;
    }

    public String getSwlnqcggwbgz() {
        return swlnqcggwbgz;
    }

    public void setSwlnqcggwbgz(String swlnqcggwbgz) {
        this.swlnqcggwbgz = swlnqcggwbgz;
    }

    public String getLmwdgwbgz() {
        return lmwdgwbgz;
    }

    public void setLmwdgwbgz(String lmwdgwbgz) {
        this.lmwdgwbgz = lmwdgwbgz;
    }

    public String getSwjlmjrqsxgz() {
        return swjlmjrqsxgz;
    }

    public void setSwjlmjrqsxgz(String swjlmjrqsxgz) {
        this.swjlmjrqsxgz = swjlmjrqsxgz;
    }

    public String getSwjlmjrqjdqzlgz() {
        return swjlmjrqjdqzlgz;
    }

    public void setSwjlmjrqjdqzlgz(String swjlmjrqjdqzlgz) {
        this.swjlmjrqjdqzlgz = swjlmjrqjdqzlgz;
    }

    public String getIpmmkwd() {
        return ipmmkwd;
    }

    public void setIpmmkwd(String ipmmkwd) {
        this.ipmmkwd = ipmmkwd;
    }

    public String getDyztw() {
        return dyztw;
    }

    public void setDyztw(String dyztw) {
        this.dyztw = dyztw;
    }

    public String getDlztw() {
        return dlztw;
    }

    public void setDlztw(String dlztw) {
        this.dlztw = dlztw;
    }

    public String getYfgwbwd() {
        return yfgwbwd;
    }

    public void setYfgwbwd(String yfgwbwd) {
        this.yfgwbwd = yfgwbwd;
    }

    public String getQfgwbwd() {
        return qfgwbwd;
    }

    public void setQfgwbwd(String qfgwbwd) {
        this.qfgwbwd = qfgwbwd;
    }

    public String getSwlnqrgwd() {
        return swlnqrgwd;
    }

    public void setSwlnqrgwd(String swlnqrgwd) {
        this.swlnqrgwd = swlnqrgwd;
    }

    public String getSwlnqcgwd() {
        return swlnqcgwd;
    }

    public void setSwlnqcgwd(String swlnqcgwd) {
        this.swlnqcgwd = swlnqcgwd;
    }

    public String getYsjpqgwbh() {
        return ysjpqgwbh;
    }

    public void setYsjpqgwbh(String ysjpqgwbh) {
        this.ysjpqgwbh = ysjpqgwbh;
    }

    public String getJlglbh() {
        return jlglbh;
    }

    public void setJlglbh(String jlglbh) {
        this.jlglbh = jlglbh;
    }

    public String getGgwbh() {
        return ggwbh;
    }

    public void setGgwbh(String ggwbh) {
        this.ggwbh = ggwbh;
    }

    public String getDeepromgz() {
        return deepromgz;
    }

    public void setDeepromgz(String deepromgz) {
        this.deepromgz = deepromgz;
    }

    public String getWhwdyc() {
        return whwdyc;
    }

    public void setWhwdyc(String whwdyc) {
        this.whwdyc = whwdyc;
    }

    public String getZrggwjp() {
        return zrggwjp;
    }

    public void setZrggwjp(String zrggwjp) {
        this.zrggwjp = zrggwjp;
    }

    public String getXtyc() {
        return xtyc;
    }

    public void setXtyc(String xtyc) {
        this.xtyc = xtyc;
    }

    @Override
    public String toString() {
        return "AirRealOuterStatus{" +
                "id=" + id +
                ", mac='" + mac + '\'' +
                ", ctime=" + ctime +
                ", ysjkgzt=" + ysjkgzt +
                ", hsms='" + hsms + '\'' +
                ", ysjyxzs='" + ysjyxzs + '\'' +
                ", wfj1zs='" + wfj1zs + '\'' +
                ", wfj2zs='" + wfj2zs + '\'' +
                ", ysjyxgl='" + ysjyxgl + '\'' +
                ", dzpzfkd='" + dzpzfkd + '\'' +
                ", zlmxdy='" + zlmxdy + '\'' +
                ", swpqwdxsbz='" + swpqwdxsbz + '\'' +
                ", swlnqwdxsbz='" + swlnqwdxsbz + '\'' +
                ", swlhjwdxsbz='" + swlhjwdxsbz + '\'' +
                ", swhjwd='" + swhjwd + '\'' +
                ", swlnqzjwd='" + swlnqzjwd + '\'' +
                ", swpqwd='" + swpqwd + '\'' +
                ", savezt=" + savezt +
                ", wjfnlzt=" + wjfnlzt +
                ", wjjyzt=" + wjjyzt +
                ", dzpzfzt=" + dzpzfzt +
                ", sfzdjzt=" + sfzdjzt +
                ", hyzt=" + hyzt +
                ", ptcszt=" + ptcszt +
                ", csyq=" + csyq +
                ", tscszt=" + tscszt +
                ", wjrytjgz=" + wjrytjgz +
                ", hszczt='" + hszczt + '\'' +
                ", wjacdlz='" + wjacdlz + '\'' +
                ", syx=" + syx +
                ", sfms=" + sfms +
                ", qzcs=" + qzcs +
                ", qzzr=" + qzzr +
                ", qzzl=" + qzzl +
                ", dredmsyxzt='" + dredmsyxzt + '\'' +
                ", glggbhxjp=" + glggbhxjp +
                ", mkdlbhxjp=" + mkdlbhxjp +
                ", mkwdbhxjp=" + mkwdbhxjp +
                ", zlmxdybhxjp=" + zlmxdybhxjp +
                ", ghbhxjp=" + ghbhxjp +
                ", fjdbhxjp=" + fjdbhxjp +
                ", pqbhxjp=" + pqbhxjp +
                ", wjacdlbhxjp=" + wjacdlbhxjp +
                ", gzgwbgz=" + gzgwbgz +
                ", pqgwbgz=" + pqgwbgz +
                ", hjgwbgz=" + hjgwbgz +
                ", swlnqzjgwbgz=" + swlnqzjgwbgz +
                ", mkgwbdlgz=" + mkgwbdlgz +
                ", ysjrgzbh=" + ysjrgzbh +
                ", pqbh=" + pqbh +
                ", gfhbh=" + gfhbh +
                ", wjacdlbh=" + wjacdlbh +
                ", mkdlfobh=" + mkdlfobh +
                ", mkwdbh=" + mkwdbh +
                ", fjdbh=" + fjdbh +
                ", glggbh=" + glggbh +
                ", ysjqnxbhqxttqx=" + ysjqnxbhqxttqx +
                ", pfcglgz=" + pfcglgz +
                ", zlmxdyggbh=" + zlmxdyggbh +
                ", zlmxdygdbh=" + zlmxdygdbh +
                ", qfbh=" + qfbh +
                ", msct=" + msct +
                ", snwjxbpp=" + snwjxbpp +
                ", ytdnwjglljytxljbpp=" + ytdnwjglljytxljbpp +
                ", jyxpdxgz=" + jyxpdxgz +
                ", glxhyc=" + glxhyc +
                ", stfhxyc=" + stfhxyc +
                ", xzkdpyc=" + xzkdpyc +
                ", swfj2gz=" + swfj2gz +
                ", swfj1gz=" + swfj1gz +
                ", gwbhswfj=" + gwbhswfj +
                ", xtdybh=" + xtdybh +
                ", xtgybh=" + xtgybh +
                ", zlmxdydlgz=" + zlmxdydlgz +
                ", zjdljcgz=" + zjdljcgz +
                ", drcdgz=" + drcdgz +
                ", ysjxdldljcgz=" + ysjxdldljcgz +
                ", ysjsb=" + ysjsb +
                ", ysjtcbh=" + ysjtcbh +
                ", ysjdz=" + ysjdz +
                ", qdsb=" + qdsb +
                ", qdmkfw=" + qdmkfw +
                ", sc=" + sc +
                ", ysjbmyc=" + ysjbmyc +
                ", qdbhjgwbgz=" + qdbhjgwbgz +
                ", jljcqbh=" + jljcqbh +
                ", wpbh=" + wpbh +
                ", cgqljbh=" + cgqljbh +
                ", qdbtxgz=" + qdbtxgz +
                ", ysjxdlgl=" + ysjxdlgl +
                ", jlsrdyyc=" + jlsrdyyc +
                ", fjtsbtxgz=" + fjtsbtxgz +
                ", yfgwbgz=" + yfgwbgz +
                ", qfgwbgz=" + qfgwbgz +
                ", swlnqrggwbgz=" + swlnqrggwbgz +
                ", swlnqcggwbgz=" + swlnqcggwbgz +
                ", lmwdgwbgz=" + lmwdgwbgz +
                ", swjlmjrqsxgz=" + swjlmjrqsxgz +
                ", swjlmjrqjdqzlgz=" + swjlmjrqjdqzlgz +
                ", ipmmkwd='" + ipmmkwd + '\'' +
                ", dyztw='" + dyztw + '\'' +
                ", dlztw='" + dlztw + '\'' +
                ", yfgwbwd='" + yfgwbwd + '\'' +
                ", qfgwbwd='" + qfgwbwd + '\'' +
                ", swlnqrgwd='" + swlnqrgwd + '\'' +
                ", swlnqcgwd='" + swlnqcgwd + '\'' +
                ", ysjpqgwbh=" + ysjpqgwbh +
                ", jlglbh=" + jlglbh +
                ", ggwbh=" + ggwbh +
                ", deepromgz=" + deepromgz +
                ", whwdyc=" + whwdyc +
                ", zrggwjp=" + zrggwjp +
                ", xtyc=" + xtyc +
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
