package com.restfulapi.service;

import com.restfulapi.common.tools.TimeZoneTool;
import com.restfulapi.entity.aircondition.*;
import com.restfulapi.hbase.entity.AirConData;
import com.restfulapi.hbase.utils.HbaseUtils;
import com.restfulapi.hbase.utils.StrUtils;
import com.restfulapi.repository.aircondition.*;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.CompareFilter;
import org.apache.hadoop.hbase.filter.Filter;
import org.apache.hadoop.hbase.filter.FilterList;
import org.apache.hadoop.hbase.filter.SingleColumnValueFilter;
import org.apache.hadoop.hbase.util.Bytes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Service
public class AirService {
    public static final Logger log = LoggerFactory.getLogger(AirService.class);
    public static final SimpleDateFormat DATEE = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static final SimpleDateFormat DATEROWKEY = new SimpleDateFormat("yyyyMMddHHmmss");

    @Autowired
    AirCtlRespRepository airCtlRespRepository;
    @Autowired
    AirDeviceRepository airDeviceRepository;
    @Autowired
    AirInnerStatusRepository airInnerStatusRepository;
    @Autowired
    AirInnerUnitRepository airInnerUnitRepository;
    @Autowired
    AirOuterStatusRepository airOuterStatusRepository;
    @Autowired
    AirOuterUnitRepository airOuterUnitRepository;

    public void scanDataAirCon(String mac, String starttime, String endtime, String mid, String evt) throws SQLException, NullPointerException,Exception {
        String tableNameStr = "GRIH:DataAirCon";
        //List<AirConData> airConDataList = new ArrayList<AirConData>();
        Connection connection = HbaseUtils.getConnection();
        TableName tableName = TableName.valueOf(tableNameStr);
        Table dataAirCon = connection.getTable(tableName);
        Scan scan = new Scan();
        scan.setCaching(1000);
        //Filter pageFilter = new PageFilter(100);
        if (StrUtils.isValidStrVal(mac)) {
            if (StrUtils.isValidStrVal(starttime)) {
                String startRow = mac + starttime;
                scan.setStartRow(Bytes.toBytes(startRow));
            }
            if (StrUtils.isValidStrVal(endtime)) {
                String endRow = mac + endtime;
                scan.setStopRow(Bytes.toBytes(endRow));
            }
        }else {
            List<Filter> filterList = new ArrayList<Filter>();
            if (StrUtils.isValidStrVal(starttime)){
                SingleColumnValueFilter startFilter = new SingleColumnValueFilter(
                    Bytes.toBytes("Time"),
                    Bytes.toBytes("svrCtime"),
                    CompareFilter.CompareOp.GREATER_OR_EQUAL,
                        Bytes.toBytes(starttime));
                startFilter.setFilterIfMissing(false); //如果不设置为 true，则那些不包含指定starttime的行也会返回
                filterList.add(startFilter);
            }
            if (StrUtils.isValidStrVal(endtime)){
                SingleColumnValueFilter endFilter = new SingleColumnValueFilter(
                        Bytes.toBytes("Time"),
                        Bytes.toBytes("svrCtime"),
                        CompareFilter.CompareOp.LESS_OR_EQUAL,
                        Bytes.toBytes(endtime));
                endFilter.setFilterIfMissing(false); //如果不设置为 true，则那些不包含指定endtime的行也会返回
                filterList.add(endFilter);
            }
            FilterList filter = new FilterList(filterList);
            scan.setFilter(filter);
        }

        if (StrUtils.isValidStrVal(mid)) {
            SingleColumnValueFilter scvFilter1 = new SingleColumnValueFilter(Bytes.toBytes("DevInfoRes"),
                    Bytes.toBytes("mid"), CompareFilter.CompareOp.EQUAL, Bytes.toBytes(mid));
            scvFilter1.setFilterIfMissing(true);
            scan.setFilter(scvFilter1);
            //filterList.addFilter(scvFilter1);
        }
        if (StrUtils.isValidStrVal(evt)) {
            SingleColumnValueFilter scvFilter2 = new SingleColumnValueFilter(Bytes.toBytes("DevInfoRes"),
                    Bytes.toBytes("evt"), CompareFilter.CompareOp.EQUAL, Bytes.toBytes(evt));
            scvFilter2.setFilterIfMissing(true);
            scan.setFilter(scvFilter2);
            //filterList.addFilter(scvFilter2);
        }
        ResultScanner resultScanner = dataAirCon.getScanner(scan);
        AirConData airConData = new AirConData();
        Result result = null;
        log.debug("正在执行，请等候！");
        while ((result = resultScanner.next()) != null) {
            airConData = getAirCon(result, airConData);
            hbase2Mysql(airConData);
        }
        log.debug("执行完毕！");
    }

    public void scanArrayAirCon(String mac, String starttime, String endtime, String mid, String evt) throws SQLException, NullPointerException,Exception {
        String tableNameStr = "GRIH:DataAirCon";
        //List<AirConData> airConDataList = new ArrayList<AirConData>();
        Connection connection = HbaseUtils.getConnection();
        TableName tableName = TableName.valueOf(tableNameStr);
        Table dataAirCon = connection.getTable(tableName);
        Scan scan = new Scan();
        scan.setCaching(1000);
        if (StrUtils.isValidStrVal(mac)) {
            if (StrUtils.isValidStrVal(starttime)) {
                String startRow = mac + DATEROWKEY.format(DATEE.parse(starttime));
                scan.setStartRow(Bytes.toBytes(startRow));
            }
            if (StrUtils.isValidStrVal(endtime)) {
                String endRow = mac + DATEROWKEY.format(DATEE.parse(endtime));
                scan.setStopRow(Bytes.toBytes(endRow));
            }
            if ((!StrUtils.isValidStrVal(starttime)) && (!StrUtils.isValidStrVal(endtime))) {
                SingleColumnValueFilter macFilter = new SingleColumnValueFilter(Bytes.toBytes("DevInfoRes"),
                        Bytes.toBytes("mac"), CompareFilter.CompareOp.EQUAL, Bytes.toBytes(mac));
                macFilter.setFilterIfMissing(true);
                scan.setFilter(macFilter);
            }
        }else {
            List<Filter> filterList = new ArrayList<Filter>();
            if (StrUtils.isValidStrVal(starttime)){
                SingleColumnValueFilter startFilter = new SingleColumnValueFilter(
                        Bytes.toBytes("Time"),
                        Bytes.toBytes("svrCtime"),
                        CompareFilter.CompareOp.GREATER_OR_EQUAL,
                        Bytes.toBytes(starttime));
                startFilter.setFilterIfMissing(false); //如果不设置为 true，则那些不包含指定starttime的行也会返回
                filterList.add(startFilter);
            }
            if (StrUtils.isValidStrVal(endtime)){
                SingleColumnValueFilter endFilter = new SingleColumnValueFilter(
                        Bytes.toBytes("Time"),
                        Bytes.toBytes("svrCtime"),
                        CompareFilter.CompareOp.LESS_OR_EQUAL,
                        Bytes.toBytes(endtime));
                endFilter.setFilterIfMissing(false); //如果不设置为 true，则那些不包含指定endtime的行也会返回
                filterList.add(endFilter);
            }
            FilterList filter = new FilterList(filterList);
            scan.setFilter(filter);
        }

        if (StrUtils.isValidStrVal(mid)) {
            SingleColumnValueFilter scvFilter1 = new SingleColumnValueFilter(Bytes.toBytes("DevInfoRes"),
                    Bytes.toBytes("mid"), CompareFilter.CompareOp.EQUAL, Bytes.toBytes(mid));
            scvFilter1.setFilterIfMissing(true);
            scan.setFilter(scvFilter1);
            //filterList.addFilter(scvFilter1);
        }

        if (StrUtils.isValidStrVal(evt)) {
            SingleColumnValueFilter scvFilter2 = new SingleColumnValueFilter(Bytes.toBytes("DevInfoRes"),
                    Bytes.toBytes("evt"), CompareFilter.CompareOp.EQUAL, Bytes.toBytes(evt));
            scvFilter2.setFilterIfMissing(true);
            scan.setFilter(scvFilter2);
            //filterList.addFilter(scvFilter2);
        }
//        filterList.addFilter(pageFilter);
//        scan.setFilter(filterList);
        ResultScanner resultScanner = dataAirCon.getScanner(scan);
        AirConData airConData = new AirConData();
        Result result = null;
        log.debug("正在执行，请等候！");
        while ((result = resultScanner.next()) != null) {
            airConData = getAirCon(result, airConData);
            hbase2Mysql(airConData);
        }
        log.debug("执行完毕！");
    }

