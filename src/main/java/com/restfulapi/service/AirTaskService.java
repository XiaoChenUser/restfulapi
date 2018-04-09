package com.restfulapi.service;

import com.restfulapi.entity.aircondition.*;
import com.restfulapi.hbase.entity.AirConData;
import com.restfulapi.hbase.utils.HbaseUtils;
import com.restfulapi.hbase.utils.StrUtils;
import com.restfulapi.repository.aircondition.*;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.restfulapi.service.AirService.getAirCon;

@Service
public class AirTaskService {
    public static final Logger log = LoggerFactory.getLogger(AirTaskService.class);
    public static final SimpleDateFormat DATEE = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Autowired
    AirRealCtlRespRepository airRealCtlRespRepository;
    @Autowired
    AirRealDeviceRepository airRealDeviceRepository;
    @Autowired
    AirRealInnerStatusRepository airRealInnerStatusRepository;
    @Autowired
    AirRealInnerUnitRepository airRealInnerUnitRepository;
    @Autowired
    AirRealOuterStatusRepository airRealOuterStatusRepository;
    @Autowired
    AirRealOuterUnitRepository airRealOuterUnitRepository;


    public void scanRealData(String starttime,String endtime) throws SQLException, NullPointerException,Exception {
        String tableNameStr = "GRIH:DataAirCon";
        //List<AirConData> airConDataList = new ArrayList<AirConData>();
        Connection connection = HbaseUtils.getConnection();
        TableName tableName = TableName.valueOf(tableNameStr);
        Table dataAirCon = connection.getTable(tableName);
        Scan scan = new Scan();
        scan.setCaching(1000);
        //Filter pageFilter = new PageFilter(100);

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
        ResultScanner resultScanner = dataAirCon.getScanner(scan);
        AirConData airConData = new AirConData();
        Result result = null;
        log.debug("正在执行定时任务，请等候！");
        while ((result = resultScanner.next()) != null) {
            airConData = getAirCon(result, airConData);
            task2Mysql(airConData);
        }
        log.debug("执行完毕！");
    }

    public void testTask(String date) throws Exception {
        String tableNameStr = "GRIH:DatParsedAirCon";
        Connection connection = HbaseUtils.getConnection();
        TableName tableName = TableName.valueOf(tableNameStr);
        Table dataAirCon = connection.getTable(tableName);
        Scan scan = new Scan();
        scan.setCaching(1000);
        if (StrUtils.isValidStrVal(date)){
            Filter rowFilter = new RowFilter(CompareFilter.CompareOp.EQUAL,
                    new BinaryPrefixComparator(date.getBytes()));
            scan.setFilter(rowFilter);
        }
        ResultScanner resultScanner = dataAirCon.getScanner(scan);
        AirConData airConData = new AirConData();
        Result result = null;
        log.debug("正在执行定时任务，请等候！");
        while ((result = resultScanner.next()) != null) {
            airConData = getAirCon(result, airConData);
            task2Mysql(airConData);
        }
        log.debug("执行完毕！");
    }



    public List<AirConData> scanAirConditionOnTime(String mac) throws Exception {
        String tableNameStr = "GRIH:AirConRealTime";
        List<AirConData> airConDataList = new ArrayList<>();
        Connection connection = HbaseUtils.getConnection();
        TableName tableName = TableName.valueOf(tableNameStr);
        Table dataAirCon = connection.getTable(tableName);
        Scan scan = new Scan();
        scan.setCaching(1000);

        if (StrUtils.isValidStrVal(mac)) {
            SingleColumnValueFilter macFilter = new SingleColumnValueFilter(Bytes.toBytes("DevInfoRes"),
                    Bytes.toBytes("mac"), CompareFilter.CompareOp.EQUAL, Bytes.toBytes(mac));
            macFilter.setFilterIfMissing(true);
            scan.setFilter(macFilter);
        }

        ResultScanner resultScanner = dataAirCon.getScanner(scan);
        AirConData airConData = new AirConData();
        Result result = null;
        log.debug("正在执行，请等候！");
        while ((result = resultScanner.next()) != null) {
            airConData = getAirCon(result, airConData);
            airConDataList.add(airConData);
        }
        log.debug("执行完毕！");
        return airConDataList;
    }

    public List<String> scanRowKey() throws SQLException, NullPointerException,Exception {
        String tableNameStr = "GRIH:AirConRealTime";
        ArrayList<String> list = new ArrayList<String>();
        Connection connection = HbaseUtils.getConnection();
        TableName tableName = TableName.valueOf(tableNameStr);
        Table dataAirCon = connection.getTable(tableName);
        Scan scan = new Scan();
        scan.setCaching(1000);
        //Filter filter2 = new RowFilter(CompareFilter.CompareOp.EQUAL, new RegexStringComparator();
        //scan.setFilter(filter2);
        log.debug("正在查询Mac列表，请等候！");
        ResultScanner resultScanner = dataAirCon.getScanner(scan);
        for (Result r: resultScanner) {
            byte[] row = r.getRow();
            list.add(new String(row,"UTF-8"));
        }
        log.debug("查询完毕！");
        return list;
    }

