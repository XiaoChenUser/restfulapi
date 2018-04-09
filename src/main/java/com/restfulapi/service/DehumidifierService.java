package com.restfulapi.service;

import com.restfulapi.common.tools.TimeZoneTool;
import com.restfulapi.entity.dehumidifier.*;
import com.restfulapi.hbase.entity.DehumidifierData;
import com.restfulapi.hbase.utils.HbaseUtils;
import com.restfulapi.hbase.utils.StrUtils;
import com.restfulapi.repository.dehumidifier.*;
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
public class DehumidifierService {
    public static final Logger log = LoggerFactory.getLogger(DehumidifierService.class);
    public static final SimpleDateFormat DATEE = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static final SimpleDateFormat DATEROWKEY = new SimpleDateFormat("yyyyMMddHHmmss");

    @Autowired
    DehumidifierCtlRespRepository dehumidifierCtlRespRepository;
    @Autowired
    DehumidifierDeviceRepository dehumidifierDeviceRepository;
    @Autowired
    DehumidifierInnerStatusRepository dehumidifierInnerStatusRepository;
    @Autowired
    DehumidifierInnerUnitRepository dehumidifierInnerUnitRepository;
    @Autowired
    DehumidifierOuterStatusRepository dehumidifierOuterStatusRepository;
    @Autowired
    DehumidifierOuterUnitRepository dehumidifierOuterUnitRepository;