    public void hbase2Mysql(AirConData airConData)throws NullPointerException,SQLException,Exception{
        AirCtlResp airCtlResp = new AirCtlResp();
        AirDevice airDevice = new AirDevice();
        AirInnerStatus airInnerStatus = new AirInnerStatus();
        AirInnerUnit airInnerUnit = new AirInnerUnit();
        AirOuterStatus airOuterStatus = new AirOuterStatus();
        AirOuterUnit airOuterUnit = new AirOuterUnit();
        if (!(airConData.airCon.CtlResponse ==null)){
            airCtlResp.setMac(airConData.mac);
            if (!(airConData.svrCtime.equals("0001-01-01 00:00:00"))){
            airCtlResp.setCtime(DATEE.parse(airConData.svrCtime));
            }
            //airCtlResp.setModtime(DATEE.parse(airConData.modTime));
            airCtlResp.setCobj(airConData.airCon.CtlResponse.cobj);
            airCtlResp.setCq(airConData.airCon.CtlResponse.cq);
            airCtlResp.setCsms(airConData.airCon.CtlResponse.csms);
            airCtlResp.setDdfs(airConData.airCon.CtlResponse.ddfs);
            airCtlResp.setDg(airConData.airCon.CtlResponse.dg);
            airCtlResp.setDnsz(airConData.airCon.CtlResponse.dnsz);
            airCtlResp.setJsq(airConData.airCon.CtlResponse.jsq);
            airCtlResp.setJsqtx(airConData.airCon.CtlResponse.jsqtx);
            airCtlResp.setHqzz(airConData.airCon.CtlResponse.hqzz);
            airCtlResp.setHqdy(airConData.airCon.CtlResponse.hqdy);
            airCtlResp.setJk(airConData.airCon.CtlResponse.jk);
            airCtlResp.setDm(airConData.airCon.CtlResponse.dm);
            airCtlResp.setQxjtx(airConData.airCon.CtlResponse.qxjtx);
            airCtlResp.setJcbfsqd(airConData.airCon.CtlResponse.jcbfsqd);
            airCtlResp.setWifikg(airConData.airCon.CtlResponse.wifikg);
            airCtlResp.setKqzlpjdw(airConData.airCon.CtlResponse.kqzlpjdw);
            airCtlResp.setPm25dw(airConData.airCon.CtlResponse.pm25dw);
            airCtlResp.setKgj(airConData.airCon.CtlResponse.kgj);
            airCtlResp.setMs(airConData.airCon.CtlResponse.ms);
            airCtlResp.setSleep(airConData.airCon.CtlResponse.sleep);
            airCtlResp.setSf(airConData.airCon.CtlResponse.sf);
            airCtlResp.setFs(airConData.airCon.CtlResponse.fs);
            airCtlResp.setWd(airConData.airCon.CtlResponse.wd);
            airCtlResp.setDs(airConData.airCon.CtlResponse.ds);
            airCtlResp.setDssj(airConData.airCon.CtlResponse.dssj);
            airCtlResp.setGz(airConData.airCon.CtlResponse.gz);
            airCtlResp.setFr(airConData.airCon.CtlResponse.fr);
            airCtlResp.setWddw(airConData.airCon.CtlResponse.wddw);
            airCtlResp.setHq(airConData.airCon.CtlResponse.hq);
            airCtlResp.setLsljydlfwql(airConData.airCon.CtlResponse.lsljydlfwql);
            airCtlResp.setZdqj(airConData.airCon.CtlResponse.zdqj);
            airCtlResp.setSxsf(airConData.airCon.CtlResponse.sxsf);
            airCtlResp.setZysf(airConData.airCon.CtlResponse.zysf);
            airCtlResp.setFm(airConData.airCon.CtlResponse.fm);
            airCtlResp.setWdxsms(airConData.airCon.CtlResponse.wdxsms);
            airCtlResp.setQcts(airConData.airCon.CtlResponse.qcts);
            airCtlResp.setYkj(airConData.airCon.CtlResponse.ykj);
            airCtlResp.setSdxs(airConData.airCon.CtlResponse.sdxs);
            airCtlResp.setSdsd(airConData.airCon.CtlResponse.sdsd);
            airCtlResp.setSaver(airConData.airCon.CtlResponse.saver);
            airCtlResp.setGea(airConData.airCon.CtlResponse.gea);
            airCtlResp.setZdqx(airConData.airCon.CtlResponse.zdqx);
            airCtlResp.setHf(airConData.airCon.CtlResponse.hf);
            airCtlResp.setHfms(airConData.airCon.CtlResponse.hfms);
            airCtlResp.setHsh1(airConData.airCon.CtlResponse.hsh1);
            //airCtlResp.setGlwqxtx(airConData.airCon.CtlResponse.glwqxtx);   不存在
            airCtlResp.setSb(airConData.airCon.CtlResponse.sb);
            airCtlResp.setSm(airConData.airCon.CtlResponse.sm);
            airCtlResp.setFcktled(airConData.airCon.CtlResponse.fcktled);
            airCtlResp.setDsxhcs(airConData.airCon.CtlResponse.dsxhcs);
            airCtlResp.setQz(airConData.airCon.CtlResponse.qz);
            airCtlResp.setDsk(airConData.airCon.CtlResponse.dsk);
            airCtlResp.setDsg(airConData.airCon.CtlResponse.dsg);
            airCtlResp.setTxsb(airConData.airCon.CtlResponse.txsb);
            airCtlResp.setSleepmode(airConData.airCon.CtlResponse.sleepMode);
            airCtlResp.setMute(airConData.airCon.CtlResponse.mute);
            airCtlResp.setQyxz(airConData.airCon.CtlResponse.qyxz);
            airCtlResp.setSm1hhwd(airConData.airCon.CtlResponse.sm1hhwd);
            airCtlResp.setSm2hhwd(airConData.airCon.CtlResponse.sm2hhwd);
            airCtlResp.setHj(airConData.airCon.CtlResponse.hj);
            airCtlResp.setF5(airConData.airCon.CtlResponse.f5);
            airCtlResp.setEx(airConData.airCon.CtlResponse.ex);
            airCtlResp.setSm3hhwd(airConData.airCon.CtlResponse.sm3hhwd);
            airCtlResp.setSm4hhwd(airConData.airCon.CtlResponse.sm4hhwd);
            airCtlResp.setSm5hhwd(airConData.airCon.CtlResponse.sm5hhwd);
            airCtlResp.setSm6hhwd(airConData.airCon.CtlResponse.sm6hhwd);
            airCtlResp.setSm7hhwd(airConData.airCon.CtlResponse.sm7hhwd);
            airCtlResp.setSm8hhwd(airConData.airCon.CtlResponse.sm8hhwd);
            airCtlResp.setStf(airConData.airCon.CtlResponse.stf);
            airCtlResp.setSxsffk(airConData.airCon.CtlResponse.sxsffk);
            airCtlResp.setHx(airConData.airCon.CtlResponse.hx);
            airCtlResp.setZnhq(airConData.airCon.CtlResponse.znhq);
            airCtlResp.setYhsdyddw(airConData.airCon.CtlResponse.yhsdyddw);
            airCtlResp.setJzcr(airConData.airCon.CtlResponse.jzcr);
            airCtlResp.setHjqy9(airConData.airCon.CtlResponse.hjqy9);
            airCtlResp.setHjqy8(airConData.airCon.CtlResponse.hjqy8);
            airCtlResp.setHjqy7(airConData.airCon.CtlResponse.hjqy7);
            airCtlResp.setHjqy6(airConData.airCon.CtlResponse.hjqy6);
            airCtlResp.setHjqy5(airConData.airCon.CtlResponse.hjqy5);
            airCtlResp.setHjqy4(airConData.airCon.CtlResponse.hjqy4);
            airCtlResp.setHjqy3(airConData.airCon.CtlResponse.hjqy3);
            airCtlResp.setHjqy2(airConData.airCon.CtlResponse.hjqy2);
            airCtlResp.setHjqy1(airConData.airCon.CtlResponse.hjqy1);
            airCtlResp.setHwyk(airConData.airCon.CtlResponse.hwyk);
            airCtlResp.setFault(airConData.airCon.CtlResponse.fault);
            airCtlResp.setZkfs(airConData.airCon.CtlResponse.zkfs);
            airCtlResp.setHjms(airConData.airCon.CtlResponse.hjms);
            airCtlResp.setXcfksxsf(airConData.airCon.CtlResponse.xcfksxsf);
            airCtlResp.setXcfkzczysf(airConData.airCon.CtlResponse.xcfkzczysf);
            airCtlResp.setXcfkyczysf(airConData.airCon.CtlResponse.xcfkyczysf);
            airCtlResp.setSnhjwd(airConData.airCon.CtlResponse.snhjwd);
            airCtlResp.setHjsdz(airConData.airCon.CtlResponse.hjsdz);
            airCtlResp.setSwhjwd(airConData.airCon.CtlResponse.swhjwd);
            airCtlRespRepository.save(airCtlResp);
        }
        if (!(airConData == null)){
            airDevice.setIp(airConData.ip);
            airDevice.setMac(airConData.mac);
            airDevice.setMid(airConData.mid);
            airDevice.setCountry(airConData.country);
            airDevice.setProvince(airConData.province);
            airDevice.setCity(airConData.city);
            airDevice.setCodes(airConData.airCon.codes);
            airDevice.setEvt(airConData.evt);
            if (airDeviceRepository.existsByMac(airConData.mac)){
                airDeviceRepository.flush();
            }
            else {
                airDeviceRepository.save(airDevice);
            }
        }
        if (!(airConData.airCon.InStatusFault.kgjzt == null)) {
            airInnerStatus.setMac(airConData.mac);
            //airInnerStatus.setDatatime(DATEE.parse(airConData.svrCtime));
            if (!(airConData.svrCtime.equals("0001-01-01 00:00:00"))){
            airInnerStatus.setCtime(DATEE.parse(airConData.svrCtime));
            }
            airInnerStatus.setKgjzt(airConData.airCon.InStatusFault.kgjzt);
            airInnerStatus.setYxms(airConData.airCon.InStatusFault.yxms);
            airInnerStatus.setNfjzs(airConData.airCon.InStatusFault.nfjzs);
            //airInnerStatus.setSnhjwdxsbz(airConData.airCon.InStatusFault.snhjwdxsbz); 不存在
            //airInnerStatus.setSnzfqzjwdxsbz(airConData.airCon.InStatusFault.snzfqzjwdxsbz);不存在
            //airInnerStatus.setSnsdwdxsbz(airConData.airCon.InStatusFault.snsdwdxsbz);不存在
            airInnerStatus.setSnsdwd(airConData.airCon.InStatusFault.snsdwd);
            airInnerStatus.setSnhjwd(airConData.airCon.InStatusFault.snhjwd);
            airInnerStatus.setSyx(airConData.airCon.InStatusFault.syx);
            airInnerStatus.setSfms(airConData.airCon.InStatusFault.sfms);
            airInnerStatus.setQzcs(airConData.airCon.InStatusFault.qzcs);
            airInnerStatus.setQzzr(airConData.airCon.InStatusFault.qzzr);
            airInnerStatus.setQzzl(airConData.airCon.InStatusFault.qzzl);
            airInnerStatus.setSnzfqzjwd(airConData.airCon.InStatusFault.snzfqzjwd);
            airInnerStatus.setBcdwdz(airConData.airCon.InStatusFault.bcdwdz);
            airInnerStatus.setSfkzwjzrwdbc(airConData.airCon.InStatusFault.sfkzwjzrwdbc);
            airInnerStatus.setSnhjsd(airConData.airCon.InStatusFault.snhjsd);
            airInnerStatus.setJszt(airConData.airCon.InStatusFault.jszt);
            airInnerStatus.setSavezt(airConData.airCon.InStatusFault.savezt);
            airInnerStatus.setHqzt(airConData.airCon.InStatusFault.hqzt);
            airInnerStatus.setJyzt(airConData.airCon.InStatusFault.jyzt);
            airInnerStatus.setFlfzt(airConData.airCon.InStatusFault.flfzt);
            airInnerStatus.setFnlzt(airConData.airCon.InStatusFault.fnlzt);
            airInnerStatus.setDfrzt(airConData.airCon.InStatusFault.dfrzt);
            airInnerStatus.setKczt(airConData.airCon.InStatusFault.kczt);
            airInnerStatus.setYsjzssdz(airConData.airCon.InStatusFault.ysjzssdz);
            airInnerStatus.setDzpzfkdsdyxzt(airConData.airCon.InStatusFault.dzpzfkdsdyxzt);
            airInnerStatus.setDzpzfkdsdz(airConData.airCon.InStatusFault.dzpzfkdsdz);
            airInnerStatus.setWsfjsdyxzt(airConData.airCon.InStatusFault.wsfjsdyxzt);
            airInnerStatus.setMbpqwdsdyxzt(airConData.airCon.InStatusFault.mbpqwdsdyxzt);
            airInnerStatus.setMbgrdsdyxzt(airConData.airCon.InStatusFault.mbgrdsdyxzt);
            airInnerStatus.setSwtjyqbz(airConData.airCon.InStatusFault.swtjyqbz);
            airInnerStatus.setSnqtcgqgz(airConData.airCon.InStatusFault.snqtcgqgz);
            airInnerStatus.setSnswcgqgz(airConData.airCon.InStatusFault.snswcgqgz);
            airInnerStatus.setSnsdcgqgz(airConData.airCon.InStatusFault.snsdcgqgz);
            airInnerStatus.setSnhjgwbgz(airConData.airCon.InStatusFault.snhjgwbgz);
            airInnerStatus.setSnzfqzjgwbgz(airConData.airCon.InStatusFault.snzfqzjgwbgz);
            airInnerStatus.setNjzbyscqtxgz(airConData.airCon.InStatusFault.njzbyscqtxgz);
            airInnerStatus.setSnjsmbh(airConData.airCon.InStatusFault.snjsmbh);
            airInnerStatus.setZdajds(airConData.airCon.InStatusFault.zdajds);
            airInnerStatus.setHdmgz(airConData.airCon.InStatusFault.hdmgz);
            airInnerStatus.setXzkdpyc(airConData.airCon.InStatusFault.xzkdpyc);
            airInnerStatus.setTxmgz(airConData.airCon.InStatusFault.txmgz);
            airInnerStatus.setJyxpdxgz(airConData.airCon.InStatusFault.jyxpdxgz);
            airInnerStatus.setSnfjgz(airConData.airCon.InStatusFault.snfjgz);
            airInnerStatus.setQfpbxz(airConData.airCon.InStatusFault.qfpbxz);
            airInnerStatus.setCsfsxz(airConData.airCon.InStatusFault.csfsxz);
            airInnerStatus.setWkjdxz(airConData.airCon.InStatusFault.wkjdxz);
            airInnerStatus.setZysfzt(airConData.airCon.InStatusFault.zysfzt);
            airInnerStatus.setSxsfzt(airConData.airCon.InStatusFault.sxsfzt);
            airInnerStatus.setRszl(airConData.airCon.InStatusFault.rszl);
            airInnerStatus.setDredmsyxzt(airConData.airCon.InStatusFault.dredmsyxzt);
            airInnerStatus.setSpmkrfgz(airConData.airCon.InStatusFault.spmkrfgz);
            airInnerStatus.setJcbtxjfgz(airConData.airCon.InStatusFault.jcbtxjfgz);
            airInnerStatus.setNjwdywjgzoe(airConData.airCon.InStatusFault.njwdywjgzOe);
            airInnerStatus.setSfjggzfc(airConData.airCon.InStatusFault.sfjggzfc);
            airInnerStatus.setWifigzgm(airConData.airCon.InStatusFault.wifigzgm);
            airInnerStatus.setNwjtxgz(airConData.airCon.InStatusFault.nwjtxgz);
            airInnerStatus.setDjdzbh(airConData.airCon.InStatusFault.djdzbh);
            airInnerStatus.setXsbhqdbgz(airConData.airCon.InStatusFault.xsbhqdbgz);
            airInnerStatus.setDbpxsbyc(airConData.airCon.InStatusFault.dbpxsbyc);
            airInnerStatus.setNjglxhgz(airConData.airCon.InStatusFault.njglxhgz);
            airInnerStatusRepository.save(airInnerStatus);
        }
        if (!(airConData.airCon.InOutStatus.in.txbb == null)){
            airInnerUnit.setMac(airConData.mac);
            //airInnerUnit.setDatatime(DATEE.parse(airConData.svrCtime));
            if (!(airConData.svrCtime.equals("0001-01-01 00:00:00"))){
            airInnerUnit.setCtime(DATEE.parse(airConData.svrCtime));
            }
            airInnerUnit.setTxbb(airConData.airCon.InOutStatus.in.txbb);
            airInnerUnit.setTxsd(airConData.airCon.InOutStatus.in.txsd);
            airInnerUnit.setLnjx(airConData.airCon.InOutStatus.in.lnjx);
            airInnerUnit.setDpbp(airConData.airCon.InOutStatus.in.dpbp);
            airInnerUnit.setNjnldm(airConData.airCon.InOutStatus.in.njnldm);
            airInnerUnit.setDypl(airConData.airCon.InOutStatus.in.dypl);
            airInnerUnit.setGdfs(airConData.airCon.InOutStatus.in.gdfs);
            airInnerUnit.setDyzl(airConData.airCon.InOutStatus.in.dyzl);
            airInnerUnit.setLmzl(airConData.airCon.InOutStatus.in.lmzl);
            airInnerUnit.setLxdm(airConData.airCon.InOutStatus.in.lxdm);
            airInnerUnit.setHjgwb(airConData.airCon.InOutStatus.in.hjgwb);
            airInnerUnit.setNgzjgwb(airConData.airCon.InOutStatus.in.ngzjgwb);
            airInnerUnit.setSdcgq(airConData.airCon.InOutStatus.in.sdcgq);
            airInnerUnit.setFjzl(airConData.airCon.InOutStatus.in.fjzl);
            airInnerUnit.setFjds(airConData.airCon.InOutStatus.in.fjds);
            airInnerUnit.setJyxphqsjbz(airConData.airCon.InOutStatus.in.jyxphqsjbz);
            airInnerUnit.setJdccgn(airConData.airCon.InOutStatus.in.jdccgn);
            airInnerUnit.setFrgn(airConData.airCon.InOutStatus.in.frgn);
            airInnerUnit.setJygn(airConData.airCon.InOutStatus.in.jygn);
            airInnerUnit.setJkgn(airConData.airCon.InOutStatus.in.jkgn);
            airInnerUnit.setHqgn(airConData.airCon.InOutStatus.in.hqgn);
            airInnerUnit.setSsggn(airConData.airCon.InOutStatus.in.ssggn);
            airInnerUnit.setDsfxxz(airConData.airCon.InOutStatus.in.dpbp);
            airInnerUnit.setTxmh(airConData.airCon.InOutStatus.in.txmh);
            airInnerUnit.setJx(airConData.airCon.InOutStatus.in.jx);
            airInnerUnitRepository.save(airInnerUnit);
        }
        if (!(airConData.airCon.InOutStatus.out.txbb == null)){
            airOuterUnit.setMac(airConData.mac);
            //airOuterUnit.setDatatime(DATEE.parse(airConData.svrCtime));
            if (!(airConData.svrCtime.equals("0001-01-01 00:00:00"))){
            airOuterUnit.setCtime(DATEE.parse(airConData.svrCtime));
            }
            airOuterUnit.setTxbb(airConData.airCon.InOutStatus.out.txbb);
            airOuterUnit.setTxsd(airConData.airCon.InOutStatus.out.txsd);
            airOuterUnit.setLnjx(airConData.airCon.InOutStatus.out.lnjx);
            airOuterUnit.setBpdp(airConData.airCon.InOutStatus.out.bpdp);
            airOuterUnit.setWjnldm(airConData.airCon.InOutStatus.out.wjnldm);
            airOuterUnit.setGdfs(airConData.airCon.InOutStatus.out.gdfs);
            airOuterUnit.setDyzl(airConData.airCon.InOutStatus.out.dyzl);
            airOuterUnit.setSwjlx(airConData.airCon.InOutStatus.out.swjlx);
            airOuterUnit.setYsjxh(airConData.airCon.InOutStatus.out.ysjxh);
            airOuterUnit.setCflx(airConData.airCon.InOutStatus.out.cflx);
            airOuterUnit.setFjzl(airConData.airCon.InOutStatus.out.fjzl);
            airOuterUnit.setFjgs(airConData.airCon.InOutStatus.out.fjgs);
            airOuterUnit.setFjds(airConData.airCon.InOutStatus.out.fjds);
            airOuterUnit.setDplnqyrdyw(airConData.airCon.InOutStatus.out.dplnqyrdyw);
            airOuterUnit.setYsjyrdyw(airConData.airCon.InOutStatus.out.ysjyrdyw);
            airOuterUnit.setXqzyryw(airConData.airCon.InOutStatus.out.xqzyryw);
            airOuterUnit.setDzpzfyw(airConData.airCon.InOutStatus.out.dzpzfyw);
            airOuterUnit.setJxm(airConData.airCon.InOutStatus.out.jxm);
            airOuterUnit.setWfjipmmk(airConData.airCon.InOutStatus.out.wfjipmmk);
            airOuterUnit.setYsjipmmk(airConData.airCon.InOutStatus.out.ysjipmmk);
            airOuterUnit.setNjcxbbh(airConData.airCon.InOutStatus.out.njcxbbh);
            airOuterUnit.setWjcxbbh(airConData.airCon.InOutStatus.out.wjcxbbh);
            airOuterUnitRepository.save(airOuterUnit);
        }
        if (!(airConData.airCon.OutStatusFault.hsms == null)){
            airOuterStatus.setMac(airConData.mac);
            //airOuterStatus.setDatatime(DATEE.parse(airConData.svrCtime));
            if (!(airConData.svrCtime.equals("0001-01-01 00:00:00"))){
            airOuterStatus.setCtime(DATEE.parse(airConData.svrCtime));
            }
            airOuterStatus.setYsjkgzt(airConData.airCon.OutStatusFault.ysjkgzt);
            airOuterStatus.setHsms(airConData.airCon.OutStatusFault.hsms);
            airOuterStatus.setYsjyxzs(airConData.airCon.OutStatusFault.ysjyxzs);
            airOuterStatus.setWfj1zs(airConData.airCon.OutStatusFault.wfj1zs);
            airOuterStatus.setWfj2zs(airConData.airCon.OutStatusFault.wfj2zs);
            airOuterStatus.setYsjyxgl(airConData.airCon.OutStatusFault.ysjyxgl);
            airOuterStatus.setDzpzfkd(airConData.airCon.OutStatusFault.dzpzfkd);
            airOuterStatus.setZlmxdy(airConData.airCon.OutStatusFault.zlmxdy);
            //airOuterStatus.setSwpqwdxsbz(airConData.airCon.OutStatusFault.swpqwdxsbz);不存在
            //airOuterStatus.setSwlnqwdxsbz(airConData.airCon.OutStatusFault.swlnqwdxsbz);不存在
            //airOuterStatus.setSwlhjwdxsbz(airConData.airCon.OutStatusFault.swlhjwdxsbz);不存在
            airOuterStatus.setSwhjwd(airConData.airCon.OutStatusFault.swhjwd);
            airOuterStatus.setSavezt(airConData.airCon.OutStatusFault.savezt);
            airOuterStatus.setWjfnlzt(airConData.airCon.OutStatusFault.wjfnlzt);
            airOuterStatus.setWjjyzt(airConData.airCon.OutStatusFault.wjjyzt);
            airOuterStatus.setDzpzfzt(airConData.airCon.OutStatusFault.dzpzfzt);
            airOuterStatus.setSfzdjzt(airConData.airCon.OutStatusFault.sfzdjzt);
            airOuterStatus.setHyzt(airConData.airCon.OutStatusFault.hyzt);
            airOuterStatus.setPtcszt(airConData.airCon.OutStatusFault.ptcszt);
            airOuterStatus.setCsyq(airConData.airCon.OutStatusFault.csyq);
            airOuterStatus.setTscszt(airConData.airCon.OutStatusFault.tscszt);
            airOuterStatus.setWjrytjgz(airConData.airCon.OutStatusFault.wjrytjgz);
            airOuterStatus.setHszczt(airConData.airCon.OutStatusFault.hszczt);
            airOuterStatus.setWjacdlz(airConData.airCon.OutStatusFault.wjacdlz);
            airOuterStatus.setSyx(airConData.airCon.OutStatusFault.syx);
            airOuterStatus.setSfms(airConData.airCon.OutStatusFault.sfms);
            airOuterStatus.setQzcs(airConData.airCon.OutStatusFault.qzcs);
            airOuterStatus.setQzzr(airConData.airCon.OutStatusFault.qzzr);
            airOuterStatus.setQzzl(airConData.airCon.OutStatusFault.qzzl);
            airOuterStatus.setDredmsyxzt(airConData.airCon.OutStatusFault.dredmsyxzt);
            airOuterStatus.setGlggbhxjp(airConData.airCon.OutStatusFault.glggbhxjp);
            airOuterStatus.setMkdlbhxjp(airConData.airCon.OutStatusFault.mkdlbhxjp);
            airOuterStatus.setMkwdbhxjp(airConData.airCon.OutStatusFault.mkwdbhxjp);
            airOuterStatus.setZlmxdybhxjp(airConData.airCon.OutStatusFault.zlmxdybhxjp);
            airOuterStatus.setGhbhxjp(airConData.airCon.OutStatusFault.ghbhxjp);
            airOuterStatus.setFjdbhxjp(airConData.airCon.OutStatusFault.fjdbhxjp);
            airOuterStatus.setPqbhxjp(airConData.airCon.OutStatusFault.pqbhxjp);
            airOuterStatus.setWjacdlbhxjp(airConData.airCon.OutStatusFault.wjacdlbhxjp);
            airOuterStatus.setGzgwbgz(airConData.airCon.OutStatusFault.gzgwbgz);
            airOuterStatus.setPqgwbgz(airConData.airCon.OutStatusFault.pqgwbgz);
            airOuterStatus.setHjgwbgz(airConData.airCon.OutStatusFault.hjgwbgz);
            airOuterStatus.setSwlnqzjgwbgz(airConData.airCon.OutStatusFault.swlnqzjgwbgz);
            airOuterStatus.setMkgwbdlgz(airConData.airCon.OutStatusFault.mkgwbdlgz);
            airOuterStatus.setYsjrgzbh(airConData.airCon.OutStatusFault.ysjrgzbh);
            airOuterStatus.setPqbh(airConData.airCon.OutStatusFault.pqbh);
            airOuterStatus.setGfhbh(airConData.airCon.OutStatusFault.gfhbh);
            airOuterStatus.setWjacdlbh(airConData.airCon.OutStatusFault.wjacdlbh);
            airOuterStatus.setMkdlfobh(airConData.airCon.OutStatusFault.mkdlfobh);
            airOuterStatus.setMkwdbh(airConData.airCon.OutStatusFault.mkwdbh);
            airOuterStatus.setFjdbh(airConData.airCon.OutStatusFault.fjdbh);
            airOuterStatus.setGlggbh(airConData.airCon.OutStatusFault.glggbh);
            airOuterStatus.setYsjqnxbhqxttqx(airConData.airCon.OutStatusFault.ysjqnxbhqxttqx);
            airOuterStatus.setPfcglgz(airConData.airCon.OutStatusFault.pfcglgz);
            airOuterStatus.setZlmxdyggbh(airConData.airCon.OutStatusFault.zlmxdyggbh);
            airOuterStatus.setZlmxdygdbh(airConData.airCon.OutStatusFault.zlmxdygdbh);
            airOuterStatus.setQfbh(airConData.airCon.OutStatusFault.qfbh);
            airOuterStatus.setMsct(airConData.airCon.OutStatusFault.msct);
            airOuterStatus.setSnwjxbpp(airConData.airCon.OutStatusFault.snwjxbpp);
            airOuterStatus.setYtdnwjglljytxljbpp(airConData.airCon.OutStatusFault.ytdnwjglljytxljbpp);
            airOuterStatus.setJyxpdxgz(airConData.airCon.OutStatusFault.jyxpdxgz);
            airOuterStatus.setGlxhyc(airConData.airCon.OutStatusFault.glxhyc);
            airOuterStatus.setStfhxyc(airConData.airCon.OutStatusFault.stfhxyc);
            airOuterStatus.setXzkdpyc(airConData.airCon.OutStatusFault.xzkdpyc);
            airOuterStatus.setSwfj2gz(airConData.airCon.OutStatusFault.swfj2gz);
            airOuterStatus.setSwfj1gz(airConData.airCon.OutStatusFault.swfj1gz);
            airOuterStatus.setGwbhswfj(airConData.airCon.OutStatusFault.gwbhswfj);
            airOuterStatus.setXtdybh(airConData.airCon.OutStatusFault.xtdybh);
            airOuterStatus.setXtgybh(airConData.airCon.OutStatusFault.xtgybh);
            airOuterStatus.setZlmxdydlgz(airConData.airCon.OutStatusFault.zlmxdydlgz);
            airOuterStatus.setZjdljcgz(airConData.airCon.OutStatusFault.zjdljcgz);
            airOuterStatus.setDrcdgz(airConData.airCon.OutStatusFault.drcdgz);
            airOuterStatus.setYsjxdldljcgz(airConData.airCon.OutStatusFault.ysjxdldljcgz);
            airOuterStatus.setYsjsb(airConData.airCon.OutStatusFault.ysjsb);
            airOuterStatus.setYsjtcbh(airConData.airCon.OutStatusFault.ysjtcbh);
            airOuterStatus.setYsjdz(airConData.airCon.OutStatusFault.ysjdz);
            airOuterStatus.setQdsb(airConData.airCon.OutStatusFault.qdsb);
            airOuterStatus.setQdmkfw(airConData.airCon.OutStatusFault.qdmkfw);
            airOuterStatus.setSc(airConData.airCon.OutStatusFault.sc);
            airOuterStatus.setYsjbmyc(airConData.airCon.OutStatusFault.ysjbmyc);
            airOuterStatus.setQdbhjgwbgz(airConData.airCon.OutStatusFault.qdbhjgwbgz);
            airOuterStatus.setJljcqbh(airConData.airCon.OutStatusFault.jljcqbh);
            airOuterStatus.setWpbh(airConData.airCon.OutStatusFault.wpbh);
            airOuterStatus.setCgqljbh(airConData.airCon.OutStatusFault.cgqljbh);
            airOuterStatus.setQdbtxgz(airConData.airCon.OutStatusFault.qdbtxgz);
            airOuterStatus.setYsjxdlgl(airConData.airCon.OutStatusFault.ysjxdlgl);
            airOuterStatus.setJlsrdyyc(airConData.airCon.OutStatusFault.jlsrdyyc);
            airOuterStatus.setFjtsbtxgz(airConData.airCon.OutStatusFault.fjtsbtxgz);
            airOuterStatus.setYfgwbgz(airConData.airCon.OutStatusFault.yfgwbgz);
            airOuterStatus.setQfgwbgz(airConData.airCon.OutStatusFault.qfgwbgz);
            airOuterStatus.setSwlnqrggwbgz(airConData.airCon.OutStatusFault.swlnqrggwbgz);
            airOuterStatus.setSwlnqcggwbgz(airConData.airCon.OutStatusFault.swlnqcggwbgz);
            airOuterStatus.setLmwdgwbgz(airConData.airCon.OutStatusFault.lmwdgwbgz);
            airOuterStatus.setSwjlmjrqsxgz(airConData.airCon.OutStatusFault.swjlmjrqsxgz);
            airOuterStatus.setSwjlmjrqjdqzlgz(airConData.airCon.OutStatusFault.swjlmjrqjdqzlgz);
            airOuterStatus.setIpmmkwd(airConData.airCon.OutStatusFault.ipmmkwd);
            airOuterStatus.setDyztw(airConData.airCon.OutStatusFault.dyztw);
            airOuterStatus.setDlztw(airConData.airCon.OutStatusFault.dlztw);
            airOuterStatus.setYfgwbwd(airConData.airCon.OutStatusFault.yfgwbwd);
            airOuterStatus.setQfgwbwd(airConData.airCon.OutStatusFault.qfgwbwd);
            airOuterStatus.setSwlnqrgwd(airConData.airCon.OutStatusFault.swlnqrgwd);
            airOuterStatus.setSwlnqcgwd(airConData.airCon.OutStatusFault.swlnqcgwd);
            airOuterStatus.setYsjpqgwbh(airConData.airCon.OutStatusFault.ysjpqgwbh);
            airOuterStatus.setJlglbh(airConData.airCon.OutStatusFault.jlglbh);
            airOuterStatus.setGgwbh(airConData.airCon.OutStatusFault.ggwbh);
            airOuterStatus.setDeepromgz(airConData.airCon.OutStatusFault.deepromgz);
            airOuterStatus.setWhwdyc(airConData.airCon.OutStatusFault.whwdyc);
            airOuterStatus.setZrggwjp(airConData.airCon.OutStatusFault.zrggwjp);
            airOuterStatus.setXtyc(airConData.airCon.OutStatusFault.xtyc);
            airOuterStatusRepository.save(airOuterStatus);
        }
    }