    public String getCalDate(Integer integer)throws NullPointerException,SQLException,Exception{
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, integer);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE,0);
        cal.set(Calendar.SECOND,0);
        cal.set(Calendar.MILLISECOND,0);
        return DATEE.format(cal.getTime());
    }


    public void task2Mysql(AirConData airConData)throws Exception{
        AirRealCtlResp ctlResp = new AirRealCtlResp();
        AirRealDevice device = new AirRealDevice();
        AirRealInnerStatus innerStatus = new AirRealInnerStatus();
        AirRealInnerUnit innerUnit = new AirRealInnerUnit();
        AirRealOuterStatus outerStatus = new AirRealOuterStatus();
        AirRealOuterUnit outerUnit = new AirRealOuterUnit();
        if (!(airConData.airCon.CtlResponse ==null)){
            ctlResp.setMac(airConData.mac);
            if (!(airConData.svrCtime.equals("0001-01-01 00:00:00"))){
                ctlResp.setCtime(DATEE.parse(airConData.svrCtime));
            }
            //ctlResp.setModtime(DATEE.parse(airConData.modTime));
            ctlResp.setCobj(airConData.airCon.CtlResponse.cobj);
            ctlResp.setCq(airConData.airCon.CtlResponse.cq);
            ctlResp.setCsms(airConData.airCon.CtlResponse.csms);
            ctlResp.setDdfs(airConData.airCon.CtlResponse.ddfs);
            ctlResp.setDg(airConData.airCon.CtlResponse.dg);
            ctlResp.setDnsz(airConData.airCon.CtlResponse.dnsz);
            ctlResp.setJsq(airConData.airCon.CtlResponse.jsq);
            ctlResp.setJsqtx(airConData.airCon.CtlResponse.jsqtx);
            ctlResp.setHqzz(airConData.airCon.CtlResponse.hqzz);
            ctlResp.setHqdy(airConData.airCon.CtlResponse.hqdy);
            ctlResp.setJk(airConData.airCon.CtlResponse.jk);
            ctlResp.setDm(airConData.airCon.CtlResponse.dm);
            ctlResp.setQxjtx(airConData.airCon.CtlResponse.qxjtx);
            ctlResp.setJcbfsqd(airConData.airCon.CtlResponse.jcbfsqd);
            ctlResp.setWifikg(airConData.airCon.CtlResponse.wifikg);
            ctlResp.setKqzlpjdw(airConData.airCon.CtlResponse.kqzlpjdw);
            ctlResp.setPm25dw(airConData.airCon.CtlResponse.pm25dw);
            ctlResp.setKgj(airConData.airCon.CtlResponse.kgj);
            ctlResp.setMs(airConData.airCon.CtlResponse.ms);
            ctlResp.setSleep(airConData.airCon.CtlResponse.sleep);
            ctlResp.setSf(airConData.airCon.CtlResponse.sf);
            ctlResp.setFs(airConData.airCon.CtlResponse.fs);
            ctlResp.setWd(airConData.airCon.CtlResponse.wd);
            ctlResp.setDs(airConData.airCon.CtlResponse.ds);
            ctlResp.setDssj(airConData.airCon.CtlResponse.dssj);
            ctlResp.setGz(airConData.airCon.CtlResponse.gz);
            ctlResp.setFr(airConData.airCon.CtlResponse.fr);
            ctlResp.setWddw(airConData.airCon.CtlResponse.wddw);
            ctlResp.setHq(airConData.airCon.CtlResponse.hq);
            ctlResp.setLsljydlfwql(airConData.airCon.CtlResponse.lsljydlfwql);
            ctlResp.setZdqj(airConData.airCon.CtlResponse.zdqj);
            ctlResp.setSxsf(airConData.airCon.CtlResponse.sxsf);
            ctlResp.setZysf(airConData.airCon.CtlResponse.zysf);
            ctlResp.setFm(airConData.airCon.CtlResponse.fm);
            ctlResp.setWdxsms(airConData.airCon.CtlResponse.wdxsms);
            ctlResp.setQcts(airConData.airCon.CtlResponse.qcts);
            ctlResp.setYkj(airConData.airCon.CtlResponse.ykj);
            ctlResp.setSdxs(airConData.airCon.CtlResponse.sdxs);
            ctlResp.setSdsd(airConData.airCon.CtlResponse.sdsd);
            ctlResp.setSaver(airConData.airCon.CtlResponse.saver);
            ctlResp.setGea(airConData.airCon.CtlResponse.gea);
            ctlResp.setZdqx(airConData.airCon.CtlResponse.zdqx);
            ctlResp.setHf(airConData.airCon.CtlResponse.hf);
            ctlResp.setHfms(airConData.airCon.CtlResponse.hfms);
            ctlResp.setHsh1(airConData.airCon.CtlResponse.hsh1);
            //ctlResp.setGlwqxtx(airConData.airCon.CtlResponse.glwqxtx);   不存在
            ctlResp.setSb(airConData.airCon.CtlResponse.sb);
            ctlResp.setSm(airConData.airCon.CtlResponse.sm);
            ctlResp.setFcktled(airConData.airCon.CtlResponse.fcktled);
            ctlResp.setDsxhcs(airConData.airCon.CtlResponse.dsxhcs);
            ctlResp.setQz(airConData.airCon.CtlResponse.qz);
            ctlResp.setDsk(airConData.airCon.CtlResponse.dsk);
            ctlResp.setDsg(airConData.airCon.CtlResponse.dsg);
            ctlResp.setTxsb(airConData.airCon.CtlResponse.txsb);
            ctlResp.setSleepmode(airConData.airCon.CtlResponse.sleepMode);
            ctlResp.setMute(airConData.airCon.CtlResponse.mute);
            ctlResp.setQyxz(airConData.airCon.CtlResponse.qyxz);
            ctlResp.setSm1hhwd(airConData.airCon.CtlResponse.sm1hhwd);
            ctlResp.setSm2hhwd(airConData.airCon.CtlResponse.sm2hhwd);
            ctlResp.setHj(airConData.airCon.CtlResponse.hj);
            ctlResp.setF5(airConData.airCon.CtlResponse.f5);
            ctlResp.setEx(airConData.airCon.CtlResponse.ex);
            ctlResp.setSm3hhwd(airConData.airCon.CtlResponse.sm3hhwd);
            ctlResp.setSm4hhwd(airConData.airCon.CtlResponse.sm4hhwd);
            ctlResp.setSm5hhwd(airConData.airCon.CtlResponse.sm5hhwd);
            ctlResp.setSm6hhwd(airConData.airCon.CtlResponse.sm6hhwd);
            ctlResp.setSm7hhwd(airConData.airCon.CtlResponse.sm7hhwd);
            ctlResp.setSm8hhwd(airConData.airCon.CtlResponse.sm8hhwd);
            ctlResp.setStf(airConData.airCon.CtlResponse.stf);
            ctlResp.setSxsffk(airConData.airCon.CtlResponse.sxsffk);
            ctlResp.setHx(airConData.airCon.CtlResponse.hx);
            ctlResp.setZnhq(airConData.airCon.CtlResponse.znhq);
            ctlResp.setYhsdyddw(airConData.airCon.CtlResponse.yhsdyddw);
            ctlResp.setJzcr(airConData.airCon.CtlResponse.jzcr);
            ctlResp.setHjqy9(airConData.airCon.CtlResponse.hjqy9);
            ctlResp.setHjqy8(airConData.airCon.CtlResponse.hjqy8);
            ctlResp.setHjqy7(airConData.airCon.CtlResponse.hjqy7);
            ctlResp.setHjqy6(airConData.airCon.CtlResponse.hjqy6);
            ctlResp.setHjqy5(airConData.airCon.CtlResponse.hjqy5);
            ctlResp.setHjqy4(airConData.airCon.CtlResponse.hjqy4);
            ctlResp.setHjqy3(airConData.airCon.CtlResponse.hjqy3);
            ctlResp.setHjqy2(airConData.airCon.CtlResponse.hjqy2);
            ctlResp.setHjqy1(airConData.airCon.CtlResponse.hjqy1);
            ctlResp.setHwyk(airConData.airCon.CtlResponse.hwyk);
            ctlResp.setFault(airConData.airCon.CtlResponse.fault);
            ctlResp.setZkfs(airConData.airCon.CtlResponse.zkfs);
            ctlResp.setHjms(airConData.airCon.CtlResponse.hjms);
            ctlResp.setXcfksxsf(airConData.airCon.CtlResponse.xcfksxsf);
            ctlResp.setXcfkzczysf(airConData.airCon.CtlResponse.xcfkzczysf);
            ctlResp.setXcfkyczysf(airConData.airCon.CtlResponse.xcfkyczysf);
            ctlResp.setSnhjwd(airConData.airCon.CtlResponse.snhjwd);
            ctlResp.setHjsdz(airConData.airCon.CtlResponse.hjsdz);
            ctlResp.setSwhjwd(airConData.airCon.CtlResponse.swhjwd);
            airRealCtlRespRepository.save(ctlResp);
        }
        if (!(airConData == null)){
            device.setIp(airConData.ip);
            device.setMac(airConData.mac);
            device.setMid(airConData.mid);
            device.setCountry(airConData.country);
            device.setProvince(airConData.province);
            device.setCity(airConData.city);
            device.setCodes(airConData.airCon.codes);
            device.setEvt(airConData.evt);
            if (airRealDeviceRepository.existsByMac(airConData.mac)){
                airRealDeviceRepository.flush();
            }
            else {
                airRealDeviceRepository.save(device);
            }
        }
        if (!(airConData.airCon.InStatusFault.kgjzt == null)) {
            innerStatus.setMac(airConData.mac);
            //innerStatus.setDatatime(DATEE.parse(airConData.svrCtime));
            if (!(airConData.svrCtime.equals("0001-01-01 00:00:00"))){
                innerStatus.setCtime(DATEE.parse(airConData.svrCtime));
            }
            innerStatus.setKgjzt(airConData.airCon.InStatusFault.kgjzt);
            innerStatus.setYxms(airConData.airCon.InStatusFault.yxms);
            innerStatus.setNfjzs(airConData.airCon.InStatusFault.nfjzs);
            //innerStatus.setSnhjwdxsbz(airConData.airCon.InStatusFault.snhjwdxsbz); 不存在
            //innerStatus.setSnzfqzjwdxsbz(airConData.airCon.InStatusFault.snzfqzjwdxsbz);不存在
            //innerStatus.setSnsdwdxsbz(airConData.airCon.InStatusFault.snsdwdxsbz);不存在
            innerStatus.setSnsdwd(airConData.airCon.InStatusFault.snsdwd);
            innerStatus.setSnhjwd(airConData.airCon.InStatusFault.snhjwd);
            innerStatus.setSyx(airConData.airCon.InStatusFault.syx);
            innerStatus.setSfms(airConData.airCon.InStatusFault.sfms);
            innerStatus.setQzcs(airConData.airCon.InStatusFault.qzcs);
            innerStatus.setQzzr(airConData.airCon.InStatusFault.qzzr);
            innerStatus.setQzzl(airConData.airCon.InStatusFault.qzzl);
            innerStatus.setSnzfqzjwd(airConData.airCon.InStatusFault.snzfqzjwd);
            innerStatus.setBcdwdz(airConData.airCon.InStatusFault.bcdwdz);
            innerStatus.setSfkzwjzrwdbc(airConData.airCon.InStatusFault.sfkzwjzrwdbc);
            innerStatus.setSnhjsd(airConData.airCon.InStatusFault.snhjsd);
            innerStatus.setJszt(airConData.airCon.InStatusFault.jszt);
            innerStatus.setSavezt(airConData.airCon.InStatusFault.savezt);
            innerStatus.setHqzt(airConData.airCon.InStatusFault.hqzt);
            innerStatus.setJyzt(airConData.airCon.InStatusFault.jyzt);
            innerStatus.setFlfzt(airConData.airCon.InStatusFault.flfzt);
            innerStatus.setFnlzt(airConData.airCon.InStatusFault.fnlzt);
            innerStatus.setDfrzt(airConData.airCon.InStatusFault.dfrzt);
            innerStatus.setKczt(airConData.airCon.InStatusFault.kczt);
            innerStatus.setYsjzssdz(airConData.airCon.InStatusFault.ysjzssdz);
            innerStatus.setDzpzfkdsdyxzt(airConData.airCon.InStatusFault.dzpzfkdsdyxzt);
            innerStatus.setDzpzfkdsdz(airConData.airCon.InStatusFault.dzpzfkdsdz);
            innerStatus.setWsfjsdyxzt(airConData.airCon.InStatusFault.wsfjsdyxzt);
            innerStatus.setMbpqwdsdyxzt(airConData.airCon.InStatusFault.mbpqwdsdyxzt);
            innerStatus.setMbgrdsdyxzt(airConData.airCon.InStatusFault.mbgrdsdyxzt);
            innerStatus.setSwtjyqbz(airConData.airCon.InStatusFault.swtjyqbz);
            innerStatus.setSnqtcgqgz(airConData.airCon.InStatusFault.snqtcgqgz);
            innerStatus.setSnswcgqgz(airConData.airCon.InStatusFault.snswcgqgz);
            innerStatus.setSnsdcgqgz(airConData.airCon.InStatusFault.snsdcgqgz);
            innerStatus.setSnhjgwbgz(airConData.airCon.InStatusFault.snhjgwbgz);
            innerStatus.setSnzfqzjgwbgz(airConData.airCon.InStatusFault.snzfqzjgwbgz);
            innerStatus.setNjzbyscqtxgz(airConData.airCon.InStatusFault.njzbyscqtxgz);
            innerStatus.setSnjsmbh(airConData.airCon.InStatusFault.snjsmbh);
            innerStatus.setZdajds(airConData.airCon.InStatusFault.zdajds);
            innerStatus.setHdmgz(airConData.airCon.InStatusFault.hdmgz);
            innerStatus.setXzkdpyc(airConData.airCon.InStatusFault.xzkdpyc);
            innerStatus.setTxmgz(airConData.airCon.InStatusFault.txmgz);
            innerStatus.setJyxpdxgz(airConData.airCon.InStatusFault.jyxpdxgz);
            innerStatus.setSnfjgz(airConData.airCon.InStatusFault.snfjgz);
            innerStatus.setQfpbxz(airConData.airCon.InStatusFault.qfpbxz);
            innerStatus.setCsfsxz(airConData.airCon.InStatusFault.csfsxz);
            innerStatus.setWkjdxz(airConData.airCon.InStatusFault.wkjdxz);
            innerStatus.setZysfzt(airConData.airCon.InStatusFault.zysfzt);
            innerStatus.setSxsfzt(airConData.airCon.InStatusFault.sxsfzt);
            innerStatus.setRszl(airConData.airCon.InStatusFault.rszl);
            innerStatus.setDredmsyxzt(airConData.airCon.InStatusFault.dredmsyxzt);
            innerStatus.setSpmkrfgz(airConData.airCon.InStatusFault.spmkrfgz);
            innerStatus.setJcbtxjfgz(airConData.airCon.InStatusFault.jcbtxjfgz);
            innerStatus.setNjwdywjgzoe(airConData.airCon.InStatusFault.njwdywjgzOe);
            innerStatus.setSfjggzfc(airConData.airCon.InStatusFault.sfjggzfc);
            innerStatus.setWifigzgm(airConData.airCon.InStatusFault.wifigzgm);
            innerStatus.setNwjtxgz(airConData.airCon.InStatusFault.nwjtxgz);
            innerStatus.setDjdzbh(airConData.airCon.InStatusFault.djdzbh);
            innerStatus.setXsbhqdbgz(airConData.airCon.InStatusFault.xsbhqdbgz);
            innerStatus.setDbpxsbyc(airConData.airCon.InStatusFault.dbpxsbyc);
            innerStatus.setNjglxhgz(airConData.airCon.InStatusFault.njglxhgz);
            airRealInnerStatusRepository.save(innerStatus);
        }
        if (!(airConData.airCon.InOutStatus.in.txbb == null)){
            innerUnit.setMac(airConData.mac);
            //innerUnit.setDatatime(DATEE.parse(airConData.svrCtime));
            if (!(airConData.svrCtime.equals("0001-01-01 00:00:00"))){
                innerUnit.setCtime(DATEE.parse(airConData.svrCtime));
            }
            innerUnit.setTxbb(airConData.airCon.InOutStatus.in.txbb);
            innerUnit.setTxsd(airConData.airCon.InOutStatus.in.txsd);
            innerUnit.setLnjx(airConData.airCon.InOutStatus.in.lnjx);
            innerUnit.setDpbp(airConData.airCon.InOutStatus.in.dpbp);
            innerUnit.setNjnldm(airConData.airCon.InOutStatus.in.njnldm);
            innerUnit.setDypl(airConData.airCon.InOutStatus.in.dypl);
            innerUnit.setGdfs(airConData.airCon.InOutStatus.in.gdfs);
            innerUnit.setDyzl(airConData.airCon.InOutStatus.in.dyzl);
            innerUnit.setLmzl(airConData.airCon.InOutStatus.in.lmzl);
            innerUnit.setLxdm(airConData.airCon.InOutStatus.in.lxdm);
            innerUnit.setHjgwb(airConData.airCon.InOutStatus.in.hjgwb);
            innerUnit.setNgzjgwb(airConData.airCon.InOutStatus.in.ngzjgwb);
            innerUnit.setSdcgq(airConData.airCon.InOutStatus.in.sdcgq);
            innerUnit.setFjzl(airConData.airCon.InOutStatus.in.fjzl);
            innerUnit.setFjds(airConData.airCon.InOutStatus.in.fjds);
            innerUnit.setJyxphqsjbz(airConData.airCon.InOutStatus.in.jyxphqsjbz);
            innerUnit.setJdccgn(airConData.airCon.InOutStatus.in.jdccgn);
            innerUnit.setFrgn(airConData.airCon.InOutStatus.in.frgn);
            innerUnit.setJygn(airConData.airCon.InOutStatus.in.jygn);
            innerUnit.setJkgn(airConData.airCon.InOutStatus.in.jkgn);
            innerUnit.setHqgn(airConData.airCon.InOutStatus.in.hqgn);
            innerUnit.setSsggn(airConData.airCon.InOutStatus.in.ssggn);
            innerUnit.setDsfxxz(airConData.airCon.InOutStatus.in.dpbp);
            innerUnit.setTxmh(airConData.airCon.InOutStatus.in.txmh);
            innerUnit.setJx(airConData.airCon.InOutStatus.in.jx);
            airRealInnerUnitRepository.save(innerUnit);
        }
        if (!(airConData.airCon.InOutStatus.out.txbb == null)){
            outerUnit.setMac(airConData.mac);
            //outerUnit.setDatatime(DATEE.parse(airConData.svrCtime));
            if (!(airConData.svrCtime.equals("0001-01-01 00:00:00"))){
                outerUnit.setCtime(DATEE.parse(airConData.svrCtime));
            }
            outerUnit.setTxbb(airConData.airCon.InOutStatus.out.txbb);
            outerUnit.setTxsd(airConData.airCon.InOutStatus.out.txsd);
            outerUnit.setLnjx(airConData.airCon.InOutStatus.out.lnjx);
            outerUnit.setBpdp(airConData.airCon.InOutStatus.out.bpdp);
            outerUnit.setWjnldm(airConData.airCon.InOutStatus.out.wjnldm);
            outerUnit.setGdfs(airConData.airCon.InOutStatus.out.gdfs);
            outerUnit.setDyzl(airConData.airCon.InOutStatus.out.dyzl);
            outerUnit.setSwjlx(airConData.airCon.InOutStatus.out.swjlx);
            outerUnit.setYsjxh(airConData.airCon.InOutStatus.out.ysjxh);
            outerUnit.setCflx(airConData.airCon.InOutStatus.out.cflx);
            outerUnit.setFjzl(airConData.airCon.InOutStatus.out.fjzl);
            outerUnit.setFjgs(airConData.airCon.InOutStatus.out.fjgs);
            outerUnit.setFjds(airConData.airCon.InOutStatus.out.fjds);
            outerUnit.setDplnqyrdyw(airConData.airCon.InOutStatus.out.dplnqyrdyw);
            outerUnit.setYsjyrdyw(airConData.airCon.InOutStatus.out.ysjyrdyw);
            outerUnit.setXqzyryw(airConData.airCon.InOutStatus.out.xqzyryw);
            outerUnit.setDzpzfyw(airConData.airCon.InOutStatus.out.dzpzfyw);
            outerUnit.setJxm(airConData.airCon.InOutStatus.out.jxm);
            outerUnit.setWfjipmmk(airConData.airCon.InOutStatus.out.wfjipmmk);
            outerUnit.setYsjipmmk(airConData.airCon.InOutStatus.out.ysjipmmk);
            outerUnit.setNjcxbbh(airConData.airCon.InOutStatus.out.njcxbbh);
            outerUnit.setWjcxbbh(airConData.airCon.InOutStatus.out.wjcxbbh);
            airRealOuterUnitRepository.save(outerUnit);
        }
        if (!(airConData.airCon.OutStatusFault.hsms == null)){
            outerStatus.setMac(airConData.mac);
            //outerStatus.setDatatime(DATEE.parse(airConData.svrCtime));
            if (!(airConData.svrCtime.equals("0001-01-01 00:00:00"))){
                outerStatus.setCtime(DATEE.parse(airConData.svrCtime));
            }
            outerStatus.setYsjkgzt(airConData.airCon.OutStatusFault.ysjkgzt);
            outerStatus.setHsms(airConData.airCon.OutStatusFault.hsms);
            outerStatus.setYsjyxzs(airConData.airCon.OutStatusFault.ysjyxzs);
            outerStatus.setWfj1zs(airConData.airCon.OutStatusFault.wfj1zs);
            outerStatus.setWfj2zs(airConData.airCon.OutStatusFault.wfj2zs);
            outerStatus.setYsjyxgl(airConData.airCon.OutStatusFault.ysjyxgl);
            outerStatus.setDzpzfkd(airConData.airCon.OutStatusFault.dzpzfkd);
            outerStatus.setZlmxdy(airConData.airCon.OutStatusFault.zlmxdy);
            //outerStatus.setSwpqwdxsbz(airConData.airCon.OutStatusFault.swpqwdxsbz);不存在
            //outerStatus.setSwlnqwdxsbz(airConData.airCon.OutStatusFault.swlnqwdxsbz);不存在
            //outerStatus.setSwlhjwdxsbz(airConData.airCon.OutStatusFault.swlhjwdxsbz);不存在
            outerStatus.setSwhjwd(airConData.airCon.OutStatusFault.swhjwd);
            outerStatus.setSavezt(airConData.airCon.OutStatusFault.savezt);
            outerStatus.setWjfnlzt(airConData.airCon.OutStatusFault.wjfnlzt);
            outerStatus.setWjjyzt(airConData.airCon.OutStatusFault.wjjyzt);
            outerStatus.setDzpzfzt(airConData.airCon.OutStatusFault.dzpzfzt);
            outerStatus.setSfzdjzt(airConData.airCon.OutStatusFault.sfzdjzt);
            outerStatus.setHyzt(airConData.airCon.OutStatusFault.hyzt);
            outerStatus.setPtcszt(airConData.airCon.OutStatusFault.ptcszt);
            outerStatus.setCsyq(airConData.airCon.OutStatusFault.csyq);
            outerStatus.setTscszt(airConData.airCon.OutStatusFault.tscszt);
            outerStatus.setWjrytjgz(airConData.airCon.OutStatusFault.wjrytjgz);
            outerStatus.setHszczt(airConData.airCon.OutStatusFault.hszczt);
            outerStatus.setWjacdlz(airConData.airCon.OutStatusFault.wjacdlz);
            outerStatus.setSyx(airConData.airCon.OutStatusFault.syx);
            outerStatus.setSfms(airConData.airCon.OutStatusFault.sfms);
            outerStatus.setQzcs(airConData.airCon.OutStatusFault.qzcs);
            outerStatus.setQzzr(airConData.airCon.OutStatusFault.qzzr);
            outerStatus.setQzzl(airConData.airCon.OutStatusFault.qzzl);
            outerStatus.setDredmsyxzt(airConData.airCon.OutStatusFault.dredmsyxzt);
            outerStatus.setGlggbhxjp(airConData.airCon.OutStatusFault.glggbhxjp);
            outerStatus.setMkdlbhxjp(airConData.airCon.OutStatusFault.mkdlbhxjp);
            outerStatus.setMkwdbhxjp(airConData.airCon.OutStatusFault.mkwdbhxjp);
            outerStatus.setZlmxdybhxjp(airConData.airCon.OutStatusFault.zlmxdybhxjp);
            outerStatus.setGhbhxjp(airConData.airCon.OutStatusFault.ghbhxjp);
            outerStatus.setFjdbhxjp(airConData.airCon.OutStatusFault.fjdbhxjp);
            outerStatus.setPqbhxjp(airConData.airCon.OutStatusFault.pqbhxjp);
            outerStatus.setWjacdlbhxjp(airConData.airCon.OutStatusFault.wjacdlbhxjp);
            outerStatus.setGzgwbgz(airConData.airCon.OutStatusFault.gzgwbgz);
            outerStatus.setPqgwbgz(airConData.airCon.OutStatusFault.pqgwbgz);
            outerStatus.setHjgwbgz(airConData.airCon.OutStatusFault.hjgwbgz);
            outerStatus.setSwlnqzjgwbgz(airConData.airCon.OutStatusFault.swlnqzjgwbgz);
            outerStatus.setMkgwbdlgz(airConData.airCon.OutStatusFault.mkgwbdlgz);
            outerStatus.setYsjrgzbh(airConData.airCon.OutStatusFault.ysjrgzbh);
            outerStatus.setPqbh(airConData.airCon.OutStatusFault.pqbh);
            outerStatus.setGfhbh(airConData.airCon.OutStatusFault.gfhbh);
            outerStatus.setWjacdlbh(airConData.airCon.OutStatusFault.wjacdlbh);
            outerStatus.setMkdlfobh(airConData.airCon.OutStatusFault.mkdlfobh);
            outerStatus.setMkwdbh(airConData.airCon.OutStatusFault.mkwdbh);
            outerStatus.setFjdbh(airConData.airCon.OutStatusFault.fjdbh);
            outerStatus.setGlggbh(airConData.airCon.OutStatusFault.glggbh);
            outerStatus.setYsjqnxbhqxttqx(airConData.airCon.OutStatusFault.ysjqnxbhqxttqx);
            outerStatus.setPfcglgz(airConData.airCon.OutStatusFault.pfcglgz);
            outerStatus.setZlmxdyggbh(airConData.airCon.OutStatusFault.zlmxdyggbh);
            outerStatus.setZlmxdygdbh(airConData.airCon.OutStatusFault.zlmxdygdbh);
            outerStatus.setQfbh(airConData.airCon.OutStatusFault.qfbh);
            outerStatus.setMsct(airConData.airCon.OutStatusFault.msct);
            outerStatus.setSnwjxbpp(airConData.airCon.OutStatusFault.snwjxbpp);
            outerStatus.setYtdnwjglljytxljbpp(airConData.airCon.OutStatusFault.ytdnwjglljytxljbpp);
            outerStatus.setJyxpdxgz(airConData.airCon.OutStatusFault.jyxpdxgz);
            outerStatus.setGlxhyc(airConData.airCon.OutStatusFault.glxhyc);
            outerStatus.setStfhxyc(airConData.airCon.OutStatusFault.stfhxyc);
            outerStatus.setXzkdpyc(airConData.airCon.OutStatusFault.xzkdpyc);
            outerStatus.setSwfj2gz(airConData.airCon.OutStatusFault.swfj2gz);
            outerStatus.setSwfj1gz(airConData.airCon.OutStatusFault.swfj1gz);
            outerStatus.setGwbhswfj(airConData.airCon.OutStatusFault.gwbhswfj);
            outerStatus.setXtdybh(airConData.airCon.OutStatusFault.xtdybh);
            outerStatus.setXtgybh(airConData.airCon.OutStatusFault.xtgybh);
            outerStatus.setZlmxdydlgz(airConData.airCon.OutStatusFault.zlmxdydlgz);
            outerStatus.setZjdljcgz(airConData.airCon.OutStatusFault.zjdljcgz);
            outerStatus.setDrcdgz(airConData.airCon.OutStatusFault.drcdgz);
            outerStatus.setYsjxdldljcgz(airConData.airCon.OutStatusFault.ysjxdldljcgz);
            outerStatus.setYsjsb(airConData.airCon.OutStatusFault.ysjsb);
            outerStatus.setYsjtcbh(airConData.airCon.OutStatusFault.ysjtcbh);
            outerStatus.setYsjdz(airConData.airCon.OutStatusFault.ysjdz);
            outerStatus.setQdsb(airConData.airCon.OutStatusFault.qdsb);
            outerStatus.setQdmkfw(airConData.airCon.OutStatusFault.qdmkfw);
            outerStatus.setSc(airConData.airCon.OutStatusFault.sc);
            outerStatus.setYsjbmyc(airConData.airCon.OutStatusFault.ysjbmyc);
            outerStatus.setQdbhjgwbgz(airConData.airCon.OutStatusFault.qdbhjgwbgz);
            outerStatus.setJljcqbh(airConData.airCon.OutStatusFault.jljcqbh);
            outerStatus.setWpbh(airConData.airCon.OutStatusFault.wpbh);
            outerStatus.setCgqljbh(airConData.airCon.OutStatusFault.cgqljbh);
            outerStatus.setQdbtxgz(airConData.airCon.OutStatusFault.qdbtxgz);
            outerStatus.setYsjxdlgl(airConData.airCon.OutStatusFault.ysjxdlgl);
            outerStatus.setJlsrdyyc(airConData.airCon.OutStatusFault.jlsrdyyc);
            outerStatus.setFjtsbtxgz(airConData.airCon.OutStatusFault.fjtsbtxgz);
            outerStatus.setYfgwbgz(airConData.airCon.OutStatusFault.yfgwbgz);
            outerStatus.setQfgwbgz(airConData.airCon.OutStatusFault.qfgwbgz);
            outerStatus.setSwlnqrggwbgz(airConData.airCon.OutStatusFault.swlnqrggwbgz);
            outerStatus.setSwlnqcggwbgz(airConData.airCon.OutStatusFault.swlnqcggwbgz);
            outerStatus.setLmwdgwbgz(airConData.airCon.OutStatusFault.lmwdgwbgz);
            outerStatus.setSwjlmjrqsxgz(airConData.airCon.OutStatusFault.swjlmjrqsxgz);
            outerStatus.setSwjlmjrqjdqzlgz(airConData.airCon.OutStatusFault.swjlmjrqjdqzlgz);
            outerStatus.setIpmmkwd(airConData.airCon.OutStatusFault.ipmmkwd);
            outerStatus.setDyztw(airConData.airCon.OutStatusFault.dyztw);
            outerStatus.setDlztw(airConData.airCon.OutStatusFault.dlztw);
            outerStatus.setYfgwbwd(airConData.airCon.OutStatusFault.yfgwbwd);
            outerStatus.setQfgwbwd(airConData.airCon.OutStatusFault.qfgwbwd);
            outerStatus.setSwlnqrgwd(airConData.airCon.OutStatusFault.swlnqrgwd);
            outerStatus.setSwlnqcgwd(airConData.airCon.OutStatusFault.swlnqcgwd);
            outerStatus.setYsjpqgwbh(airConData.airCon.OutStatusFault.ysjpqgwbh);
            outerStatus.setJlglbh(airConData.airCon.OutStatusFault.jlglbh);
            outerStatus.setGgwbh(airConData.airCon.OutStatusFault.ggwbh);
            outerStatus.setDeepromgz(airConData.airCon.OutStatusFault.deepromgz);
            outerStatus.setWhwdyc(airConData.airCon.OutStatusFault.whwdyc);
            outerStatus.setZrggwjp(airConData.airCon.OutStatusFault.zrggwjp);
            outerStatus.setXtyc(airConData.airCon.OutStatusFault.xtyc);
            airRealOuterStatusRepository.save(outerStatus);
        }
    }
}
