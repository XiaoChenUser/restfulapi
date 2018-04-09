package com.restfulapi.service;

import com.restfulapi.entity.dehumidifier.*;
import com.restfulapi.hbase.entity.DehumidifierData;
import com.restfulapi.hbase.utils.HbaseUtils;
import com.restfulapi.hbase.utils.StrUtils;
import com.restfulapi.repository.dehumidifier.*;
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

import static com.restfulapi.service.DehumidifierService.getDehumidifier;

@Service
public class DehumidifierTaskService {
    public static final Logger log = LoggerFactory.getLogger(DehumidifierTaskService.class);
    public static final SimpleDateFormat DATEE = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    @Autowired
    DehumidifierRealCtlRespRepository dehumidifierRealCtlRespRepository;
    @Autowired
    DehumidifierRealDeviceRepository dehumidifierRealDeviceRepository;
    @Autowired
    DehumidifierRealInnerStatusRepository dehumidifierRealInnerStatusRepository;
    @Autowired
    DehumidifierRealInnerUnitRepository dehumidifierRealInnerUnitRepository;
    @Autowired
    DehumidifierRealOuterStatusRepository dehumidifierRealOuterStatusRepository;
    @Autowired
    DehumidifierRealOuterUnitRepository dehumidifierRealOuterUnitRepository;


    public void dehumidifierTask(String starttime,String endtime) throws SQLException, NullPointerException,Exception {
        String tableNameStr = "GRIH:DataDehumidifier";
        Connection connection = HbaseUtils.getConnection();
        TableName tableName = TableName.valueOf(tableNameStr);
        Table dehumidifier = connection.getTable(tableName);
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
        ResultScanner resultScanner = dehumidifier.getScanner(scan);
        DehumidifierData dehumidifierData = new DehumidifierData();
        Result result = null;
        log.debug("正在执行定时任务，请等候！");
        while ((result = resultScanner.next()) != null) {
            dehumidifierData = getDehumidifier(result, dehumidifierData);
            dehumidifier2Mysql(dehumidifierData);
        }
        log.debug("执行完毕！");
    }

    public void testTask(String date) throws Exception {
        String tableNameStr = "GRIH:DataDehumidifier";
        Connection connection = HbaseUtils.getConnection();
        TableName tableName = TableName.valueOf(tableNameStr);
        Table dehumidifier = connection.getTable(tableName);
        Scan scan = new Scan();
        scan.setCaching(1000);
        if (StrUtils.isValidStrVal(date)){
            Filter rowFilter = new RowFilter(CompareFilter.CompareOp.EQUAL,
                    new BinaryPrefixComparator(date.getBytes()));
            scan.setFilter(rowFilter);
        }
        ResultScanner resultScanner = dehumidifier.getScanner(scan);
        DehumidifierData dehumidifierData = new DehumidifierData();
        Result result = null;
        log.debug("正在执行定时任务，请等候！");
        while ((result = resultScanner.next()) != null) {
            dehumidifierData = getDehumidifier(result, dehumidifierData);
            dehumidifier2Mysql(dehumidifierData);
        }
        log.debug("执行完毕！");
    }



    public List<DehumidifierData> scanDehumiditifierOnTime(String mac) throws SQLException, NullPointerException,Exception {
        String tableNameStr = "GRIH:DehumidifierRealTime";
        List<DehumidifierData> dehumidifierDataList = new ArrayList<DehumidifierData>();
        Connection connection = HbaseUtils.getConnection();
        TableName tableName = TableName.valueOf(tableNameStr);
        Table dehumidifier = connection.getTable(tableName);
        Scan scan = new Scan();
        scan.setCaching(1000);
        //Filter pageFilter = new PageFilter(100);
        if (StrUtils.isValidStrVal(mac)) {
            SingleColumnValueFilter macFilter = new SingleColumnValueFilter(Bytes.toBytes("DevInfoRes"),
                    Bytes.toBytes("mac"), CompareFilter.CompareOp.EQUAL, Bytes.toBytes(mac));
            macFilter.setFilterIfMissing(true);
            scan.setFilter(macFilter);
        }

        ResultScanner resultScanner = dehumidifier.getScanner(scan);
        DehumidifierData dehumidifierData = new DehumidifierData();
        Result result = null;
        log.debug("正在执行，请等候！");
        while ((result = resultScanner.next()) != null) {
            dehumidifierData = getDehumidifier(result, dehumidifierData);
            dehumidifierDataList.add(dehumidifierData);
        }
        log.debug("执行完毕！");
        return dehumidifierDataList;
    }