    public static AirConData getAirCon(Result result,AirConData airConData)throws Exception{
        try {
            //1.DevInfoRes
            byte[] ipBytes = result.getValue(Bytes.toBytes("DevInfoRes"),Bytes.toBytes("ip"));
            if(ipBytes!=null){
                airConData.ip =new String(ipBytes,"UTF-8");
            }

            byte[] countryBytes = result.getValue(Bytes.toBytes("DevInfoRes"),Bytes.toBytes("country"));
            if(countryBytes!=null){
                airConData.country=new String(countryBytes,"UTF-8");
            }

            byte[] provinceBytes = result.getValue(Bytes.toBytes("DevInfoRes"),Bytes.toBytes("province"));
            if(provinceBytes!=null){
                airConData.province=new String(countryBytes,"UTF-8");
            }

            byte[] cityBytes = result.getValue(Bytes.toBytes("DevInfoRes"),Bytes.toBytes("city"));
            if(cityBytes!=null){
                airConData.city=new String(cityBytes,"UTF-8");
            }

            byte[] macBytes = result.getValue(Bytes.toBytes("DevInfoRes"), Bytes.toBytes("mac"));
            if (macBytes!=null){
                airConData.mac = new String(macBytes,"UTF-8");
            }

            byte[] midBytes = result.getValue(Bytes.toBytes("DevInfoRes"), Bytes.toBytes("mid"));
            if (midBytes!=null){
                airConData.mid = new String(midBytes,"UTF-8");
            }

            byte[] rowBytes = result.getValue(Bytes.toBytes("DevInfoRes"), Bytes.toBytes("rowKey"));
            if (rowBytes!=null){
                airConData.rowKey = new String(rowBytes,"UTF-8");
            }

            byte[] modTimeBytes = result.getValue(Bytes.toBytes("Time"), Bytes.toBytes("modTime"));
            if (modTimeBytes!=null){
                airConData.modTime = TimeZoneTool.formatTimeAfterEight(new String(modTimeBytes,"UTF-8"));
            }

            byte[] svrCtimeBytes = result.getValue(Bytes.toBytes("Time"), Bytes.toBytes("svrCtime"));
            if (svrCtimeBytes!=null){
                airConData.svrCtime = TimeZoneTool.formatTimeAfterEight(new String(svrCtimeBytes,"UTF-8"));
            }

            byte[] insTimeBytes = result.getValue(Bytes.toBytes("Time"), Bytes.toBytes("insTime"));
            if (insTimeBytes!=null){
                airConData.insTime = TimeZoneTool.formatTimeAfterEight(new String(insTimeBytes,"UTF-8"));
            }

            byte[] evtBytes = result.getValue(Bytes.toBytes("DevInfoRes"), Bytes.toBytes("evt"));
            if (evtBytes!=null){
                airConData.evt = new String(evtBytes,"UTF-8");
            }
//            airConData.mid=new String(result.getValue(Bytes.toBytes("DevInfoRes"),Bytes.toBytes("mid")),"UTF-8");
//            airConData.rowKey=new String(result.getValue(Bytes.toBytes("DevInfoRes"),Bytes.toBytes("rowKey")),"UTF-8");
            //airConData.modTime=new String(result.getValue(Bytes.toBytes("Time"),Bytes.toBytes("modTime")),"UTF-8");
//            airConData.svrCtime=new String(result.getValue(Bytes.toBytes("Time"),Bytes.toBytes("svrCtime")),"UTF-8");
//            airConData.insTime =new String(result.getValue(Bytes.toBytes("Time"),Bytes.toBytes("insTime")),"UTF-8");
//            airConData.evt=new String(result.getValue(Bytes.toBytes("DevInfoRes"),Bytes.toBytes("evt")),"UTF-8");

            airConData.airCon = new AirConData.AirCon();
            byte[] codeBytes = result.getValue(Bytes.toBytes("DevInfoRes"),Bytes.toBytes("codes"));
            if(codeBytes!=null) {
                airConData.airCon.codes = new String(codeBytes, "UTF-8");
            }
            byte[] origin;
            //2.CtlResponse
            airConData.airCon.CtlResponse = new AirConData.AirCon.CtlResponse();
            origin = result.getValue(Bytes.toBytes("CtlResponse"),Bytes.toBytes("origin"));
            if(origin!=null && origin.length>0){
                airConData.airCon.CtlResponse.jsq=new String(result.getValue(Bytes.toBytes("CtlResponse"),Bytes.toBytes("jsq")),"UTF-8");
                airConData.airCon.CtlResponse.jsqtx=new String(result.getValue(Bytes.toBytes("CtlResponse"),Bytes.toBytes("jsqtx")),"UTF-8");
                airConData.airCon.CtlResponse.hqzz=new String(result.getValue(Bytes.toBytes("CtlResponse"),Bytes.toBytes("hqzz")),"UTF-8");
                airConData.airCon.CtlResponse.hqdy=new String(result.getValue(Bytes.toBytes("CtlResponse"),Bytes.toBytes("hqdy")),"UTF-8");
                airConData.airCon.CtlResponse.jk=new String(result.getValue(Bytes.toBytes("CtlResponse"),Bytes.toBytes("jk")),"UTF-8");
                airConData.airCon.CtlResponse.dm=new String(result.getValue(Bytes.toBytes("CtlResponse"),Bytes.toBytes("dm")),"UTF-8");
                airConData.airCon.CtlResponse.qxjtx=new String(result.getValue(Bytes.toBytes("CtlResponse"),Bytes.toBytes("qxjtx")),"UTF-8");
                airConData.airCon.CtlResponse.jcbfsqd=new String(result.getValue(Bytes.toBytes("CtlResponse"),Bytes.toBytes("jcbfsqd")),"UTF-8");
                airConData.airCon.CtlResponse.wifikg=new String(result.getValue(Bytes.toBytes("CtlResponse"),Bytes.toBytes("wifikg")),"UTF-8");
                airConData.airCon.CtlResponse.wifihfcc=new String(result.getValue(Bytes.toBytes("CtlResponse"),Bytes.toBytes("wifihfcc")),"UTF-8");
                airConData.airCon.CtlResponse.kqzlpjdw=new String(result.getValue(Bytes.toBytes("CtlResponse"),Bytes.toBytes("kqzlpjdw")),"UTF-8");
                airConData.airCon.CtlResponse.pm25dw=new String(result.getValue(Bytes.toBytes("CtlResponse"),Bytes.toBytes("pm25dw")),"UTF-8");
                airConData.airCon.CtlResponse.kgj=new String(result.getValue(Bytes.toBytes("CtlResponse"),Bytes.toBytes("kgj")),"UTF-8");
                airConData.airCon.CtlResponse.ms=new String(result.getValue(Bytes.toBytes("CtlResponse"),Bytes.toBytes("ms")),"UTF-8");
                airConData.airCon.CtlResponse.sleep=new String(result.getValue(Bytes.toBytes("CtlResponse"),Bytes.toBytes("sleep")),"UTF-8");
                airConData.airCon.CtlResponse.sf=new String(result.getValue(Bytes.toBytes("CtlResponse"),Bytes.toBytes("sf")),"UTF-8");
                airConData.airCon.CtlResponse.fs=new String(result.getValue(Bytes.toBytes("CtlResponse"),Bytes.toBytes("fs")),"UTF-8");
                airConData.airCon.CtlResponse.wd=new String(result.getValue(Bytes.toBytes("CtlResponse"),Bytes.toBytes("wd")),"UTF-8");
                airConData.airCon.CtlResponse.ds=new String(result.getValue(Bytes.toBytes("CtlResponse"),Bytes.toBytes("ds")),"UTF-8");
                airConData.airCon.CtlResponse.dssj=new String(result.getValue(Bytes.toBytes("CtlResponse"),Bytes.toBytes("dssj")),"UTF-8");
                airConData.airCon.CtlResponse.gz=new String(result.getValue(Bytes.toBytes("CtlResponse"),Bytes.toBytes("gz")),"UTF-8");
                airConData.airCon.CtlResponse.fr=new String(result.getValue(Bytes.toBytes("CtlResponse"),Bytes.toBytes("fr")),"UTF-8");
                byte[] frmsBytes = result.getValue(Bytes.toBytes("CtlResponse"),Bytes.toBytes("frms"));
                if(frmsBytes!=null) {
                    airConData.airCon.CtlResponse.frms = new String(frmsBytes, "UTF-8");
                }
                airConData.airCon.CtlResponse.dg=new String(result.getValue(Bytes.toBytes("CtlResponse"),Bytes.toBytes("dg")),"UTF-8");
                airConData.airCon.CtlResponse.cq=new String(result.getValue(Bytes.toBytes("CtlResponse"),Bytes.toBytes("cq")),"UTF-8");
                airConData.airCon.CtlResponse.wddw=new String(result.getValue(Bytes.toBytes("CtlResponse"),Bytes.toBytes("wddw")),"UTF-8");
                airConData.airCon.CtlResponse.hq=new String(result.getValue(Bytes.toBytes("CtlResponse"),Bytes.toBytes("hq")),"UTF-8");
                airConData.airCon.CtlResponse.lsljydlfwql=new String(result.getValue(Bytes.toBytes("CtlResponse"),Bytes.toBytes("lsljydlfwql")),"UTF-8");
                airConData.airCon.CtlResponse.zdqj=new String(result.getValue(Bytes.toBytes("CtlResponse"),Bytes.toBytes("zdqj")),"UTF-8");
                airConData.airCon.CtlResponse.sxsf=new String(result.getValue(Bytes.toBytes("CtlResponse"),Bytes.toBytes("sxsf")),"UTF-8");
                airConData.airCon.CtlResponse.zysf=new String(result.getValue(Bytes.toBytes("CtlResponse"),Bytes.toBytes("zysf")),"UTF-8");
                airConData.airCon.CtlResponse.fm=new String(result.getValue(Bytes.toBytes("CtlResponse"),Bytes.toBytes("fm")),"UTF-8");
                airConData.airCon.CtlResponse.wdxsms=new String(result.getValue(Bytes.toBytes("CtlResponse"),Bytes.toBytes("wdxsms")),"UTF-8");
                airConData.airCon.CtlResponse.qcts=new String(result.getValue(Bytes.toBytes("CtlResponse"),Bytes.toBytes("qcts")),"UTF-8");
                airConData.airCon.CtlResponse.ykj=new String(result.getValue(Bytes.toBytes("CtlResponse"),Bytes.toBytes("ykj")),"UTF-8");
                airConData.airCon.CtlResponse.sdxs=new String(result.getValue(Bytes.toBytes("CtlResponse"),Bytes.toBytes("sdxs")),"UTF-8");
                airConData.airCon.CtlResponse.sdsd=new String(result.getValue(Bytes.toBytes("CtlResponse"),Bytes.toBytes("sdsd")),"UTF-8");
                airConData.airCon.CtlResponse.saver=new String(result.getValue(Bytes.toBytes("CtlResponse"),Bytes.toBytes("saver")),"UTF-8");
                airConData.airCon.CtlResponse.cobj=new String(result.getValue(Bytes.toBytes("CtlResponse"),Bytes.toBytes("cobj")),"UTF-8");
                airConData.airCon.CtlResponse.gea=new String(result.getValue(Bytes.toBytes("CtlResponse"),Bytes.toBytes("gea")),"UTF-8");
                airConData.airCon.CtlResponse.zdqx=new String(result.getValue(Bytes.toBytes("CtlResponse"),Bytes.toBytes("zdqx")),"UTF-8");
                airConData.airCon.CtlResponse.hf=new String(result.getValue(Bytes.toBytes("CtlResponse"),Bytes.toBytes("hf")),"UTF-8");
                airConData.airCon.CtlResponse.hfms=new String(result.getValue(Bytes.toBytes("CtlResponse"),Bytes.toBytes("hfms")),"UTF-8");
                airConData.airCon.CtlResponse.hsh1=new String(result.getValue(Bytes.toBytes("CtlResponse"),Bytes.toBytes("hsh1")),"UTF-8");
                airConData.airCon.CtlResponse.yuyin=new String(result.getValue(Bytes.toBytes("CtlResponse"),Bytes.toBytes("yuyin")),"UTF-8");
                airConData.airCon.CtlResponse.sb=new String(result.getValue(Bytes.toBytes("CtlResponse"),Bytes.toBytes("sb")),"UTF-8");
                airConData.airCon.CtlResponse.sm=new String(result.getValue(Bytes.toBytes("CtlResponse"),Bytes.toBytes("sm")),"UTF-8");
                airConData.airCon.CtlResponse.fcktled=new String(result.getValue(Bytes.toBytes("CtlResponse"),Bytes.toBytes("fcktled")),"UTF-8");
                airConData.airCon.CtlResponse.dsxhcs=new String(result.getValue(Bytes.toBytes("CtlResponse"),Bytes.toBytes("dsxhcs")),"UTF-8");
                airConData.airCon.CtlResponse.qz=new String(result.getValue(Bytes.toBytes("CtlResponse"),Bytes.toBytes("qz")),"UTF-8");
                airConData.airCon.CtlResponse.dsk=new String(result.getValue(Bytes.toBytes("CtlResponse"),Bytes.toBytes("dsk")),"UTF-8");
                airConData.airCon.CtlResponse.dsg=new String(result.getValue(Bytes.toBytes("CtlResponse"),Bytes.toBytes("dsg")),"UTF-8");
                airConData.airCon.CtlResponse.csms=new String(result.getValue(Bytes.toBytes("CtlResponse"),Bytes.toBytes("csms")),"UTF-8");
                airConData.airCon.CtlResponse.txsb=new String(result.getValue(Bytes.toBytes("CtlResponse"),Bytes.toBytes("txsb")),"UTF-8");
                airConData.airCon.CtlResponse.sleepMode=new String(result.getValue(Bytes.toBytes("CtlResponse"),Bytes.toBytes("sleepMode")),"UTF-8");
                airConData.airCon.CtlResponse.mute=new String(result.getValue(Bytes.toBytes("CtlResponse"),Bytes.toBytes("mute")),"UTF-8");
                airConData.airCon.CtlResponse.qyxz=new String(result.getValue(Bytes.toBytes("CtlResponse"),Bytes.toBytes("qyxz")),"UTF-8");
                airConData.airCon.CtlResponse.sm1hhwd=new String(result.getValue(Bytes.toBytes("CtlResponse"),Bytes.toBytes("sm1hhwd")),"UTF-8");
                airConData.airCon.CtlResponse.sm2hhwd=new String(result.getValue(Bytes.toBytes("CtlResponse"),Bytes.toBytes("sm2hhwd")),"UTF-8");
                airConData.airCon.CtlResponse.hj=new String(result.getValue(Bytes.toBytes("CtlResponse"),Bytes.toBytes("hj")),"UTF-8");
                airConData.airCon.CtlResponse.f5=new String(result.getValue(Bytes.toBytes("CtlResponse"),Bytes.toBytes("f5")),"UTF-8");
                airConData.airCon.CtlResponse.ddfs=new String(result.getValue(Bytes.toBytes("CtlResponse"),Bytes.toBytes("ddfs")),"UTF-8");
                airConData.airCon.CtlResponse.ex=new String(result.getValue(Bytes.toBytes("CtlResponse"),Bytes.toBytes("ex")),"UTF-8");
                airConData.airCon.CtlResponse.sm3hhwd=new String(result.getValue(Bytes.toBytes("CtlResponse"),Bytes.toBytes("sm3hhwd")),"UTF-8");
                airConData.airCon.CtlResponse.sm4hhwd=new String(result.getValue(Bytes.toBytes("CtlResponse"),Bytes.toBytes("sm4hhwd")),"UTF-8");
                airConData.airCon.CtlResponse.sm5hhwd=new String(result.getValue(Bytes.toBytes("CtlResponse"),Bytes.toBytes("sm5hhwd")),"UTF-8");
                airConData.airCon.CtlResponse.sm6hhwd=new String(result.getValue(Bytes.toBytes("CtlResponse"),Bytes.toBytes("sm6hhwd")),"UTF-8");
                airConData.airCon.CtlResponse.sm7hhwd=new String(result.getValue(Bytes.toBytes("CtlResponse"),Bytes.toBytes("sm7hhwd")),"UTF-8");
                airConData.airCon.CtlResponse.sm8hhwd=new String(result.getValue(Bytes.toBytes("CtlResponse"),Bytes.toBytes("sm8hhwd")),"UTF-8");
                airConData.airCon.CtlResponse.stf=new String(result.getValue(Bytes.toBytes("CtlResponse"),Bytes.toBytes("stf")),"UTF-8");
                airConData.airCon.CtlResponse.sxsffk=new String(result.getValue(Bytes.toBytes("CtlResponse"),Bytes.toBytes("sxsffk")),"UTF-8");

                byte[] hxBytes = result.getValue(Bytes.toBytes("CtlResponse"),Bytes.toBytes("hx"));
                if(hxBytes!=null) {
                    airConData.airCon.CtlResponse.hx = new String(hxBytes, "UTF-8");
                }
                byte[] znhqBytes = result.getValue(Bytes.toBytes("CtlResponse"),Bytes.toBytes("znhq"));
                if(znhqBytes!=null) {
                    airConData.airCon.CtlResponse.znhq = new String(znhqBytes, "UTF-8");
                }
                airConData.airCon.CtlResponse.dnsz=new String(result.getValue(Bytes.toBytes("CtlResponse"),Bytes.toBytes("dnsz")),"UTF-8");
                airConData.airCon.CtlResponse.yhsdyddw=new String(result.getValue(Bytes.toBytes("CtlResponse"),Bytes.toBytes("yhsdyddw")),"UTF-8");
                airConData.airCon.CtlResponse.jzcr=new String(result.getValue(Bytes.toBytes("CtlResponse"),Bytes.toBytes("jzcr")),"UTF-8");
                airConData.airCon.CtlResponse.hjqy9=new String(result.getValue(Bytes.toBytes("CtlResponse"),Bytes.toBytes("hjqy9")),"UTF-8");
                airConData.airCon.CtlResponse.hjqy8=new String(result.getValue(Bytes.toBytes("CtlResponse"),Bytes.toBytes("hjqy8")),"UTF-8");
                airConData.airCon.CtlResponse.hjqy7=new String(result.getValue(Bytes.toBytes("CtlResponse"),Bytes.toBytes("hjqy7")),"UTF-8");
                airConData.airCon.CtlResponse.hjqy6=new String(result.getValue(Bytes.toBytes("CtlResponse"),Bytes.toBytes("hjqy6")),"UTF-8");
                airConData.airCon.CtlResponse.hjqy5=new String(result.getValue(Bytes.toBytes("CtlResponse"),Bytes.toBytes("hjqy5")),"UTF-8");
                airConData.airCon.CtlResponse.hjqy4=new String(result.getValue(Bytes.toBytes("CtlResponse"),Bytes.toBytes("hjqy4")),"UTF-8");
                airConData.airCon.CtlResponse.hjqy3=new String(result.getValue(Bytes.toBytes("CtlResponse"),Bytes.toBytes("hjqy3")),"UTF-8");
                airConData.airCon.CtlResponse.hjqy2=new String(result.getValue(Bytes.toBytes("CtlResponse"),Bytes.toBytes("hjqy2")),"UTF-8");
                airConData.airCon.CtlResponse.hjqy1=new String(result.getValue(Bytes.toBytes("CtlResponse"),Bytes.toBytes("hjqy1")),"UTF-8");
                airConData.airCon.CtlResponse.hwyk=new String(result.getValue(Bytes.toBytes("CtlResponse"),Bytes.toBytes("hwyk")),"UTF-8");

                byte[] faultBytes = result.getValue(Bytes.toBytes("CtlResponse"),Bytes.toBytes("fault"));
                if(faultBytes!=null) {
                    airConData.airCon.CtlResponse.fault = new String(faultBytes, "UTF-8");
                }
                airConData.airCon.CtlResponse.zkfs=new String(result.getValue(Bytes.toBytes("CtlResponse"),Bytes.toBytes("zkfs")),"UTF-8");
                airConData.airCon.CtlResponse.hjms=new String(result.getValue(Bytes.toBytes("CtlResponse"),Bytes.toBytes("hjms")),"UTF-8");
                airConData.airCon.CtlResponse.xcfksxsf=new String(result.getValue(Bytes.toBytes("CtlResponse"),Bytes.toBytes("xcfksxsf")),"UTF-8");
                airConData.airCon.CtlResponse.xcfkzczysf=new String(result.getValue(Bytes.toBytes("CtlResponse"),Bytes.toBytes("xcfkzczysf")),"UTF-8");
                airConData.airCon.CtlResponse.xcfkyczysf=new String(result.getValue(Bytes.toBytes("CtlResponse"),Bytes.toBytes("xcfkyczysf")),"UTF-8");
                airConData.airCon.CtlResponse.snhjwd=new String(result.getValue(Bytes.toBytes("CtlResponse"),Bytes.toBytes("snhjwd")),"UTF-8");
                airConData.airCon.CtlResponse.hjsdz=new String(result.getValue(Bytes.toBytes("CtlResponse"),Bytes.toBytes("hjsdz")),"UTF-8");
                airConData.airCon.CtlResponse.swhjwd=new String(result.getValue(Bytes.toBytes("CtlResponse"),Bytes.toBytes("swhjwd")),"UTF-8");
            }

            airConData.airCon.InOutStatus = new AirConData.AirCon.InOutUnit();
            airConData.airCon.InOutStatus.in = new AirConData.AirCon.InOutUnit.InnerUnit();
            airConData.airCon.InOutStatus.out = new AirConData.AirCon.InOutUnit.OuterUnit();
            origin = result.getValue(Bytes.toBytes("InnerUnit"),Bytes.toBytes("origin"));
            if(origin!=null && origin.length>0){
                airConData.airCon.InOutStatus.in.txbb=new String(result.getValue(Bytes.toBytes("InnerUnit"),Bytes.toBytes("txbb")),"UTF-8");
                airConData.airCon.InOutStatus.in.txsd=new String(result.getValue(Bytes.toBytes("InnerUnit"),Bytes.toBytes("txsd")),"UTF-8");
                airConData.airCon.InOutStatus.in.lnjx=new String(result.getValue(Bytes.toBytes("InnerUnit"),Bytes.toBytes("lnjx")),"UTF-8");
                airConData.airCon.InOutStatus.in.dpbp=new String(result.getValue(Bytes.toBytes("InnerUnit"),Bytes.toBytes("dpbp")),"UTF-8");
                airConData.airCon.InOutStatus.in.njnldm=new String(result.getValue(Bytes.toBytes("InnerUnit"),Bytes.toBytes("njnldm")),"UTF-8");
                airConData.airCon.InOutStatus.in.dypl=new String(result.getValue(Bytes.toBytes("InnerUnit"),Bytes.toBytes("dypl")),"UTF-8");
                airConData.airCon.InOutStatus.in.gdfs=new String(result.getValue(Bytes.toBytes("InnerUnit"),Bytes.toBytes("gdfs")),"UTF-8");
                airConData.airCon.InOutStatus.in.dyzl=new String(result.getValue(Bytes.toBytes("InnerUnit"),Bytes.toBytes("dyzl")),"UTF-8");
                airConData.airCon.InOutStatus.in.lmzl=new String(result.getValue(Bytes.toBytes("InnerUnit"),Bytes.toBytes("lmzl")),"UTF-8");
                airConData.airCon.InOutStatus.in.lxdm=new String(result.getValue(Bytes.toBytes("InnerUnit"),Bytes.toBytes("lxdm")),"UTF-8");
                airConData.airCon.InOutStatus.in.hjgwb=new String(result.getValue(Bytes.toBytes("InnerUnit"),Bytes.toBytes("hjgwb")),"UTF-8");
                airConData.airCon.InOutStatus.in.ngzjgwb=new String(result.getValue(Bytes.toBytes("InnerUnit"),Bytes.toBytes("ngzjgwb")),"UTF-8");
                airConData.airCon.InOutStatus.in.sdcgq=new String(result.getValue(Bytes.toBytes("InnerUnit"),Bytes.toBytes("sdcgq")),"UTF-8");
                airConData.airCon.InOutStatus.in.fjzl=new String(result.getValue(Bytes.toBytes("InnerUnit"),Bytes.toBytes("fjzl")),"UTF-8");
                airConData.airCon.InOutStatus.in.fjds=new String(result.getValue(Bytes.toBytes("InnerUnit"),Bytes.toBytes("fjds")),"UTF-8");
                airConData.airCon.InOutStatus.in.jyxphqsjbz=new String(result.getValue(Bytes.toBytes("InnerUnit"),Bytes.toBytes("jyxphqsjbz")),"UTF-8");
                airConData.airCon.InOutStatus.in.jdccgn=new String(result.getValue(Bytes.toBytes("InnerUnit"),Bytes.toBytes("jdccgn")),"UTF-8");
                airConData.airCon.InOutStatus.in.frgn=new String(result.getValue(Bytes.toBytes("InnerUnit"),Bytes.toBytes("frgn")),"UTF-8");
                airConData.airCon.InOutStatus.in.jygn=new String(result.getValue(Bytes.toBytes("InnerUnit"),Bytes.toBytes("jygn")),"UTF-8");
                airConData.airCon.InOutStatus.in.jkgn=new String(result.getValue(Bytes.toBytes("InnerUnit"),Bytes.toBytes("jkgn")),"UTF-8");
                airConData.airCon.InOutStatus.in.hqgn=new String(result.getValue(Bytes.toBytes("InnerUnit"),Bytes.toBytes("hqgn")),"UTF-8");
                airConData.airCon.InOutStatus.in.ssggn=new String(result.getValue(Bytes.toBytes("InnerUnit"),Bytes.toBytes("ssggn")),"UTF-8");
                airConData.airCon.InOutStatus.in.dsfsxz=new String(result.getValue(Bytes.toBytes("InnerUnit"),Bytes.toBytes("dsfsxz")),"UTF-8");
                airConData.airCon.InOutStatus.in.txmh=new String(result.getValue(Bytes.toBytes("InnerUnit"),Bytes.toBytes("txmh")),"UTF-8");
                airConData.airCon.InOutStatus.in.jx=new String(result.getValue(Bytes.toBytes("InnerUnit"),Bytes.toBytes("jx")),"UTF-8");

                airConData.airCon.InOutStatus.out.txbb=new String(result.getValue(Bytes.toBytes("OuterUnit"),Bytes.toBytes("txbb")),"UTF-8");
                airConData.airCon.InOutStatus.out.txsd=new String(result.getValue(Bytes.toBytes("OuterUnit"),Bytes.toBytes("txsd")),"UTF-8");
                airConData.airCon.InOutStatus.out.lnjx=new String(result.getValue(Bytes.toBytes("OuterUnit"),Bytes.toBytes("lnjx")),"UTF-8");
                airConData.airCon.InOutStatus.out.bpdp=new String(result.getValue(Bytes.toBytes("OuterUnit"),Bytes.toBytes("bpdp")),"UTF-8");
                airConData.airCon.InOutStatus.out.wjnldm=new String(result.getValue(Bytes.toBytes("OuterUnit"),Bytes.toBytes("wjnldm")),"UTF-8");
                airConData.airCon.InOutStatus.out.gdfs=new String(result.getValue(Bytes.toBytes("OuterUnit"),Bytes.toBytes("gdfs")),"UTF-8");
                airConData.airCon.InOutStatus.out.dyzl=new String(result.getValue(Bytes.toBytes("OuterUnit"),Bytes.toBytes("dyzl")),"UTF-8");
                airConData.airCon.InOutStatus.out.swjlx=new String(result.getValue(Bytes.toBytes("OuterUnit"),Bytes.toBytes("swjlx")),"UTF-8");
                airConData.airCon.InOutStatus.out.ysjxh=new String(result.getValue(Bytes.toBytes("OuterUnit"),Bytes.toBytes("ysjxh")),"UTF-8");
                airConData.airCon.InOutStatus.out.cflx=new String(result.getValue(Bytes.toBytes("OuterUnit"),Bytes.toBytes("cflx")),"UTF-8");
                airConData.airCon.InOutStatus.out.fjzl=new String(result.getValue(Bytes.toBytes("OuterUnit"),Bytes.toBytes("fjzl")),"UTF-8");
                airConData.airCon.InOutStatus.out.fjgs=new String(result.getValue(Bytes.toBytes("OuterUnit"),Bytes.toBytes("fjgs")),"UTF-8");
                airConData.airCon.InOutStatus.out.fjds=new String(result.getValue(Bytes.toBytes("OuterUnit"),Bytes.toBytes("fjds")),"UTF-8");
                airConData.airCon.InOutStatus.out.dplnqyrdyw=new String(result.getValue(Bytes.toBytes("OuterUnit"),Bytes.toBytes("dplnqyrdyw")),"UTF-8");
                airConData.airCon.InOutStatus.out.ysjyrdyw=new String(result.getValue(Bytes.toBytes("OuterUnit"),Bytes.toBytes("ysjyrdyw")),"UTF-8");
                airConData.airCon.InOutStatus.out.xqzyryw=new String(result.getValue(Bytes.toBytes("OuterUnit"),Bytes.toBytes("xqzyryw")),"UTF-8");
                airConData.airCon.InOutStatus.out.dzpzfyw=new String(result.getValue(Bytes.toBytes("OuterUnit"),Bytes.toBytes("dzpzfyw")),"UTF-8");
                airConData.airCon.InOutStatus.out.jxm=new String(result.getValue(Bytes.toBytes("OuterUnit"),Bytes.toBytes("jxm")),"UTF-8");
                airConData.airCon.InOutStatus.out.wfjipmmk=new String(result.getValue(Bytes.toBytes("OuterUnit"),Bytes.toBytes("wfjipmmk")),"UTF-8");
                airConData.airCon.InOutStatus.out.ysjipmmk=new String(result.getValue(Bytes.toBytes("OuterUnit"),Bytes.toBytes("ysjipmmk")),"UTF-8");
                airConData.airCon.InOutStatus.out.njcxbbh=new String(result.getValue(Bytes.toBytes("OuterUnit"),Bytes.toBytes("njcxbbh")),"UTF-8");
                airConData.airCon.InOutStatus.out.wjcxbbh=new String(result.getValue(Bytes.toBytes("OuterUnit"),Bytes.toBytes("wjcxbbh")),"UTF-8");
            }

            airConData.airCon.InStatusFault = new AirConData.AirCon.InnerStatus();
            origin = result.getValue(Bytes.toBytes("InnerStatus"),Bytes.toBytes("origin"));
            if(origin!=null && origin.length>0){
                airConData.airCon.InStatusFault.kgjzt=new String(result.getValue(Bytes.toBytes("InnerStatus"),Bytes.toBytes("kgjzt")),"UTF-8");
                airConData.airCon.InStatusFault.yxms=new String(result.getValue(Bytes.toBytes("InnerStatus"),Bytes.toBytes("yxms")),"UTF-8");
                airConData.airCon.InStatusFault.nfjzs=new String(result.getValue(Bytes.toBytes("InnerStatus"),Bytes.toBytes("nfjzs")),"UTF-8");
                airConData.airCon.InStatusFault.snsdwd=new String(result.getValue(Bytes.toBytes("InnerStatus"),Bytes.toBytes("snsdwd")),"UTF-8");
                airConData.airCon.InStatusFault.snhjwd=new String(result.getValue(Bytes.toBytes("InnerStatus"),Bytes.toBytes("snhjwd")),"UTF-8");
                airConData.airCon.InStatusFault.snzfqzjwd=new String(result.getValue(Bytes.toBytes("InnerStatus"),Bytes.toBytes("snzfqzjwd")),"UTF-8");
                airConData.airCon.InStatusFault.syx=new String(result.getValue(Bytes.toBytes("InnerStatus"),Bytes.toBytes("syx")),"UTF-8");
                airConData.airCon.InStatusFault.sfms=new String(result.getValue(Bytes.toBytes("InnerStatus"),Bytes.toBytes("sfms")),"UTF-8");
                airConData.airCon.InStatusFault.qzcs=new String(result.getValue(Bytes.toBytes("InnerStatus"),Bytes.toBytes("qzcs")),"UTF-8");
                airConData.airCon.InStatusFault.qzzr=new String(result.getValue(Bytes.toBytes("InnerStatus"),Bytes.toBytes("qzzr")),"UTF-8");
                airConData.airCon.InStatusFault.qzzl=new String(result.getValue(Bytes.toBytes("InnerStatus"),Bytes.toBytes("qzzl")),"UTF-8");
                airConData.airCon.InStatusFault.bcdwdz=new String(result.getValue(Bytes.toBytes("InnerStatus"),Bytes.toBytes("bcdwdz")),"UTF-8");
                airConData.airCon.InStatusFault.sfkzwjzrwdbc=new String(result.getValue(Bytes.toBytes("InnerStatus"),Bytes.toBytes("sfkzwjzrwdbc")),"UTF-8");
                airConData.airCon.InStatusFault.snhjsd=new String(result.getValue(Bytes.toBytes("InnerStatus"),Bytes.toBytes("snhjsd")),"UTF-8");
                airConData.airCon.InStatusFault.jszt=new String(result.getValue(Bytes.toBytes("InnerStatus"),Bytes.toBytes("jszt")),"UTF-8");
                airConData.airCon.InStatusFault.savezt=new String(result.getValue(Bytes.toBytes("InnerStatus"),Bytes.toBytes("savezt")),"UTF-8");
                airConData.airCon.InStatusFault.hqzt=new String(result.getValue(Bytes.toBytes("InnerStatus"),Bytes.toBytes("hqzt")),"UTF-8");
                airConData.airCon.InStatusFault.jyzt=new String(result.getValue(Bytes.toBytes("InnerStatus"),Bytes.toBytes("jyzt")),"UTF-8");
                airConData.airCon.InStatusFault.flfzt=new String(result.getValue(Bytes.toBytes("InnerStatus"),Bytes.toBytes("flfzt")),"UTF-8");
                airConData.airCon.InStatusFault.fnlzt=new String(result.getValue(Bytes.toBytes("InnerStatus"),Bytes.toBytes("fnlzt")),"UTF-8");

                byte[] dfrztBytes = result.getValue(Bytes.toBytes("InnerStatus"),Bytes.toBytes("dfrzt"));
                if(dfrztBytes!=null) {
                    airConData.airCon.InStatusFault.dfrzt = new String(dfrztBytes, "UTF-8");
                }
                airConData.airCon.InStatusFault.kczt=new String(result.getValue(Bytes.toBytes("InnerStatus"),Bytes.toBytes("kczt")),"UTF-8");
                airConData.airCon.InStatusFault.ysjzssdz=new String(result.getValue(Bytes.toBytes("InnerStatus"),Bytes.toBytes("ysjzssdz")),"UTF-8");
                airConData.airCon.InStatusFault.dzpzfkdsdyxzt=new String(result.getValue(Bytes.toBytes("InnerStatus"),Bytes.toBytes("dzpzfkdsdyxzt")),"UTF-8");
                airConData.airCon.InStatusFault.dzpzfkdsdz=new String(result.getValue(Bytes.toBytes("InnerStatus"),Bytes.toBytes("dzpzfkdsdz")),"UTF-8");
                airConData.airCon.InStatusFault.wsfjsdyxzt=new String(result.getValue(Bytes.toBytes("InnerStatus"),Bytes.toBytes("wsfjsdyxzt")),"UTF-8");
                airConData.airCon.InStatusFault.mbpqwdsdyxzt=new String(result.getValue(Bytes.toBytes("InnerStatus"),Bytes.toBytes("mbpqwdsdyxzt")),"UTF-8");
                airConData.airCon.InStatusFault.mbgrdsdyxzt=new String(result.getValue(Bytes.toBytes("InnerStatus"),Bytes.toBytes("mbgrdsdyxzt")),"UTF-8");
                airConData.airCon.InStatusFault.swtjyqbz=new String(result.getValue(Bytes.toBytes("InnerStatus"),Bytes.toBytes("swtjyqbz")),"UTF-8");
                airConData.airCon.InStatusFault.snqtcgqgz=new String(result.getValue(Bytes.toBytes("InnerStatus"),Bytes.toBytes("snqtcgqgz")),"UTF-8");
                airConData.airCon.InStatusFault.snswcgqgz=new String(result.getValue(Bytes.toBytes("InnerStatus"),Bytes.toBytes("snswcgqgz")),"UTF-8");
                airConData.airCon.InStatusFault.snsdcgqgz=new String(result.getValue(Bytes.toBytes("InnerStatus"),Bytes.toBytes("snsdcgqgz")),"UTF-8");
                airConData.airCon.InStatusFault.snhjgwbgz=new String(result.getValue(Bytes.toBytes("InnerStatus"),Bytes.toBytes("snhjgwbgz")),"UTF-8");
                airConData.airCon.InStatusFault.snzfqzjgwbgz=new String(result.getValue(Bytes.toBytes("InnerStatus"),Bytes.toBytes("snzfqzjgwbgz")),"UTF-8");
                airConData.airCon.InStatusFault.njzbyscqtxgz=new String(result.getValue(Bytes.toBytes("InnerStatus"),Bytes.toBytes("njzbyscqtxgz")),"UTF-8");
                airConData.airCon.InStatusFault.snjsmbh=new String(result.getValue(Bytes.toBytes("InnerStatus"),Bytes.toBytes("snjsmbh")),"UTF-8");
                airConData.airCon.InStatusFault.zdajds=new String(result.getValue(Bytes.toBytes("InnerStatus"),Bytes.toBytes("zdajds")),"UTF-8");
                airConData.airCon.InStatusFault.hdmgz=new String(result.getValue(Bytes.toBytes("InnerStatus"),Bytes.toBytes("hdmgz")),"UTF-8");
                airConData.airCon.InStatusFault.xzkdpyc=new String(result.getValue(Bytes.toBytes("InnerStatus"),Bytes.toBytes("xzkdpyc")),"UTF-8");
                airConData.airCon.InStatusFault.txmgz=new String(result.getValue(Bytes.toBytes("InnerStatus"),Bytes.toBytes("txmgz")),"UTF-8");
                airConData.airCon.InStatusFault.jyxpdxgz=new String(result.getValue(Bytes.toBytes("InnerStatus"),Bytes.toBytes("jyxpdxgz")),"UTF-8");
                airConData.airCon.InStatusFault.snfjgz=new String(result.getValue(Bytes.toBytes("InnerStatus"),Bytes.toBytes("snfjgz")),"UTF-8");

                byte[] _0x34data23Bytes = result.getValue(Bytes.toBytes("InnerStatus"),Bytes.toBytes("_0x34data23"));
                if(_0x34data23Bytes!=null) {
                    airConData.airCon.InStatusFault._0x34data23 = new String(_0x34data23Bytes, "UTF-8");
                    airConData.airCon.InStatusFault._0x34data24 = new String(result.getValue(Bytes.toBytes("InnerStatus"), Bytes.toBytes("_0x34data24")), "UTF-8");
                }
                airConData.airCon.InStatusFault.qfpbxz=new String(result.getValue(Bytes.toBytes("InnerStatus"),Bytes.toBytes("qfpbxz")),"UTF-8");
                airConData.airCon.InStatusFault.csfsxz=new String(result.getValue(Bytes.toBytes("InnerStatus"),Bytes.toBytes("csfsxz")),"UTF-8");
                airConData.airCon.InStatusFault.wkjdxz=new String(result.getValue(Bytes.toBytes("InnerStatus"),Bytes.toBytes("wkjdxz")),"UTF-8");
                airConData.airCon.InStatusFault.zysfzt=new String(result.getValue(Bytes.toBytes("InnerStatus"),Bytes.toBytes("zysfzt")),"UTF-8");
                airConData.airCon.InStatusFault.sxsfzt=new String(result.getValue(Bytes.toBytes("InnerStatus"),Bytes.toBytes("sxsfzt")),"UTF-8");
                airConData.airCon.InStatusFault.rszl=new String(result.getValue(Bytes.toBytes("InnerStatus"),Bytes.toBytes("rszl")),"UTF-8");
                airConData.airCon.InStatusFault.dredmsyxzt=new String(result.getValue(Bytes.toBytes("InnerStatus"),Bytes.toBytes("dredmsyxzt")),"UTF-8");
                airConData.airCon.InStatusFault.spmkrfgz=new String(result.getValue(Bytes.toBytes("InnerStatus"),Bytes.toBytes("spmkrfgz")),"UTF-8");
                airConData.airCon.InStatusFault.jcbtxjfgz=new String(result.getValue(Bytes.toBytes("InnerStatus"),Bytes.toBytes("jcbtxjfgz")),"UTF-8");
                airConData.airCon.InStatusFault.njwdywjgzOe=new String(result.getValue(Bytes.toBytes("InnerStatus"),Bytes.toBytes("njwdywjgzOe")),"UTF-8");
                airConData.airCon.InStatusFault.sfjggzfc=new String(result.getValue(Bytes.toBytes("InnerStatus"),Bytes.toBytes("sfjggzfc")),"UTF-8");
                airConData.airCon.InStatusFault.wifigzgm=new String(result.getValue(Bytes.toBytes("InnerStatus"),Bytes.toBytes("wifigzgm")),"UTF-8");
                airConData.airCon.InStatusFault.nwjtxgz=new String(result.getValue(Bytes.toBytes("InnerStatus"),Bytes.toBytes("nwjtxgz")),"UTF-8");
                airConData.airCon.InStatusFault.djdzbh=new String(result.getValue(Bytes.toBytes("InnerStatus"),Bytes.toBytes("djdzbh")),"UTF-8");
                airConData.airCon.InStatusFault.xsbhqdbgz=new String(result.getValue(Bytes.toBytes("InnerStatus"),Bytes.toBytes("xsbhqdbgz")),"UTF-8");
                airConData.airCon.InStatusFault.dbpxsbyc=new String(result.getValue(Bytes.toBytes("InnerStatus"),Bytes.toBytes("dbpxsbyc")),"UTF-8");
                airConData.airCon.InStatusFault.njglxhgz=new String(result.getValue(Bytes.toBytes("InnerStatus"),Bytes.toBytes("njglxhgz")),"UTF-8");
            }

            airConData.airCon.OutStatusFault = new AirConData.AirCon.OuterStatus();
            origin = result.getValue(Bytes.toBytes("OuterStatus"),Bytes.toBytes("origin"));
            if(origin!=null && origin.length>0){
                airConData.airCon.OutStatusFault.ysjkgzt=new String(result.getValue(Bytes.toBytes("OuterStatus"),Bytes.toBytes("ysjkgzt")),"UTF-8");
                airConData.airCon.OutStatusFault.hsms=new String(result.getValue(Bytes.toBytes("OuterStatus"),Bytes.toBytes("hsms")),"UTF-8");
                airConData.airCon.OutStatusFault.ysjyxzs=new String(result.getValue(Bytes.toBytes("OuterStatus"),Bytes.toBytes("ysjyxzs")),"UTF-8");
                airConData.airCon.OutStatusFault.wfj1zs=new String(result.getValue(Bytes.toBytes("OuterStatus"),Bytes.toBytes("wfj1zs")),"UTF-8");
                airConData.airCon.OutStatusFault.wfj2zs=new String(result.getValue(Bytes.toBytes("OuterStatus"),Bytes.toBytes("wfj2zs")),"UTF-8");
                airConData.airCon.OutStatusFault.ysjyxgl=new String(result.getValue(Bytes.toBytes("OuterStatus"),Bytes.toBytes("ysjyxgl")),"UTF-8");
                airConData.airCon.OutStatusFault.dzpzfkd=new String(result.getValue(Bytes.toBytes("OuterStatus"),Bytes.toBytes("dzpzfkd")),"UTF-8");

                byte[] dzpzfkdaBytes = result.getValue(Bytes.toBytes("OuterStatus"),Bytes.toBytes("dzpzfkda"));
                if(dzpzfkdaBytes!=null) {
                    airConData.airCon.OutStatusFault.dzpzfkda = new String(dzpzfkdaBytes, "UTF-8");
                    airConData.airCon.OutStatusFault.dzpzfkdb = new String(result.getValue(Bytes.toBytes("OuterStatus"), Bytes.toBytes("dzpzfkdb")), "UTF-8");
                }
                airConData.airCon.OutStatusFault.zlmxdy=new String(result.getValue(Bytes.toBytes("OuterStatus"),Bytes.toBytes("zlmxdy")),"UTF-8");
                airConData.airCon.OutStatusFault.swhjwd=new String(result.getValue(Bytes.toBytes("OuterStatus"),Bytes.toBytes("swhjwd")),"UTF-8");
                airConData.airCon.OutStatusFault.swlnqzjwd=new String(result.getValue(Bytes.toBytes("OuterStatus"),Bytes.toBytes("swlnqzjwd")),"UTF-8");
                airConData.airCon.OutStatusFault.swpqwd=new String(result.getValue(Bytes.toBytes("OuterStatus"),Bytes.toBytes("swpqwd")),"UTF-8");
                airConData.airCon.OutStatusFault.savezt=new String(result.getValue(Bytes.toBytes("OuterStatus"),Bytes.toBytes("savezt")),"UTF-8");
                airConData.airCon.OutStatusFault.wjfnlzt=new String(result.getValue(Bytes.toBytes("OuterStatus"),Bytes.toBytes("wjfnlzt")),"UTF-8");
                airConData.airCon.OutStatusFault.wjjyzt=new String(result.getValue(Bytes.toBytes("OuterStatus"),Bytes.toBytes("wjjyzt")),"UTF-8");
                airConData.airCon.OutStatusFault.dzpzfzt=new String(result.getValue(Bytes.toBytes("OuterStatus"),Bytes.toBytes("dzpzfzt")),"UTF-8");
                airConData.airCon.OutStatusFault.sfzdjzt=new String(result.getValue(Bytes.toBytes("OuterStatus"),Bytes.toBytes("sfzdjzt")),"UTF-8");
                airConData.airCon.OutStatusFault.hyzt=new String(result.getValue(Bytes.toBytes("OuterStatus"),Bytes.toBytes("hyzt")),"UTF-8");
                airConData.airCon.OutStatusFault.ptcszt=new String(result.getValue(Bytes.toBytes("OuterStatus"),Bytes.toBytes("ptcszt")),"UTF-8");
                airConData.airCon.OutStatusFault.csyq=new String(result.getValue(Bytes.toBytes("OuterStatus"),Bytes.toBytes("csyq")),"UTF-8");
                airConData.airCon.OutStatusFault.tscszt=new String(result.getValue(Bytes.toBytes("OuterStatus"),Bytes.toBytes("tscszt")),"UTF-8");
                airConData.airCon.OutStatusFault.wjrytjgz=new String(result.getValue(Bytes.toBytes("OuterStatus"),Bytes.toBytes("wjrytjgz")),"UTF-8");
                airConData.airCon.OutStatusFault.hszczt=new String(result.getValue(Bytes.toBytes("OuterStatus"),Bytes.toBytes("hszczt")),"UTF-8");
                airConData.airCon.OutStatusFault.wjacdlz=new String(result.getValue(Bytes.toBytes("OuterStatus"),Bytes.toBytes("wjacdlz")),"UTF-8");
                airConData.airCon.OutStatusFault.syx=new String(result.getValue(Bytes.toBytes("OuterStatus"),Bytes.toBytes("syx")),"UTF-8");
                airConData.airCon.OutStatusFault.sfms=new String(result.getValue(Bytes.toBytes("OuterStatus"),Bytes.toBytes("sfms")),"UTF-8");
                airConData.airCon.OutStatusFault.qzcs=new String(result.getValue(Bytes.toBytes("OuterStatus"),Bytes.toBytes("qzcs")),"UTF-8");
                airConData.airCon.OutStatusFault.qzzr=new String(result.getValue(Bytes.toBytes("OuterStatus"),Bytes.toBytes("qzzr")),"UTF-8");
                airConData.airCon.OutStatusFault.qzzl=new String(result.getValue(Bytes.toBytes("OuterStatus"),Bytes.toBytes("qzzl")),"UTF-8");
                airConData.airCon.OutStatusFault.dredmsyxzt=new String(result.getValue(Bytes.toBytes("OuterStatus"),Bytes.toBytes("dredmsyxzt")),"UTF-8");
                airConData.airCon.OutStatusFault.glggbhxjp=new String(result.getValue(Bytes.toBytes("OuterStatus"),Bytes.toBytes("glggbhxjp")),"UTF-8");
                airConData.airCon.OutStatusFault.mkdlbhxjp=new String(result.getValue(Bytes.toBytes("OuterStatus"),Bytes.toBytes("mkdlbhxjp")),"UTF-8");
                airConData.airCon.OutStatusFault.mkwdbhxjp=new String(result.getValue(Bytes.toBytes("OuterStatus"),Bytes.toBytes("mkwdbhxjp")),"UTF-8");
                airConData.airCon.OutStatusFault.zlmxdybhxjp=new String(result.getValue(Bytes.toBytes("OuterStatus"),Bytes.toBytes("zlmxdybhxjp")),"UTF-8");
                airConData.airCon.OutStatusFault.ghbhxjp=new String(result.getValue(Bytes.toBytes("OuterStatus"),Bytes.toBytes("ghbhxjp")),"UTF-8");
                airConData.airCon.OutStatusFault.fjdbhxjp=new String(result.getValue(Bytes.toBytes("OuterStatus"),Bytes.toBytes("fjdbhxjp")),"UTF-8");
                airConData.airCon.OutStatusFault.pqbhxjp=new String(result.getValue(Bytes.toBytes("OuterStatus"),Bytes.toBytes("pqbhxjp")),"UTF-8");
                airConData.airCon.OutStatusFault.wjacdlbhxjp=new String(result.getValue(Bytes.toBytes("OuterStatus"),Bytes.toBytes("wjacdlbhxjp")),"UTF-8");
                airConData.airCon.OutStatusFault.gzgwbgz=new String(result.getValue(Bytes.toBytes("OuterStatus"),Bytes.toBytes("gzgwbgz")),"UTF-8");
                airConData.airCon.OutStatusFault.pqgwbgz=new String(result.getValue(Bytes.toBytes("OuterStatus"),Bytes.toBytes("pqgwbgz")),"UTF-8");
                airConData.airCon.OutStatusFault.hjgwbgz=new String(result.getValue(Bytes.toBytes("OuterStatus"),Bytes.toBytes("hjgwbgz")),"UTF-8");
                airConData.airCon.OutStatusFault.swlnqzjgwbgz=new String(result.getValue(Bytes.toBytes("OuterStatus"),Bytes.toBytes("swlnqzjgwbgz")),"UTF-8");
                airConData.airCon.OutStatusFault.mkgwbdlgz=new String(result.getValue(Bytes.toBytes("OuterStatus"),Bytes.toBytes("mkgwbdlgz")),"UTF-8");
                airConData.airCon.OutStatusFault.ysjrgzbh=new String(result.getValue(Bytes.toBytes("OuterStatus"),Bytes.toBytes("ysjrgzbh")),"UTF-8");
                airConData.airCon.OutStatusFault.pqbh=new String(result.getValue(Bytes.toBytes("OuterStatus"),Bytes.toBytes("pqbh")),"UTF-8");
                airConData.airCon.OutStatusFault.gfhbh=new String(result.getValue(Bytes.toBytes("OuterStatus"),Bytes.toBytes("gfhbh")),"UTF-8");
                airConData.airCon.OutStatusFault.wjacdlbh=new String(result.getValue(Bytes.toBytes("OuterStatus"),Bytes.toBytes("wjacdlbh")),"UTF-8");
                airConData.airCon.OutStatusFault.mkdlfobh=new String(result.getValue(Bytes.toBytes("OuterStatus"),Bytes.toBytes("mkdlfobh")),"UTF-8");
                airConData.airCon.OutStatusFault.mkwdbh=new String(result.getValue(Bytes.toBytes("OuterStatus"),Bytes.toBytes("mkwdbh")),"UTF-8");
                airConData.airCon.OutStatusFault.fjdbh=new String(result.getValue(Bytes.toBytes("OuterStatus"),Bytes.toBytes("fjdbh")),"UTF-8");
                airConData.airCon.OutStatusFault.glggbh=new String(result.getValue(Bytes.toBytes("OuterStatus"),Bytes.toBytes("glggbh")),"UTF-8");
                airConData.airCon.OutStatusFault.ysjqnxbhqxttqx=new String(result.getValue(Bytes.toBytes("OuterStatus"),Bytes.toBytes("ysjqnxbhqxttqx")),"UTF-8");
                airConData.airCon.OutStatusFault.pfcglgz=new String(result.getValue(Bytes.toBytes("OuterStatus"),Bytes.toBytes("pfcglgz")),"UTF-8");
                airConData.airCon.OutStatusFault.zlmxdyggbh=new String(result.getValue(Bytes.toBytes("OuterStatus"),Bytes.toBytes("zlmxdyggbh")),"UTF-8");
                airConData.airCon.OutStatusFault.zlmxdygdbh=new String(result.getValue(Bytes.toBytes("OuterStatus"),Bytes.toBytes("zlmxdygdbh")),"UTF-8");
                airConData.airCon.OutStatusFault.qfbh=new String(result.getValue(Bytes.toBytes("OuterStatus"),Bytes.toBytes("qfbh")),"UTF-8");
                airConData.airCon.OutStatusFault.msct=new String(result.getValue(Bytes.toBytes("OuterStatus"),Bytes.toBytes("msct")),"UTF-8");
                airConData.airCon.OutStatusFault.snwjxbpp=new String(result.getValue(Bytes.toBytes("OuterStatus"),Bytes.toBytes("snwjxbpp")),"UTF-8");
                airConData.airCon.OutStatusFault.ytdnwjglljytxljbpp=new String(result.getValue(Bytes.toBytes("OuterStatus"),Bytes.toBytes("ytdnwjglljytxljbpp")),"UTF-8");
                airConData.airCon.OutStatusFault.jyxpdxgz=new String(result.getValue(Bytes.toBytes("OuterStatus"),Bytes.toBytes("jyxpdxgz")),"UTF-8");
                airConData.airCon.OutStatusFault.glxhyc=new String(result.getValue(Bytes.toBytes("OuterStatus"),Bytes.toBytes("glxhyc")),"UTF-8");
                airConData.airCon.OutStatusFault.stfhxyc=new String(result.getValue(Bytes.toBytes("OuterStatus"),Bytes.toBytes("stfhxyc")),"UTF-8");
                airConData.airCon.OutStatusFault.xzkdpyc=new String(result.getValue(Bytes.toBytes("OuterStatus"),Bytes.toBytes("xzkdpyc")),"UTF-8");
                airConData.airCon.OutStatusFault.swfj2gz=new String(result.getValue(Bytes.toBytes("OuterStatus"),Bytes.toBytes("swfj2gz")),"UTF-8");
                airConData.airCon.OutStatusFault.swfj1gz=new String(result.getValue(Bytes.toBytes("OuterStatus"),Bytes.toBytes("swfj1gz")),"UTF-8");
                airConData.airCon.OutStatusFault.gwbhswfj=new String(result.getValue(Bytes.toBytes("OuterStatus"),Bytes.toBytes("gwbhswfj")),"UTF-8");
                airConData.airCon.OutStatusFault.xtdybh=new String(result.getValue(Bytes.toBytes("OuterStatus"),Bytes.toBytes("xtdybh")),"UTF-8");
                airConData.airCon.OutStatusFault.xtgybh=new String(result.getValue(Bytes.toBytes("OuterStatus"),Bytes.toBytes("xtgybh")),"UTF-8");
                airConData.airCon.OutStatusFault.zlmxdydlgz=new String(result.getValue(Bytes.toBytes("OuterStatus"),Bytes.toBytes("zlmxdydlgz")),"UTF-8");
                airConData.airCon.OutStatusFault.zjdljcgz=new String(result.getValue(Bytes.toBytes("OuterStatus"),Bytes.toBytes("zjdljcgz")),"UTF-8");
                airConData.airCon.OutStatusFault.drcdgz=new String(result.getValue(Bytes.toBytes("OuterStatus"),Bytes.toBytes("drcdgz")),"UTF-8");
                airConData.airCon.OutStatusFault.ysjxdldljcgz=new String(result.getValue(Bytes.toBytes("OuterStatus"),Bytes.toBytes("ysjxdldljcgz")),"UTF-8");
                airConData.airCon.OutStatusFault.ysjsb=new String(result.getValue(Bytes.toBytes("OuterStatus"),Bytes.toBytes("ysjsb")),"UTF-8");
                airConData.airCon.OutStatusFault.ysjtcbh=new String(result.getValue(Bytes.toBytes("OuterStatus"),Bytes.toBytes("ysjtcbh")),"UTF-8");
                airConData.airCon.OutStatusFault.ysjdz=new String(result.getValue(Bytes.toBytes("OuterStatus"),Bytes.toBytes("ysjdz")),"UTF-8");
                airConData.airCon.OutStatusFault.qdsb=new String(result.getValue(Bytes.toBytes("OuterStatus"),Bytes.toBytes("qdsb")),"UTF-8");
                airConData.airCon.OutStatusFault.qdmkfw=new String(result.getValue(Bytes.toBytes("OuterStatus"),Bytes.toBytes("qdmkfw")),"UTF-8");
                airConData.airCon.OutStatusFault.sc=new String(result.getValue(Bytes.toBytes("OuterStatus"),Bytes.toBytes("sc")),"UTF-8");
                airConData.airCon.OutStatusFault.ysjbmyc=new String(result.getValue(Bytes.toBytes("OuterStatus"),Bytes.toBytes("ysjbmyc")),"UTF-8");
                airConData.airCon.OutStatusFault.qdbhjgwbgz=new String(result.getValue(Bytes.toBytes("OuterStatus"),Bytes.toBytes("qdbhjgwbgz")),"UTF-8");
                airConData.airCon.OutStatusFault.jljcqbh=new String(result.getValue(Bytes.toBytes("OuterStatus"),Bytes.toBytes("jljcqbh")),"UTF-8");
                airConData.airCon.OutStatusFault.wpbh=new String(result.getValue(Bytes.toBytes("OuterStatus"),Bytes.toBytes("wpbh")),"UTF-8");
                airConData.airCon.OutStatusFault.cgqljbh=new String(result.getValue(Bytes.toBytes("OuterStatus"),Bytes.toBytes("cgqljbh")),"UTF-8");
                airConData.airCon.OutStatusFault.qdbtxgz=new String(result.getValue(Bytes.toBytes("OuterStatus"),Bytes.toBytes("qdbtxgz")),"UTF-8");
                airConData.airCon.OutStatusFault.ysjxdlgl=new String(result.getValue(Bytes.toBytes("OuterStatus"),Bytes.toBytes("ysjxdlgl")),"UTF-8");
                airConData.airCon.OutStatusFault.jlsrdyyc=new String(result.getValue(Bytes.toBytes("OuterStatus"),Bytes.toBytes("jlsrdyyc")),"UTF-8");
                airConData.airCon.OutStatusFault.fjtsbtxgz=new String(result.getValue(Bytes.toBytes("OuterStatus"),Bytes.toBytes("fjtsbtxgz")),"UTF-8");
                airConData.airCon.OutStatusFault.yfgwbgz=new String(result.getValue(Bytes.toBytes("OuterStatus"),Bytes.toBytes("yfgwbgz")),"UTF-8");
                airConData.airCon.OutStatusFault.qfgwbgz=new String(result.getValue(Bytes.toBytes("OuterStatus"),Bytes.toBytes("qfgwbgz")),"UTF-8");
                airConData.airCon.OutStatusFault.swlnqrggwbgz=new String(result.getValue(Bytes.toBytes("OuterStatus"),Bytes.toBytes("swlnqrggwbgz")),"UTF-8");
                airConData.airCon.OutStatusFault.swlnqcggwbgz=new String(result.getValue(Bytes.toBytes("OuterStatus"),Bytes.toBytes("swlnqcggwbgz")),"UTF-8");
                airConData.airCon.OutStatusFault.lmwdgwbgz=new String(result.getValue(Bytes.toBytes("OuterStatus"),Bytes.toBytes("lmwdgwbgz")),"UTF-8");
                airConData.airCon.OutStatusFault.swjlmjrqsxgz=new String(result.getValue(Bytes.toBytes("OuterStatus"),Bytes.toBytes("swjlmjrqsxgz")),"UTF-8");
                airConData.airCon.OutStatusFault.swjlmjrqjdqzlgz=new String(result.getValue(Bytes.toBytes("OuterStatus"),Bytes.toBytes("swjlmjrqjdqzlgz")),"UTF-8");
                airConData.airCon.OutStatusFault.ipmmkwd=new String(result.getValue(Bytes.toBytes("OuterStatus"),Bytes.toBytes("ipmmkwd")),"UTF-8");
                airConData.airCon.OutStatusFault.xtyc=new String(result.getValue(Bytes.toBytes("OuterStatus"),Bytes.toBytes("xtyc")),"UTF-8");
                airConData.airCon.OutStatusFault.dyztw=new String(result.getValue(Bytes.toBytes("OuterStatus"),Bytes.toBytes("dyztw")),"UTF-8");
                airConData.airCon.OutStatusFault.dlztw=new String(result.getValue(Bytes.toBytes("OuterStatus"),Bytes.toBytes("dlztw")),"UTF-8");
                airConData.airCon.OutStatusFault.yfgwbwd=new String(result.getValue(Bytes.toBytes("OuterStatus"),Bytes.toBytes("yfgwbwd")),"UTF-8");
                airConData.airCon.OutStatusFault.qfgwbwd=new String(result.getValue(Bytes.toBytes("OuterStatus"),Bytes.toBytes("qfgwbwd")),"UTF-8");
                airConData.airCon.OutStatusFault.swlnqrgwd=new String(result.getValue(Bytes.toBytes("OuterStatus"),Bytes.toBytes("swlnqrgwd")),"UTF-8");
                airConData.airCon.OutStatusFault.swlnqcgwd=new String(result.getValue(Bytes.toBytes("OuterStatus"),Bytes.toBytes("swlnqcgwd")),"UTF-8");
                airConData.airCon.OutStatusFault.ysjpqgwbh=new String(result.getValue(Bytes.toBytes("OuterStatus"),Bytes.toBytes("ysjpqgwbh")),"UTF-8");
                airConData.airCon.OutStatusFault.jlglbh=new String(result.getValue(Bytes.toBytes("OuterStatus"),Bytes.toBytes("jlglbh")),"UTF-8");
                airConData.airCon.OutStatusFault.ggwbh=new String(result.getValue(Bytes.toBytes("OuterStatus"),Bytes.toBytes("ggwbh")),"UTF-8");
                airConData.airCon.OutStatusFault.deepromgz=new String(result.getValue(Bytes.toBytes("OuterStatus"),Bytes.toBytes("deepromgz")),"UTF-8");
                airConData.airCon.OutStatusFault.whwdyc=new String(result.getValue(Bytes.toBytes("OuterStatus"),Bytes.toBytes("whwdyc")),"UTF-8");
                airConData.airCon.OutStatusFault.zrggwjp=new String(result.getValue(Bytes.toBytes("OuterStatus"),Bytes.toBytes("zrggwjp")),"UTF-8");
            }
            return airConData;

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }
}