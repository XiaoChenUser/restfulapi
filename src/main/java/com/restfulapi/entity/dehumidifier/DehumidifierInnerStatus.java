package com.restfulapi.entity.dehumidifier;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "dehumidifier_innerstatustable")
public class DehumidifierInnerStatus implements Serializable{
    @Id                           //主键配置
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;


    /**
     * mac地址转long, 长度24位.
     */
    private String mac;


    /**
     * 数据在本地的生成时间
     */

    private Date ctime;

    /**
     * 开关机状态	0：关机 1：开机

     */
    private String kgjzt;

    /**
     * 运行模式	0000：Free
     0001：制冷模式
     0010：抽湿模式
     0011：送风模式
     0100：制热模式
     0101：强制制冷
     0110：强制制热
     0111：强制除霜
     1000：制冷能力测试
     1001：制热能力测试
     1010：收氟模式
     1011：试运行

     */
    private String yxms;

    /**
     * 内风机转速	0000：内风机关
     0001：内风机低速
     0010：内风机低高速
     0011：内风机中速
     0100：内风机中高速
     0101：内风机高速
     0110：内风机超高速
     0111：静音低
     1000：静音中
     1001：静音高
     1010：微风档

     */
    private String nfjzs;

    /**
     * 室内环境温度小数标志	3～2   7～6     温度
     00     00       0℃标志
     00     01       0.1℃标志
     00     10       0.2℃标志
     01     00       0.3℃标志
     01     01       0.4℃标志
     10     00       0.5℃标志
     10     01       0.6℃标志
     10     10       0.7℃标志
     11     00       0.8℃标志
     11     01       0.9℃标志

     */
    private String snhjwdxsbz;

    /**
     * 室内蒸发器中间温度小数标志	00： 0℃标志
     01： 0.3℃标志
     10： 0.5℃标志
     11： 0.8℃标志

     */
    private String snzfqzjwdxsbz;

    /**
     * 室内设定温度小数标志	00： 0℃标志
     01： 0.3℃标志
     10： 0.5℃标志
     11： 0.8℃标志

     */
    private String snsdwdxsbz;

    /**
     * 室内设定温度	0h：-40℃，数据每增加1则温度增加1℃

     */
    private String snsdwd;

    /**
     * 室内环境温度	0h：-40℃，数据每增加1则温度增加1℃

     */
    private String snhjwd;

    /**
     * 试运行	0：未设定；1：设定

     */
    private String syx;

    /**
     * 收氟模式	0：未设定；1：设定

     */
    private String sfms;

    /**
     * 强制除霜	0：未设定；1：设定

     */
    private String qzcs;

    /**
     * 强制制热	0：未设定；1：设定

     */
    private String qzzr;

    /**
     * 强制制冷	0：未设定；1：设定

     */
    private String qzzl;

    /**
     * 室内蒸发器中间温度	0h：-40℃，数据每增加1则温度增加1℃

     */
    private String snzfqzjwd;

    /**
     * 补偿的温度值
     （bit0为1时有效）
     （注：以往分体机的那种制热补偿为正）	1001：-7度
     ……
     1111：-1度
     0000： 0度
     0001： 1度
     ……
     0111： 7度

     */
    private String bcdwdz;

    /**
     * 是否控制外机制热温度补偿	0：不控制外机补偿；1：控制外机补偿

     */
    private String sfkzwjzrwdbc;

    /**
     * 室内环境湿度	0h：0%，数据每增加1则湿度增加1％

     */
    private String snhjsd;

    /**
     * 加湿状态	0：OFF（无效） 1：ON

     */
    private String jszt;

    /**
     * SAVE状态	0：OFF（无效） 1：ON

     */
    private String savezt;

    /**
     * 换气状态	0：OFF（无效） 1：ON

     */
    private String hqzt;

    /**
     * 静音状态	0：OFF（无效） 1：ON

     */
    private String jyzt;

    /**
     * 防冷风状态	0：OFF（无效） 1：ON

     */
    private String flfzt;

    /**
     * 防凝露状态	0：OFF（无效） 1：ON

     */
    private String fnlzt;

    /**
     * 电辅热状态	0：OFF（无效） 1：ON

     */
    private String dfrzt;

