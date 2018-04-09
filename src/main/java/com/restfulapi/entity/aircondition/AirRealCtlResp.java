package com.restfulapi.entity.aircondition;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "aircondition_historyctlresponsetable")
public class AirRealCtlResp implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    /**
     * mac地址转long, 长度24位.
     */

    private String mac;

    /**
     * 创建时间服务器UTC
     */

    private Date ctime;

    /**
     *  CO报警及类似功能：1 --- 打开，0 --- 关闭（上电默认
     */
    private String cobj;

    /**
     *  1 --- 超强开 0 --- 超强关
     */
    private String cq;

    /**
     *  0000 ---- 普通除湿，0001 ---- 连续除湿，0010 ---- 智能除湿，0100 ---- 静音除湿，0101 ---- 干衣模式，0110 ---- 地下室模式，0111 ---- 起居模式，1000 ---- 卧室模式
     */
    private String csms;

    /**
     *  多档风速：000 - 风速自动 001 - 111：风速1 - 7
     */
    private String ddfs;

    /**
     *  1 --- 灯光开 0 --- 灯光关
     */
    private String dg;

    /**
     *  0 - 无效状态，1 - 电能设置有效
     */
    private String dnsz;

    /**
     * 0 –加湿器关， 1 –加湿器开
     */
    private String jsq;

    /**
     * 0 –无遥控信号或有遥控信号无改变过加湿状态， 1 –有遥控信号改变过加湿状态
     */
    private String jsqtx;

    /**
     * 00 –换气关， 01 –换气1档， 10 –换气2档， 11 –预留
     */
    private String hqzz;

    /**
     * 0 –换气断电， 1 –换气上电
     */
    private String hqdy;

    /**
     * 0 –健康关，1-健康开
     */
    private String jk;

    /**
     * 0-不启动对码，1-启动对码
     */
    private String dm;

    /**
     * 0 –无遥控信号或有遥控信号无改变过健康状态， 1 –有遥控信号改变过健康状态
     */
    private String qxjtx;

    /**
     * 0000 0000 --------------------代表强度为0   0000 0001 --------------------代表强度为1   ...    1111 1110 --------------------代表强度为254 1111  1111 -------------------- 代表强度为255
     */
    private String jcbfsqd;

    /**
     *  0 - WiFi功能关闭， 1–WiFi功能开
     */
    private String wifikg;

    /**
     *  0 –无效， 1 –恢复WiFi模块出厂设置参数
     */
    private String wifihfcc;

    /**
     *  001 ---- 档位1 (最优) ，010 ---- 档位2，011 ---- 档位3，100 ---- 档位4，101 ---- 档位5
     */
    private String kqzlpjdw;

    /**
     *  001 ---- 档位1 (最优) ，010 ---- 档位2，011 ---- 档位3，100 ---- 档位4，101 ---- 档位5
     */
    private String pm25dw;

    /**
     *  1 --- 开机，0 --- 关机（上电默认）
     */
    private String kgj;

    /**
     *  000 -- 自动（上电默认），001 -- 制冷，010 -- 抽湿，011 -- 送风，100 -- 制热，101 - 节能
     */
    private String ms;

    /**
     *  1 --- 开睡眠；0 --- 无睡眠（上电默认）
     */
    private String sleep;

    /**
     *  1 --- 开扫风，0 --- 关扫风（上电默认）
     */
    private String sf;

    /**
     *  00 --- 自动风（上电默认），01 --- 低风，10 --- 中风，11 --- 高风
     */
    private String fs;

    /**
     *  (	见华氏与摄氏对照表见表1)
     */
    private String wd;

    /**
     *  1 --- 有定时；0 --- 无定时
     */
    private String ds;

    /**
     *  6.2：时间定时：1 --- 定时十位数为2；0 --- 定时十位数不是2 6.1：时间定时：1 --- 定时十位数为1；0 --- 定时十位数不是1  6.0：时间定时：1 --- 定时小数位为5；0 --- 定时小数位为0 7.7 --- Bit 6 -- -- Bit 5 --- Bit 4：定时：0000—1001：0h—9h（定时个位数）
     */
    private String dssj;

    /**
     *  当5.6—5.4为001制冷或010除湿时 1 --- 干燥开 0 --- 干燥关
     */
    private String gz;

    /**
     *  当5.6—5.4为100制热时 1 --- 辅热关 0 --- 辅热开
     */
    private String fr;

    /**
     *  1 --- 表示华氏 0 --- 表示摄氏
     */
    private String wddw;

    /**
     *  00 --- 换气关， 01 --- 吸气 ， 10 --- 排气
     */
    private String hq;

    /**
     *  0 - 无效，1–历史累计用电量复位清零
     */
    private String lsljydlfwql;

    /**
     *  1 --- 自动清洁开；0 --- 自动清洁关（上电默认）
     */
    private String zdqj;

    /**
     *  0000 --- 关（上电默认），0001 --- 15扫风，0010 --- 1位置，0011 --- 2位置，  0100 --- 3位置，0101 --- 4位置，0110 --- 5位置，0111 --- 35扫风，1000 --- 25扫风，1001 --- 24扫风，1010 --- 14扫风，1011 --- 13扫风。
     */
    private String sxsf;

    /**
     *  0000 --- 关（上电默认），0001 --- 同向扫风，0010 --- 1位置，0011 --- 2位置，0100 --- 3位置，0101 --- 4位置，0110 --- 5位置，1100 --- 15位置，1101 --- 相向扫风
     */
    private String zysf;

    /**
     *  00 --- FM关（上电默认），01 --- FM1开，10 --- FM2开，11 --- FM3开
     */
    private String fm;

    /**
     *  00 --- 控制器按客户要求显示。如果无客户要求，则显示设定温度（上电默认），01 --- 设定温度显示，10 --- 室内环境温度显示，11 --- 室外环境温度显示
     */
    private String wdxsms;

    /**
     *  1 --- 清除，0 --- 不清除
     */
    private String qcts;

    /**
     *  00 --- 无提示，如 : “易快洁”接收到该信息后永不提示清洗。01 --- 干净，如 : “易快洁”接收到该信息后则确定它在累计运行1500h后提示清洗。10 --- 一般，如 : “易快洁”接收到  该信息后则确定它在累计运行1000h后提示清（上电默认）。11 --- 脏，如 : “易快洁”接收到该信息后则确定它在累计运行500h后提示清洗。
     */
    private String ykj;

    /**
     *  湿度显示：1 --- 显示环境湿度，0 --- 不显示环境湿度。
     */
    private String sdxs;

    /**
     *  000 --- 关加湿器（上电默认），001 --- 连续，010 --- 智能，011 --- 湿度设定40％，100 --- 湿度设定50％，101 --- 湿度设定60％，110 --- 湿度设定70％，111 --- 预留
     */
    private String sdsd;

    /**
     *  省电及类似功能 (Energy Save) ：1 --- 打开，0 --- 关闭
     */
    private String saver;

    /**
     *  GEA（人体感应及类似功能）：1 --- 打开，0 --- 关闭（上电默认）
     */
    private String gea;

    /**
     *  自动清洗功能：0 -- -- 关 非新国标辅热（手动辅热开），1 --- 开 新国标辅热（自动辅热开）
     */
    private String zdqx;

    /**
     *  和风功能控制位：1 --- 打开和风，0 --- 关闭和风功能
     */
    private String hf;

    /**
     *  当5.1—5.0＝01时为和风1，＝10时为和风2，＝11时为和风3，
     */
    private String hfms;

    /**
     *  V6.8化霜H1功能：1 --- 打开，0 --- 关闭
     */
    private String hsh1;

    private String glwqxtx;

    /**
     *  0 --- 水泵关，1 --- 水泵开
     */
    private String sb;

    /**
     *  0 --- 没有水满，1 --- 水满
     */
    private String sm;

    /**
     *  0 --- 房车空调LED关，1 --- 房车空调LED开
     */
    private String fcktled;

    /**
     *  定时的循环次数：0 -- 一次有效（即时间定时），1 -- 每天（24h）循环（即时刻定时）
     */
    private String dsxhcs;

    /**
     *  00 --- 无强制功能（上电默认），01 --- 强制制冷。10 --- 强制制热，11 --- 强制化霜
     */
    private String qz;

    /**
     *  1 --- 有定时开，0 --- 无定时开，上电默认无定时开
     */
    private String dsk;

    /**
     *  1 --- 有定时关，0 --- 无定时关，上电默认无定时关
     */
    private String dsg;

    /**
     *  图像识别功能：00 --- 图像识别功能关（上电默认）；01 --- 图像识别的单人模式；10 --- 图像识别的多人模式
     */
    private String txsb;

    /**
     *  睡眠：与2.3关联使用 5.3 --- 17.4：00 ---- 睡眠关；10 --- 普通睡眠（睡眠1）；01 --- 新睡眠（睡眠2）；11 --- 自定义睡眠（睡眠3）
     */
    private String sleepmode;

    /**
     *  静音功能：00 --- 静音功能关（上电默认）；01 --- 自动静音；10 --- 静音
     */
    private String mute;

    /**
     *  区域选择：00 --- 区域选择关（上电默认）；01 --- 选择左半区域；10 --- 选择右半区域
     */
    private String qyxz;

    /**
     *  客户自定义睡眠曲线对应的nh后的温度值（睡眠3功能下客户自定义睡眠曲线设定专用），对照睡眠曲线对应温度值
     */
    private String sm1hhwd;

    /**
     *  客户自定义睡眠曲线对应的nh后的温度值（睡眠3功能下客户自定义睡眠曲线设定专用），对照睡眠曲线对应温度值
     */
    private String sm2hhwd;

    /**
     *  环境功能：000 --- 普通模式（上电默认）；001 --- 节能模式；010 --- 客厅模式； 011 --- 会议 / 办公模式；100 --- 餐厅模式
     */
    private String hj;

    /**
     *  是否发送F5码，0 - 不发送F5码，1 - 发送F5码
     */
    private String f5;

    /**
     *  000：无E享模式；001：E享模式—冷；010：E享模式—凉；011：E享模式—舒适；100：E享模式—暖；101：E享模式—热；110～111：未定义。
     */
    private String ex;

    /**
     *  客户自定义睡眠曲线对应的nh后的温度值（睡眠3功能下客户自定义睡眠曲线设定专用），对照睡眠曲线对应温度值
     */
    private String sm3hhwd;

    /**
     *  客户自定义睡眠曲线对应的nh后的温度值（睡眠3功能下客户自定义睡眠曲线设定专用），对照睡眠曲线对应温度值
     */
    private String sm4hhwd;

    /**
     *  客户自定义睡眠曲线对应的nh后的温度值（睡眠3功能下客户自定义睡眠曲线设定专用），对照睡眠曲线对应温度值
     */
    private String sm5hhwd;

    /**
     *  客户自定义睡眠曲线对应的nh后的温度值（睡眠3功能下客户自定义睡眠曲线设定专用），对照睡眠曲线对应温度值
     */
    private String sm6hhwd;

    /**
     *  客户自定义睡眠曲线对应的nh后的温度值（睡眠3功能下客户自定义睡眠曲线设定专用），对照睡眠曲线对应温度值
     */
    private String sm7hhwd;

    /**
     *  客户自定义睡眠曲线对应的nh后的温度值（睡眠3功能下客户自定义睡眠曲线设定专用），对照睡眠曲线对应温度值
     */
    private String sm8hhwd;

    /**
     * 2017-02-23 03:35:40MENT  生态风功能 0 -- 生态风关；1 -- 生态风开 注：生态风也有遥控器叫海洋风
     */
    private String stf;

    /**
     *  上下扫风风口选择00—空白（主板按默认执行）；01—上开下闭；10—上开下开；11—上闭下开；
     */
    private String sxsffk;

    /**
     *  0 –唤醒关， 1 –唤醒开
     */
    private String hx;

    /**
     *  换气智能设置， 0 –智能换气关， 1 --- 智能换气开
     */
    private String znhq;

    /**
     *  0000 --- 1档（性能），0001 --- 2档（性能），0010 --- 3档（性能），0011 --- 4档（性能），0100 --- 5档（节能），0101 --- 6档（节能），0110 ---  7档（节能），0111 --- 8档（节能）
     */
    private String yhsdyddw;

    /**
     *  0 –“禁止吹人”无效， 1 –“禁止吹人”使能
     */
    private String jzcr;

    /**
     *  Bit7 环境区域8： 0 –无效， 1 –有效；
     */
    private String hjqy9;

    /**
     *  Bit7 环境区域8： 0 –无效， 1 –有效；
     */
    private String hjqy8;

    /**
     *  Bit6 环境区域7： 0 –无效， 1 –有效；
     */
    private String hjqy7;

    /**
     *  Bit5 环境区域6： 0 –无效， 1 –有效；
     */
    private String hjqy6;

    /**
     *  Bit4 环境区域5： 0 –无效， 1 –有效；
     */
    private String hjqy5;

    /**
     *  Bit3 环境区域4： 0 –无效， 1 –有效；
     */
    private String hjqy4;

    /**
     *  Bit2 环境区域3： 0 –无效， 1 –有效；
     */
    private String hjqy3;

    /**
     *  Bit1 环境区域2： 0 –无效， 1 –有效；
     */
    private String hjqy2;

    /**
     *  Bit0 环境区域1： 0 –无效， 1 –有效；
     */
    private String hjqy1;

    /**
     *  0 --- 无红外遥控； 1 -- -- 有红外遥控；
     */
    private String hwyk;

    /**
     *  0 - 无故障，1 - 有故障
     */
    private String fault;

    /**
     *  00 ---- 正常制冷；01 ---- Power制冷；10 ---- Wide制冷；11 ---- Long制冷
     */
    private String zkfs;

    /**
     *  回家模式控制开关位：1：开，0：关
     */
    private String hjms;

    /**
     *  0000 --- 关（上电默认），0001 --- 15扫风，0010 --- 1位置，0011 --- 2位置，0100 --- 3位置，0101 --- 4位置，0110 --- 5位置，0111 --- 35扫风，1000 --- 25扫风，1001 --- 24扫风  ，1010 --- 14扫风，1011 --- 13扫风。
     */
    private String xcfksxsf;

    /**
     *  0000 --- 关（上电默认），0001 --- 同向扫风，0010 --- 1位置，0011 --- 2位置，0100 --- 3位置，0101 --- 4位置，0110 --- 5位置，1100 --- 15位置，1101 --- 相向扫风
     */
    private String xcfkzczysf;

    /**
     *  0000 --- 关（上电默认），0001 --- 同向扫风，0010 --- 1位置，0011 --- 2位置，0100 --- 3位置，0101 --- 4位置，0110 --- 5位置，1100 --- 15位置，1101 --- 相向扫风
     */
    private String xcfkyczysf;

    /**
     *  0h： - 40℃，数据每增加1则温度增加1℃
     */
    private String snhjwd;

    /**
     *  环境湿度值 0h : 0 % ，01h：1 % ，数据每增加1则湿度增加1 %
     */
    private String hjsdz;

    /**
     *  0h： - 40℃，数据每增加1则温度增加1℃
     */
    private String swhjwd;

    public AirRealCtlResp() {

    }

    public AirRealCtlResp(String mac, Date ctime, String cobj, String cq, String csms, String ddfs, String dg, String dnsz, String jsq, String jsqtx, String hqzz, String hqdy, String jk, String dm, String qxjtx, String jcbfsqd, String wifikg, String wifihfcc, String kqzlpjdw, String pm25dw, String kgj, String ms, String sleep, String sf, String fs, String wd, String ds, String dssj, String gz, String fr, String wddw, String hq, String lsljydlfwql, String zdqj, String sxsf, String zysf, String fm, String wdxsms, String qcts, String ykj, String sdxs, String sdsd, String saver, String gea, String zdqx, String hf, String hfms, String hsh1, String glwqxtx, String sb, String sm, String fcktled, String dsxhcs, String qz, String dsk, String dsg, String txsb, String sleepmode, String mute, String qyxz, String sm1hhwd, String sm2hhwd, String hj, String f5, String ex, String sm3hhwd, String sm4hhwd, String sm5hhwd, String sm6hhwd, String sm7hhwd, String sm8hhwd, String stf, String sxsffk, String hx, String znhq, String yhsdyddw, String jzcr, String hjqy9, String hjqy8, String hjqy7, String hjqy6, String hjqy5, String hjqy4, String hjqy3, String hjqy2, String hjqy1, String hwyk, String fault, String zkfs, String hjms, String xcfksxsf, String xcfkzczysf, String xcfkyczysf, String snhjwd, String hjsdz, String swhjwd) {
        this.mac = mac;
        this.ctime = ctime;
        this.cobj = cobj;
        this.cq = cq;
        this.csms = csms;
        this.ddfs = ddfs;
        this.dg = dg;
        this.dnsz = dnsz;
        this.jsq = jsq;
        this.jsqtx = jsqtx;
        this.hqzz = hqzz;
        this.hqdy = hqdy;
        this.jk = jk;
        this.dm = dm;
        this.qxjtx = qxjtx;
        this.jcbfsqd = jcbfsqd;
        this.wifikg = wifikg;
        this.wifihfcc = wifihfcc;
        this.kqzlpjdw = kqzlpjdw;
        this.pm25dw = pm25dw;
        this.kgj = kgj;
        this.ms = ms;
        this.sleep = sleep;
        this.sf = sf;
        this.fs = fs;
        this.wd = wd;
        this.ds = ds;
        this.dssj = dssj;
        this.gz = gz;
        this.fr = fr;
        this.wddw = wddw;
        this.hq = hq;
        this.lsljydlfwql = lsljydlfwql;
        this.zdqj = zdqj;
        this.sxsf = sxsf;
        this.zysf = zysf;
        this.fm = fm;
        this.wdxsms = wdxsms;
        this.qcts = qcts;
        this.ykj = ykj;
        this.sdxs = sdxs;
        this.sdsd = sdsd;
        this.saver = saver;
        this.gea = gea;
        this.zdqx = zdqx;
        this.hf = hf;
        this.hfms = hfms;
        this.hsh1 = hsh1;
        this.glwqxtx = glwqxtx;
        this.sb = sb;
        this.sm = sm;
        this.fcktled = fcktled;
        this.dsxhcs = dsxhcs;
        this.qz = qz;
        this.dsk = dsk;
        this.dsg = dsg;
        this.txsb = txsb;
        this.sleepmode = sleepmode;
        this.mute = mute;
        this.qyxz = qyxz;
        this.sm1hhwd = sm1hhwd;
        this.sm2hhwd = sm2hhwd;
        this.hj = hj;
        this.f5 = f5;
        this.ex = ex;
        this.sm3hhwd = sm3hhwd;
        this.sm4hhwd = sm4hhwd;
        this.sm5hhwd = sm5hhwd;
        this.sm6hhwd = sm6hhwd;
        this.sm7hhwd = sm7hhwd;
        this.sm8hhwd = sm8hhwd;
        this.stf = stf;
        this.sxsffk = sxsffk;
        this.hx = hx;
        this.znhq = znhq;
        this.yhsdyddw = yhsdyddw;
        this.jzcr = jzcr;
        this.hjqy9 = hjqy9;
        this.hjqy8 = hjqy8;
        this.hjqy7 = hjqy7;
        this.hjqy6 = hjqy6;
        this.hjqy5 = hjqy5;
        this.hjqy4 = hjqy4;
        this.hjqy3 = hjqy3;
        this.hjqy2 = hjqy2;
        this.hjqy1 = hjqy1;
        this.hwyk = hwyk;
        this.fault = fault;
        this.zkfs = zkfs;
        this.hjms = hjms;
        this.xcfksxsf = xcfksxsf;
        this.xcfkzczysf = xcfkzczysf;
        this.xcfkyczysf = xcfkyczysf;
        this.snhjwd = snhjwd;
        this.hjsdz = hjsdz;
        this.swhjwd = swhjwd;
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

    public String getCobj() {
        return cobj;
    }

    public void setCobj(String cobj) {
        this.cobj = cobj;
    }

    public String getCq() {
        return cq;
    }

    public void setCq(String cq) {
        this.cq = cq;
    }

    public String getCsms() {
        return csms;
    }

    public void setCsms(String csms) {
        this.csms = csms;
    }

    public String getDdfs() {
        return ddfs;
    }

    public void setDdfs(String ddfs) {
        this.ddfs = ddfs;
    }

    public String getDg() {
        return dg;
    }

    public void setDg(String dg) {
        this.dg = dg;
    }

    public String getDnsz() {
        return dnsz;
    }

    public void setDnsz(String dnsz) {
        this.dnsz = dnsz;
    }

    public String getJsq() {
        return jsq;
    }

    public void setJsq(String jsq) {
        this.jsq = jsq;
    }

    public String getJsqtx() {
        return jsqtx;
    }

    public void setJsqtx(String jsqtx) {
        this.jsqtx = jsqtx;
    }

    public String getHqzz() {
        return hqzz;
    }

    public void setHqzz(String hqzz) {
        this.hqzz = hqzz;
    }

    public String getHqdy() {
        return hqdy;
    }

    public void setHqdy(String hqdy) {
        this.hqdy = hqdy;
    }

    public String getJk() {
        return jk;
    }

    public void setJk(String jk) {
        this.jk = jk;
    }

    public String getDm() {
        return dm;
    }

    public void setDm(String dm) {
        this.dm = dm;
    }

    public String getQxjtx() {
        return qxjtx;
    }

    public void setQxjtx(String qxjtx) {
        this.qxjtx = qxjtx;
    }

    public String getJcbfsqd() {
        return jcbfsqd;
    }

    public void setJcbfsqd(String jcbfsqd) {
        this.jcbfsqd = jcbfsqd;
    }

    public String getWifikg() {
        return wifikg;
    }

    public void setWifikg(String wifikg) {
        this.wifikg = wifikg;
    }

    public String getWifihfcc() {
        return wifihfcc;
    }

    public void setWifihfcc(String wifihfcc) {
        this.wifihfcc = wifihfcc;
    }

    public String getKqzlpjdw() {
        return kqzlpjdw;
    }

    public void setKqzlpjdw(String kqzlpjdw) {
        this.kqzlpjdw = kqzlpjdw;
    }

    public String getPm25dw() {
        return pm25dw;
    }

    public void setPm25dw(String pm25dw) {
        this.pm25dw = pm25dw;
    }

    public String getKgj() {
        return kgj;
    }

    public void setKgj(String kgj) {
        this.kgj = kgj;
    }

    public String getMs() {
        return ms;
    }

    public void setMs(String ms) {
        this.ms = ms;
    }

    public String getSleep() {
        return sleep;
    }

    public void setSleep(String sleep) {
        this.sleep = sleep;
    }

    public String getSf() {
        return sf;
    }

    public void setSf(String sf) {
        this.sf = sf;
    }

    public String getFs() {
        return fs;
    }

    public void setFs(String fs) {
        this.fs = fs;
    }

    public String getWd() {
        return wd;
    }

    public void setWd(String wd) {
        this.wd = wd;
    }

    public String getDs() {
        return ds;
    }

    public void setDs(String ds) {
        this.ds = ds;
    }

    public String getDssj() {
        return dssj;
    }

    public void setDssj(String dssj) {
        this.dssj = dssj;
    }

    public String getGz() {
        return gz;
    }

    public void setGz(String gz) {
        this.gz = gz;
    }

    public String getFr() {
        return fr;
    }

    public void setFr(String fr) {
        this.fr = fr;
    }

    public String getWddw() {
        return wddw;
    }

    public void setWddw(String wddw) {
        this.wddw = wddw;
    }

    public String getHq() {
        return hq;
    }

    public void setHq(String hq) {
        this.hq = hq;
    }

    public String getLsljydlfwql() {
        return lsljydlfwql;
    }

    public void setLsljydlfwql(String lsljydlfwql) {
        this.lsljydlfwql = lsljydlfwql;
    }

    public String getZdqj() {
        return zdqj;
    }

    public void setZdqj(String zdqj) {
        this.zdqj = zdqj;
    }

    public String getSxsf() {
        return sxsf;
    }

    public void setSxsf(String sxsf) {
        this.sxsf = sxsf;
    }

    public String getZysf() {
        return zysf;
    }

    public void setZysf(String zysf) {
        this.zysf = zysf;
    }

    public String getFm() {
        return fm;
    }

    public void setFm(String fm) {
        this.fm = fm;
    }

    public String getWdxsms() {
        return wdxsms;
    }

    public void setWdxsms(String wdxsms) {
        this.wdxsms = wdxsms;
    }

    public String getQcts() {
        return qcts;
    }

    public void setQcts(String qcts) {
        this.qcts = qcts;
    }

    public String getYkj() {
        return ykj;
    }

    public void setYkj(String ykj) {
        this.ykj = ykj;
    }

    public String getSdxs() {
        return sdxs;
    }

    public void setSdxs(String sdxs) {
        this.sdxs = sdxs;
    }

    public String getSdsd() {
        return sdsd;
    }

    public void setSdsd(String sdsd) {
        this.sdsd = sdsd;
    }

    public String getSaver() {
        return saver;
    }

    public void setSaver(String saver) {
        this.saver = saver;
    }

    public String getGea() {
        return gea;
    }

    public void setGea(String gea) {
        this.gea = gea;
    }

    public String getZdqx() {
        return zdqx;
    }

    public void setZdqx(String zdqx) {
        this.zdqx = zdqx;
    }

    public String getHf() {
        return hf;
    }

    public void setHf(String hf) {
        this.hf = hf;
    }

    public String getHfms() {
        return hfms;
    }

    public void setHfms(String hfms) {
        this.hfms = hfms;
    }

    public String getHsh1() {
        return hsh1;
    }

    public void setHsh1(String hsh1) {
        this.hsh1 = hsh1;
    }

    public String getGlwqxtx() {
        return glwqxtx;
    }

    public void setGlwqxtx(String glwqxtx) {
        this.glwqxtx = glwqxtx;
    }

    public String getSb() {
        return sb;
    }

    public void setSb(String sb) {
        this.sb = sb;
    }

    public String getSm() {
        return sm;
    }

    public void setSm(String sm) {
        this.sm = sm;
    }

    public String getFcktled() {
        return fcktled;
    }

    public void setFcktled(String fcktled) {
        this.fcktled = fcktled;
    }

    public String getDsxhcs() {
        return dsxhcs;
    }

    public void setDsxhcs(String dsxhcs) {
        this.dsxhcs = dsxhcs;
    }

    public String getQz() {
        return qz;
    }

    public void setQz(String qz) {
        this.qz = qz;
    }

    public String getDsk() {
        return dsk;
    }

    public void setDsk(String dsk) {
        this.dsk = dsk;
    }

    public String getDsg() {
        return dsg;
    }

    public void setDsg(String dsg) {
        this.dsg = dsg;
    }

    public String getTxsb() {
        return txsb;
    }

    public void setTxsb(String txsb) {
        this.txsb = txsb;
    }

    public String getSleepmode() {
        return sleepmode;
    }

    public void setSleepmode(String sleepmode) {
        this.sleepmode = sleepmode;
    }

    public String getMute() {
        return mute;
    }

    public void setMute(String mute) {
        this.mute = mute;
    }

    public String getQyxz() {
        return qyxz;
    }

    public void setQyxz(String qyxz) {
        this.qyxz = qyxz;
    }

    public String getSm1hhwd() {
        return sm1hhwd;
    }

    public void setSm1hhwd(String sm1hhwd) {
        this.sm1hhwd = sm1hhwd;
    }

    public String getSm2hhwd() {
        return sm2hhwd;
    }

    public void setSm2hhwd(String sm2hhwd) {
        this.sm2hhwd = sm2hhwd;
    }

    public String getHj() {
        return hj;
    }

    public void setHj(String hj) {
        this.hj = hj;
    }

    public String getF5() {
        return f5;
    }

    public void setF5(String f5) {
        this.f5 = f5;
    }

    public String getEx() {
        return ex;
    }

    public void setEx(String ex) {
        this.ex = ex;
    }

    public String getSm3hhwd() {
        return sm3hhwd;
    }

    public void setSm3hhwd(String sm3hhwd) {
        this.sm3hhwd = sm3hhwd;
    }

    public String getSm4hhwd() {
        return sm4hhwd;
    }

    public void setSm4hhwd(String sm4hhwd) {
        this.sm4hhwd = sm4hhwd;
    }

    public String getSm5hhwd() {
        return sm5hhwd;
    }

    public void setSm5hhwd(String sm5hhwd) {
        this.sm5hhwd = sm5hhwd;
    }

    public String getSm6hhwd() {
        return sm6hhwd;
    }

    public void setSm6hhwd(String sm6hhwd) {
        this.sm6hhwd = sm6hhwd;
    }

    public String getSm7hhwd() {
        return sm7hhwd;
    }

    public void setSm7hhwd(String sm7hhwd) {
        this.sm7hhwd = sm7hhwd;
    }

    public String getSm8hhwd() {
        return sm8hhwd;
    }

    public void setSm8hhwd(String sm8hhwd) {
        this.sm8hhwd = sm8hhwd;
    }

    public String getStf() {
        return stf;
    }

    public void setStf(String stf) {
        this.stf = stf;
    }

    public String getSxsffk() {
        return sxsffk;
    }

    public void setSxsffk(String sxsffk) {
        this.sxsffk = sxsffk;
    }

    public String getHx() {
        return hx;
    }

    public void setHx(String hx) {
        this.hx = hx;
    }

    public String getZnhq() {
        return znhq;
    }

    public void setZnhq(String znhq) {
        this.znhq = znhq;
    }

    public String getYhsdyddw() {
        return yhsdyddw;
    }

    public void setYhsdyddw(String yhsdyddw) {
        this.yhsdyddw = yhsdyddw;
    }

    public String getJzcr() {
        return jzcr;
    }

    public void setJzcr(String jzcr) {
        this.jzcr = jzcr;
    }

    public String getHjqy9() {
        return hjqy9;
    }

    public void setHjqy9(String hjqy9) {
        this.hjqy9 = hjqy9;
    }

    public String getHjqy8() {
        return hjqy8;
    }

    public void setHjqy8(String hjqy8) {
        this.hjqy8 = hjqy8;
    }

    public String getHjqy7() {
        return hjqy7;
    }

    public void setHjqy7(String hjqy7) {
        this.hjqy7 = hjqy7;
    }

    public String getHjqy6() {
        return hjqy6;
    }

    public void setHjqy6(String hjqy6) {
        this.hjqy6 = hjqy6;
    }

    public String getHjqy5() {
        return hjqy5;
    }

    public void setHjqy5(String hjqy5) {
        this.hjqy5 = hjqy5;
    }

    public String getHjqy4() {
        return hjqy4;
    }

    public void setHjqy4(String hjqy4) {
        this.hjqy4 = hjqy4;
    }

    public String getHjqy3() {
        return hjqy3;
    }

    public void setHjqy3(String hjqy3) {
        this.hjqy3 = hjqy3;
    }

    public String getHjqy2() {
        return hjqy2;
    }

    public void setHjqy2(String hjqy2) {
        this.hjqy2 = hjqy2;
    }

    public String getHjqy1() {
        return hjqy1;
    }

    public void setHjqy1(String hjqy1) {
        this.hjqy1 = hjqy1;
    }

    public String getHwyk() {
        return hwyk;
    }

    public void setHwyk(String hwyk) {
        this.hwyk = hwyk;
    }

    public String getFault() {
        return fault;
    }

    public void setFault(String fault) {
        this.fault = fault;
    }

    public String getZkfs() {
        return zkfs;
    }

    public void setZkfs(String zkfs) {
        this.zkfs = zkfs;
    }

    public String getHjms() {
        return hjms;
    }

    public void setHjms(String hjms) {
        this.hjms = hjms;
    }

    public String getXcfksxsf() {
        return xcfksxsf;
    }

    public void setXcfksxsf(String xcfksxsf) {
        this.xcfksxsf = xcfksxsf;
    }

    public String getXcfkzczysf() {
        return xcfkzczysf;
    }

    public void setXcfkzczysf(String xcfkzczysf) {
        this.xcfkzczysf = xcfkzczysf;
    }

    public String getXcfkyczysf() {
        return xcfkyczysf;
    }

    public void setXcfkyczysf(String xcfkyczysf) {
        this.xcfkyczysf = xcfkyczysf;
    }

    public String getSnhjwd() {
        return snhjwd;
    }

    public void setSnhjwd(String snhjwd) {
        this.snhjwd = snhjwd;
    }

    public String getHjsdz() {
        return hjsdz;
    }

    public void setHjsdz(String hjsdz) {
        this.hjsdz = hjsdz;
    }

    public String getSwhjwd() {
        return swhjwd;
    }

    public void setSwhjwd(String swhjwd) {
        this.swhjwd = swhjwd;
    }

    @Override
    public String toString() {
        return "AirRealCtlResp{" +
                "id=" + id +
                ", mac='" + mac + '\'' +
                ", ctime=" + ctime +
                ", cobj=" + cobj +
                ", cq=" + cq +
                ", csms='" + csms + '\'' +
                ", ddfs='" + ddfs + '\'' +
                ", dg=" + dg +
                ", dnsz=" + dnsz +
                ", jsq=" + jsq +
                ", jsqtx=" + jsqtx +
                ", hqzz='" + hqzz + '\'' +
                ", hqdy=" + hqdy +
                ", jk=" + jk +
                ", dm=" + dm +
                ", qxjtx=" + qxjtx +
                ", jcbfsqd='" + jcbfsqd + '\'' +
                ", wifikg=" + wifikg +
                ", wifihfcc=" + wifihfcc +
                ", kqzlpjdw='" + kqzlpjdw + '\'' +
                ", pm25dw='" + pm25dw + '\'' +
                ", kgj=" + kgj +
                ", ms='" + ms + '\'' +
                ", sleep=" + sleep +
                ", sf=" + sf +
                ", fs='" + fs + '\'' +
                ", wd='" + wd + '\'' +
                ", ds=" + ds +
                ", dssj='" + dssj + '\'' +
                ", gz=" + gz +
                ", fr=" + fr +
                ", wddw=" + wddw +
                ", hq='" + hq + '\'' +
                ", lsljydlfwql=" + lsljydlfwql +
                ", zdqj=" + zdqj +
                ", sxsf='" + sxsf + '\'' +
                ", zysf='" + zysf + '\'' +
                ", fm='" + fm + '\'' +
                ", wdxsms='" + wdxsms + '\'' +
                ", qcts=" + qcts +
                ", ykj='" + ykj + '\'' +
                ", sdxs=" + sdxs +
                ", sdsd='" + sdsd + '\'' +
                ", saver=" + saver +
                ", gea=" + gea +
                ", zdqx=" + zdqx +
                ", hf=" + hf +
                ", hfms='" + hfms + '\'' +
                ", hsh1=" + hsh1 +
                ", glwqxtx=" + glwqxtx +
                ", sb=" + sb +
                ", sm=" + sm +
                ", fcktled=" + fcktled +
                ", dsxhcs=" + dsxhcs +
                ", qz='" + qz + '\'' +
                ", dsk=" + dsk +
                ", dsg=" + dsg +
                ", txsb='" + txsb + '\'' +
                ", sleepmode='" + sleepmode + '\'' +
                ", mute='" + mute + '\'' +
                ", qyxz='" + qyxz + '\'' +
                ", sm1hhwd='" + sm1hhwd + '\'' +
                ", sm2hhwd='" + sm2hhwd + '\'' +
                ", hj='" + hj + '\'' +
                ", f5=" + f5 +
                ", ex='" + ex + '\'' +
                ", sm3hhwd='" + sm3hhwd + '\'' +
                ", sm4hhwd='" + sm4hhwd + '\'' +
                ", sm5hhwd='" + sm5hhwd + '\'' +
                ", sm6hhwd='" + sm6hhwd + '\'' +
                ", sm7hhwd='" + sm7hhwd + '\'' +
                ", sm8hhwd='" + sm8hhwd + '\'' +
                ", stf=" + stf +
                ", sxsffk='" + sxsffk + '\'' +
                ", hx=" + hx +
                ", znhq=" + znhq +
                ", yhsdyddw='" + yhsdyddw + '\'' +
                ", jzcr=" + jzcr +
                ", hjqy9=" + hjqy9 +
                ", hjqy8=" + hjqy8 +
                ", hjqy7=" + hjqy7 +
                ", hjqy6=" + hjqy6 +
                ", hjqy5=" + hjqy5 +
                ", hjqy4=" + hjqy4 +
                ", hjqy3=" + hjqy3 +
                ", hjqy2=" + hjqy2 +
                ", hjqy1=" + hjqy1 +
                ", hwyk=" + hwyk +
                ", fault=" + fault +
                ", zkfs='" + zkfs + '\'' +
                ", hjms=" + hjms +
                ", xcfksxsf='" + xcfksxsf + '\'' +
                ", xcfkzczysf='" + xcfkzczysf + '\'' +
                ", xcfkyczysf='" + xcfkyczysf + '\'' +
                ", snhjwd='" + snhjwd + '\'' +
                ", hjsdz='" + hjsdz + '\'' +
                ", swhjwd='" + swhjwd + '\'' +
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
