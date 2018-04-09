package com.restfulapi.controller;

import com.restfulapi.common.pojo.*;
import com.restfulapi.common.tools.TimeZoneTool;
import com.restfulapi.service.AirService;
import com.restfulapi.service.DehumidifierService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
public class HbaseController {
    public static final Logger log = LoggerFactory.getLogger(HbaseController.class);
    public static final SimpleDateFormat DATEF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    @Autowired
    AirService airService;
    @Autowired
    DehumidifierService dehumidifierService;

    @RequestMapping(value = "/data", method = { RequestMethod.POST, RequestMethod.GET })
    @ResponseBody
    public RespEntity dataRequest(@RequestBody  ReqEntity reqEntity ,@RequestHeader HttpHeaders headers)throws Exception {
        JsonResult jsonResult = new JsonResult();
        if ((headers.getFirst("header").equals("iot_7"))){
            if(!(reqEntity == null)){
                String beginTime = null;
                String endTime = null;
                if (!(reqEntity.getBeginTime() == null)){
                    beginTime = reqEntity.getBeginTime();
                }
                if (!(reqEntity.getEndTime() == null)){
                    endTime = reqEntity.getEndTime();
                }
                log.debug("data接口");
                log.debug("Mac:{},Mid:{},Header:{},TransFlag:{},AskFlag:{},BeginTime:{},EndTime:{}",reqEntity.getMac(),reqEntity.getMid(),headers.getFirst("header"),reqEntity.getTransFlag(),reqEntity.getAskFlag(),beginTime,endTime);
                airService.scanDataAirCon(null,beginTime,endTime,null,null);
                jsonResult.setRespFlag("iot_1");
                jsonResult.setTransFlag(true);
                jsonResult.setDate(DATEF.format(new Date()));
            }
            return RespEntity.success(jsonResult);
        }else {
            jsonResult.setRespFlag("iot_1");
            jsonResult.setTransFlag(false);
            jsonResult.setDate(DATEF.format(new Date()));
            return RespEntity.fail(jsonResult);
        }
    }


    @RequestMapping(value = "/array", method = { RequestMethod.POST, RequestMethod.GET })
    public RespEntity arrayMacRequest(@RequestBody ArrayRequest arrayRequest , @RequestHeader HttpHeaders headers)throws SQLException, NullPointerException,Exception {
        JsonResult jsonResult = new JsonResult();
        if (!(headers.getFirst("header").indexOf("iot_7_") == -1)) {
            try {
                String beginTime = null;
                String endTime = null;
                if (!(arrayRequest.getBeginTime() == null)) {
                    beginTime = TimeZoneTool.formatTimeBeforeEight(arrayRequest.getBeginTime());
                }
                if (!(arrayRequest.getEndTime() == null)) {
                    endTime = TimeZoneTool.formatTimeBeforeEight(arrayRequest.getEndTime());
                }
                switch (headers.getFirst("header")) {
                    case "iot_7_" + Device.KONGTIAO_DEVICE:

                        if (!(arrayRequest.getMac() == null)) {
                            log.debug("空调array mac接口,arrayRequest.getMac().size:{},beginTime:{},endTime:{}", arrayRequest.getMac().size(),beginTime,endTime);
                            for (int i = 0; i < arrayRequest.getMac().size(); i++) {
                                log.debug("Mac:{},Mid:{},Header:{},TransFlag:{},AskFlag:{},BeginTime:{},EndTime:{}", arrayRequest.getMac().get(i), arrayRequest.getMid(), headers.getFirst("header"), arrayRequest.getTransFlag(), arrayRequest.getAskFlag(), beginTime, endTime);
                                airService.scanArrayAirCon( String.valueOf(arrayRequest.getMac().get(i)), beginTime, endTime, null, null);
                            }
                        } else {
                            if (!(arrayRequest.getMid() == null)) {
                                log.debug("空调array mid接口,arrayRequest.getMid().size{}", arrayRequest.getMid().size());
                                for (int i = 0; i < arrayRequest.getMid().size(); i++) {
                                    log.debug("Mac:{},Mid:{},Header:{},TransFlag:{},AskFlag:{},BeginTime:{},EndTime:{}", arrayRequest.getMac(), arrayRequest.getMid().get(i), headers.getFirst("header"), arrayRequest.getTransFlag(), arrayRequest.getAskFlag(), beginTime, endTime);
                                    airService.scanArrayAirCon(null, beginTime, endTime, String.valueOf(arrayRequest.getMid().get(i)), null);
                                }
                            } else {
                                log.debug("空调array接口");
                                log.debug("Mac:{},Mid:{},Header:{},TransFlag:{},AskFlag:{},BeginTime:{},EndTime:{}", arrayRequest.getMac(), arrayRequest.getMid(), headers.getFirst("header"), arrayRequest.getTransFlag(), arrayRequest.getAskFlag(), beginTime, endTime);
                                airService.scanArrayAirCon(null, beginTime, endTime, null, null);
                            }
                        }
                        jsonResult.setRespFlag("iot_1");
                        jsonResult.setTransFlag(true);
                        jsonResult.setDate(DATEF.format(new Date()));
                        break;
                    case "iot_7_" + Device.CHUSHIJI_DEVICE:

                        if (!(arrayRequest.getMac() == null)) {
                            log.debug("除湿机array mac接口,arrayRequest.getMac().size:{},BeginTime:{},EndTime:{}", arrayRequest.getMac().size(),beginTime,endTime);
                            for (int i = 0; i < arrayRequest.getMac().size(); i++) {
                                log.debug("Mac:{},Mid:{},Header:{},TransFlag:{},AskFlag:{},BeginTime:{},EndTime:{}", arrayRequest.getMac().get(i), arrayRequest.getMid(), headers.getFirst("header"), arrayRequest.getTransFlag(), arrayRequest.getAskFlag(), beginTime, endTime);
                                dehumidifierService.scanDehumidifier( String.valueOf(arrayRequest.getMac().get(i)), beginTime, endTime, null, null);
                            }
                        } else {
                            if (!(arrayRequest.getMid() == null)) {
                                log.debug("除湿机array mid接口,arrayRequest.getMid().size{},BeginTime:{},EndTime:{}", arrayRequest.getMid().size(),beginTime,endTime);
                                for (int i = 0; i < arrayRequest.getMid().size(); i++) {
                                    log.debug("Mac:{},Mid:{},Header:{},TransFlag:{},AskFlag:{},BeginTime:{},EndTime:{}", arrayRequest.getMac(), arrayRequest.getMid().get(i), headers.getFirst("header"), arrayRequest.getTransFlag(), arrayRequest.getAskFlag(), beginTime, endTime);
                                    dehumidifierService.scanDehumidifier(null, beginTime, endTime, String.valueOf(arrayRequest.getMid().get(i)), null);
                                }
                            } else {
                                log.debug("除湿机array接口");
                                log.debug("Mac:{},Mid:{},Header:{},TransFlag:{},AskFlag:{},BeginTime:{},EndTime:{}", arrayRequest.getMac(), arrayRequest.getMid(), headers.getFirst("header"), arrayRequest.getTransFlag(), arrayRequest.getAskFlag(), beginTime, endTime);
                                dehumidifierService.scanDehumidifier(null, beginTime, endTime, null, null);
                            }
                        }
                        jsonResult.setRespFlag("iot_1");
                        jsonResult.setTransFlag(true);
                        jsonResult.setDate(DATEF.format(new Date()));
                        break;
                }
            } catch (Exception e) {
                log.debug("array controller error:{}", e.getMessage());
            } finally {
                return RespEntity.success(jsonResult);
            }
        } else {
            jsonResult.setRespFlag("iot_1");
            jsonResult.setTransFlag(false);
            jsonResult.setDate(DATEF.format(new Date()));
            return RespEntity.fail(jsonResult);
        }
    }