    /**
     * 快测状态	0：OFF（无效） 1：ON

     */
    private String kczt;

    private String zczt;

    private String sbgz;

    private String syytqf;

    private String nlcsztxz;

    /**
     * 压缩机转速设定值	设定压缩机转速   单位：rps

     */
    private String ysjzssdz;

    /**
     * 电子膨胀阀开度设定允许状态	0：OFF（default）1：ON

     */
    private String dzpzfkdsdyxzt;

    /**
     * 电子膨胀阀开度设定值	设定电子膨胀阀步数（D[13]为低位）
     单位：step

     */
    private String dzpzfkdsdz;

    /**
     * 室外风机设定允许状态	0：OFF（default）1：ON

     */
    private String wsfjsdyxzt;

    /**
     * 目标排气温度设定允许状态	0：OFF（default）1：ON

     */
    private String mbpqwdsdyxzt;

    /**
     * 目标过热度设定允许状态	0：OFF（default）1：ON

     */
    private String mbgrdsdyxzt;

    /**
     * 室外停机要求标识	0：无要求   1：要求室外停机（如内机需要外机做停机处理时，此标识置位）

     */
    private String swtjyqbz;

    /**
     * 室内气体传感器故障	0：正常            1：故障

     */
    private String snqtcgqgz;

    /**
     * 室内水位传感器故障	0：正常            1：故障

     */
    private String snswcgqgz;

    /**
     * 室内湿度传感器故障	0：正常            1：故障

     */
    private String snsdcgqgz;

    /**
     * 室内环境感温包故障	0：正常            1：故障

     */
    private String snhjgwbgz;

    /**
     * 室内蒸发器中间感温包故障	0：正常            1：故障

     */
    private String snzfqzjgwbgz;

    /**
     * 内机主板与手操器通讯故障（带手操器工作时有效，接灯板时发0）	0：正常    1：故障

     */
    private String njzbyscqtxgz;

    /**
     * 内机水满保护	0：正常            1：故障

     */
    private String snjsmbh;

    /**
     * 自动按键顶死	0：正常            1：故障

     */
    private String zdajds;

    /**
     * 滑动门故障	0：正常            1：故障

     */
    private String hdmgz;

    /**
     * 选择口电平异常	0：正常            1：故障

     */
    private String xzkdpyc;

    /**
     * 跳线帽故障	0：正常            1：故障

     */
    private String txmgz;

    /**
     * 记忆芯片读写故障	0：正常            1：故障

     */
    private String jyxpdxgz;

    /**
     * 室内风机故障	0：正常            1：故障

     */
    private String snfjgz;

    /**
     * 缺氟屏蔽选择	0：不屏蔽缺氟和阀门截止保护
     1：屏蔽缺氟和阀门截止保护

     */
    private String qfpbxz;

    /**
     * 除霜方式选择	0：除霜模式1（常规除霜模式）
     1：除霜模式2（自由配需求）

     */
    private String csfsxz;

    /**
     * 温控精度选择	000：不区分，按默认执行
     001：±1℃
     010：±2℃

     */
    private String wkjdxz;

    /**
     * 左右扫风状态	0000-关，0001-同向扫风，0010-1位置，
     0011-2位置，0100-3位置，0101-4位置，0110-5位置，1100-15位置，1101-相向扫风

     */
    private String zysfzt;

    /**
     * 上下扫风状态	0000-关，0001-15扫风，0010-1位置，
     0011-2位置，0100-3位置，0101-4位置，0110-5位置，0111-35扫风，1000-25扫风，1001-24扫风，1010-14扫风，1011-13扫风

     */
    private String sxsfzt;

    /**
     * 柔湿制冷	0：无    1：柔湿制冷

     */
    private String rszl;

    /**
     * DRED模式运行状态	000-无DRED模式运行
     001-DRED 1模式运行
     010-DRED 2模式运行
     011-DRED 3模式运行

     */
    private String dredmsyxzt;

    /**
     * 射频模块rF故障	0：正常            1：故障

     */
    private String spmkrfgz;

    /**
     * 检测板通讯JF故障	0：正常            1：故障

     */
    private String jcbtxjfgz;

    /**
     * 内机未定义外机故障OE	0：正常            1：故障

     */
    private String njwdywjgzoe;