    public void scanDehumidifier(String mac, String starttime, String endtime, String mid, String evt)throws SQLException, NullPointerException,Exception{
        String tableNameStr = "GRIH:DataDehumidifier";
        Connection connection = HbaseUtils.getConnection();
        TableName tableName = TableName.valueOf(tableNameStr);
        Table dataDehumidifier = connection.getTable(tableName);
        Scan scan = new Scan();
        if (StrUtils.isValidStrVal(mac)) {
            if(StrUtils.isValidStrVal(starttime)){
                String startRow = mac + DATEROWKEY.format(DATEE.parse(starttime));
                scan.setStartRow(Bytes.toBytes(startRow));
            }
            if(StrUtils.isValidStrVal(endtime)){
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
        if(StrUtils.isValidStrVal(mid)){
            SingleColumnValueFilter scvFilter1 = new SingleColumnValueFilter(Bytes.toBytes("DevInfoRes"),
                    Bytes.toBytes("mid"), CompareFilter.CompareOp.EQUAL,Bytes.toBytes(mid));
            scvFilter1.setFilterIfMissing(true);
            scan.setFilter(scvFilter1);
        }
        if(StrUtils.isValidStrVal(evt)){
            SingleColumnValueFilter scvFilter2 = new SingleColumnValueFilter(Bytes.toBytes("DevInfoRes"),
                    Bytes.toBytes("evt"), CompareFilter.CompareOp.EQUAL,Bytes.toBytes(evt));
            scvFilter2.setFilterIfMissing(true);
            scan.setFilter(scvFilter2);
        }

        ResultScanner resultScanner = dataDehumidifier.getScanner(scan);
        DehumidifierData dehumidifierData = new DehumidifierData();
        Result result = null;
        log.debug("正在执行，请等候！");
        while ((result=resultScanner.next())!=null){
            dehumidifierData = getDehumidifier(result,dehumidifierData);
            dehumidifier2Mysql(dehumidifierData);
        }
        log.debug("执行完毕！");
    }

    public void dehumidifier2Mysql(DehumidifierData dehumidifierData)throws NullPointerException,SQLException,Exception{
        DehumidifierCtlResp dehumidifierCtlResp = new DehumidifierCtlResp();
        DehumidifierDevice dehumidifierDevice = new DehumidifierDevice();
        DehumidifierInnerStatus dehumidifierInnerStatus = new DehumidifierInnerStatus();
        DehumidifierInnerUnit dehumidifierInnerUnit = new DehumidifierInnerUnit();
        DehumidifierOuterStatus dehumidifierOuterStatus = new DehumidifierOuterStatus();
        DehumidifierOuterUnit dehumidifierOuterUnit = new DehumidifierOuterUnit();
        if (!(dehumidifierData.dehumidifier.CtlResponse ==null)){
            dehumidifierCtlResp.setMac(dehumidifierData.mac);
            if (!(dehumidifierData.svrCtime.equals("0001-01-01 00:00:00"))){
                dehumidifierCtlResp.setCtime(DATEE.parse(dehumidifierData.svrCtime));
            }
            //dehumidifierCtlResp.setModtime(DATEE.parse(dehumidifierData.modTime));
            dehumidifierCtlResp.setCobj(dehumidifierData.dehumidifier.CtlResponse.cobj);
            dehumidifierCtlResp.setCq(dehumidifierData.dehumidifier.CtlResponse.cq);
            dehumidifierCtlResp.setCsms(dehumidifierData.dehumidifier.CtlResponse.csms);
            dehumidifierCtlResp.setDdfs(dehumidifierData.dehumidifier.CtlResponse.ddfs);
            dehumidifierCtlResp.setDg(dehumidifierData.dehumidifier.CtlResponse.dg);
            dehumidifierCtlResp.setDnsz(dehumidifierData.dehumidifier.CtlResponse.dnsz);
            dehumidifierCtlResp.setJsq(dehumidifierData.dehumidifier.CtlResponse.jsq);
            dehumidifierCtlResp.setJsqtx(dehumidifierData.dehumidifier.CtlResponse.jsqtx);
            dehumidifierCtlResp.setHqzz(dehumidifierData.dehumidifier.CtlResponse.hqzz);
            dehumidifierCtlResp.setHqdy(dehumidifierData.dehumidifier.CtlResponse.hqdy);
            dehumidifierCtlResp.setJk(dehumidifierData.dehumidifier.CtlResponse.jk);
            dehumidifierCtlResp.setDm(dehumidifierData.dehumidifier.CtlResponse.dm);
            dehumidifierCtlResp.setQxjtx(dehumidifierData.dehumidifier.CtlResponse.qxjtx);
            dehumidifierCtlResp.setJcbfsqd(dehumidifierData.dehumidifier.CtlResponse.jcbfsqd);
            dehumidifierCtlResp.setWifikg(dehumidifierData.dehumidifier.CtlResponse.wifikg);
            dehumidifierCtlResp.setKqzlpjdw(dehumidifierData.dehumidifier.CtlResponse.kqzlpjdw);
            dehumidifierCtlResp.setPm25dw(dehumidifierData.dehumidifier.CtlResponse.pm25dw);
            dehumidifierCtlResp.setKgj(dehumidifierData.dehumidifier.CtlResponse.kgj);
            dehumidifierCtlResp.setMs(dehumidifierData.dehumidifier.CtlResponse.ms);
            dehumidifierCtlResp.setSleep(dehumidifierData.dehumidifier.CtlResponse.sleep);
            dehumidifierCtlResp.setSf(dehumidifierData.dehumidifier.CtlResponse.sf);
            dehumidifierCtlResp.setFs(dehumidifierData.dehumidifier.CtlResponse.fs);
            dehumidifierCtlResp.setWd(dehumidifierData.dehumidifier.CtlResponse.wd);
            dehumidifierCtlResp.setDs(dehumidifierData.dehumidifier.CtlResponse.ds);
            dehumidifierCtlResp.setDssj(dehumidifierData.dehumidifier.CtlResponse.dssj);
            dehumidifierCtlResp.setGz(dehumidifierData.dehumidifier.CtlResponse.gz);
            dehumidifierCtlResp.setFr(dehumidifierData.dehumidifier.CtlResponse.fr);
            dehumidifierCtlResp.setWddw(dehumidifierData.dehumidifier.CtlResponse.wddw);
            dehumidifierCtlResp.setHq(dehumidifierData.dehumidifier.CtlResponse.hq);
            dehumidifierCtlResp.setLsljydlfwql(dehumidifierData.dehumidifier.CtlResponse.lsljydlfwql);
            dehumidifierCtlResp.setZdqj(dehumidifierData.dehumidifier.CtlResponse.zdqj);
            dehumidifierCtlResp.setSxsf(dehumidifierData.dehumidifier.CtlResponse.sxsf);
            dehumidifierCtlResp.setZysf(dehumidifierData.dehumidifier.CtlResponse.zysf);
            dehumidifierCtlResp.setFm(dehumidifierData.dehumidifier.CtlResponse.fm);
            dehumidifierCtlResp.setWdxsms(dehumidifierData.dehumidifier.CtlResponse.wdxsms);
            dehumidifierCtlResp.setQcts(dehumidifierData.dehumidifier.CtlResponse.qcts);
            dehumidifierCtlResp.setYkj(dehumidifierData.dehumidifier.CtlResponse.ykj);
            dehumidifierCtlResp.setSdxs(dehumidifierData.dehumidifier.CtlResponse.sdxs);
            dehumidifierCtlResp.setSdsd(dehumidifierData.dehumidifier.CtlResponse.sdsd);
            dehumidifierCtlResp.setSaver(dehumidifierData.dehumidifier.CtlResponse.saver);
            dehumidifierCtlResp.setGea(dehumidifierData.dehumidifier.CtlResponse.gea);
            dehumidifierCtlResp.setZdqx(dehumidifierData.dehumidifier.CtlResponse.zdqx);
            dehumidifierCtlResp.setHf(dehumidifierData.dehumidifier.CtlResponse.hf);
            dehumidifierCtlResp.setHfms(dehumidifierData.dehumidifier.CtlResponse.hfms);
            dehumidifierCtlResp.setHsh1(dehumidifierData.dehumidifier.CtlResponse.hsh1);
            //dehumidifierCtlResp.setGlwqxtx(dehumidifierData.dehumidifier.CtlResponse.glwqxtx);   不存在
            dehumidifierCtlResp.setSb(dehumidifierData.dehumidifier.CtlResponse.sb);
            dehumidifierCtlResp.setSm(dehumidifierData.dehumidifier.CtlResponse.sm);
            dehumidifierCtlResp.setFcktled(dehumidifierData.dehumidifier.CtlResponse.fcktled);
            dehumidifierCtlResp.setDsxhcs(dehumidifierData.dehumidifier.CtlResponse.dsxhcs);
            dehumidifierCtlResp.setQz(dehumidifierData.dehumidifier.CtlResponse.qz);
            dehumidifierCtlResp.setDsk(dehumidifierData.dehumidifier.CtlResponse.dsk);
            dehumidifierCtlResp.setDsg(dehumidifierData.dehumidifier.CtlResponse.dsg);
            dehumidifierCtlResp.setTxsb(dehumidifierData.dehumidifier.CtlResponse.txsb);
            dehumidifierCtlResp.setSleepmode(dehumidifierData.dehumidifier.CtlResponse.sleepMode);
            dehumidifierCtlResp.setMute(dehumidifierData.dehumidifier.CtlResponse.mute);
            dehumidifierCtlResp.setQyxz(dehumidifierData.dehumidifier.CtlResponse.qyxz);
            dehumidifierCtlResp.setSm1hhwd(dehumidifierData.dehumidifier.CtlResponse.sm1hhwd);
            dehumidifierCtlResp.setSm2hhwd(dehumidifierData.dehumidifier.CtlResponse.sm2hhwd);
            dehumidifierCtlResp.setHj(dehumidifierData.dehumidifier.CtlResponse.hj);
            dehumidifierCtlResp.setF5(dehumidifierData.dehumidifier.CtlResponse.f5);
            dehumidifierCtlResp.setEx(dehumidifierData.dehumidifier.CtlResponse.ex);
            dehumidifierCtlResp.setSm3hhwd(dehumidifierData.dehumidifier.CtlResponse.sm3hhwd);
            dehumidifierCtlResp.setSm4hhwd(dehumidifierData.dehumidifier.CtlResponse.sm4hhwd);
            dehumidifierCtlResp.setSm5hhwd(dehumidifierData.dehumidifier.CtlResponse.sm5hhwd);
            dehumidifierCtlResp.setSm6hhwd(dehumidifierData.dehumidifier.CtlResponse.sm6hhwd);
            dehumidifierCtlResp.setSm7hhwd(dehumidifierData.dehumidifier.CtlResponse.sm7hhwd);
            dehumidifierCtlResp.setSm8hhwd(dehumidifierData.dehumidifier.CtlResponse.sm8hhwd);
            dehumidifierCtlResp.setStf(dehumidifierData.dehumidifier.CtlResponse.stf);
            dehumidifierCtlResp.setSxsffk(dehumidifierData.dehumidifier.CtlResponse.sxsffk);
            dehumidifierCtlResp.setHx(dehumidifierData.dehumidifier.CtlResponse.hx);
            dehumidifierCtlResp.setZnhq(dehumidifierData.dehumidifier.CtlResponse.znhq);
            dehumidifierCtlResp.setYhsdyddw(dehumidifierData.dehumidifier.CtlResponse.yhsdyddw);
            dehumidifierCtlResp.setJzcr(dehumidifierData.dehumidifier.CtlResponse.jzcr);
            dehumidifierCtlResp.setHjqy9(dehumidifierData.dehumidifier.CtlResponse.hjqy9);
            dehumidifierCtlResp.setHjqy8(dehumidifierData.dehumidifier.CtlResponse.hjqy8);
            dehumidifierCtlResp.setHjqy7(dehumidifierData.dehumidifier.CtlResponse.hjqy7);
            dehumidifierCtlResp.setHjqy6(dehumidifierData.dehumidifier.CtlResponse.hjqy6);
            dehumidifierCtlResp.setHjqy5(dehumidifierData.dehumidifier.CtlResponse.hjqy5);
            dehumidifierCtlResp.setHjqy4(dehumidifierData.dehumidifier.CtlResponse.hjqy4);
            dehumidifierCtlResp.setHjqy3(dehumidifierData.dehumidifier.CtlResponse.hjqy3);
            dehumidifierCtlResp.setHjqy2(dehumidifierData.dehumidifier.CtlResponse.hjqy2);
            dehumidifierCtlResp.setHjqy1(dehumidifierData.dehumidifier.CtlResponse.hjqy1);
            dehumidifierCtlResp.setHwyk(dehumidifierData.dehumidifier.CtlResponse.hwyk);
            dehumidifierCtlResp.setFault(dehumidifierData.dehumidifier.CtlResponse.fault);
            dehumidifierCtlResp.setZkfs(dehumidifierData.dehumidifier.CtlResponse.zkfs);
            dehumidifierCtlResp.setHjms(dehumidifierData.dehumidifier.CtlResponse.hjms);
            dehumidifierCtlResp.setXcfksxsf(dehumidifierData.dehumidifier.CtlResponse.xcfksxsf);
            dehumidifierCtlResp.setXcfkzczysf(dehumidifierData.dehumidifier.CtlResponse.xcfkzczysf);
            dehumidifierCtlResp.setXcfkyczysf(dehumidifierData.dehumidifier.CtlResponse.xcfkyczysf);
            dehumidifierCtlResp.setSnhjwd(dehumidifierData.dehumidifier.CtlResponse.snhjwd);
            dehumidifierCtlResp.setHjsdz(dehumidifierData.dehumidifier.CtlResponse.hjsdz);
            dehumidifierCtlResp.setSwhjwd(dehumidifierData.dehumidifier.CtlResponse.swhjwd);
            dehumidifierCtlRespRepository.save(dehumidifierCtlResp);
        }
        if (!(dehumidifierData == null)){
            dehumidifierDevice.setMac(dehumidifierData.mac);
            dehumidifierDevice.setMid(dehumidifierData.mid);

            dehumidifierDevice.setCodes(dehumidifierData.dehumidifier.codes);
            dehumidifierDevice.setEvt(dehumidifierData.evt);
            if (dehumidifierDeviceRepository.existsByMac(dehumidifierData.mac)){
                dehumidifierDeviceRepository.flush();
            }
            else {
                dehumidifierDeviceRepository.save(dehumidifierDevice);
            }
        }
        //if (!(dehumidifierData.dehumidifier.InStatusFault.kgjzt == null)) {
            dehumidifierInnerStatus.setMac(dehumidifierData.mac);
            //dehumidifierInnerStatus.setDatatime(DATEE.parse(dehumidifierData.svrCtime));
            if (!(dehumidifierData.svrCtime.equals("0001-01-01 00:00:00"))){
                dehumidifierInnerStatus.setCtime(DATEE.parse(dehumidifierData.svrCtime));
            }
            dehumidifierInnerStatus.setKgjzt(dehumidifierData.dehumidifier.InStatusFault.kgjzt);
            dehumidifierInnerStatus.setYxms(dehumidifierData.dehumidifier.InStatusFault.yxms);
            dehumidifierInnerStatus.setNfjzs(dehumidifierData.dehumidifier.InStatusFault.nfjzs);
            //dehumidifierInnerStatus.setSnhjwdxsbz(dehumidifierData.dehumidifier.InStatusFault.snhjwdxsbz); 不存在
            //dehumidifierInnerStatus.setSnzfqzjwdxsbz(dehumidifierData.dehumidifier.InStatusFault.snzfqzjwdxsbz);不存在
            //dehumidifierInnerStatus.setSnsdwdxsbz(dehumidifierData.dehumidifier.InStatusFault.snsdwdxsbz);不存在
            dehumidifierInnerStatus.setSnsdwd(dehumidifierData.dehumidifier.InStatusFault.snsdwd);
            dehumidifierInnerStatus.setSnhjwd(dehumidifierData.dehumidifier.InStatusFault.snhjwd);
            dehumidifierInnerStatus.setSyx(dehumidifierData.dehumidifier.InStatusFault.syx);
            dehumidifierInnerStatus.setSfms(dehumidifierData.dehumidifier.InStatusFault.sfms);
            dehumidifierInnerStatus.setQzcs(dehumidifierData.dehumidifier.InStatusFault.qzcs);
            dehumidifierInnerStatus.setQzzr(dehumidifierData.dehumidifier.InStatusFault.qzzr);
            dehumidifierInnerStatus.setQzzl(dehumidifierData.dehumidifier.InStatusFault.qzzl);
            dehumidifierInnerStatus.setSnzfqzjwd(dehumidifierData.dehumidifier.InStatusFault.snzfqzjwd);
            dehumidifierInnerStatus.setBcdwdz(dehumidifierData.dehumidifier.InStatusFault.bcdwdz);
            dehumidifierInnerStatus.setSfkzwjzrwdbc(dehumidifierData.dehumidifier.InStatusFault.sfkzwjzrwdbc);
            dehumidifierInnerStatus.setSnhjsd(dehumidifierData.dehumidifier.InStatusFault.snhjsd);
            dehumidifierInnerStatus.setJszt(dehumidifierData.dehumidifier.InStatusFault.jszt);
            dehumidifierInnerStatus.setSavezt(dehumidifierData.dehumidifier.InStatusFault.savezt);
            dehumidifierInnerStatus.setHqzt(dehumidifierData.dehumidifier.InStatusFault.hqzt);
            dehumidifierInnerStatus.setJyzt(dehumidifierData.dehumidifier.InStatusFault.jyzt);
            dehumidifierInnerStatus.setFlfzt(dehumidifierData.dehumidifier.InStatusFault.flfzt);
            dehumidifierInnerStatus.setFnlzt(dehumidifierData.dehumidifier.InStatusFault.fnlzt);
            dehumidifierInnerStatus.setDfrzt(dehumidifierData.dehumidifier.InStatusFault.dfrzt);
            dehumidifierInnerStatus.setKczt(dehumidifierData.dehumidifier.InStatusFault.kczt);
            dehumidifierInnerStatus.setZczt(dehumidifierData.dehumidifier.InStatusFault.zczt);
            dehumidifierInnerStatus.setSbgz(dehumidifierData.dehumidifier.InStatusFault.sbgz);
            dehumidifierInnerStatus.setSyytqf(dehumidifierData.dehumidifier.InStatusFault.syytqf);
            dehumidifierInnerStatus.setNlcsztxz(dehumidifierData.dehumidifier.InStatusFault.nlcsztxz);
            dehumidifierInnerStatus.setYsjzssdz(dehumidifierData.dehumidifier.InStatusFault.ysjzssdz);
            dehumidifierInnerStatus.setDzpzfkdsdyxzt(dehumidifierData.dehumidifier.InStatusFault.dzpzfkdsdyxzt);
            dehumidifierInnerStatus.setDzpzfkdsdz(dehumidifierData.dehumidifier.InStatusFault.dzpzfkdsdz);
            dehumidifierInnerStatus.setWsfjsdyxzt(dehumidifierData.dehumidifier.InStatusFault.wsfjsdyxzt);
            dehumidifierInnerStatus.setMbpqwdsdyxzt(dehumidifierData.dehumidifier.InStatusFault.mbpqwdsdyxzt);
            dehumidifierInnerStatus.setMbgrdsdyxzt(dehumidifierData.dehumidifier.InStatusFault.mbgrdsdyxzt);
            dehumidifierInnerStatus.setSwtjyqbz(dehumidifierData.dehumidifier.InStatusFault.swtjyqbz);
            dehumidifierInnerStatus.setSnqtcgqgz(dehumidifierData.dehumidifier.InStatusFault.snqtcgqgz);
            dehumidifierInnerStatus.setSnswcgqgz(dehumidifierData.dehumidifier.InStatusFault.snswcgqgz);
            dehumidifierInnerStatus.setSnsdcgqgz(dehumidifierData.dehumidifier.InStatusFault.snsdcgqgz);
            dehumidifierInnerStatus.setSnhjgwbgz(dehumidifierData.dehumidifier.InStatusFault.snhjgwbgz);
            dehumidifierInnerStatus.setSnzfqzjgwbgz(dehumidifierData.dehumidifier.InStatusFault.snzfqzjgwbgz);
            dehumidifierInnerStatus.setNjzbyscqtxgz(dehumidifierData.dehumidifier.InStatusFault.njzbyscqtxgz);
            dehumidifierInnerStatus.setSnjsmbh(dehumidifierData.dehumidifier.InStatusFault.snjsmbh);
            dehumidifierInnerStatus.setZdajds(dehumidifierData.dehumidifier.InStatusFault.zdajds);
            dehumidifierInnerStatus.setHdmgz(dehumidifierData.dehumidifier.InStatusFault.hdmgz);
            dehumidifierInnerStatus.setXzkdpyc(dehumidifierData.dehumidifier.InStatusFault.xzkdpyc);
            dehumidifierInnerStatus.setTxmgz(dehumidifierData.dehumidifier.InStatusFault.txmgz);
            dehumidifierInnerStatus.setJyxpdxgz(dehumidifierData.dehumidifier.InStatusFault.jyxpdxgz);
            dehumidifierInnerStatus.setSnfjgz(dehumidifierData.dehumidifier.InStatusFault.snfjgz);
            dehumidifierInnerStatus.setQfpbxz(dehumidifierData.dehumidifier.InStatusFault.qfpbxz);
            dehumidifierInnerStatus.setCsfsxz(dehumidifierData.dehumidifier.InStatusFault.csfsxz);
            dehumidifierInnerStatus.setWkjdxz(dehumidifierData.dehumidifier.InStatusFault.wkjdxz);
            dehumidifierInnerStatus.setZysfzt(dehumidifierData.dehumidifier.InStatusFault.zysfzt);
            dehumidifierInnerStatus.setSxsfzt(dehumidifierData.dehumidifier.InStatusFault.sxsfzt);
            dehumidifierInnerStatus.setRszl(dehumidifierData.dehumidifier.InStatusFault.rszl);
            dehumidifierInnerStatus.setDredmsyxzt(dehumidifierData.dehumidifier.InStatusFault.dredmsyxzt);
            dehumidifierInnerStatus.setSpmkrfgz(dehumidifierData.dehumidifier.InStatusFault.spmkrfgz);
            dehumidifierInnerStatus.setJcbtxjfgz(dehumidifierData.dehumidifier.InStatusFault.jcbtxjfgz);
            dehumidifierInnerStatus.setNjwdywjgzoe(dehumidifierData.dehumidifier.InStatusFault.njwdywjgzOe);
            dehumidifierInnerStatus.setSfjggzfc(dehumidifierData.dehumidifier.InStatusFault.sfjggzfc);
            dehumidifierInnerStatus.setWifigzgm(dehumidifierData.dehumidifier.InStatusFault.wifigzgm);
            dehumidifierInnerStatus.setNwjtxgz(dehumidifierData.dehumidifier.InStatusFault.nwjtxgz);
            dehumidifierInnerStatus.setDjdzbh(dehumidifierData.dehumidifier.InStatusFault.djdzbh);
            dehumidifierInnerStatus.setXsbhqdbgz(dehumidifierData.dehumidifier.InStatusFault.xsbhqdbgz);
            dehumidifierInnerStatus.setDbpxsbyc(dehumidifierData.dehumidifier.InStatusFault.dbpxsbyc);
            dehumidifierInnerStatus.setNjglxhgz(dehumidifierData.dehumidifier.InStatusFault.njglxhgz);
            dehumidifierInnerStatusRepository.save(dehumidifierInnerStatus);
        //}
        //if (!(dehumidifierData.dehumidifier.InOutStatus.in.txbb == null)){
            dehumidifierInnerUnit.setMac(dehumidifierData.mac);
            //dehumidifierInnerUnit.setDatatime(DATEE.parse(dehumidifierData.svrCtime));
            if (!(dehumidifierData.svrCtime.equals("0001-01-01 00:00:00"))){
                dehumidifierInnerUnit.setCtime(DATEE.parse(dehumidifierData.svrCtime));
            }
            dehumidifierInnerUnit.setTxbb(dehumidifierData.dehumidifier.InOutStatus.in.txbb);
            dehumidifierInnerUnit.setTxsd(dehumidifierData.dehumidifier.InOutStatus.in.txsd);
            dehumidifierInnerUnit.setLnjx(dehumidifierData.dehumidifier.InOutStatus.in.lnjx);
            dehumidifierInnerUnit.setDpbp(dehumidifierData.dehumidifier.InOutStatus.in.dpbp);
            dehumidifierInnerUnit.setNjnldm(dehumidifierData.dehumidifier.InOutStatus.in.njnldm);
            dehumidifierInnerUnit.setDypl(dehumidifierData.dehumidifier.InOutStatus.in.dypl);
            dehumidifierInnerUnit.setGdfs(dehumidifierData.dehumidifier.InOutStatus.in.gdfs);
            dehumidifierInnerUnit.setDyzl(dehumidifierData.dehumidifier.InOutStatus.in.dyzl);
            dehumidifierInnerUnit.setLmzl(dehumidifierData.dehumidifier.InOutStatus.in.lmzl);
            dehumidifierInnerUnit.setLxdm(dehumidifierData.dehumidifier.InOutStatus.in.lxdm);
            dehumidifierInnerUnit.setHjgwb(dehumidifierData.dehumidifier.InOutStatus.in.hjgwb);
            dehumidifierInnerUnit.setNgzjgwb(dehumidifierData.dehumidifier.InOutStatus.in.ngzjgwb);
            dehumidifierInnerUnit.setSdcgq(dehumidifierData.dehumidifier.InOutStatus.in.sdcgq);
            dehumidifierInnerUnit.setFjzl(dehumidifierData.dehumidifier.InOutStatus.in.fjzl);
            dehumidifierInnerUnit.setFjds(dehumidifierData.dehumidifier.InOutStatus.in.fjds);
            dehumidifierInnerUnit.setJyxphqsjbz(dehumidifierData.dehumidifier.InOutStatus.in.jyxphqsjbz);
            dehumidifierInnerUnit.setJdccgn(dehumidifierData.dehumidifier.InOutStatus.in.jdccgn);
            dehumidifierInnerUnit.setFrgn(dehumidifierData.dehumidifier.InOutStatus.in.frgn);
            dehumidifierInnerUnit.setJygn(dehumidifierData.dehumidifier.InOutStatus.in.jygn);
            dehumidifierInnerUnit.setJkgn(dehumidifierData.dehumidifier.InOutStatus.in.jkgn);
            dehumidifierInnerUnit.setHqgn(dehumidifierData.dehumidifier.InOutStatus.in.hqgn);
            dehumidifierInnerUnit.setSsggn(dehumidifierData.dehumidifier.InOutStatus.in.ssggn);
            dehumidifierInnerUnit.setDsfxxz(dehumidifierData.dehumidifier.InOutStatus.in.dpbp);
            dehumidifierInnerUnit.setTxmh(dehumidifierData.dehumidifier.InOutStatus.in.txmh);
            dehumidifierInnerUnit.setJx(dehumidifierData.dehumidifier.InOutStatus.in.jx);
            dehumidifierInnerUnitRepository.save(dehumidifierInnerUnit);
        //}
        //if (!(dehumidifierData.dehumidifier.InOutStatus.out.txbb == null)){
            dehumidifierOuterUnit.setMac(dehumidifierData.mac);
            //dehumidifierOuterUnit.setDatatime(DATEE.parse(dehumidifierData.svrCtime));
            if (!(dehumidifierData.svrCtime.equals("0001-01-01 00:00:00"))){
                dehumidifierOuterUnit.setCtime(DATEE.parse(dehumidifierData.svrCtime));
            }
            dehumidifierOuterUnit.setTxbb(dehumidifierData.dehumidifier.InOutStatus.out.txbb);
            dehumidifierOuterUnit.setTxsd(dehumidifierData.dehumidifier.InOutStatus.out.txsd);
            dehumidifierOuterUnit.setLnjx(dehumidifierData.dehumidifier.InOutStatus.out.lnjx);
            dehumidifierOuterUnit.setBpdp(dehumidifierData.dehumidifier.InOutStatus.out.bpdp);
            dehumidifierOuterUnit.setWjnldm(dehumidifierData.dehumidifier.InOutStatus.out.wjnldm);
            dehumidifierOuterUnit.setGdfs(dehumidifierData.dehumidifier.InOutStatus.out.gdfs);
            dehumidifierOuterUnit.setDyzl(dehumidifierData.dehumidifier.InOutStatus.out.dyzl);
            dehumidifierOuterUnit.setSwjlx(dehumidifierData.dehumidifier.InOutStatus.out.swjlx);
            dehumidifierOuterUnit.setYsjxh(dehumidifierData.dehumidifier.InOutStatus.out.ysjxh);
            dehumidifierOuterUnit.setCflx(dehumidifierData.dehumidifier.InOutStatus.out.cflx);
            dehumidifierOuterUnit.setFjzl(dehumidifierData.dehumidifier.InOutStatus.out.fjzl);
            dehumidifierOuterUnit.setFjgs(dehumidifierData.dehumidifier.InOutStatus.out.fjgs);
            dehumidifierOuterUnit.setFjds(dehumidifierData.dehumidifier.InOutStatus.out.fjds);
            dehumidifierOuterUnit.setDplnqyrdyw(dehumidifierData.dehumidifier.InOutStatus.out.dplnqyrdyw);
            dehumidifierOuterUnit.setYsjyrdyw(dehumidifierData.dehumidifier.InOutStatus.out.ysjyrdyw);
            dehumidifierOuterUnit.setXqzyryw(dehumidifierData.dehumidifier.InOutStatus.out.xqzyryw);
            dehumidifierOuterUnit.setDzpzfyw(dehumidifierData.dehumidifier.InOutStatus.out.dzpzfyw);
            dehumidifierOuterUnit.setJxm(dehumidifierData.dehumidifier.InOutStatus.out.jxm);
            dehumidifierOuterUnit.setWfjipmmk(dehumidifierData.dehumidifier.InOutStatus.out.wfjipmmk);
            dehumidifierOuterUnit.setYsjipmmk(dehumidifierData.dehumidifier.InOutStatus.out.ysjipmmk);
            dehumidifierOuterUnit.setNjcxbbh(dehumidifierData.dehumidifier.InOutStatus.out.njcxbbh);
            dehumidifierOuterUnit.setWjcxbbh(dehumidifierData.dehumidifier.InOutStatus.out.wjcxbbh);
            dehumidifierOuterUnitRepository.save(dehumidifierOuterUnit);
        //}
        //if (!(dehumidifierData.dehumidifier.OutStatusFault.hsms == null)){
            dehumidifierOuterStatus.setMac(dehumidifierData.mac);
            //dehumidifierOuterStatus.setDatatime(DATEE.parse(dehumidifierData.svrCtime));
            if (!(dehumidifierData.svrCtime.equals("0001-01-01 00:00:00"))){
                dehumidifierOuterStatus.setCtime(DATEE.parse(dehumidifierData.svrCtime));
            }
            dehumidifierOuterStatus.setYsjkgzt(dehumidifierData.dehumidifier.OutStatusFault.ysjkgzt);
            dehumidifierOuterStatus.setHsms(dehumidifierData.dehumidifier.OutStatusFault.hsms);
            dehumidifierOuterStatus.setYsjyxzs(dehumidifierData.dehumidifier.OutStatusFault.ysjyxzs);
            dehumidifierOuterStatus.setWfj1zs(dehumidifierData.dehumidifier.OutStatusFault.wfj1zs);
            dehumidifierOuterStatus.setWfj2zs(dehumidifierData.dehumidifier.OutStatusFault.wfj2zs);
            dehumidifierOuterStatus.setYsjyxgl(dehumidifierData.dehumidifier.OutStatusFault.ysjyxgl);
            dehumidifierOuterStatus.setDzpzfkd(dehumidifierData.dehumidifier.OutStatusFault.dzpzfkd);
            dehumidifierOuterStatus.setZlmxdy(dehumidifierData.dehumidifier.OutStatusFault.zlmxdy);
            //dehumidifierOuterStatus.setSwpqwdxsbz(dehumidifierData.dehumidifier.OutStatusFault.swpqwdxsbz);不存在
            //dehumidifierOuterStatus.setSwlnqwdxsbz(dehumidifierData.dehumidifier.OutStatusFault.swlnqwdxsbz);不存在
            //dehumidifierOuterStatus.setSwlhjwdxsbz(dehumidifierData.dehumidifier.OutStatusFault.swlhjwdxsbz);不存在
            dehumidifierOuterStatus.setSwhjwd(dehumidifierData.dehumidifier.OutStatusFault.swhjwd);
            dehumidifierOuterStatus.setSwlnqzjwd(dehumidifierData.dehumidifier.OutStatusFault.swlnqzjwd);
            dehumidifierOuterStatus.setSwpqwd(dehumidifierData.dehumidifier.OutStatusFault.swpqwd);
            dehumidifierOuterStatus.setSavezt(dehumidifierData.dehumidifier.OutStatusFault.savezt);
            dehumidifierOuterStatus.setWjfnlzt(dehumidifierData.dehumidifier.OutStatusFault.wjfnlzt);
            dehumidifierOuterStatus.setWjjyzt(dehumidifierData.dehumidifier.OutStatusFault.wjjyzt);
            dehumidifierOuterStatus.setDzpzfzt(dehumidifierData.dehumidifier.OutStatusFault.dzpzfzt);
            dehumidifierOuterStatus.setSfzdjzt(dehumidifierData.dehumidifier.OutStatusFault.sfzdjzt);
            dehumidifierOuterStatus.setHyzt(dehumidifierData.dehumidifier.OutStatusFault.hyzt);
            dehumidifierOuterStatus.setPtcszt(dehumidifierData.dehumidifier.OutStatusFault.ptcszt);
            dehumidifierOuterStatus.setCsyq(dehumidifierData.dehumidifier.OutStatusFault.csyq);
            dehumidifierOuterStatus.setTscszt(dehumidifierData.dehumidifier.OutStatusFault.tscszt);
            dehumidifierOuterStatus.setWjrytjgz(dehumidifierData.dehumidifier.OutStatusFault.wjrytjgz);
            dehumidifierOuterStatus.setHszczt(dehumidifierData.dehumidifier.OutStatusFault.hszczt);
            dehumidifierOuterStatus.setWjacdlz(dehumidifierData.dehumidifier.OutStatusFault.wjacdlz);
            dehumidifierOuterStatus.setSyx(dehumidifierData.dehumidifier.OutStatusFault.syx);
            dehumidifierOuterStatus.setSfms(dehumidifierData.dehumidifier.OutStatusFault.sfms);
            dehumidifierOuterStatus.setQzcs(dehumidifierData.dehumidifier.OutStatusFault.qzcs);
            dehumidifierOuterStatus.setQzzr(dehumidifierData.dehumidifier.OutStatusFault.qzzr);
            dehumidifierOuterStatus.setQzzl(dehumidifierData.dehumidifier.OutStatusFault.qzzl);
            dehumidifierOuterStatus.setDredmsyxzt(dehumidifierData.dehumidifier.OutStatusFault.dredmsyxzt);
            dehumidifierOuterStatus.setGlggbhxjp(dehumidifierData.dehumidifier.OutStatusFault.glggbhxjp);
            dehumidifierOuterStatus.setMkdlbhxjp(dehumidifierData.dehumidifier.OutStatusFault.mkdlbhxjp);
            dehumidifierOuterStatus.setMkwdbhxjp(dehumidifierData.dehumidifier.OutStatusFault.mkwdbhxjp);
            dehumidifierOuterStatus.setZlmxdybhxjp(dehumidifierData.dehumidifier.OutStatusFault.zlmxdybhxjp);
            dehumidifierOuterStatus.setGhbhxjp(dehumidifierData.dehumidifier.OutStatusFault.ghbhxjp);
            dehumidifierOuterStatus.setFjdbhxjp(dehumidifierData.dehumidifier.OutStatusFault.fjdbhxjp);
            dehumidifierOuterStatus.setPqbhxjp(dehumidifierData.dehumidifier.OutStatusFault.pqbhxjp);
            dehumidifierOuterStatus.setWjacdlbhxjp(dehumidifierData.dehumidifier.OutStatusFault.wjacdlbhxjp);
            dehumidifierOuterStatus.setGzgwbgz(dehumidifierData.dehumidifier.OutStatusFault.gzgwbgz);
            dehumidifierOuterStatus.setPqgwbgz(dehumidifierData.dehumidifier.OutStatusFault.pqgwbgz);
            dehumidifierOuterStatus.setHjgwbgz(dehumidifierData.dehumidifier.OutStatusFault.hjgwbgz);
            dehumidifierOuterStatus.setSwlnqzjgwbgz(dehumidifierData.dehumidifier.OutStatusFault.swlnqzjgwbgz);
            dehumidifierOuterStatus.setMkgwbdlgz(dehumidifierData.dehumidifier.OutStatusFault.mkgwbdlgz);
            dehumidifierOuterStatus.setYsjrgzbh(dehumidifierData.dehumidifier.OutStatusFault.ysjrgzbh);
            dehumidifierOuterStatus.setPqbh(dehumidifierData.dehumidifier.OutStatusFault.pqbh);
            dehumidifierOuterStatus.setGfhbh(dehumidifierData.dehumidifier.OutStatusFault.gfhbh);
            dehumidifierOuterStatus.setWjacdlbh(dehumidifierData.dehumidifier.OutStatusFault.wjacdlbh);
            dehumidifierOuterStatus.setMkdlfobh(dehumidifierData.dehumidifier.OutStatusFault.mkdlfobh);
            dehumidifierOuterStatus.setMkwdbh(dehumidifierData.dehumidifier.OutStatusFault.mkwdbh);
            dehumidifierOuterStatus.setFjdbh(dehumidifierData.dehumidifier.OutStatusFault.fjdbh);
            dehumidifierOuterStatus.setGlggbh(dehumidifierData.dehumidifier.OutStatusFault.glggbh);
            dehumidifierOuterStatus.setYsjqnxbhqxttqx(dehumidifierData.dehumidifier.OutStatusFault.ysjqnxbhqxttqx);
            dehumidifierOuterStatus.setPfcglgz(dehumidifierData.dehumidifier.OutStatusFault.pfcglgz);
            dehumidifierOuterStatus.setZlmxdyggbh(dehumidifierData.dehumidifier.OutStatusFault.zlmxdyggbh);
            dehumidifierOuterStatus.setZlmxdygdbh(dehumidifierData.dehumidifier.OutStatusFault.zlmxdygdbh);
            dehumidifierOuterStatus.setQfbh(dehumidifierData.dehumidifier.OutStatusFault.qfbh);
            dehumidifierOuterStatus.setMsct(dehumidifierData.dehumidifier.OutStatusFault.msct);
            dehumidifierOuterStatus.setSnwjxbpp(dehumidifierData.dehumidifier.OutStatusFault.snwjxbpp);
            dehumidifierOuterStatus.setYtdnwjglljytxljbpp(dehumidifierData.dehumidifier.OutStatusFault.ytdnwjglljytxljbpp);
            dehumidifierOuterStatus.setJyxpdxgz(dehumidifierData.dehumidifier.OutStatusFault.jyxpdxgz);
            dehumidifierOuterStatus.setGlxhyc(dehumidifierData.dehumidifier.OutStatusFault.glxhyc);
            dehumidifierOuterStatus.setStfhxyc(dehumidifierData.dehumidifier.OutStatusFault.stfhxyc);
            dehumidifierOuterStatus.setXzkdpyc(dehumidifierData.dehumidifier.OutStatusFault.xzkdpyc);
            dehumidifierOuterStatus.setSwfj2gz(dehumidifierData.dehumidifier.OutStatusFault.swfj2gz);
            dehumidifierOuterStatus.setSwfj1gz(dehumidifierData.dehumidifier.OutStatusFault.swfj1gz);
            dehumidifierOuterStatus.setGwbhswfj(dehumidifierData.dehumidifier.OutStatusFault.gwbhswfj);
            dehumidifierOuterStatus.setXtdybh(dehumidifierData.dehumidifier.OutStatusFault.xtdybh);
            dehumidifierOuterStatus.setXtgybh(dehumidifierData.dehumidifier.OutStatusFault.xtgybh);
            dehumidifierOuterStatus.setZlmxdydlgz(dehumidifierData.dehumidifier.OutStatusFault.zlmxdydlgz);
            dehumidifierOuterStatus.setZjdljcgz(dehumidifierData.dehumidifier.OutStatusFault.zjdljcgz);
            dehumidifierOuterStatus.setDrcdgz(dehumidifierData.dehumidifier.OutStatusFault.drcdgz);
            dehumidifierOuterStatus.setYsjxdldljcgz(dehumidifierData.dehumidifier.OutStatusFault.ysjxdldljcgz);
            dehumidifierOuterStatus.setYsjsb(dehumidifierData.dehumidifier.OutStatusFault.ysjsb);
            dehumidifierOuterStatus.setYsjtcbh(dehumidifierData.dehumidifier.OutStatusFault.ysjtcbh);
            dehumidifierOuterStatus.setYsjdz(dehumidifierData.dehumidifier.OutStatusFault.ysjdz);
            dehumidifierOuterStatus.setQdsb(dehumidifierData.dehumidifier.OutStatusFault.qdsb);
            dehumidifierOuterStatus.setQdmkfw(dehumidifierData.dehumidifier.OutStatusFault.qdmkfw);
            dehumidifierOuterStatus.setSc(dehumidifierData.dehumidifier.OutStatusFault.sc);
            dehumidifierOuterStatus.setYsjbmyc(dehumidifierData.dehumidifier.OutStatusFault.ysjbmyc);
            dehumidifierOuterStatus.setQdbhjgwbgz(dehumidifierData.dehumidifier.OutStatusFault.qdbhjgwbgz);
            dehumidifierOuterStatus.setJljcqbh(dehumidifierData.dehumidifier.OutStatusFault.jljcqbh);
            dehumidifierOuterStatus.setWpbh(dehumidifierData.dehumidifier.OutStatusFault.wpbh);
            dehumidifierOuterStatus.setCgqljbh(dehumidifierData.dehumidifier.OutStatusFault.cgqljbh);
            dehumidifierOuterStatus.setQdbtxgz(dehumidifierData.dehumidifier.OutStatusFault.qdbtxgz);
            dehumidifierOuterStatus.setYsjxdlgl(dehumidifierData.dehumidifier.OutStatusFault.ysjxdlgl);
            dehumidifierOuterStatus.setJlsrdyyc(dehumidifierData.dehumidifier.OutStatusFault.jlsrdyyc);
            dehumidifierOuterStatus.setFjtsbtxgz(dehumidifierData.dehumidifier.OutStatusFault.fjtsbtxgz);
            dehumidifierOuterStatus.setYfgwbgz(dehumidifierData.dehumidifier.OutStatusFault.yfgwbgz);
            dehumidifierOuterStatus.setQfgwbgz(dehumidifierData.dehumidifier.OutStatusFault.qfgwbgz);
            dehumidifierOuterStatus.setSwlnqrggwbgz(dehumidifierData.dehumidifier.OutStatusFault.swlnqrggwbgz);
            dehumidifierOuterStatus.setSwlnqcggwbgz(dehumidifierData.dehumidifier.OutStatusFault.swlnqcggwbgz);
            dehumidifierOuterStatus.setLmwdgwbgz(dehumidifierData.dehumidifier.OutStatusFault.lmwdgwbgz);
            dehumidifierOuterStatus.setSwjlmjrqsxgz(dehumidifierData.dehumidifier.OutStatusFault.swjlmjrqsxgz);
            dehumidifierOuterStatus.setSwjlmjrqjdqzlgz(dehumidifierData.dehumidifier.OutStatusFault.swjlmjrqjdqzlgz);
            dehumidifierOuterStatus.setIpmmkwd(dehumidifierData.dehumidifier.OutStatusFault.ipmmkwd);
            dehumidifierOuterStatus.setDyztw(dehumidifierData.dehumidifier.OutStatusFault.dyztw);
            dehumidifierOuterStatus.setDlztw(dehumidifierData.dehumidifier.OutStatusFault.dlztw);
            dehumidifierOuterStatus.setYfgwbwd(dehumidifierData.dehumidifier.OutStatusFault.yfgwbwd);
            dehumidifierOuterStatus.setQfgwbwd(dehumidifierData.dehumidifier.OutStatusFault.qfgwbwd);
            dehumidifierOuterStatus.setSwlnqrgwd(dehumidifierData.dehumidifier.OutStatusFault.swlnqrgwd);
            dehumidifierOuterStatus.setSwlnqcgwd(dehumidifierData.dehumidifier.OutStatusFault.swlnqcgwd);
            dehumidifierOuterStatus.setYsjpqgwbh(dehumidifierData.dehumidifier.OutStatusFault.ysjpqgwbh);
            dehumidifierOuterStatus.setJlglbh(dehumidifierData.dehumidifier.OutStatusFault.jlglbh);
            dehumidifierOuterStatus.setGgwbh(dehumidifierData.dehumidifier.OutStatusFault.ggwbh);
            dehumidifierOuterStatus.setDeepromgz(dehumidifierData.dehumidifier.OutStatusFault.deepromgz);
            dehumidifierOuterStatus.setWhwdyc(dehumidifierData.dehumidifier.OutStatusFault.whwdyc);
            dehumidifierOuterStatus.setZrggwjp(dehumidifierData.dehumidifier.OutStatusFault.zrggwjp);
            dehumidifierOuterStatus.setXtyc(dehumidifierData.dehumidifier.OutStatusFault.xtyc);
            dehumidifierOuterStatusRepository.save(dehumidifierOuterStatus);
        //}
    }

    public static DehumidifierData getDehumidifier(Result result, DehumidifierData dehumidifierData)throws Exception{
        try {
            //1.DevInfoRes

            byte[] macBytes = result.getValue(Bytes.toBytes("DevInfoRes"), Bytes.toBytes("mac"));
            if (macBytes!=null){
                dehumidifierData.mac = new String(macBytes,"UTF-8");
            }

            byte[] midBytes = result.getValue(Bytes.toBytes("DevInfoRes"), Bytes.toBytes("mid"));
            if (midBytes!=null){
                dehumidifierData.mid = new String(midBytes,"UTF-8");
            }

            byte[] rowBytes = result.getValue(Bytes.toBytes("DevInfoRes"), Bytes.toBytes("rowKey"));
            if (rowBytes!=null){
                dehumidifierData.rowKey = new String(rowBytes,"UTF-8");
            }

            byte[] modTimeBytes = result.getValue(Bytes.toBytes("Time"), Bytes.toBytes("modTime"));
            if (modTimeBytes!=null){
                dehumidifierData.modTime = TimeZoneTool.formatTimeAfterEight(new String(modTimeBytes,"UTF-8"));
            }

            byte[] svrCtimeBytes = result.getValue(Bytes.toBytes("Time"), Bytes.toBytes("svrCtime"));
            if (svrCtimeBytes!=null){
                dehumidifierData.svrCtime = TimeZoneTool.formatTimeAfterEight(new String(svrCtimeBytes,"UTF-8"));
            }

            byte[] insTimeBytes = result.getValue(Bytes.toBytes("Time"), Bytes.toBytes("insTime"));
            if (insTimeBytes!=null){
                dehumidifierData.insTime = TimeZoneTool.formatTimeAfterEight(new String(insTimeBytes,"UTF-8"));
            }

            byte[] evtBytes = result.getValue(Bytes.toBytes("DevInfoRes"), Bytes.toBytes("evt"));
            if (evtBytes!=null){
                dehumidifierData.evt = new String(evtBytes,"UTF-8");
            }

            dehumidifierData.dehumidifier = new DehumidifierData.Dehumidifier();
            byte[] codeBytes = result.getValue(Bytes.toBytes("DevInfoRes"),Bytes.toBytes("codes"));
            if(codeBytes!=null) {
                dehumidifierData.dehumidifier.codes = new String(codeBytes, "UTF-8");
            }
            byte[] origin;
            
            //2.CtlResponse
            dehumidifierData.dehumidifier.CtlResponse = new DehumidifierData.Dehumidifier.CtlResponse();
            origin = result.getValue(Bytes.toBytes("CtlResponse"),Bytes.toBytes("origin"));
            if(origin!=null && origin.length>0){
                dehumidifierData.dehumidifier.CtlResponse.jsq=new String(result.getValue(Bytes.toBytes("CtlResponse"),Bytes.toBytes("jsq")),"UTF-8");
                dehumidifierData.dehumidifier.CtlResponse.jsqtx=new String(result.getValue(Bytes.toBytes("CtlResponse"),Bytes.toBytes("jsqtx")),"UTF-8");
                dehumidifierData.dehumidifier.CtlResponse.hqzz=new String(result.getValue(Bytes.toBytes("CtlResponse"),Bytes.toBytes("hqzz")),"UTF-8");
                dehumidifierData.dehumidifier.CtlResponse.hqdy=new String(result.getValue(Bytes.toBytes("CtlResponse"),Bytes.toBytes("hqdy")),"UTF-8");
                dehumidifierData.dehumidifier.CtlResponse.jk=new String(result.getValue(Bytes.toBytes("CtlResponse"),Bytes.toBytes("jk")),"UTF-8");
                dehumidifierData.dehumidifier.CtlResponse.dm=new String(result.getValue(Bytes.toBytes("CtlResponse"),Bytes.toBytes("dm")),"UTF-8");
                dehumidifierData.dehumidifier.CtlResponse.qxjtx=new String(result.getValue(Bytes.toBytes("CtlResponse"),Bytes.toBytes("qxjtx")),"UTF-8");
                dehumidifierData.dehumidifier.CtlResponse.jcbfsqd=new String(result.getValue(Bytes.toBytes("CtlResponse"),Bytes.toBytes("jcbfsqd")),"UTF-8");
                dehumidifierData.dehumidifier.CtlResponse.wifikg=new String(result.getValue(Bytes.toBytes("CtlResponse"),Bytes.toBytes("wifikg")),"UTF-8");
                dehumidifierData.dehumidifier.CtlResponse.wifihfcc=new String(result.getValue(Bytes.toBytes("CtlResponse"),Bytes.toBytes("wifihfcc")),"UTF-8");
                dehumidifierData.dehumidifier.CtlResponse.kqzlpjdw=new String(result.getValue(Bytes.toBytes("CtlResponse"),Bytes.toBytes("kqzlpjdw")),"UTF-8");
                dehumidifierData.dehumidifier.CtlResponse.pm25dw=new String(result.getValue(Bytes.toBytes("CtlResponse"),Bytes.toBytes("pm25dw")),"UTF-8");
                dehumidifierData.dehumidifier.CtlResponse.kgj=new String(result.getValue(Bytes.toBytes("CtlResponse"),Bytes.toBytes("kgj")),"UTF-8");
                dehumidifierData.dehumidifier.CtlResponse.ms=new String(result.getValue(Bytes.toBytes("CtlResponse"),Bytes.toBytes("ms")),"UTF-8");
                dehumidifierData.dehumidifier.CtlResponse.sleep=new String(result.getValue(Bytes.toBytes("CtlResponse"),Bytes.toBytes("sleep")),"UTF-8");
                dehumidifierData.dehumidifier.CtlResponse.sf=new String(result.getValue(Bytes.toBytes("CtlResponse"),Bytes.toBytes("sf")),"UTF-8");
                dehumidifierData.dehumidifier.CtlResponse.fs=new String(result.getValue(Bytes.toBytes("CtlResponse"),Bytes.toBytes("fs")),"UTF-8");
                dehumidifierData.dehumidifier.CtlResponse.wd=new String(result.getValue(Bytes.toBytes("CtlResponse"),Bytes.toBytes("wd")),"UTF-8");
                dehumidifierData.dehumidifier.CtlResponse.ds=new String(result.getValue(Bytes.toBytes("CtlResponse"),Bytes.toBytes("ds")),"UTF-8");
                dehumidifierData.dehumidifier.CtlResponse.dssj=new String(result.getValue(Bytes.toBytes("CtlResponse"),Bytes.toBytes("dssj")),"UTF-8");
                dehumidifierData.dehumidifier.CtlResponse.gz=new String(result.getValue(Bytes.toBytes("CtlResponse"),Bytes.toBytes("gz")),"UTF-8");
                dehumidifierData.dehumidifier.CtlResponse.fr=new String(result.getValue(Bytes.toBytes("CtlResponse"),Bytes.toBytes("fr")),"UTF-8");
                dehumidifierData.dehumidifier.CtlResponse.dg=new String(result.getValue(Bytes.toBytes("CtlResponse"),Bytes.toBytes("dg")),"UTF-8");
                dehumidifierData.dehumidifier.CtlResponse.cq=new String(result.getValue(Bytes.toBytes("CtlResponse"),Bytes.toBytes("cq")),"UTF-8");
                dehumidifierData.dehumidifier.CtlResponse.wddw=new String(result.getValue(Bytes.toBytes("CtlResponse"),Bytes.toBytes("wddw")),"UTF-8");
                dehumidifierData.dehumidifier.CtlResponse.hq=new String(result.getValue(Bytes.toBytes("CtlResponse"),Bytes.toBytes("hq")),"UTF-8");
                dehumidifierData.dehumidifier.CtlResponse.lsljydlfwql=new String(result.getValue(Bytes.toBytes("CtlResponse"),Bytes.toBytes("lsljydlfwql")),"UTF-8");
                dehumidifierData.dehumidifier.CtlResponse.zdqj=new String(result.getValue(Bytes.toBytes("CtlResponse"),Bytes.toBytes("zdqj")),"UTF-8");
                dehumidifierData.dehumidifier.CtlResponse.sxsf=new String(result.getValue(Bytes.toBytes("CtlResponse"),Bytes.toBytes("sxsf")),"UTF-8");
                dehumidifierData.dehumidifier.CtlResponse.zysf=new String(result.getValue(Bytes.toBytes("CtlResponse"),Bytes.toBytes("zysf")),"UTF-8");
                dehumidifierData.dehumidifier.CtlResponse.fm=new String(result.getValue(Bytes.toBytes("CtlResponse"),Bytes.toBytes("fm")),"UTF-8");
                dehumidifierData.dehumidifier.CtlResponse.wdxsms=new String(result.getValue(Bytes.toBytes("CtlResponse"),Bytes.toBytes("wdxsms")),"UTF-8");
                dehumidifierData.dehumidifier.CtlResponse.qcts=new String(result.getValue(Bytes.toBytes("CtlResponse"),Bytes.toBytes("qcts")),"UTF-8");
                dehumidifierData.dehumidifier.CtlResponse.ykj=new String(result.getValue(Bytes.toBytes("CtlResponse"),Bytes.toBytes("ykj")),"UTF-8");
                dehumidifierData.dehumidifier.CtlResponse.sdxs=new String(result.getValue(Bytes.toBytes("CtlResponse"),Bytes.toBytes("sdxs")),"UTF-8");
                dehumidifierData.dehumidifier.CtlResponse.sdsd=new String(result.getValue(Bytes.toBytes("CtlResponse"),Bytes.toBytes("sdsd")),"UTF-8");
                dehumidifierData.dehumidifier.CtlResponse.saver=new String(result.getValue(Bytes.toBytes("CtlResponse"),Bytes.toBytes("saver")),"UTF-8");
                dehumidifierData.dehumidifier.CtlResponse.cobj=new String(result.getValue(Bytes.toBytes("CtlResponse"),Bytes.toBytes("cobj")),"UTF-8");
                dehumidifierData.dehumidifier.CtlResponse.gea=new String(result.getValue(Bytes.toBytes("CtlResponse"),Bytes.toBytes("gea")),"UTF-8");
                dehumidifierData.dehumidifier.CtlResponse.zdqx=new String(result.getValue(Bytes.toBytes("CtlResponse"),Bytes.toBytes("zdqx")),"UTF-8");
                dehumidifierData.dehumidifier.CtlResponse.hf=new String(result.getValue(Bytes.toBytes("CtlResponse"),Bytes.toBytes("hf")),"UTF-8");
                dehumidifierData.dehumidifier.CtlResponse.hfms=new String(result.getValue(Bytes.toBytes("CtlResponse"),Bytes.toBytes("hfms")),"UTF-8");
                dehumidifierData.dehumidifier.CtlResponse.hsh1=new String(result.getValue(Bytes.toBytes("CtlResponse"),Bytes.toBytes("hsh1")),"UTF-8");
                dehumidifierData.dehumidifier.CtlResponse.yuyin=new String(result.getValue(Bytes.toBytes("CtlResponse"),Bytes.toBytes("yuyin")),"UTF-8");
                dehumidifierData.dehumidifier.CtlResponse.sb=new String(result.getValue(Bytes.toBytes("CtlResponse"),Bytes.toBytes("sb")),"UTF-8");
                dehumidifierData.dehumidifier.CtlResponse.sm=new String(result.getValue(Bytes.toBytes("CtlResponse"),Bytes.toBytes("sm")),"UTF-8");
                dehumidifierData.dehumidifier.CtlResponse.fcktled=new String(result.getValue(Bytes.toBytes("CtlResponse"),Bytes.toBytes("fcktled")),"UTF-8");
                dehumidifierData.dehumidifier.CtlResponse.dsxhcs=new String(result.getValue(Bytes.toBytes("CtlResponse"),Bytes.toBytes("dsxhcs")),"UTF-8");
                dehumidifierData.dehumidifier.CtlResponse.qz=new String(result.getValue(Bytes.toBytes("CtlResponse"),Bytes.toBytes("qz")),"UTF-8");
                dehumidifierData.dehumidifier.CtlResponse.dsk=new String(result.getValue(Bytes.toBytes("CtlResponse"),Bytes.toBytes("dsk")),"UTF-8");
                dehumidifierData.dehumidifier.CtlResponse.dsg=new String(result.getValue(Bytes.toBytes("CtlResponse"),Bytes.toBytes("dsg")),"UTF-8");
                dehumidifierData.dehumidifier.CtlResponse.csms=new String(result.getValue(Bytes.toBytes("CtlResponse"),Bytes.toBytes("csms")),"UTF-8");
                dehumidifierData.dehumidifier.CtlResponse.txsb=new String(result.getValue(Bytes.toBytes("CtlResponse"),Bytes.toBytes("txsb")),"UTF-8");
                dehumidifierData.dehumidifier.CtlResponse.sleepMode=new String(result.getValue(Bytes.toBytes("CtlResponse"),Bytes.toBytes("sleepMode")),"UTF-8");
                dehumidifierData.dehumidifier.CtlResponse.mute=new String(result.getValue(Bytes.toBytes("CtlResponse"),Bytes.toBytes("mute")),"UTF-8");
                dehumidifierData.dehumidifier.CtlResponse.qyxz=new String(result.getValue(Bytes.toBytes("CtlResponse"),Bytes.toBytes("qyxz")),"UTF-8");
                dehumidifierData.dehumidifier.CtlResponse.sm1hhwd=new String(result.getValue(Bytes.toBytes("CtlResponse"),Bytes.toBytes("sm1hhwd")),"UTF-8");
                dehumidifierData.dehumidifier.CtlResponse.sm2hhwd=new String(result.getValue(Bytes.toBytes("CtlResponse"),Bytes.toBytes("sm2hhwd")),"UTF-8");
                dehumidifierData.dehumidifier.CtlResponse.hj=new String(result.getValue(Bytes.toBytes("CtlResponse"),Bytes.toBytes("hj")),"UTF-8");
                dehumidifierData.dehumidifier.CtlResponse.f5=new String(result.getValue(Bytes.toBytes("CtlResponse"),Bytes.toBytes("f5")),"UTF-8");
                dehumidifierData.dehumidifier.CtlResponse.ddfs=new String(result.getValue(Bytes.toBytes("CtlResponse"),Bytes.toBytes("ddfs")),"UTF-8");
                dehumidifierData.dehumidifier.CtlResponse.ex=new String(result.getValue(Bytes.toBytes("CtlResponse"),Bytes.toBytes("ex")),"UTF-8");
                dehumidifierData.dehumidifier.CtlResponse.sm3hhwd=new String(result.getValue(Bytes.toBytes("CtlResponse"),Bytes.toBytes("sm3hhwd")),"UTF-8");
                dehumidifierData.dehumidifier.CtlResponse.sm4hhwd=new String(result.getValue(Bytes.toBytes("CtlResponse"),Bytes.toBytes("sm4hhwd")),"UTF-8");
                dehumidifierData.dehumidifier.CtlResponse.sm5hhwd=new String(result.getValue(Bytes.toBytes("CtlResponse"),Bytes.toBytes("sm5hhwd")),"UTF-8");
                dehumidifierData.dehumidifier.CtlResponse.sm6hhwd=new String(result.getValue(Bytes.toBytes("CtlResponse"),Bytes.toBytes("sm6hhwd")),"UTF-8");
                dehumidifierData.dehumidifier.CtlResponse.sm7hhwd=new String(result.getValue(Bytes.toBytes("CtlResponse"),Bytes.toBytes("sm7hhwd")),"UTF-8");
                dehumidifierData.dehumidifier.CtlResponse.sm8hhwd=new String(result.getValue(Bytes.toBytes("CtlResponse"),Bytes.toBytes("sm8hhwd")),"UTF-8");
                dehumidifierData.dehumidifier.CtlResponse.stf=new String(result.getValue(Bytes.toBytes("CtlResponse"),Bytes.toBytes("stf")),"UTF-8");
                dehumidifierData.dehumidifier.CtlResponse.sxsffk=new String(result.getValue(Bytes.toBytes("CtlResponse"),Bytes.toBytes("sxsffk")),"UTF-8");

                byte[] hxBytes = result.getValue(Bytes.toBytes("CtlResponse"),Bytes.toBytes("hx"));
                if(hxBytes!=null) {
                    dehumidifierData.dehumidifier.CtlResponse.hx = new String(hxBytes, "UTF-8");
                }
                byte[] znhqBytes = result.getValue(Bytes.toBytes("CtlResponse"),Bytes.toBytes("znhq"));
                if(znhqBytes!=null) {
                    dehumidifierData.dehumidifier.CtlResponse.znhq = new String(znhqBytes, "UTF-8");
                }
                dehumidifierData.dehumidifier.CtlResponse.dnsz=new String(result.getValue(Bytes.toBytes("CtlResponse"),Bytes.toBytes("dnsz")),"UTF-8");
                dehumidifierData.dehumidifier.CtlResponse.yhsdyddw=new String(result.getValue(Bytes.toBytes("CtlResponse"),Bytes.toBytes("yhsdyddw")),"UTF-8");
                dehumidifierData.dehumidifier.CtlResponse.jzcr=new String(result.getValue(Bytes.toBytes("CtlResponse"),Bytes.toBytes("jzcr")),"UTF-8");
                dehumidifierData.dehumidifier.CtlResponse.hjqy9=new String(result.getValue(Bytes.toBytes("CtlResponse"),Bytes.toBytes("hjqy9")),"UTF-8");
                dehumidifierData.dehumidifier.CtlResponse.hjqy8=new String(result.getValue(Bytes.toBytes("CtlResponse"),Bytes.toBytes("hjqy8")),"UTF-8");
                dehumidifierData.dehumidifier.CtlResponse.hjqy7=new String(result.getValue(Bytes.toBytes("CtlResponse"),Bytes.toBytes("hjqy7")),"UTF-8");
                dehumidifierData.dehumidifier.CtlResponse.hjqy6=new String(result.getValue(Bytes.toBytes("CtlResponse"),Bytes.toBytes("hjqy6")),"UTF-8");
                dehumidifierData.dehumidifier.CtlResponse.hjqy5=new String(result.getValue(Bytes.toBytes("CtlResponse"),Bytes.toBytes("hjqy5")),"UTF-8");
                dehumidifierData.dehumidifier.CtlResponse.hjqy4=new String(result.getValue(Bytes.toBytes("CtlResponse"),Bytes.toBytes("hjqy4")),"UTF-8");
                dehumidifierData.dehumidifier.CtlResponse.hjqy3=new String(result.getValue(Bytes.toBytes("CtlResponse"),Bytes.toBytes("hjqy3")),"UTF-8");
                dehumidifierData.dehumidifier.CtlResponse.hjqy2=new String(result.getValue(Bytes.toBytes("CtlResponse"),Bytes.toBytes("hjqy2")),"UTF-8");
                dehumidifierData.dehumidifier.CtlResponse.hjqy1=new String(result.getValue(Bytes.toBytes("CtlResponse"),Bytes.toBytes("hjqy1")),"UTF-8");
                dehumidifierData.dehumidifier.CtlResponse.hwyk=new String(result.getValue(Bytes.toBytes("CtlResponse"),Bytes.toBytes("hwyk")),"UTF-8");

                byte[] faultBytes = result.getValue(Bytes.toBytes("CtlResponse"),Bytes.toBytes("fault"));
                if(faultBytes!=null) {
                    dehumidifierData.dehumidifier.CtlResponse.fault = new String(faultBytes, "UTF-8");
                }
                dehumidifierData.dehumidifier.CtlResponse.zkfs=new String(result.getValue(Bytes.toBytes("CtlResponse"),Bytes.toBytes("zkfs")),"UTF-8");
                dehumidifierData.dehumidifier.CtlResponse.hjms=new String(result.getValue(Bytes.toBytes("CtlResponse"),Bytes.toBytes("hjms")),"UTF-8");
                dehumidifierData.dehumidifier.CtlResponse.xcfksxsf=new String(result.getValue(Bytes.toBytes("CtlResponse"),Bytes.toBytes("xcfksxsf")),"UTF-8");
                dehumidifierData.dehumidifier.CtlResponse.xcfkzczysf=new String(result.getValue(Bytes.toBytes("CtlResponse"),Bytes.toBytes("xcfkzczysf")),"UTF-8");
                dehumidifierData.dehumidifier.CtlResponse.xcfkyczysf=new String(result.getValue(Bytes.toBytes("CtlResponse"),Bytes.toBytes("xcfkyczysf")),"UTF-8");
                dehumidifierData.dehumidifier.CtlResponse.snhjwd=new String(result.getValue(Bytes.toBytes("CtlResponse"),Bytes.toBytes("snhjwd")),"UTF-8");
                dehumidifierData.dehumidifier.CtlResponse.hjsdz=new String(result.getValue(Bytes.toBytes("CtlResponse"),Bytes.toBytes("hjsdz")),"UTF-8");
                dehumidifierData.dehumidifier.CtlResponse.swhjwd=new String(result.getValue(Bytes.toBytes("CtlResponse"),Bytes.toBytes("swhjwd")),"UTF-8");
            }

            dehumidifierData.dehumidifier.InOutStatus = new DehumidifierData.Dehumidifier.InOutStatus();
            dehumidifierData.dehumidifier.InOutStatus.in = new DehumidifierData.Dehumidifier.InOutStatus.In();
            dehumidifierData.dehumidifier.InOutStatus.out = new DehumidifierData.Dehumidifier.InOutStatus.Out();
            origin = result.getValue(Bytes.toBytes("InStatus"),Bytes.toBytes("origin"));
            if(origin!=null && origin.length>0){
                dehumidifierData.dehumidifier.InOutStatus.in.txbb=new String(result.getValue(Bytes.toBytes("InStatus"),Bytes.toBytes("txbb")),"UTF-8");
                dehumidifierData.dehumidifier.InOutStatus.in.txsd=new String(result.getValue(Bytes.toBytes("InStatus"),Bytes.toBytes("txsd")),"UTF-8");
                dehumidifierData.dehumidifier.InOutStatus.in.lnjx=new String(result.getValue(Bytes.toBytes("InStatus"),Bytes.toBytes("lnjx")),"UTF-8");
                dehumidifierData.dehumidifier.InOutStatus.in.dpbp=new String(result.getValue(Bytes.toBytes("InStatus"),Bytes.toBytes("dpbp")),"UTF-8");
                dehumidifierData.dehumidifier.InOutStatus.in.njnldm=new String(result.getValue(Bytes.toBytes("InStatus"),Bytes.toBytes("njnldm")),"UTF-8");
                dehumidifierData.dehumidifier.InOutStatus.in.dypl=new String(result.getValue(Bytes.toBytes("InStatus"),Bytes.toBytes("dypl")),"UTF-8");
                dehumidifierData.dehumidifier.InOutStatus.in.gdfs=new String(result.getValue(Bytes.toBytes("InStatus"),Bytes.toBytes("gdfs")),"UTF-8");
                dehumidifierData.dehumidifier.InOutStatus.in.dyzl=new String(result.getValue(Bytes.toBytes("InStatus"),Bytes.toBytes("dyzl")),"UTF-8");
                dehumidifierData.dehumidifier.InOutStatus.in.lmzl=new String(result.getValue(Bytes.toBytes("InStatus"),Bytes.toBytes("lmzl")),"UTF-8");
                dehumidifierData.dehumidifier.InOutStatus.in.lxdm=new String(result.getValue(Bytes.toBytes("InStatus"),Bytes.toBytes("lxdm")),"UTF-8");
                dehumidifierData.dehumidifier.InOutStatus.in.hjgwb=new String(result.getValue(Bytes.toBytes("InStatus"),Bytes.toBytes("hjgwb")),"UTF-8");
                dehumidifierData.dehumidifier.InOutStatus.in.ngzjgwb=new String(result.getValue(Bytes.toBytes("InStatus"),Bytes.toBytes("ngzjgwb")),"UTF-8");
                dehumidifierData.dehumidifier.InOutStatus.in.sdcgq=new String(result.getValue(Bytes.toBytes("InStatus"),Bytes.toBytes("sdcgq")),"UTF-8");
                dehumidifierData.dehumidifier.InOutStatus.in.fjzl=new String(result.getValue(Bytes.toBytes("InStatus"),Bytes.toBytes("fjzl")),"UTF-8");
                dehumidifierData.dehumidifier.InOutStatus.in.fjds=new String(result.getValue(Bytes.toBytes("InStatus"),Bytes.toBytes("fjds")),"UTF-8");
                dehumidifierData.dehumidifier.InOutStatus.in.jyxphqsjbz=new String(result.getValue(Bytes.toBytes("InStatus"),Bytes.toBytes("jyxphqsjbz")),"UTF-8");
                dehumidifierData.dehumidifier.InOutStatus.in.jdccgn=new String(result.getValue(Bytes.toBytes("InStatus"),Bytes.toBytes("jdccgn")),"UTF-8");
                dehumidifierData.dehumidifier.InOutStatus.in.frgn=new String(result.getValue(Bytes.toBytes("InStatus"),Bytes.toBytes("frgn")),"UTF-8");
                dehumidifierData.dehumidifier.InOutStatus.in.jygn=new String(result.getValue(Bytes.toBytes("InStatus"),Bytes.toBytes("jygn")),"UTF-8");
                dehumidifierData.dehumidifier.InOutStatus.in.jkgn=new String(result.getValue(Bytes.toBytes("InStatus"),Bytes.toBytes("jkgn")),"UTF-8");
                dehumidifierData.dehumidifier.InOutStatus.in.hqgn=new String(result.getValue(Bytes.toBytes("InStatus"),Bytes.toBytes("hqgn")),"UTF-8");
                dehumidifierData.dehumidifier.InOutStatus.in.ssggn=new String(result.getValue(Bytes.toBytes("InStatus"),Bytes.toBytes("ssggn")),"UTF-8");
                dehumidifierData.dehumidifier.InOutStatus.in.dsfsxz=new String(result.getValue(Bytes.toBytes("InStatus"),Bytes.toBytes("dsfsxz")),"UTF-8");
                dehumidifierData.dehumidifier.InOutStatus.in.txmh=new String(result.getValue(Bytes.toBytes("InStatus"),Bytes.toBytes("txmh")),"UTF-8");
                dehumidifierData.dehumidifier.InOutStatus.in.jx=new String(result.getValue(Bytes.toBytes("InStatus"),Bytes.toBytes("jx")),"UTF-8");

                dehumidifierData.dehumidifier.InOutStatus.out.txbb=new String(result.getValue(Bytes.toBytes("OutStatus"),Bytes.toBytes("txbb")),"UTF-8");
                dehumidifierData.dehumidifier.InOutStatus.out.txsd=new String(result.getValue(Bytes.toBytes("OutStatus"),Bytes.toBytes("txsd")),"UTF-8");
                dehumidifierData.dehumidifier.InOutStatus.out.lnjx=new String(result.getValue(Bytes.toBytes("OutStatus"),Bytes.toBytes("lnjx")),"UTF-8");
                dehumidifierData.dehumidifier.InOutStatus.out.bpdp=new String(result.getValue(Bytes.toBytes("OutStatus"),Bytes.toBytes("bpdp")),"UTF-8");
                dehumidifierData.dehumidifier.InOutStatus.out.wjnldm=new String(result.getValue(Bytes.toBytes("OutStatus"),Bytes.toBytes("wjnldm")),"UTF-8");
                dehumidifierData.dehumidifier.InOutStatus.out.gdfs=new String(result.getValue(Bytes.toBytes("OutStatus"),Bytes.toBytes("gdfs")),"UTF-8");
                dehumidifierData.dehumidifier.InOutStatus.out.dyzl=new String(result.getValue(Bytes.toBytes("OutStatus"),Bytes.toBytes("dyzl")),"UTF-8");
                dehumidifierData.dehumidifier.InOutStatus.out.swjlx=new String(result.getValue(Bytes.toBytes("OutStatus"),Bytes.toBytes("swjlx")),"UTF-8");
                dehumidifierData.dehumidifier.InOutStatus.out.ysjxh=new String(result.getValue(Bytes.toBytes("OutStatus"),Bytes.toBytes("ysjxh")),"UTF-8");
                dehumidifierData.dehumidifier.InOutStatus.out.cflx=new String(result.getValue(Bytes.toBytes("OutStatus"),Bytes.toBytes("cflx")),"UTF-8");
                dehumidifierData.dehumidifier.InOutStatus.out.fjzl=new String(result.getValue(Bytes.toBytes("OutStatus"),Bytes.toBytes("fjzl")),"UTF-8");
                dehumidifierData.dehumidifier.InOutStatus.out.fjgs=new String(result.getValue(Bytes.toBytes("OutStatus"),Bytes.toBytes("fjgs")),"UTF-8");
                dehumidifierData.dehumidifier.InOutStatus.out.fjds=new String(result.getValue(Bytes.toBytes("OutStatus"),Bytes.toBytes("fjds")),"UTF-8");
                dehumidifierData.dehumidifier.InOutStatus.out.dplnqyrdyw=new String(result.getValue(Bytes.toBytes("OutStatus"),Bytes.toBytes("dplnqyrdyw")),"UTF-8");
                dehumidifierData.dehumidifier.InOutStatus.out.ysjyrdyw=new String(result.getValue(Bytes.toBytes("OutStatus"),Bytes.toBytes("ysjyrdyw")),"UTF-8");
                dehumidifierData.dehumidifier.InOutStatus.out.xqzyryw=new String(result.getValue(Bytes.toBytes("OutStatus"),Bytes.toBytes("xqzyryw")),"UTF-8");
                dehumidifierData.dehumidifier.InOutStatus.out.dzpzfyw=new String(result.getValue(Bytes.toBytes("OutStatus"),Bytes.toBytes("dzpzfyw")),"UTF-8");
                dehumidifierData.dehumidifier.InOutStatus.out.jxm=new String(result.getValue(Bytes.toBytes("OutStatus"),Bytes.toBytes("jxm")),"UTF-8");
                dehumidifierData.dehumidifier.InOutStatus.out.wfjipmmk=new String(result.getValue(Bytes.toBytes("OutStatus"),Bytes.toBytes("wfjipmmk")),"UTF-8");
                dehumidifierData.dehumidifier.InOutStatus.out.ysjipmmk=new String(result.getValue(Bytes.toBytes("OutStatus"),Bytes.toBytes("ysjipmmk")),"UTF-8");
                dehumidifierData.dehumidifier.InOutStatus.out.njcxbbh=new String(result.getValue(Bytes.toBytes("OutStatus"),Bytes.toBytes("njcxbbh")),"UTF-8");
                dehumidifierData.dehumidifier.InOutStatus.out.wjcxbbh=new String(result.getValue(Bytes.toBytes("OutStatus"),Bytes.toBytes("wjcxbbh")),"UTF-8");
            }

            dehumidifierData.dehumidifier.InStatusFault = new DehumidifierData.Dehumidifier.InStatusFault();
            origin = result.getValue(Bytes.toBytes("InStatusFault"),Bytes.toBytes("origin"));
            if(origin!=null && origin.length>0){
                dehumidifierData.dehumidifier.InStatusFault.kgjzt=new String(result.getValue(Bytes.toBytes("InStatusFault"),Bytes.toBytes("kgjzt")),"UTF-8");
                dehumidifierData.dehumidifier.InStatusFault.yxms=new String(result.getValue(Bytes.toBytes("InStatusFault"),Bytes.toBytes("yxms")),"UTF-8");
                dehumidifierData.dehumidifier.InStatusFault.nfjzs=new String(result.getValue(Bytes.toBytes("InStatusFault"),Bytes.toBytes("nfjzs")),"UTF-8");
                dehumidifierData.dehumidifier.InStatusFault.snsdwd=new String(result.getValue(Bytes.toBytes("InStatusFault"),Bytes.toBytes("snsdwd")),"UTF-8");
                dehumidifierData.dehumidifier.InStatusFault.snhjwd=new String(result.getValue(Bytes.toBytes("InStatusFault"),Bytes.toBytes("snhjwd")),"UTF-8");
                dehumidifierData.dehumidifier.InStatusFault.snzfqzjwd=new String(result.getValue(Bytes.toBytes("InStatusFault"),Bytes.toBytes("snzfqzjwd")),"UTF-8");
                dehumidifierData.dehumidifier.InStatusFault.syx=new String(result.getValue(Bytes.toBytes("InStatusFault"),Bytes.toBytes("syx")),"UTF-8");
                dehumidifierData.dehumidifier.InStatusFault.sfms=new String(result.getValue(Bytes.toBytes("InStatusFault"),Bytes.toBytes("sfms")),"UTF-8");
                dehumidifierData.dehumidifier.InStatusFault.qzcs=new String(result.getValue(Bytes.toBytes("InStatusFault"),Bytes.toBytes("qzcs")),"UTF-8");
                dehumidifierData.dehumidifier.InStatusFault.qzzr=new String(result.getValue(Bytes.toBytes("InStatusFault"),Bytes.toBytes("qzzr")),"UTF-8");
                dehumidifierData.dehumidifier.InStatusFault.qzzl=new String(result.getValue(Bytes.toBytes("InStatusFault"),Bytes.toBytes("qzzl")),"UTF-8");
                dehumidifierData.dehumidifier.InStatusFault.bcdwdz=new String(result.getValue(Bytes.toBytes("InStatusFault"),Bytes.toBytes("bcdwdz")),"UTF-8");
                dehumidifierData.dehumidifier.InStatusFault.sfkzwjzrwdbc=new String(result.getValue(Bytes.toBytes("InStatusFault"),Bytes.toBytes("sfkzwjzrwdbc")),"UTF-8");
                dehumidifierData.dehumidifier.InStatusFault.snhjsd=new String(result.getValue(Bytes.toBytes("InStatusFault"),Bytes.toBytes("snhjsd")),"UTF-8");
                dehumidifierData.dehumidifier.InStatusFault.jszt=new String(result.getValue(Bytes.toBytes("InStatusFault"),Bytes.toBytes("jszt")),"UTF-8");
                dehumidifierData.dehumidifier.InStatusFault.savezt=new String(result.getValue(Bytes.toBytes("InStatusFault"),Bytes.toBytes("savezt")),"UTF-8");
                dehumidifierData.dehumidifier.InStatusFault.hqzt=new String(result.getValue(Bytes.toBytes("InStatusFault"),Bytes.toBytes("hqzt")),"UTF-8");
                dehumidifierData.dehumidifier.InStatusFault.jyzt=new String(result.getValue(Bytes.toBytes("InStatusFault"),Bytes.toBytes("jyzt")),"UTF-8");
                dehumidifierData.dehumidifier.InStatusFault.flfzt=new String(result.getValue(Bytes.toBytes("InStatusFault"),Bytes.toBytes("flfzt")),"UTF-8");
                dehumidifierData.dehumidifier.InStatusFault.fnlzt=new String(result.getValue(Bytes.toBytes("InStatusFault"),Bytes.toBytes("fnlzt")),"UTF-8");
                dehumidifierData.dehumidifier.InStatusFault.dfrzt=new String(result.getValue(Bytes.toBytes("InStatusFault"),Bytes.toBytes("dfrzt")),"UTF-8");
                dehumidifierData.dehumidifier.InStatusFault.kczt=new String(result.getValue(Bytes.toBytes("InStatusFault"),Bytes.toBytes("kczt")),"UTF-8");
                dehumidifierData.dehumidifier.InStatusFault.zczt=new String(result.getValue(Bytes.toBytes("InStatusFault"),Bytes.toBytes("zczt")),"UTF-8");
                dehumidifierData.dehumidifier.InStatusFault.sbgz=new String(result.getValue(Bytes.toBytes("InStatusFault"),Bytes.toBytes("sbgz")),"UTF-8");
                dehumidifierData.dehumidifier.InStatusFault.syytqf=new String(result.getValue(Bytes.toBytes("InStatusFault"),Bytes.toBytes("syytqf")),"UTF-8");
                dehumidifierData.dehumidifier.InStatusFault.nlcsztxz=new String(result.getValue(Bytes.toBytes("InStatusFault"),Bytes.toBytes("nlcsztxz")),"UTF-8");
                dehumidifierData.dehumidifier.InStatusFault.ysjzssdz=new String(result.getValue(Bytes.toBytes("InStatusFault"),Bytes.toBytes("ysjzssdz")),"UTF-8");
                dehumidifierData.dehumidifier.InStatusFault.dzpzfkdsdyxzt=new String(result.getValue(Bytes.toBytes("InStatusFault"),Bytes.toBytes("dzpzfkdsdyxzt")),"UTF-8");
                dehumidifierData.dehumidifier.InStatusFault.dzpzfkdsdz=new String(result.getValue(Bytes.toBytes("InStatusFault"),Bytes.toBytes("dzpzfkdsdz")),"UTF-8");
                dehumidifierData.dehumidifier.InStatusFault.wsfjsdyxzt=new String(result.getValue(Bytes.toBytes("InStatusFault"),Bytes.toBytes("wsfjsdyxzt")),"UTF-8");
                dehumidifierData.dehumidifier.InStatusFault.mbpqwdsdyxzt=new String(result.getValue(Bytes.toBytes("InStatusFault"),Bytes.toBytes("mbpqwdsdyxzt")),"UTF-8");
                dehumidifierData.dehumidifier.InStatusFault.mbgrdsdyxzt=new String(result.getValue(Bytes.toBytes("InStatusFault"),Bytes.toBytes("mbgrdsdyxzt")),"UTF-8");
                dehumidifierData.dehumidifier.InStatusFault.swtjyqbz=new String(result.getValue(Bytes.toBytes("InStatusFault"),Bytes.toBytes("swtjyqbz")),"UTF-8");
                dehumidifierData.dehumidifier.InStatusFault.snqtcgqgz=new String(result.getValue(Bytes.toBytes("InStatusFault"),Bytes.toBytes("snqtcgqgz")),"UTF-8");
                dehumidifierData.dehumidifier.InStatusFault.snswcgqgz=new String(result.getValue(Bytes.toBytes("InStatusFault"),Bytes.toBytes("snswcgqgz")),"UTF-8");
                dehumidifierData.dehumidifier.InStatusFault.snsdcgqgz=new String(result.getValue(Bytes.toBytes("InStatusFault"),Bytes.toBytes("snsdcgqgz")),"UTF-8");
                dehumidifierData.dehumidifier.InStatusFault.snhjgwbgz=new String(result.getValue(Bytes.toBytes("InStatusFault"),Bytes.toBytes("snhjgwbgz")),"UTF-8");
                dehumidifierData.dehumidifier.InStatusFault.snzfqzjgwbgz=new String(result.getValue(Bytes.toBytes("InStatusFault"),Bytes.toBytes("snzfqzjgwbgz")),"UTF-8");
                dehumidifierData.dehumidifier.InStatusFault.njzbyscqtxgz=new String(result.getValue(Bytes.toBytes("InStatusFault"),Bytes.toBytes("njzbyscqtxgz")),"UTF-8");
                dehumidifierData.dehumidifier.InStatusFault.snjsmbh=new String(result.getValue(Bytes.toBytes("InStatusFault"),Bytes.toBytes("snjsmbh")),"UTF-8");
                dehumidifierData.dehumidifier.InStatusFault.zdajds=new String(result.getValue(Bytes.toBytes("InStatusFault"),Bytes.toBytes("zdajds")),"UTF-8");
                dehumidifierData.dehumidifier.InStatusFault.hdmgz=new String(result.getValue(Bytes.toBytes("InStatusFault"),Bytes.toBytes("hdmgz")),"UTF-8");
                dehumidifierData.dehumidifier.InStatusFault.xzkdpyc=new String(result.getValue(Bytes.toBytes("InStatusFault"),Bytes.toBytes("xzkdpyc")),"UTF-8");
                dehumidifierData.dehumidifier.InStatusFault.txmgz=new String(result.getValue(Bytes.toBytes("InStatusFault"),Bytes.toBytes("txmgz")),"UTF-8");
                dehumidifierData.dehumidifier.InStatusFault.jyxpdxgz=new String(result.getValue(Bytes.toBytes("InStatusFault"),Bytes.toBytes("jyxpdxgz")),"UTF-8");
                dehumidifierData.dehumidifier.InStatusFault.snfjgz=new String(result.getValue(Bytes.toBytes("InStatusFault"),Bytes.toBytes("snfjgz")),"UTF-8");
                dehumidifierData.dehumidifier.InStatusFault.qfpbxz=new String(result.getValue(Bytes.toBytes("InStatusFault"),Bytes.toBytes("qfpbxz")),"UTF-8");
                dehumidifierData.dehumidifier.InStatusFault.csfsxz=new String(result.getValue(Bytes.toBytes("InStatusFault"),Bytes.toBytes("csfsxz")),"UTF-8");
                dehumidifierData.dehumidifier.InStatusFault.wkjdxz=new String(result.getValue(Bytes.toBytes("InStatusFault"),Bytes.toBytes("wkjdxz")),"UTF-8");
                dehumidifierData.dehumidifier.InStatusFault.zysfzt=new String(result.getValue(Bytes.toBytes("InStatusFault"),Bytes.toBytes("zysfzt")),"UTF-8");
                dehumidifierData.dehumidifier.InStatusFault.sxsfzt=new String(result.getValue(Bytes.toBytes("InStatusFault"),Bytes.toBytes("sxsfzt")),"UTF-8");
                dehumidifierData.dehumidifier.InStatusFault.rszl=new String(result.getValue(Bytes.toBytes("InStatusFault"),Bytes.toBytes("rszl")),"UTF-8");
                dehumidifierData.dehumidifier.InStatusFault.dredmsyxzt=new String(result.getValue(Bytes.toBytes("InStatusFault"),Bytes.toBytes("dredmsyxzt")),"UTF-8");
                dehumidifierData.dehumidifier.InStatusFault.spmkrfgz=new String(result.getValue(Bytes.toBytes("InStatusFault"),Bytes.toBytes("spmkrfgz")),"UTF-8");
                dehumidifierData.dehumidifier.InStatusFault.jcbtxjfgz=new String(result.getValue(Bytes.toBytes("InStatusFault"),Bytes.toBytes("jcbtxjfgz")),"UTF-8");
                dehumidifierData.dehumidifier.InStatusFault.njwdywjgzOe=new String(result.getValue(Bytes.toBytes("InStatusFault"),Bytes.toBytes("njwdywjgzOe")),"UTF-8");
                dehumidifierData.dehumidifier.InStatusFault.sfjggzfc=new String(result.getValue(Bytes.toBytes("InStatusFault"),Bytes.toBytes("sfjggzfc")),"UTF-8");
                dehumidifierData.dehumidifier.InStatusFault.wifigzgm=new String(result.getValue(Bytes.toBytes("InStatusFault"),Bytes.toBytes("wifigzgm")),"UTF-8");
                dehumidifierData.dehumidifier.InStatusFault.nwjtxgz=new String(result.getValue(Bytes.toBytes("InStatusFault"),Bytes.toBytes("nwjtxgz")),"UTF-8");
                dehumidifierData.dehumidifier.InStatusFault.djdzbh=new String(result.getValue(Bytes.toBytes("InStatusFault"),Bytes.toBytes("djdzbh")),"UTF-8");
                dehumidifierData.dehumidifier.InStatusFault.xsbhqdbgz=new String(result.getValue(Bytes.toBytes("InStatusFault"),Bytes.toBytes("xsbhqdbgz")),"UTF-8");
                dehumidifierData.dehumidifier.InStatusFault.dbpxsbyc=new String(result.getValue(Bytes.toBytes("InStatusFault"),Bytes.toBytes("dbpxsbyc")),"UTF-8");
                dehumidifierData.dehumidifier.InStatusFault.njglxhgz=new String(result.getValue(Bytes.toBytes("InStatusFault"),Bytes.toBytes("njglxhgz")),"UTF-8");
            }

            dehumidifierData.dehumidifier.OutStatusFault = new DehumidifierData.Dehumidifier.OutStatusFault();
            origin = result.getValue(Bytes.toBytes("OutStatusFault"),Bytes.toBytes("origin"));
            if(origin!=null && origin.length>0){
                dehumidifierData.dehumidifier.OutStatusFault.ysjkgzt=new String(result.getValue(Bytes.toBytes("OutStatusFault"),Bytes.toBytes("ysjkgzt")),"UTF-8");
                dehumidifierData.dehumidifier.OutStatusFault.hsms=new String(result.getValue(Bytes.toBytes("OutStatusFault"),Bytes.toBytes("hsms")),"UTF-8");
                dehumidifierData.dehumidifier.OutStatusFault.ysjyxzs=new String(result.getValue(Bytes.toBytes("OutStatusFault"),Bytes.toBytes("ysjyxzs")),"UTF-8");
                dehumidifierData.dehumidifier.OutStatusFault.wfj1zs=new String(result.getValue(Bytes.toBytes("OutStatusFault"),Bytes.toBytes("wfj1zs")),"UTF-8");
                dehumidifierData.dehumidifier.OutStatusFault.wfj2zs=new String(result.getValue(Bytes.toBytes("OutStatusFault"),Bytes.toBytes("wfj2zs")),"UTF-8");
                dehumidifierData.dehumidifier.OutStatusFault.ysjyxgl=new String(result.getValue(Bytes.toBytes("OutStatusFault"),Bytes.toBytes("ysjyxgl")),"UTF-8");
                dehumidifierData.dehumidifier.OutStatusFault.dzpzfkd=new String(result.getValue(Bytes.toBytes("OutStatusFault"),Bytes.toBytes("dzpzfkd")),"UTF-8");
                dehumidifierData.dehumidifier.OutStatusFault.zlmxdy=new String(result.getValue(Bytes.toBytes("OutStatusFault"),Bytes.toBytes("zlmxdy")),"UTF-8");
                dehumidifierData.dehumidifier.OutStatusFault.swhjwd=new String(result.getValue(Bytes.toBytes("OutStatusFault"),Bytes.toBytes("swhjwd")),"UTF-8");
                dehumidifierData.dehumidifier.OutStatusFault.swlnqzjwd=new String(result.getValue(Bytes.toBytes("OutStatusFault"),Bytes.toBytes("swlnqzjwd")),"UTF-8");
                dehumidifierData.dehumidifier.OutStatusFault.swpqwd=new String(result.getValue(Bytes.toBytes("OutStatusFault"),Bytes.toBytes("swpqwd")),"UTF-8");
                dehumidifierData.dehumidifier.OutStatusFault.savezt=new String(result.getValue(Bytes.toBytes("OutStatusFault"),Bytes.toBytes("savezt")),"UTF-8");
                dehumidifierData.dehumidifier.OutStatusFault.wjfnlzt=new String(result.getValue(Bytes.toBytes("OutStatusFault"),Bytes.toBytes("wjfnlzt")),"UTF-8");
                dehumidifierData.dehumidifier.OutStatusFault.wjjyzt=new String(result.getValue(Bytes.toBytes("OutStatusFault"),Bytes.toBytes("wjjyzt")),"UTF-8");
                dehumidifierData.dehumidifier.OutStatusFault.dzpzfzt=new String(result.getValue(Bytes.toBytes("OutStatusFault"),Bytes.toBytes("dzpzfzt")),"UTF-8");
                dehumidifierData.dehumidifier.OutStatusFault.sfzdjzt=new String(result.getValue(Bytes.toBytes("OutStatusFault"),Bytes.toBytes("sfzdjzt")),"UTF-8");
                dehumidifierData.dehumidifier.OutStatusFault.hyzt=new String(result.getValue(Bytes.toBytes("OutStatusFault"),Bytes.toBytes("hyzt")),"UTF-8");
                dehumidifierData.dehumidifier.OutStatusFault.ptcszt=new String(result.getValue(Bytes.toBytes("OutStatusFault"),Bytes.toBytes("ptcszt")),"UTF-8");
                dehumidifierData.dehumidifier.OutStatusFault.csyq=new String(result.getValue(Bytes.toBytes("OutStatusFault"),Bytes.toBytes("csyq")),"UTF-8");
                dehumidifierData.dehumidifier.OutStatusFault.tscszt=new String(result.getValue(Bytes.toBytes("OutStatusFault"),Bytes.toBytes("tscszt")),"UTF-8");
                dehumidifierData.dehumidifier.OutStatusFault.wjrytjgz=new String(result.getValue(Bytes.toBytes("OutStatusFault"),Bytes.toBytes("wjrytjgz")),"UTF-8");
                dehumidifierData.dehumidifier.OutStatusFault.hszczt=new String(result.getValue(Bytes.toBytes("OutStatusFault"),Bytes.toBytes("hszczt")),"UTF-8");
                dehumidifierData.dehumidifier.OutStatusFault.wjacdlz=new String(result.getValue(Bytes.toBytes("OutStatusFault"),Bytes.toBytes("wjacdlz")),"UTF-8");
                dehumidifierData.dehumidifier.OutStatusFault.syx=new String(result.getValue(Bytes.toBytes("OutStatusFault"),Bytes.toBytes("syx")),"UTF-8");
                dehumidifierData.dehumidifier.OutStatusFault.sfms=new String(result.getValue(Bytes.toBytes("OutStatusFault"),Bytes.toBytes("sfms")),"UTF-8");
                dehumidifierData.dehumidifier.OutStatusFault.qzcs=new String(result.getValue(Bytes.toBytes("OutStatusFault"),Bytes.toBytes("qzcs")),"UTF-8");
                dehumidifierData.dehumidifier.OutStatusFault.qzzr=new String(result.getValue(Bytes.toBytes("OutStatusFault"),Bytes.toBytes("qzzr")),"UTF-8");
                dehumidifierData.dehumidifier.OutStatusFault.qzzl=new String(result.getValue(Bytes.toBytes("OutStatusFault"),Bytes.toBytes("qzzl")),"UTF-8");
                dehumidifierData.dehumidifier.OutStatusFault.dredmsyxzt=new String(result.getValue(Bytes.toBytes("OutStatusFault"),Bytes.toBytes("dredmsyxzt")),"UTF-8");
                dehumidifierData.dehumidifier.OutStatusFault.glggbhxjp=new String(result.getValue(Bytes.toBytes("OutStatusFault"),Bytes.toBytes("glggbhxjp")),"UTF-8");
                dehumidifierData.dehumidifier.OutStatusFault.mkdlbhxjp=new String(result.getValue(Bytes.toBytes("OutStatusFault"),Bytes.toBytes("mkdlbhxjp")),"UTF-8");
                dehumidifierData.dehumidifier.OutStatusFault.mkwdbhxjp=new String(result.getValue(Bytes.toBytes("OutStatusFault"),Bytes.toBytes("mkwdbhxjp")),"UTF-8");
                dehumidifierData.dehumidifier.OutStatusFault.zlmxdybhxjp=new String(result.getValue(Bytes.toBytes("OutStatusFault"),Bytes.toBytes("zlmxdybhxjp")),"UTF-8");
                dehumidifierData.dehumidifier.OutStatusFault.ghbhxjp=new String(result.getValue(Bytes.toBytes("OutStatusFault"),Bytes.toBytes("ghbhxjp")),"UTF-8");
                dehumidifierData.dehumidifier.OutStatusFault.fjdbhxjp=new String(result.getValue(Bytes.toBytes("OutStatusFault"),Bytes.toBytes("fjdbhxjp")),"UTF-8");
                dehumidifierData.dehumidifier.OutStatusFault.pqbhxjp=new String(result.getValue(Bytes.toBytes("OutStatusFault"),Bytes.toBytes("pqbhxjp")),"UTF-8");
                dehumidifierData.dehumidifier.OutStatusFault.wjacdlbhxjp=new String(result.getValue(Bytes.toBytes("OutStatusFault"),Bytes.toBytes("wjacdlbhxjp")),"UTF-8");
                dehumidifierData.dehumidifier.OutStatusFault.gzgwbgz=new String(result.getValue(Bytes.toBytes("OutStatusFault"),Bytes.toBytes("gzgwbgz")),"UTF-8");
                dehumidifierData.dehumidifier.OutStatusFault.pqgwbgz=new String(result.getValue(Bytes.toBytes("OutStatusFault"),Bytes.toBytes("pqgwbgz")),"UTF-8");
                dehumidifierData.dehumidifier.OutStatusFault.hjgwbgz=new String(result.getValue(Bytes.toBytes("OutStatusFault"),Bytes.toBytes("hjgwbgz")),"UTF-8");
                dehumidifierData.dehumidifier.OutStatusFault.swlnqzjgwbgz=new String(result.getValue(Bytes.toBytes("OutStatusFault"),Bytes.toBytes("swlnqzjgwbgz")),"UTF-8");
                dehumidifierData.dehumidifier.OutStatusFault.mkgwbdlgz=new String(result.getValue(Bytes.toBytes("OutStatusFault"),Bytes.toBytes("mkgwbdlgz")),"UTF-8");
                dehumidifierData.dehumidifier.OutStatusFault.ysjrgzbh=new String(result.getValue(Bytes.toBytes("OutStatusFault"),Bytes.toBytes("ysjrgzbh")),"UTF-8");
                dehumidifierData.dehumidifier.OutStatusFault.pqbh=new String(result.getValue(Bytes.toBytes("OutStatusFault"),Bytes.toBytes("pqbh")),"UTF-8");
                dehumidifierData.dehumidifier.OutStatusFault.gfhbh=new String(result.getValue(Bytes.toBytes("OutStatusFault"),Bytes.toBytes("gfhbh")),"UTF-8");
                dehumidifierData.dehumidifier.OutStatusFault.wjacdlbh=new String(result.getValue(Bytes.toBytes("OutStatusFault"),Bytes.toBytes("wjacdlbh")),"UTF-8");
                dehumidifierData.dehumidifier.OutStatusFault.mkdlfobh=new String(result.getValue(Bytes.toBytes("OutStatusFault"),Bytes.toBytes("mkdlfobh")),"UTF-8");
                dehumidifierData.dehumidifier.OutStatusFault.mkwdbh=new String(result.getValue(Bytes.toBytes("OutStatusFault"),Bytes.toBytes("mkwdbh")),"UTF-8");
                dehumidifierData.dehumidifier.OutStatusFault.fjdbh=new String(result.getValue(Bytes.toBytes("OutStatusFault"),Bytes.toBytes("fjdbh")),"UTF-8");
                dehumidifierData.dehumidifier.OutStatusFault.glggbh=new String(result.getValue(Bytes.toBytes("OutStatusFault"),Bytes.toBytes("glggbh")),"UTF-8");
                dehumidifierData.dehumidifier.OutStatusFault.ysjqnxbhqxttqx=new String(result.getValue(Bytes.toBytes("OutStatusFault"),Bytes.toBytes("ysjqnxbhqxttqx")),"UTF-8");
                dehumidifierData.dehumidifier.OutStatusFault.pfcglgz=new String(result.getValue(Bytes.toBytes("OutStatusFault"),Bytes.toBytes("pfcglgz")),"UTF-8");
                dehumidifierData.dehumidifier.OutStatusFault.zlmxdyggbh=new String(result.getValue(Bytes.toBytes("OutStatusFault"),Bytes.toBytes("zlmxdyggbh")),"UTF-8");
                dehumidifierData.dehumidifier.OutStatusFault.zlmxdygdbh=new String(result.getValue(Bytes.toBytes("OutStatusFault"),Bytes.toBytes("zlmxdygdbh")),"UTF-8");
                dehumidifierData.dehumidifier.OutStatusFault.qfbh=new String(result.getValue(Bytes.toBytes("OutStatusFault"),Bytes.toBytes("qfbh")),"UTF-8");
                dehumidifierData.dehumidifier.OutStatusFault.msct=new String(result.getValue(Bytes.toBytes("OutStatusFault"),Bytes.toBytes("msct")),"UTF-8");
                dehumidifierData.dehumidifier.OutStatusFault.snwjxbpp=new String(result.getValue(Bytes.toBytes("OutStatusFault"),Bytes.toBytes("snwjxbpp")),"UTF-8");
                dehumidifierData.dehumidifier.OutStatusFault.ytdnwjglljytxljbpp=new String(result.getValue(Bytes.toBytes("OutStatusFault"),Bytes.toBytes("ytdnwjglljytxljbpp")),"UTF-8");
                dehumidifierData.dehumidifier.OutStatusFault.jyxpdxgz=new String(result.getValue(Bytes.toBytes("OutStatusFault"),Bytes.toBytes("jyxpdxgz")),"UTF-8");
                dehumidifierData.dehumidifier.OutStatusFault.glxhyc=new String(result.getValue(Bytes.toBytes("OutStatusFault"),Bytes.toBytes("glxhyc")),"UTF-8");
                dehumidifierData.dehumidifier.OutStatusFault.stfhxyc=new String(result.getValue(Bytes.toBytes("OutStatusFault"),Bytes.toBytes("stfhxyc")),"UTF-8");
                dehumidifierData.dehumidifier.OutStatusFault.xzkdpyc=new String(result.getValue(Bytes.toBytes("OutStatusFault"),Bytes.toBytes("xzkdpyc")),"UTF-8");
                dehumidifierData.dehumidifier.OutStatusFault.swfj2gz=new String(result.getValue(Bytes.toBytes("OutStatusFault"),Bytes.toBytes("swfj2gz")),"UTF-8");
                dehumidifierData.dehumidifier.OutStatusFault.swfj1gz=new String(result.getValue(Bytes.toBytes("OutStatusFault"),Bytes.toBytes("swfj1gz")),"UTF-8");
                dehumidifierData.dehumidifier.OutStatusFault.gwbhswfj=new String(result.getValue(Bytes.toBytes("OutStatusFault"),Bytes.toBytes("gwbhswfj")),"UTF-8");
                dehumidifierData.dehumidifier.OutStatusFault.xtdybh=new String(result.getValue(Bytes.toBytes("OutStatusFault"),Bytes.toBytes("xtdybh")),"UTF-8");
                dehumidifierData.dehumidifier.OutStatusFault.xtgybh=new String(result.getValue(Bytes.toBytes("OutStatusFault"),Bytes.toBytes("xtgybh")),"UTF-8");
                dehumidifierData.dehumidifier.OutStatusFault.zlmxdydlgz=new String(result.getValue(Bytes.toBytes("OutStatusFault"),Bytes.toBytes("zlmxdydlgz")),"UTF-8");
                dehumidifierData.dehumidifier.OutStatusFault.zjdljcgz=new String(result.getValue(Bytes.toBytes("OutStatusFault"),Bytes.toBytes("zjdljcgz")),"UTF-8");
                dehumidifierData.dehumidifier.OutStatusFault.drcdgz=new String(result.getValue(Bytes.toBytes("OutStatusFault"),Bytes.toBytes("drcdgz")),"UTF-8");
                dehumidifierData.dehumidifier.OutStatusFault.ysjxdldljcgz=new String(result.getValue(Bytes.toBytes("OutStatusFault"),Bytes.toBytes("ysjxdldljcgz")),"UTF-8");
                dehumidifierData.dehumidifier.OutStatusFault.ysjsb=new String(result.getValue(Bytes.toBytes("OutStatusFault"),Bytes.toBytes("ysjsb")),"UTF-8");
                dehumidifierData.dehumidifier.OutStatusFault.ysjtcbh=new String(result.getValue(Bytes.toBytes("OutStatusFault"),Bytes.toBytes("ysjtcbh")),"UTF-8");
                dehumidifierData.dehumidifier.OutStatusFault.ysjdz=new String(result.getValue(Bytes.toBytes("OutStatusFault"),Bytes.toBytes("ysjdz")),"UTF-8");
                dehumidifierData.dehumidifier.OutStatusFault.qdsb=new String(result.getValue(Bytes.toBytes("OutStatusFault"),Bytes.toBytes("qdsb")),"UTF-8");
                dehumidifierData.dehumidifier.OutStatusFault.qdmkfw=new String(result.getValue(Bytes.toBytes("OutStatusFault"),Bytes.toBytes("qdmkfw")),"UTF-8");
                dehumidifierData.dehumidifier.OutStatusFault.sc=new String(result.getValue(Bytes.toBytes("OutStatusFault"),Bytes.toBytes("sc")),"UTF-8");
                dehumidifierData.dehumidifier.OutStatusFault.ysjbmyc=new String(result.getValue(Bytes.toBytes("OutStatusFault"),Bytes.toBytes("ysjbmyc")),"UTF-8");
                dehumidifierData.dehumidifier.OutStatusFault.qdbhjgwbgz=new String(result.getValue(Bytes.toBytes("OutStatusFault"),Bytes.toBytes("qdbhjgwbgz")),"UTF-8");
                dehumidifierData.dehumidifier.OutStatusFault.jljcqbh=new String(result.getValue(Bytes.toBytes("OutStatusFault"),Bytes.toBytes("jljcqbh")),"UTF-8");
                dehumidifierData.dehumidifier.OutStatusFault.wpbh=new String(result.getValue(Bytes.toBytes("OutStatusFault"),Bytes.toBytes("wpbh")),"UTF-8");
                dehumidifierData.dehumidifier.OutStatusFault.cgqljbh=new String(result.getValue(Bytes.toBytes("OutStatusFault"),Bytes.toBytes("cgqljbh")),"UTF-8");
                dehumidifierData.dehumidifier.OutStatusFault.qdbtxgz=new String(result.getValue(Bytes.toBytes("OutStatusFault"),Bytes.toBytes("qdbtxgz")),"UTF-8");
                dehumidifierData.dehumidifier.OutStatusFault.ysjxdlgl=new String(result.getValue(Bytes.toBytes("OutStatusFault"),Bytes.toBytes("ysjxdlgl")),"UTF-8");
                dehumidifierData.dehumidifier.OutStatusFault.jlsrdyyc=new String(result.getValue(Bytes.toBytes("OutStatusFault"),Bytes.toBytes("jlsrdyyc")),"UTF-8");
                dehumidifierData.dehumidifier.OutStatusFault.fjtsbtxgz=new String(result.getValue(Bytes.toBytes("OutStatusFault"),Bytes.toBytes("fjtsbtxgz")),"UTF-8");
                dehumidifierData.dehumidifier.OutStatusFault.yfgwbgz=new String(result.getValue(Bytes.toBytes("OutStatusFault"),Bytes.toBytes("yfgwbgz")),"UTF-8");
                dehumidifierData.dehumidifier.OutStatusFault.qfgwbgz=new String(result.getValue(Bytes.toBytes("OutStatusFault"),Bytes.toBytes("qfgwbgz")),"UTF-8");
                dehumidifierData.dehumidifier.OutStatusFault.swlnqrggwbgz=new String(result.getValue(Bytes.toBytes("OutStatusFault"),Bytes.toBytes("swlnqrggwbgz")),"UTF-8");
                dehumidifierData.dehumidifier.OutStatusFault.swlnqcggwbgz=new String(result.getValue(Bytes.toBytes("OutStatusFault"),Bytes.toBytes("swlnqcggwbgz")),"UTF-8");
                dehumidifierData.dehumidifier.OutStatusFault.lmwdgwbgz=new String(result.getValue(Bytes.toBytes("OutStatusFault"),Bytes.toBytes("lmwdgwbgz")),"UTF-8");
                dehumidifierData.dehumidifier.OutStatusFault.swjlmjrqsxgz=new String(result.getValue(Bytes.toBytes("OutStatusFault"),Bytes.toBytes("swjlmjrqsxgz")),"UTF-8");
                dehumidifierData.dehumidifier.OutStatusFault.swjlmjrqjdqzlgz=new String(result.getValue(Bytes.toBytes("OutStatusFault"),Bytes.toBytes("swjlmjrqjdqzlgz")),"UTF-8");
                dehumidifierData.dehumidifier.OutStatusFault.ipmmkwd=new String(result.getValue(Bytes.toBytes("OutStatusFault"),Bytes.toBytes("ipmmkwd")),"UTF-8");
                dehumidifierData.dehumidifier.OutStatusFault.xtyc=new String(result.getValue(Bytes.toBytes("OutStatusFault"),Bytes.toBytes("xtyc")),"UTF-8");
                dehumidifierData.dehumidifier.OutStatusFault.dyztw=new String(result.getValue(Bytes.toBytes("OutStatusFault"),Bytes.toBytes("dyztw")),"UTF-8");
                dehumidifierData.dehumidifier.OutStatusFault.dlztw=new String(result.getValue(Bytes.toBytes("OutStatusFault"),Bytes.toBytes("dlztw")),"UTF-8");
                dehumidifierData.dehumidifier.OutStatusFault.yfgwbwd=new String(result.getValue(Bytes.toBytes("OutStatusFault"),Bytes.toBytes("yfgwbwd")),"UTF-8");
                dehumidifierData.dehumidifier.OutStatusFault.qfgwbwd=new String(result.getValue(Bytes.toBytes("OutStatusFault"),Bytes.toBytes("qfgwbwd")),"UTF-8");
                dehumidifierData.dehumidifier.OutStatusFault.swlnqrgwd=new String(result.getValue(Bytes.toBytes("OutStatusFault"),Bytes.toBytes("swlnqrgwd")),"UTF-8");
                dehumidifierData.dehumidifier.OutStatusFault.swlnqcgwd=new String(result.getValue(Bytes.toBytes("OutStatusFault"),Bytes.toBytes("swlnqcgwd")),"UTF-8");
                dehumidifierData.dehumidifier.OutStatusFault.ysjpqgwbh=new String(result.getValue(Bytes.toBytes("OutStatusFault"),Bytes.toBytes("ysjpqgwbh")),"UTF-8");
                dehumidifierData.dehumidifier.OutStatusFault.jlglbh=new String(result.getValue(Bytes.toBytes("OutStatusFault"),Bytes.toBytes("jlglbh")),"UTF-8");
                dehumidifierData.dehumidifier.OutStatusFault.ggwbh=new String(result.getValue(Bytes.toBytes("OutStatusFault"),Bytes.toBytes("ggwbh")),"UTF-8");
                dehumidifierData.dehumidifier.OutStatusFault.deepromgz=new String(result.getValue(Bytes.toBytes("OutStatusFault"),Bytes.toBytes("deepromgz")),"UTF-8");
                dehumidifierData.dehumidifier.OutStatusFault.whwdyc=new String(result.getValue(Bytes.toBytes("OutStatusFault"),Bytes.toBytes("whwdyc")),"UTF-8");
                dehumidifierData.dehumidifier.OutStatusFault.zrggwjp=new String(result.getValue(Bytes.toBytes("OutStatusFault"),Bytes.toBytes("zrggwjp")),"UTF-8");
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return dehumidifierData;
    }
}