    @RequestMapping(value = "/test", method = { RequestMethod.POST, RequestMethod.GET })
    public RespEntity test(@RequestBody ArrayRequest arrayRequest , @RequestHeader HttpHeaders headers)throws SQLException, NullPointerException,Exception{
        JsonResult jsonResult = new JsonResult();
        if (!(headers.getFirst("header").indexOf("iot_7_") == -1)){
            try {
                String beginTime = null;
                String endTime = null;
                if (!(arrayRequest.getBeginTime() == null)) {
                    beginTime = TimeZoneTool.formatTimeBeforeEight(arrayRequest.getBeginTime());
                }
                if (!(arrayRequest.getEndTime() == null)) {
                    endTime = TimeZoneTool.formatTimeBeforeEight(arrayRequest.getEndTime());
                }
                switch (headers.getFirst("header")){
                case "iot_7_"+Device.KONGTIAO_DEVICE:
                    if (!(arrayRequest == null)) {

                        log.debug("test空调接口");
                        log.debug("Mac:{},Mid:{},Header:{},TransFlag:{},AskFlag:{},BeginTime:{},EndTime:{}", arrayRequest.getMac(), arrayRequest.getMid(), headers.getFirst("header"), arrayRequest.getTransFlag(), arrayRequest.getAskFlag(), beginTime, endTime);
                        jsonResult.setRespFlag("iot_1");
                        jsonResult.setTransFlag(true);
                        jsonResult.setDate(DATEF.format(new Date()));
                    }
                    break;
                    case "iot_7_"+Device.CHUSHIJI_DEVICE:
                        if (!(arrayRequest == null)) {

                            log.debug("test除湿机接口");
                            log.debug("Mac:{},Mid:{},Header:{},TransFlag:{},AskFlag:{},BeginTime:{},EndTime:{}", arrayRequest.getMac(), arrayRequest.getMid(), headers.getFirst("header"), arrayRequest.getTransFlag(), arrayRequest.getAskFlag(), beginTime, endTime);
                            jsonResult.setRespFlag("iot_1");
                            jsonResult.setTransFlag(true);
                            jsonResult.setDate(DATEF.format(new Date()));
                        }
                        break;
                }
            }catch (Exception e){
                log.debug("test controller error:{}",e.getMessage());
            }finally {
                return RespEntity.success(jsonResult);
            }
        }else {
            jsonResult.setRespFlag("iot_1");
            jsonResult.setTransFlag(false);
            jsonResult.setDate(DATEF.format(new Date()));
            return RespEntity.fail(jsonResult);
        }
    }
}