    public List<String> scanRowKey() throws SQLException, NullPointerException,Exception {
        String tableNameStr = "GRIH:DehumidifierRealTime";
        ArrayList<String> list = new ArrayList<String>();
        Connection connection = HbaseUtils.getConnection();
        TableName tableName = TableName.valueOf(tableNameStr);
        Table dehumidifier = connection.getTable(tableName);
        Scan scan = new Scan();
        scan.setCaching(1000);
        //Filter filter2 = new RowFilter(CompareFilter.CompareOp.EQUAL, new RegexStringComparator();
        //scan.setFilter(filter2);
        log.debug("正在查询Mac列表，请等候！");
        ResultScanner resultScanner = dehumidifier.getScanner(scan);
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


    public void dehumidifier2Mysql(DehumidifierData dehumidifierData)throws Exception{
        DehumidifierRealCtlResp ctlResp = new DehumidifierRealCtlResp();
        DehumidifierRealDevice device = new DehumidifierRealDevice();
        DehumidifierRealInnerStatus innerStatus = new DehumidifierRealInnerStatus();
        DehumidifierRealInnerUnit innerUnit = new DehumidifierRealInnerUnit();
        DehumidifierRealOuterStatus outerStatus = new DehumidifierRealOuterStatus();
        DehumidifierRealOuterUnit outerUnit = new DehumidifierRealOuterUnit();
        if (!(dehumidifierData.dehumidifier.CtlResponse ==null)){
            ctlResp.setMac(dehumidifierData.mac);
            if (!(dehumidifierData.svrCtime.equals("0001-01-01 00:00:00"))){
                ctlResp.setCtime(DATEE.parse(dehumidifierData.svrCtime));
            }
            //ctlResp.setModtime(DATEE.parse(dehumidifierData.modTime));
            ctlResp.setCobj(dehumidifierData.dehumidifier.CtlResponse.cobj);
            ctlResp.setCq(dehumidifierData.dehumidifier.CtlResponse.cq);
            ctlResp.setCsms(dehumidifierData.dehumidifier.CtlResponse.csms);
            ctlResp.setDdfs(dehumidifierData.dehumidifier.CtlResponse.ddfs);
            ctlResp.setDg(dehumidifierData.dehumidifier.CtlResponse.dg);
            ctlResp.setDnsz(dehumidifierData.dehumidifier.CtlResponse.dnsz);
            ctlResp.setJsq(dehumidifierData.dehumidifier.CtlResponse.jsq);
            ctlResp.setJsqtx(dehumidifierData.dehumidifier.CtlResponse.jsqtx);
            ctlResp.setHqzz(dehumidifierData.dehumidifier.CtlResponse.hqzz);
            ctlResp.setHqdy(dehumidifierData.dehumidifier.CtlResponse.hqdy);
            ctlResp.setJk(dehumidifierData.dehumidifier.CtlResponse.jk);
            ctlResp.setDm(dehumidifierData.dehumidifier.CtlResponse.dm);
            ctlResp.setQxjtx(dehumidifierData.dehumidifier.CtlResponse.qxjtx);
            ctlResp.setJcbfsqd(dehumidifierData.dehumidifier.CtlResponse.jcbfsqd);
            ctlResp.setWifikg(dehumidifierData.dehumidifier.CtlResponse.wifikg);
            ctlResp.setKqzlpjdw(dehumidifierData.dehumidifier.CtlResponse.kqzlpjdw);
            ctlResp.setPm25dw(dehumidifierData.dehumidifier.CtlResponse.pm25dw);
            ctlResp.setKgj(dehumidifierData.dehumidifier.CtlResponse.kgj);
            ctlResp.setMs(dehumidifierData.dehumidifier.CtlResponse.ms);
            ctlResp.setSleep(dehumidifierData.dehumidifier.CtlResponse.sleep);
            ctlResp.setSf(dehumidifierData.dehumidifier.CtlResponse.sf);
            ctlResp.setFs(dehumidifierData.dehumidifier.CtlResponse.fs);
            ctlResp.setWd(dehumidifierData.dehumidifier.CtlResponse.wd);
            ctlResp.setDs(dehumidifierData.dehumidifier.CtlResponse.ds);
            ctlResp.setDssj(dehumidifierData.dehumidifier.CtlResponse.dssj);
            ctlResp.setGz(dehumidifierData.dehumidifier.CtlResponse.gz);
            ctlResp.setFr(dehumidifierData.dehumidifier.CtlResponse.fr);
            ctlResp.setWddw(dehumidifierData.dehumidifier.CtlResponse.wddw);
            ctlResp.setHq(dehumidifierData.dehumidifier.CtlResponse.hq);
            ctlResp.setLsljydlfwql(dehumidifierData.dehumidifier.CtlResponse.lsljydlfwql);
            ctlResp.setZdqj(dehumidifierData.dehumidifier.CtlResponse.zdqj);
            ctlResp.setSxsf(dehumidifierData.dehumidifier.CtlResponse.sxsf);
            ctlResp.setZysf(dehumidifierData.dehumidifier.CtlResponse.zysf);
            ctlResp.setFm(dehumidifierData.dehumidifier.CtlResponse.fm);
            ctlResp.setWdxsms(dehumidifierData.dehumidifier.CtlResponse.wdxsms);
            ctlResp.setQcts(dehumidifierData.dehumidifier.CtlResponse.qcts);
            ctlResp.setYkj(dehumidifierData.dehumidifier.CtlResponse.ykj);
            ctlResp.setSdxs(dehumidifierData.dehumidifier.CtlResponse.sdxs);
            ctlResp.setSdsd(dehumidifierData.dehumidifier.CtlResponse.sdsd);
            ctlResp.setSaver(dehumidifierData.dehumidifier.CtlResponse.saver);
            ctlResp.setGea(dehumidifierData.dehumidifier.CtlResponse.gea);
            ctlResp.setZdqx(dehumidifierData.dehumidifier.CtlResponse.zdqx);
            ctlResp.setHf(dehumidifierData.dehumidifier.CtlResponse.hf);
            ctlResp.setHfms(dehumidifierData.dehumidifier.CtlResponse.hfms);
            ctlResp.setHsh1(dehumidifierData.dehumidifier.CtlResponse.hsh1);
            //ctlResp.setGlwqxtx(dehumidifierData.dehumidifier.CtlResponse.glwqxtx);   不存在
            ctlResp.setSb(dehumidifierData.dehumidifier.CtlResponse.sb);
            ctlResp.setSm(dehumidifierData.dehumidifier.CtlResponse.sm);
            ctlResp.setFcktled(dehumidifierData.dehumidifier.CtlResponse.fcktled);
            ctlResp.setDsxhcs(dehumidifierData.dehumidifier.CtlResponse.dsxhcs);
            ctlResp.setQz(dehumidifierData.dehumidifier.CtlResponse.qz);
            ctlResp.setDsk(dehumidifierData.dehumidifier.CtlResponse.dsk);
            ctlResp.setDsg(dehumidifierData.dehumidifier.CtlResponse.dsg);
            ctlResp.setTxsb(dehumidifierData.dehumidifier.CtlResponse.txsb);
            ctlResp.setSleepmode(dehumidifierData.dehumidifier.CtlResponse.sleepMode);
            ctlResp.setMute(dehumidifierData.dehumidifier.CtlResponse.mute);
            ctlResp.setQyxz(dehumidifierData.dehumidifier.CtlResponse.qyxz);
            ctlResp.setSm1hhwd(dehumidifierData.dehumidifier.CtlResponse.sm1hhwd);
            ctlResp.setSm2hhwd(dehumidifierData.dehumidifier.CtlResponse.sm2hhwd);
            ctlResp.setHj(dehumidifierData.dehumidifier.CtlResponse.hj);
            ctlResp.setF5(dehumidifierData.dehumidifier.CtlResponse.f5);
            ctlResp.setEx(dehumidifierData.dehumidifier.CtlResponse.ex);
            ctlResp.setSm3hhwd(dehumidifierData.dehumidifier.CtlResponse.sm3hhwd);
            ctlResp.setSm4hhwd(dehumidifierData.dehumidifier.CtlResponse.sm4hhwd);
            ctlResp.setSm5hhwd(dehumidifierData.dehumidifier.CtlResponse.sm5hhwd);
            ctlResp.setSm6hhwd(dehumidifierData.dehumidifier.CtlResponse.sm6hhwd);
            ctlResp.setSm7hhwd(dehumidifierData.dehumidifier.CtlResponse.sm7hhwd);
            ctlResp.setSm8hhwd(dehumidifierData.dehumidifier.CtlResponse.sm8hhwd);
            ctlResp.setStf(dehumidifierData.dehumidifier.CtlResponse.stf);
            ctlResp.setSxsffk(dehumidifierData.dehumidifier.CtlResponse.sxsffk);
            ctlResp.setHx(dehumidifierData.dehumidifier.CtlResponse.hx);
            ctlResp.setZnhq(dehumidifierData.dehumidifier.CtlResponse.znhq);
            ctlResp.setYhsdyddw(dehumidifierData.dehumidifier.CtlResponse.yhsdyddw);
            ctlResp.setJzcr(dehumidifierData.dehumidifier.CtlResponse.jzcr);
            ctlResp.setHjqy9(dehumidifierData.dehumidifier.CtlResponse.hjqy9);
            ctlResp.setHjqy8(dehumidifierData.dehumidifier.CtlResponse.hjqy8);
            ctlResp.setHjqy7(dehumidifierData.dehumidifier.CtlResponse.hjqy7);
            ctlResp.setHjqy6(dehumidifierData.dehumidifier.CtlResponse.hjqy6);
            ctlResp.setHjqy5(dehumidifierData.dehumidifier.CtlResponse.hjqy5);
            ctlResp.setHjqy4(dehumidifierData.dehumidifier.CtlResponse.hjqy4);
            ctlResp.setHjqy3(dehumidifierData.dehumidifier.CtlResponse.hjqy3);
            ctlResp.setHjqy2(dehumidifierData.dehumidifier.CtlResponse.hjqy2);
            ctlResp.setHjqy1(dehumidifierData.dehumidifier.CtlResponse.hjqy1);
            ctlResp.setHwyk(dehumidifierData.dehumidifier.CtlResponse.hwyk);
            ctlResp.setFault(dehumidifierData.dehumidifier.CtlResponse.fault);
            ctlResp.setZkfs(dehumidifierData.dehumidifier.CtlResponse.zkfs);
            ctlResp.setHjms(dehumidifierData.dehumidifier.CtlResponse.hjms);
            ctlResp.setXcfksxsf(dehumidifierData.dehumidifier.CtlResponse.xcfksxsf);
            ctlResp.setXcfkzczysf(dehumidifierData.dehumidifier.CtlResponse.xcfkzczysf);
            ctlResp.setXcfkyczysf(dehumidifierData.dehumidifier.CtlResponse.xcfkyczysf);
            ctlResp.setSnhjwd(dehumidifierData.dehumidifier.CtlResponse.snhjwd);
            ctlResp.setHjsdz(dehumidifierData.dehumidifier.CtlResponse.hjsdz);
            ctlResp.setSwhjwd(dehumidifierData.dehumidifier.CtlResponse.swhjwd);
            dehumidifierRealCtlRespRepository.save(ctlResp);
        }
        if (!(dehumidifierData == null)){
            device.setMac(dehumidifierData.mac);
            device.setMid(dehumidifierData.mid);
            device.setCodes(dehumidifierData.dehumidifier.codes);
            device.setEvt(dehumidifierData.evt);
            if (dehumidifierRealDeviceRepository.existsByMac(dehumidifierData.mac)){
                dehumidifierRealDeviceRepository.flush();
            }
            else {
                dehumidifierRealDeviceRepository.save(device);
            }
        }
        //if (!(dehumidifierData.dehumidifier.InStatusFault.kgjzt == null)) {
            innerStatus.setMac(dehumidifierData.mac);
            //innerStatus.setDatatime(DATEE.parse(dehumidifierData.svrCtime));
            if (!(dehumidifierData.svrCtime.equals("0001-01-01 00:00:00"))){
                innerStatus.setCtime(DATEE.parse(dehumidifierData.svrCtime));
            }
            innerStatus.setKgjzt(dehumidifierData.dehumidifier.InStatusFault.kgjzt);
            innerStatus.setYxms(dehumidifierData.dehumidifier.InStatusFault.yxms);
            innerStatus.setNfjzs(dehumidifierData.dehumidifier.InStatusFault.nfjzs);
            //innerStatus.setSnhjwdxsbz(dehumidifierData.dehumidifier.InStatusFault.snhjwdxsbz); 不存在
            //innerStatus.setSnzfqzjwdxsbz(dehumidifierData.dehumidifier.InStatusFault.snzfqzjwdxsbz);不存在
            //innerStatus.setSnsdwdxsbz(dehumidifierData.dehumidifier.InStatusFault.snsdwdxsbz);不存在
            innerStatus.setSnsdwd(dehumidifierData.dehumidifier.InStatusFault.snsdwd);
            innerStatus.setSnhjwd(dehumidifierData.dehumidifier.InStatusFault.snhjwd);
            innerStatus.setSyx(dehumidifierData.dehumidifier.InStatusFault.syx);
            innerStatus.setSfms(dehumidifierData.dehumidifier.InStatusFault.sfms);
            innerStatus.setQzcs(dehumidifierData.dehumidifier.InStatusFault.qzcs);
            innerStatus.setQzzr(dehumidifierData.dehumidifier.InStatusFault.qzzr);
            innerStatus.setQzzl(dehumidifierData.dehumidifier.InStatusFault.qzzl);
            innerStatus.setSnzfqzjwd(dehumidifierData.dehumidifier.InStatusFault.snzfqzjwd);
            innerStatus.setBcdwdz(dehumidifierData.dehumidifier.InStatusFault.bcdwdz);
            innerStatus.setSfkzwjzrwdbc(dehumidifierData.dehumidifier.InStatusFault.sfkzwjzrwdbc);
            innerStatus.setSnhjsd(dehumidifierData.dehumidifier.InStatusFault.snhjsd);
            innerStatus.setJszt(dehumidifierData.dehumidifier.InStatusFault.jszt);
            innerStatus.setSavezt(dehumidifierData.dehumidifier.InStatusFault.savezt);
            innerStatus.setHqzt(dehumidifierData.dehumidifier.InStatusFault.hqzt);
            innerStatus.setJyzt(dehumidifierData.dehumidifier.InStatusFault.jyzt);
            innerStatus.setFlfzt(dehumidifierData.dehumidifier.InStatusFault.flfzt);
            innerStatus.setFnlzt(dehumidifierData.dehumidifier.InStatusFault.fnlzt);
            innerStatus.setDfrzt(dehumidifierData.dehumidifier.InStatusFault.dfrzt);
            innerStatus.setKczt(dehumidifierData.dehumidifier.InStatusFault.kczt);
            innerStatus.setYsjzssdz(dehumidifierData.dehumidifier.InStatusFault.ysjzssdz);
            innerStatus.setDzpzfkdsdyxzt(dehumidifierData.dehumidifier.InStatusFault.dzpzfkdsdyxzt);
            innerStatus.setDzpzfkdsdz(dehumidifierData.dehumidifier.InStatusFault.dzpzfkdsdz);
            innerStatus.setWsfjsdyxzt(dehumidifierData.dehumidifier.InStatusFault.wsfjsdyxzt);
            innerStatus.setMbpqwdsdyxzt(dehumidifierData.dehumidifier.InStatusFault.mbpqwdsdyxzt);
            innerStatus.setMbgrdsdyxzt(dehumidifierData.dehumidifier.InStatusFault.mbgrdsdyxzt);
            innerStatus.setSwtjyqbz(dehumidifierData.dehumidifier.InStatusFault.swtjyqbz);
            innerStatus.setSnqtcgqgz(dehumidifierData.dehumidifier.InStatusFault.snqtcgqgz);
            innerStatus.setSnswcgqgz(dehumidifierData.dehumidifier.InStatusFault.snswcgqgz);
            innerStatus.setSnsdcgqgz(dehumidifierData.dehumidifier.InStatusFault.snsdcgqgz);
            innerStatus.setSnhjgwbgz(dehumidifierData.dehumidifier.InStatusFault.snhjgwbgz);
            innerStatus.setSnzfqzjgwbgz(dehumidifierData.dehumidifier.InStatusFault.snzfqzjgwbgz);
            innerStatus.setNjzbyscqtxgz(dehumidifierData.dehumidifier.InStatusFault.njzbyscqtxgz);
            innerStatus.setSnjsmbh(dehumidifierData.dehumidifier.InStatusFault.snjsmbh);
            innerStatus.setZdajds(dehumidifierData.dehumidifier.InStatusFault.zdajds);
            innerStatus.setHdmgz(dehumidifierData.dehumidifier.InStatusFault.hdmgz);
            innerStatus.setXzkdpyc(dehumidifierData.dehumidifier.InStatusFault.xzkdpyc);
            innerStatus.setTxmgz(dehumidifierData.dehumidifier.InStatusFault.txmgz);
            innerStatus.setJyxpdxgz(dehumidifierData.dehumidifier.InStatusFault.jyxpdxgz);
            innerStatus.setSnfjgz(dehumidifierData.dehumidifier.InStatusFault.snfjgz);
            innerStatus.setQfpbxz(dehumidifierData.dehumidifier.InStatusFault.qfpbxz);
            innerStatus.setCsfsxz(dehumidifierData.dehumidifier.InStatusFault.csfsxz);
            innerStatus.setWkjdxz(dehumidifierData.dehumidifier.InStatusFault.wkjdxz);
            innerStatus.setZysfzt(dehumidifierData.dehumidifier.InStatusFault.zysfzt);
            innerStatus.setSxsfzt(dehumidifierData.dehumidifier.InStatusFault.sxsfzt);
            innerStatus.setRszl(dehumidifierData.dehumidifier.InStatusFault.rszl);
            innerStatus.setDredmsyxzt(dehumidifierData.dehumidifier.InStatusFault.dredmsyxzt);
            innerStatus.setSpmkrfgz(dehumidifierData.dehumidifier.InStatusFault.spmkrfgz);
            innerStatus.setJcbtxjfgz(dehumidifierData.dehumidifier.InStatusFault.jcbtxjfgz);
            innerStatus.setNjwdywjgzoe(dehumidifierData.dehumidifier.InStatusFault.njwdywjgzOe);
            innerStatus.setSfjggzfc(dehumidifierData.dehumidifier.InStatusFault.sfjggzfc);
            innerStatus.setWifigzgm(dehumidifierData.dehumidifier.InStatusFault.wifigzgm);
            innerStatus.setNwjtxgz(dehumidifierData.dehumidifier.InStatusFault.nwjtxgz);
            innerStatus.setDjdzbh(dehumidifierData.dehumidifier.InStatusFault.djdzbh);
            innerStatus.setXsbhqdbgz(dehumidifierData.dehumidifier.InStatusFault.xsbhqdbgz);
            innerStatus.setDbpxsbyc(dehumidifierData.dehumidifier.InStatusFault.dbpxsbyc);
            innerStatus.setNjglxhgz(dehumidifierData.dehumidifier.InStatusFault.njglxhgz);
            dehumidifierRealInnerStatusRepository.save(innerStatus);
        //}
        //if (!(dehumidifierData.dehumidifier.InOutStatus.in.txbb == null)){
            innerUnit.setMac(dehumidifierData.mac);
            //innerUnit.setDatatime(DATEE.parse(dehumidifierData.svrCtime));
            if (!(dehumidifierData.svrCtime.equals("0001-01-01 00:00:00"))){
                innerUnit.setCtime(DATEE.parse(dehumidifierData.svrCtime));
            }
            innerUnit.setTxbb(dehumidifierData.dehumidifier.InOutStatus.in.txbb);
            innerUnit.setTxsd(dehumidifierData.dehumidifier.InOutStatus.in.txsd);
            innerUnit.setLnjx(dehumidifierData.dehumidifier.InOutStatus.in.lnjx);
            innerUnit.setDpbp(dehumidifierData.dehumidifier.InOutStatus.in.dpbp);
            innerUnit.setNjnldm(dehumidifierData.dehumidifier.InOutStatus.in.njnldm);
            innerUnit.setDypl(dehumidifierData.dehumidifier.InOutStatus.in.dypl);
            innerUnit.setGdfs(dehumidifierData.dehumidifier.InOutStatus.in.gdfs);
            innerUnit.setDyzl(dehumidifierData.dehumidifier.InOutStatus.in.dyzl);
            innerUnit.setLmzl(dehumidifierData.dehumidifier.InOutStatus.in.lmzl);
            innerUnit.setLxdm(dehumidifierData.dehumidifier.InOutStatus.in.lxdm);
            innerUnit.setHjgwb(dehumidifierData.dehumidifier.InOutStatus.in.hjgwb);
            innerUnit.setNgzjgwb(dehumidifierData.dehumidifier.InOutStatus.in.ngzjgwb);
            innerUnit.setSdcgq(dehumidifierData.dehumidifier.InOutStatus.in.sdcgq);
            innerUnit.setFjzl(dehumidifierData.dehumidifier.InOutStatus.in.fjzl);
            innerUnit.setFjds(dehumidifierData.dehumidifier.InOutStatus.in.fjds);
            innerUnit.setJyxphqsjbz(dehumidifierData.dehumidifier.InOutStatus.in.jyxphqsjbz);
            innerUnit.setJdccgn(dehumidifierData.dehumidifier.InOutStatus.in.jdccgn);
            innerUnit.setFrgn(dehumidifierData.dehumidifier.InOutStatus.in.frgn);
            innerUnit.setJygn(dehumidifierData.dehumidifier.InOutStatus.in.jygn);
            innerUnit.setJkgn(dehumidifierData.dehumidifier.InOutStatus.in.jkgn);
            innerUnit.setHqgn(dehumidifierData.dehumidifier.InOutStatus.in.hqgn);
            innerUnit.setSsggn(dehumidifierData.dehumidifier.InOutStatus.in.ssggn);
            innerUnit.setDsfxxz(dehumidifierData.dehumidifier.InOutStatus.in.dpbp);
            innerUnit.setTxmh(dehumidifierData.dehumidifier.InOutStatus.in.txmh);
            innerUnit.setJx(dehumidifierData.dehumidifier.InOutStatus.in.jx);
            dehumidifierRealInnerUnitRepository.save(innerUnit);
        //}
        //if (!(dehumidifierData.dehumidifier.InOutStatus.out.txbb == null)){
            outerUnit.setMac(dehumidifierData.mac);
            //outerUnit.setDatatime(DATEE.parse(dehumidifierData.svrCtime));
            if (!(dehumidifierData.svrCtime.equals("0001-01-01 00:00:00"))){
                outerUnit.setCtime(DATEE.parse(dehumidifierData.svrCtime));
            }
            outerUnit.setTxbb(dehumidifierData.dehumidifier.InOutStatus.out.txbb);
            outerUnit.setTxsd(dehumidifierData.dehumidifier.InOutStatus.out.txsd);
            outerUnit.setLnjx(dehumidifierData.dehumidifier.InOutStatus.out.lnjx);
            outerUnit.setBpdp(dehumidifierData.dehumidifier.InOutStatus.out.bpdp);
            outerUnit.setWjnldm(dehumidifierData.dehumidifier.InOutStatus.out.wjnldm);
            outerUnit.setGdfs(dehumidifierData.dehumidifier.InOutStatus.out.gdfs);
            outerUnit.setDyzl(dehumidifierData.dehumidifier.InOutStatus.out.dyzl);
            outerUnit.setSwjlx(dehumidifierData.dehumidifier.InOutStatus.out.swjlx);
            outerUnit.setYsjxh(dehumidifierData.dehumidifier.InOutStatus.out.ysjxh);
            outerUnit.setCflx(dehumidifierData.dehumidifier.InOutStatus.out.cflx);
            outerUnit.setFjzl(dehumidifierData.dehumidifier.InOutStatus.out.fjzl);
            outerUnit.setFjgs(dehumidifierData.dehumidifier.InOutStatus.out.fjgs);
            outerUnit.setFjds(dehumidifierData.dehumidifier.InOutStatus.out.fjds);
            outerUnit.setDplnqyrdyw(dehumidifierData.dehumidifier.InOutStatus.out.dplnqyrdyw);
            outerUnit.setYsjyrdyw(dehumidifierData.dehumidifier.InOutStatus.out.ysjyrdyw);
            outerUnit.setXqzyryw(dehumidifierData.dehumidifier.InOutStatus.out.xqzyryw);
            outerUnit.setDzpzfyw(dehumidifierData.dehumidifier.InOutStatus.out.dzpzfyw);
            outerUnit.setJxm(dehumidifierData.dehumidifier.InOutStatus.out.jxm);
            outerUnit.setWfjipmmk(dehumidifierData.dehumidifier.InOutStatus.out.wfjipmmk);
            outerUnit.setYsjipmmk(dehumidifierData.dehumidifier.InOutStatus.out.ysjipmmk);
            outerUnit.setNjcxbbh(dehumidifierData.dehumidifier.InOutStatus.out.njcxbbh);
            outerUnit.setWjcxbbh(dehumidifierData.dehumidifier.InOutStatus.out.wjcxbbh);
            dehumidifierRealOuterUnitRepository.save(outerUnit);
        //}
        //if (!(dehumidifierData.dehumidifier.OutStatusFault.hsms == null)){
            outerStatus.setMac(dehumidifierData.mac);
            //outerStatus.setDatatime(DATEE.parse(dehumidifierData.svrCtime));
            if (!(dehumidifierData.svrCtime.equals("0001-01-01 00:00:00"))){
                outerStatus.setCtime(DATEE.parse(dehumidifierData.svrCtime));
            }
            outerStatus.setYsjkgzt(dehumidifierData.dehumidifier.OutStatusFault.ysjkgzt);
            outerStatus.setHsms(dehumidifierData.dehumidifier.OutStatusFault.hsms);
            outerStatus.setYsjyxzs(dehumidifierData.dehumidifier.OutStatusFault.ysjyxzs);
            outerStatus.setWfj1zs(dehumidifierData.dehumidifier.OutStatusFault.wfj1zs);
            outerStatus.setWfj2zs(dehumidifierData.dehumidifier.OutStatusFault.wfj2zs);
            outerStatus.setYsjyxgl(dehumidifierData.dehumidifier.OutStatusFault.ysjyxgl);
            outerStatus.setDzpzfkd(dehumidifierData.dehumidifier.OutStatusFault.dzpzfkd);
            outerStatus.setZlmxdy(dehumidifierData.dehumidifier.OutStatusFault.zlmxdy);
            //outerStatus.setSwpqwdxsbz(dehumidifierData.dehumidifier.OutStatusFault.swpqwdxsbz);不存在
            //outerStatus.setSwlnqwdxsbz(dehumidifierData.dehumidifier.OutStatusFault.swlnqwdxsbz);不存在
            //outerStatus.setSwlhjwdxsbz(dehumidifierData.dehumidifier.OutStatusFault.swlhjwdxsbz);不存在
            outerStatus.setSwhjwd(dehumidifierData.dehumidifier.OutStatusFault.swhjwd);
            outerStatus.setSwlnqzjwd(dehumidifierData.dehumidifier.OutStatusFault.swlnqzjwd);
            outerStatus.setSwpqwd(dehumidifierData.dehumidifier.OutStatusFault.swpqwd);
            outerStatus.setSavezt(dehumidifierData.dehumidifier.OutStatusFault.savezt);
            outerStatus.setWjfnlzt(dehumidifierData.dehumidifier.OutStatusFault.wjfnlzt);
            outerStatus.setWjjyzt(dehumidifierData.dehumidifier.OutStatusFault.wjjyzt);
            outerStatus.setDzpzfzt(dehumidifierData.dehumidifier.OutStatusFault.dzpzfzt);
            outerStatus.setSfzdjzt(dehumidifierData.dehumidifier.OutStatusFault.sfzdjzt);
            outerStatus.setHyzt(dehumidifierData.dehumidifier.OutStatusFault.hyzt);
            outerStatus.setPtcszt(dehumidifierData.dehumidifier.OutStatusFault.ptcszt);
            outerStatus.setCsyq(dehumidifierData.dehumidifier.OutStatusFault.csyq);
            outerStatus.setTscszt(dehumidifierData.dehumidifier.OutStatusFault.tscszt);
            outerStatus.setWjrytjgz(dehumidifierData.dehumidifier.OutStatusFault.wjrytjgz);
            outerStatus.setHszczt(dehumidifierData.dehumidifier.OutStatusFault.hszczt);
            outerStatus.setWjacdlz(dehumidifierData.dehumidifier.OutStatusFault.wjacdlz);
            outerStatus.setSyx(dehumidifierData.dehumidifier.OutStatusFault.syx);
            outerStatus.setSfms(dehumidifierData.dehumidifier.OutStatusFault.sfms);
            outerStatus.setQzcs(dehumidifierData.dehumidifier.OutStatusFault.qzcs);
            outerStatus.setQzzr(dehumidifierData.dehumidifier.OutStatusFault.qzzr);
            outerStatus.setQzzl(dehumidifierData.dehumidifier.OutStatusFault.qzzl);
            outerStatus.setDredmsyxzt(dehumidifierData.dehumidifier.OutStatusFault.dredmsyxzt);
            outerStatus.setGlggbhxjp(dehumidifierData.dehumidifier.OutStatusFault.glggbhxjp);
            outerStatus.setMkdlbhxjp(dehumidifierData.dehumidifier.OutStatusFault.mkdlbhxjp);
            outerStatus.setMkwdbhxjp(dehumidifierData.dehumidifier.OutStatusFault.mkwdbhxjp);
            outerStatus.setZlmxdybhxjp(dehumidifierData.dehumidifier.OutStatusFault.zlmxdybhxjp);
            outerStatus.setGhbhxjp(dehumidifierData.dehumidifier.OutStatusFault.ghbhxjp);
            outerStatus.setFjdbhxjp(dehumidifierData.dehumidifier.OutStatusFault.fjdbhxjp);
            outerStatus.setPqbhxjp(dehumidifierData.dehumidifier.OutStatusFault.pqbhxjp);
            outerStatus.setWjacdlbhxjp(dehumidifierData.dehumidifier.OutStatusFault.wjacdlbhxjp);
            outerStatus.setGzgwbgz(dehumidifierData.dehumidifier.OutStatusFault.gzgwbgz);
            outerStatus.setPqgwbgz(dehumidifierData.dehumidifier.OutStatusFault.pqgwbgz);
            outerStatus.setHjgwbgz(dehumidifierData.dehumidifier.OutStatusFault.hjgwbgz);
            outerStatus.setSwlnqzjgwbgz(dehumidifierData.dehumidifier.OutStatusFault.swlnqzjgwbgz);
            outerStatus.setMkgwbdlgz(dehumidifierData.dehumidifier.OutStatusFault.mkgwbdlgz);
            outerStatus.setYsjrgzbh(dehumidifierData.dehumidifier.OutStatusFault.ysjrgzbh);
            outerStatus.setPqbh(dehumidifierData.dehumidifier.OutStatusFault.pqbh);
            outerStatus.setGfhbh(dehumidifierData.dehumidifier.OutStatusFault.gfhbh);
            outerStatus.setWjacdlbh(dehumidifierData.dehumidifier.OutStatusFault.wjacdlbh);
            outerStatus.setMkdlfobh(dehumidifierData.dehumidifier.OutStatusFault.mkdlfobh);
            outerStatus.setMkwdbh(dehumidifierData.dehumidifier.OutStatusFault.mkwdbh);
            outerStatus.setFjdbh(dehumidifierData.dehumidifier.OutStatusFault.fjdbh);
            outerStatus.setGlggbh(dehumidifierData.dehumidifier.OutStatusFault.glggbh);
            outerStatus.setYsjqnxbhqxttqx(dehumidifierData.dehumidifier.OutStatusFault.ysjqnxbhqxttqx);
            outerStatus.setPfcglgz(dehumidifierData.dehumidifier.OutStatusFault.pfcglgz);
            outerStatus.setZlmxdyggbh(dehumidifierData.dehumidifier.OutStatusFault.zlmxdyggbh);
            outerStatus.setZlmxdygdbh(dehumidifierData.dehumidifier.OutStatusFault.zlmxdygdbh);
            outerStatus.setQfbh(dehumidifierData.dehumidifier.OutStatusFault.qfbh);
            outerStatus.setMsct(dehumidifierData.dehumidifier.OutStatusFault.msct);
            outerStatus.setSnwjxbpp(dehumidifierData.dehumidifier.OutStatusFault.snwjxbpp);
            outerStatus.setYtdnwjglljytxljbpp(dehumidifierData.dehumidifier.OutStatusFault.ytdnwjglljytxljbpp);
            outerStatus.setJyxpdxgz(dehumidifierData.dehumidifier.OutStatusFault.jyxpdxgz);
            outerStatus.setGlxhyc(dehumidifierData.dehumidifier.OutStatusFault.glxhyc);
            outerStatus.setStfhxyc(dehumidifierData.dehumidifier.OutStatusFault.stfhxyc);
            outerStatus.setXzkdpyc(dehumidifierData.dehumidifier.OutStatusFault.xzkdpyc);
            outerStatus.setSwfj2gz(dehumidifierData.dehumidifier.OutStatusFault.swfj2gz);
            outerStatus.setSwfj1gz(dehumidifierData.dehumidifier.OutStatusFault.swfj1gz);
            outerStatus.setGwbhswfj(dehumidifierData.dehumidifier.OutStatusFault.gwbhswfj);
            outerStatus.setXtdybh(dehumidifierData.dehumidifier.OutStatusFault.xtdybh);
            outerStatus.setXtgybh(dehumidifierData.dehumidifier.OutStatusFault.xtgybh);
            outerStatus.setZlmxdydlgz(dehumidifierData.dehumidifier.OutStatusFault.zlmxdydlgz);
            outerStatus.setZjdljcgz(dehumidifierData.dehumidifier.OutStatusFault.zjdljcgz);
            outerStatus.setDrcdgz(dehumidifierData.dehumidifier.OutStatusFault.drcdgz);
            outerStatus.setYsjxdldljcgz(dehumidifierData.dehumidifier.OutStatusFault.ysjxdldljcgz);
            outerStatus.setYsjsb(dehumidifierData.dehumidifier.OutStatusFault.ysjsb);
            outerStatus.setYsjtcbh(dehumidifierData.dehumidifier.OutStatusFault.ysjtcbh);
            outerStatus.setYsjdz(dehumidifierData.dehumidifier.OutStatusFault.ysjdz);
            outerStatus.setQdsb(dehumidifierData.dehumidifier.OutStatusFault.qdsb);
            outerStatus.setQdmkfw(dehumidifierData.dehumidifier.OutStatusFault.qdmkfw);
            outerStatus.setSc(dehumidifierData.dehumidifier.OutStatusFault.sc);
            outerStatus.setYsjbmyc(dehumidifierData.dehumidifier.OutStatusFault.ysjbmyc);
            outerStatus.setQdbhjgwbgz(dehumidifierData.dehumidifier.OutStatusFault.qdbhjgwbgz);
            outerStatus.setJljcqbh(dehumidifierData.dehumidifier.OutStatusFault.jljcqbh);
            outerStatus.setWpbh(dehumidifierData.dehumidifier.OutStatusFault.wpbh);
            outerStatus.setCgqljbh(dehumidifierData.dehumidifier.OutStatusFault.cgqljbh);
            outerStatus.setQdbtxgz(dehumidifierData.dehumidifier.OutStatusFault.qdbtxgz);
            outerStatus.setYsjxdlgl(dehumidifierData.dehumidifier.OutStatusFault.ysjxdlgl);
            outerStatus.setJlsrdyyc(dehumidifierData.dehumidifier.OutStatusFault.jlsrdyyc);
            outerStatus.setFjtsbtxgz(dehumidifierData.dehumidifier.OutStatusFault.fjtsbtxgz);
            outerStatus.setYfgwbgz(dehumidifierData.dehumidifier.OutStatusFault.yfgwbgz);
            outerStatus.setQfgwbgz(dehumidifierData.dehumidifier.OutStatusFault.qfgwbgz);
            outerStatus.setSwlnqrggwbgz(dehumidifierData.dehumidifier.OutStatusFault.swlnqrggwbgz);
            outerStatus.setSwlnqcggwbgz(dehumidifierData.dehumidifier.OutStatusFault.swlnqcggwbgz);
            outerStatus.setLmwdgwbgz(dehumidifierData.dehumidifier.OutStatusFault.lmwdgwbgz);
            outerStatus.setSwjlmjrqsxgz(dehumidifierData.dehumidifier.OutStatusFault.swjlmjrqsxgz);
            outerStatus.setSwjlmjrqjdqzlgz(dehumidifierData.dehumidifier.OutStatusFault.swjlmjrqjdqzlgz);
            outerStatus.setIpmmkwd(dehumidifierData.dehumidifier.OutStatusFault.ipmmkwd);
            outerStatus.setDyztw(dehumidifierData.dehumidifier.OutStatusFault.dyztw);
            outerStatus.setDlztw(dehumidifierData.dehumidifier.OutStatusFault.dlztw);
            outerStatus.setYfgwbwd(dehumidifierData.dehumidifier.OutStatusFault.yfgwbwd);
            outerStatus.setQfgwbwd(dehumidifierData.dehumidifier.OutStatusFault.qfgwbwd);
            outerStatus.setSwlnqrgwd(dehumidifierData.dehumidifier.OutStatusFault.swlnqrgwd);
            outerStatus.setSwlnqcgwd(dehumidifierData.dehumidifier.OutStatusFault.swlnqcgwd);
            outerStatus.setYsjpqgwbh(dehumidifierData.dehumidifier.OutStatusFault.ysjpqgwbh);
            outerStatus.setJlglbh(dehumidifierData.dehumidifier.OutStatusFault.jlglbh);
            outerStatus.setGgwbh(dehumidifierData.dehumidifier.OutStatusFault.ggwbh);
            outerStatus.setDeepromgz(dehumidifierData.dehumidifier.OutStatusFault.deepromgz);
            outerStatus.setWhwdyc(dehumidifierData.dehumidifier.OutStatusFault.whwdyc);
            outerStatus.setZrggwjp(dehumidifierData.dehumidifier.OutStatusFault.zrggwjp);
            outerStatus.setXtyc(dehumidifierData.dehumidifier.OutStatusFault.xtyc);
            dehumidifierRealOuterStatusRepository.save(outerStatus);
        //}
    }

}