    /**
     * 扫风机构故障FC	0：正常            1：故障

     */
    private String sfjggzfc;

    /**
     * WiFi故障代码	0：正常            1：故障

     */
    private String wifigzgm;

    /**
     * 内外机通讯故障	0：正常            1：故障

     */
    private String nwjtxgz;

    /**
     * 电机堵转保护	0：正常            1：故障

     */
    private String djdzbh;

    /**
     * 显示板和驱动板故障	0：正常            1：故障

     */
    private String xsbhqdbgz;

    /**
     * 定变频显示板用错	0：正常            1：故障

     */
    private String dbpxsbyc;

    /**
     * 内机过零信号故障	0：正常            1：故障

     */
    private String njglxhgz;

    public DehumidifierInnerStatus() {

    }

    public DehumidifierInnerStatus(String mac, Date ctime, String kgjzt, String yxms, String nfjzs, String snhjwdxsbz, String snzfqzjwdxsbz, String snsdwdxsbz, String snsdwd, String snhjwd, String syx, String sfms, String qzcs, String qzzr, String qzzl, String snzfqzjwd, String bcdwdz, String sfkzwjzrwdbc, String snhjsd, String jszt, String savezt, String hqzt, String jyzt, String flfzt, String fnlzt, String dfrzt, String kczt, String zczt, String sbgz, String syytqf, String nlcsztxz, String ysjzssdz, String dzpzfkdsdyxzt, String dzpzfkdsdz, String wsfjsdyxzt, String mbpqwdsdyxzt, String mbgrdsdyxzt, String swtjyqbz, String snqtcgqgz, String snswcgqgz, String snsdcgqgz, String snhjgwbgz, String snzfqzjgwbgz, String njzbyscqtxgz, String snjsmbh, String zdajds, String hdmgz, String xzkdpyc, String txmgz, String jyxpdxgz, String snfjgz, String qfpbxz, String csfsxz, String wkjdxz, String zysfzt, String sxsfzt, String rszl, String dredmsyxzt, String spmkrfgz, String jcbtxjfgz, String njwdywjgzoe, String sfjggzfc, String wifigzgm, String nwjtxgz, String djdzbh, String xsbhqdbgz, String dbpxsbyc, String njglxhgz) {
        this.mac = mac;
        this.ctime = ctime;
        this.kgjzt = kgjzt;
        this.yxms = yxms;
        this.nfjzs = nfjzs;
        this.snhjwdxsbz = snhjwdxsbz;
        this.snzfqzjwdxsbz = snzfqzjwdxsbz;
        this.snsdwdxsbz = snsdwdxsbz;
        this.snsdwd = snsdwd;
        this.snhjwd = snhjwd;
        this.syx = syx;
        this.sfms = sfms;
        this.qzcs = qzcs;
        this.qzzr = qzzr;
        this.qzzl = qzzl;
        this.snzfqzjwd = snzfqzjwd;
        this.bcdwdz = bcdwdz;
        this.sfkzwjzrwdbc = sfkzwjzrwdbc;
        this.snhjsd = snhjsd;
        this.jszt = jszt;
        this.savezt = savezt;
        this.hqzt = hqzt;
        this.jyzt = jyzt;
        this.flfzt = flfzt;
        this.fnlzt = fnlzt;
        this.dfrzt = dfrzt;
        this.kczt = kczt;
        this.zczt = zczt;
        this.sbgz = sbgz;
        this.syytqf = syytqf;
        this.nlcsztxz = nlcsztxz;
        this.ysjzssdz = ysjzssdz;
        this.dzpzfkdsdyxzt = dzpzfkdsdyxzt;
        this.dzpzfkdsdz = dzpzfkdsdz;
        this.wsfjsdyxzt = wsfjsdyxzt;
        this.mbpqwdsdyxzt = mbpqwdsdyxzt;
        this.mbgrdsdyxzt = mbgrdsdyxzt;
        this.swtjyqbz = swtjyqbz;
        this.snqtcgqgz = snqtcgqgz;
        this.snswcgqgz = snswcgqgz;
        this.snsdcgqgz = snsdcgqgz;
        this.snhjgwbgz = snhjgwbgz;
        this.snzfqzjgwbgz = snzfqzjgwbgz;
        this.njzbyscqtxgz = njzbyscqtxgz;
        this.snjsmbh = snjsmbh;
        this.zdajds = zdajds;
        this.hdmgz = hdmgz;
        this.xzkdpyc = xzkdpyc;
        this.txmgz = txmgz;
        this.jyxpdxgz = jyxpdxgz;
        this.snfjgz = snfjgz;
        this.qfpbxz = qfpbxz;
        this.csfsxz = csfsxz;
        this.wkjdxz = wkjdxz;
        this.zysfzt = zysfzt;
        this.sxsfzt = sxsfzt;
        this.rszl = rszl;
        this.dredmsyxzt = dredmsyxzt;
        this.spmkrfgz = spmkrfgz;
        this.jcbtxjfgz = jcbtxjfgz;
        this.njwdywjgzoe = njwdywjgzoe;
        this.sfjggzfc = sfjggzfc;
        this.wifigzgm = wifigzgm;
        this.nwjtxgz = nwjtxgz;
        this.djdzbh = djdzbh;
        this.xsbhqdbgz = xsbhqdbgz;
        this.dbpxsbyc = dbpxsbyc;
        this.njglxhgz = njglxhgz;
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

    public String getKgjzt() {
        return kgjzt;
    }

    public void setKgjzt(String kgjzt) {
        this.kgjzt = kgjzt;
    }

    public String getYxms() {
        return yxms;
    }

    public void setYxms(String yxms) {
        this.yxms = yxms;
    }

    public String getNfjzs() {
        return nfjzs;
    }

    public void setNfjzs(String nfjzs) {
        this.nfjzs = nfjzs;
    }

    public String getSnhjwdxsbz() {
        return snhjwdxsbz;
    }

    public void setSnhjwdxsbz(String snhjwdxsbz) {
        this.snhjwdxsbz = snhjwdxsbz;
    }

    public String getSnzfqzjwdxsbz() {
        return snzfqzjwdxsbz;
    }

    public void setSnzfqzjwdxsbz(String snzfqzjwdxsbz) {
        this.snzfqzjwdxsbz = snzfqzjwdxsbz;
    }

    public String getSnsdwdxsbz() {
        return snsdwdxsbz;
    }

    public void setSnsdwdxsbz(String snsdwdxsbz) {
        this.snsdwdxsbz = snsdwdxsbz;
    }

    public String getSnsdwd() {
        return snsdwd;
    }

    public void setSnsdwd(String snsdwd) {
        this.snsdwd = snsdwd;
    }

    public String getSnhjwd() {
        return snhjwd;
    }

    public void setSnhjwd(String snhjwd) {
        this.snhjwd = snhjwd;
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

    public String getSnzfqzjwd() {
        return snzfqzjwd;
    }

    public void setSnzfqzjwd(String snzfqzjwd) {
        this.snzfqzjwd = snzfqzjwd;
    }

    public String getBcdwdz() {
        return bcdwdz;
    }

    public void setBcdwdz(String bcdwdz) {
        this.bcdwdz = bcdwdz;
    }

    public String getSfkzwjzrwdbc() {
        return sfkzwjzrwdbc;
    }

    public void setSfkzwjzrwdbc(String sfkzwjzrwdbc) {
        this.sfkzwjzrwdbc = sfkzwjzrwdbc;
    }

    public String getSnhjsd() {
        return snhjsd;
    }

    public void setSnhjsd(String snhjsd) {
        this.snhjsd = snhjsd;
    }

    public String getJszt() {
        return jszt;
    }

    public void setJszt(String jszt) {
        this.jszt = jszt;
    }

    public String getSavezt() {
        return savezt;
    }

    public void setSavezt(String savezt) {
        this.savezt = savezt;
    }

    public String getHqzt() {
        return hqzt;
    }

    public void setHqzt(String hqzt) {
        this.hqzt = hqzt;
    }

    public String getJyzt() {
        return jyzt;
    }

    public void setJyzt(String jyzt) {
        this.jyzt = jyzt;
    }

    public String getFlfzt() {
        return flfzt;
    }

    public void setFlfzt(String flfzt) {
        this.flfzt = flfzt;
    }

    public String getFnlzt() {
        return fnlzt;
    }

    public void setFnlzt(String fnlzt) {
        this.fnlzt = fnlzt;
    }

    public String getDfrzt() {
        return dfrzt;
    }

    public void setDfrzt(String dfrzt) {
        this.dfrzt = dfrzt;
    }

    public String getKczt() {
        return kczt;
    }

    public void setKczt(String kczt) {
        this.kczt = kczt;
    }

    public String getZczt() {
        return zczt;
    }

    public void setZczt(String zczt) {
        this.zczt = zczt;
    }

    public String getSbgz() {
        return sbgz;
    }

    public void setSbgz(String sbgz) {
        this.sbgz = sbgz;
    }

    public String getSyytqf() {
        return syytqf;
    }

    public void setSyytqf(String syytqf) {
        this.syytqf = syytqf;
    }

    public String getNlcsztxz() {
        return nlcsztxz;
    }

    public void setNlcsztxz(String nlcsztxz) {
        this.nlcsztxz = nlcsztxz;
    }

    public String getYsjzssdz() {
        return ysjzssdz;
    }

    public void setYsjzssdz(String ysjzssdz) {
        this.ysjzssdz = ysjzssdz;
    }

    public String getDzpzfkdsdyxzt() {
        return dzpzfkdsdyxzt;
    }

    public void setDzpzfkdsdyxzt(String dzpzfkdsdyxzt) {
        this.dzpzfkdsdyxzt = dzpzfkdsdyxzt;
    }

    public String getDzpzfkdsdz() {
        return dzpzfkdsdz;
    }

    public void setDzpzfkdsdz(String dzpzfkdsdz) {
        this.dzpzfkdsdz = dzpzfkdsdz;
    }

    public String getWsfjsdyxzt() {
        return wsfjsdyxzt;
    }

    public void setWsfjsdyxzt(String wsfjsdyxzt) {
        this.wsfjsdyxzt = wsfjsdyxzt;
    }

    public String getMbpqwdsdyxzt() {
        return mbpqwdsdyxzt;
    }

    public void setMbpqwdsdyxzt(String mbpqwdsdyxzt) {
        this.mbpqwdsdyxzt = mbpqwdsdyxzt;
    }

    public String getMbgrdsdyxzt() {
        return mbgrdsdyxzt;
    }

    public void setMbgrdsdyxzt(String mbgrdsdyxzt) {
        this.mbgrdsdyxzt = mbgrdsdyxzt;
    }

    public String getSwtjyqbz() {
        return swtjyqbz;
    }

    public void setSwtjyqbz(String swtjyqbz) {
        this.swtjyqbz = swtjyqbz;
    }

    public String getSnqtcgqgz() {
        return snqtcgqgz;
    }

    public void setSnqtcgqgz(String snqtcgqgz) {
        this.snqtcgqgz = snqtcgqgz;
    }

    public String getSnswcgqgz() {
        return snswcgqgz;
    }

    public void setSnswcgqgz(String snswcgqgz) {
        this.snswcgqgz = snswcgqgz;
    }

    public String getSnsdcgqgz() {
        return snsdcgqgz;
    }

    public void setSnsdcgqgz(String snsdcgqgz) {
        this.snsdcgqgz = snsdcgqgz;
    }

    public String getSnhjgwbgz() {
        return snhjgwbgz;
    }

    public void setSnhjgwbgz(String snhjgwbgz) {
        this.snhjgwbgz = snhjgwbgz;
    }

    public String getSnzfqzjgwbgz() {
        return snzfqzjgwbgz;
    }

    public void setSnzfqzjgwbgz(String snzfqzjgwbgz) {
        this.snzfqzjgwbgz = snzfqzjgwbgz;
    }

    public String getNjzbyscqtxgz() {
        return njzbyscqtxgz;
    }

    public void setNjzbyscqtxgz(String njzbyscqtxgz) {
        this.njzbyscqtxgz = njzbyscqtxgz;
    }

    public String getSnjsmbh() {
        return snjsmbh;
    }

    public void setSnjsmbh(String snjsmbh) {
        this.snjsmbh = snjsmbh;
    }

    public String getZdajds() {
        return zdajds;
    }

    public void setZdajds(String zdajds) {
        this.zdajds = zdajds;
    }

    public String getHdmgz() {
        return hdmgz;
    }

    public void setHdmgz(String hdmgz) {
        this.hdmgz = hdmgz;
    }

    public String getXzkdpyc() {
        return xzkdpyc;
    }

    public void setXzkdpyc(String xzkdpyc) {
        this.xzkdpyc = xzkdpyc;
    }

    public String getTxmgz() {
        return txmgz;
    }

    public void setTxmgz(String txmgz) {
        this.txmgz = txmgz;
    }

    public String getJyxpdxgz() {
        return jyxpdxgz;
    }

    public void setJyxpdxgz(String jyxpdxgz) {
        this.jyxpdxgz = jyxpdxgz;
    }

    public String getSnfjgz() {
        return snfjgz;
    }

    public void setSnfjgz(String snfjgz) {
        this.snfjgz = snfjgz;
    }

    public String getQfpbxz() {
        return qfpbxz;
    }

    public void setQfpbxz(String qfpbxz) {
        this.qfpbxz = qfpbxz;
    }

    public String getCsfsxz() {
        return csfsxz;
    }

    public void setCsfsxz(String csfsxz) {
        this.csfsxz = csfsxz;
    }

    public String getWkjdxz() {
        return wkjdxz;
    }

    public void setWkjdxz(String wkjdxz) {
        this.wkjdxz = wkjdxz;
    }

    public String getZysfzt() {
        return zysfzt;
    }

    public void setZysfzt(String zysfzt) {
        this.zysfzt = zysfzt;
    }

    public String getSxsfzt() {
        return sxsfzt;
    }

    public void setSxsfzt(String sxsfzt) {
        this.sxsfzt = sxsfzt;
    }

    public String getRszl() {
        return rszl;
    }

    public void setRszl(String rszl) {
        this.rszl = rszl;
    }

    public String getDredmsyxzt() {
        return dredmsyxzt;
    }

    public void setDredmsyxzt(String dredmsyxzt) {
        this.dredmsyxzt = dredmsyxzt;
    }

    public String getSpmkrfgz() {
        return spmkrfgz;
    }

    public void setSpmkrfgz(String spmkrfgz) {
        this.spmkrfgz = spmkrfgz;
    }

    public String getJcbtxjfgz() {
        return jcbtxjfgz;
    }

    public void setJcbtxjfgz(String jcbtxjfgz) {
        this.jcbtxjfgz = jcbtxjfgz;
    }

    public String getNjwdywjgzoe() {
        return njwdywjgzoe;
    }

    public void setNjwdywjgzoe(String njwdywjgzoe) {
        this.njwdywjgzoe = njwdywjgzoe;
    }

    public String getSfjggzfc() {
        return sfjggzfc;
    }

    public void setSfjggzfc(String sfjggzfc) {
        this.sfjggzfc = sfjggzfc;
    }

    public String getWifigzgm() {
        return wifigzgm;
    }

    public void setWifigzgm(String wifigzgm) {
        this.wifigzgm = wifigzgm;
    }

    public String getNwjtxgz() {
        return nwjtxgz;
    }

    public void setNwjtxgz(String nwjtxgz) {
        this.nwjtxgz = nwjtxgz;
    }

    public String getDjdzbh() {
        return djdzbh;
    }

    public void setDjdzbh(String djdzbh) {
        this.djdzbh = djdzbh;
    }

    public String getXsbhqdbgz() {
        return xsbhqdbgz;
    }

    public void setXsbhqdbgz(String xsbhqdbgz) {
        this.xsbhqdbgz = xsbhqdbgz;
    }

    public String getDbpxsbyc() {
        return dbpxsbyc;
    }

    public void setDbpxsbyc(String dbpxsbyc) {
        this.dbpxsbyc = dbpxsbyc;
    }

    public String getNjglxhgz() {
        return njglxhgz;
    }

    public void setNjglxhgz(String njglxhgz) {
        this.njglxhgz = njglxhgz;
    }

    @Override
    public String toString() {
        return "AirInnerStatus{" +
                "id=" + id +
                ", mac='" + mac + '\'' +
                ", ctime=" + ctime +
                ", kgjzt='" + kgjzt + '\'' +
                ", yxms='" + yxms + '\'' +
                ", nfjzs='" + nfjzs + '\'' +
                ", snhjwdxsbz='" + snhjwdxsbz + '\'' +
                ", snzfqzjwdxsbz='" + snzfqzjwdxsbz + '\'' +
                ", snsdwdxsbz='" + snsdwdxsbz + '\'' +
                ", snsdwd='" + snsdwd + '\'' +
                ", snhjwd='" + snhjwd + '\'' +
                ", syx='" + syx + '\'' +
                ", sfms='" + sfms + '\'' +
                ", qzcs='" + qzcs + '\'' +
                ", qzzr='" + qzzr + '\'' +
                ", qzzl='" + qzzl + '\'' +
                ", snzfqzjwd='" + snzfqzjwd + '\'' +
                ", bcdwdz='" + bcdwdz + '\'' +
                ", sfkzwjzrwdbc='" + sfkzwjzrwdbc + '\'' +
                ", snhjsd='" + snhjsd + '\'' +
                ", jszt='" + jszt + '\'' +
                ", savezt='" + savezt + '\'' +
                ", hqzt='" + hqzt + '\'' +
                ", jyzt='" + jyzt + '\'' +
                ", flfzt='" + flfzt + '\'' +
                ", fnlzt='" + fnlzt + '\'' +
                ", dfrzt='" + dfrzt + '\'' +
                ", kczt='" + kczt + '\'' +
                ", zczt='" + zczt + '\'' +
                ", sbgz='" + sbgz + '\'' +
                ", syytqf='" + syytqf + '\'' +
                ", nlcsztxz='" + nlcsztxz + '\'' +
                ", ysjzssdz='" + ysjzssdz + '\'' +
                ", dzpzfkdsdyxzt='" + dzpzfkdsdyxzt + '\'' +
                ", dzpzfkdsdz='" + dzpzfkdsdz + '\'' +
                ", wsfjsdyxzt='" + wsfjsdyxzt + '\'' +
                ", mbpqwdsdyxzt='" + mbpqwdsdyxzt + '\'' +
                ", mbgrdsdyxzt='" + mbgrdsdyxzt + '\'' +
                ", swtjyqbz='" + swtjyqbz + '\'' +
                ", snqtcgqgz='" + snqtcgqgz + '\'' +
                ", snswcgqgz='" + snswcgqgz + '\'' +
                ", snsdcgqgz='" + snsdcgqgz + '\'' +
                ", snhjgwbgz='" + snhjgwbgz + '\'' +
                ", snzfqzjgwbgz='" + snzfqzjgwbgz + '\'' +
                ", njzbyscqtxgz='" + njzbyscqtxgz + '\'' +
                ", snjsmbh='" + snjsmbh + '\'' +
                ", zdajds='" + zdajds + '\'' +
                ", hdmgz='" + hdmgz + '\'' +
                ", xzkdpyc='" + xzkdpyc + '\'' +
                ", txmgz='" + txmgz + '\'' +
                ", jyxpdxgz='" + jyxpdxgz + '\'' +
                ", snfjgz='" + snfjgz + '\'' +
                ", qfpbxz='" + qfpbxz + '\'' +
                ", csfsxz='" + csfsxz + '\'' +
                ", wkjdxz='" + wkjdxz + '\'' +
                ", zysfzt='" + zysfzt + '\'' +
                ", sxsfzt='" + sxsfzt + '\'' +
                ", rszl='" + rszl + '\'' +
                ", dredmsyxzt='" + dredmsyxzt + '\'' +
                ", spmkrfgz='" + spmkrfgz + '\'' +
                ", jcbtxjfgz='" + jcbtxjfgz + '\'' +
                ", njwdywjgzoe='" + njwdywjgzoe + '\'' +
                ", sfjggzfc='" + sfjggzfc + '\'' +
                ", wifigzgm='" + wifigzgm + '\'' +
                ", nwjtxgz='" + nwjtxgz + '\'' +
                ", djdzbh='" + djdzbh + '\'' +
                ", xsbhqdbgz='" + xsbhqdbgz + '\'' +
                ", dbpxsbyc='" + dbpxsbyc + '\'' +
                ", njglxhgz='" + njglxhgz + '\'' +
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
