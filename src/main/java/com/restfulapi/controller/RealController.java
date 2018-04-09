package com.restfulapi.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.restfulapi.common.pojo.ArrayRequest;
import com.restfulapi.common.pojo.Device;
import com.restfulapi.common.pojo.JsonResult;
import com.restfulapi.common.pojo.RespEntity;
import com.restfulapi.hbase.entity.AirConData;
import com.restfulapi.hbase.entity.DehumidifierData;
import com.restfulapi.service.AirTaskService;
import com.restfulapi.service.DehumidifierTaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/x")
public class RealController {
    public static final Logger log = LoggerFactory.getLogger(RealController.class);
    public static final SimpleDateFormat DATEF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static final SimpleDateFormat DATEDF = new SimpleDateFormat("yyyy-MM-dd");

    @Autowired
    AirTaskService airTaskService;
    @Autowired
    DehumidifierTaskService dehumidifierTaskService;


    @RequestMapping(value = "/real", method = { RequestMethod.POST, RequestMethod.GET })
    public RespEntity onTimeRequest(@RequestBody ArrayRequest arrayRequest , @RequestHeader HttpHeaders headers)throws SQLException, NullPointerException,Exception {
        JsonResult jsonResult = new JsonResult();
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY) - 8);
        String date = DATEDF.format(calendar.getTime());

        if (!(headers.getFirst("header").indexOf("iot_7_") == -1)){
            try {
                switch (headers.getFirst("header")){
                    case "iot_7_"+Device.KONGTIAO_DEVICE:
                        //if (!(arrayRequest.getMac() == null)) {
                            log.debug("real空调接口,arrayRequest.getMac().size:{}", arrayRequest.getMac().size());
                            for (int i = 0; i < arrayRequest.getMac().size(); i++) {
                                log.debug("Mac:{},Mid:{},Header:{},TransFlag:{},AskFlag:{},Time:{}", arrayRequest.getMac().get(i), arrayRequest.getMid(), headers.getFirst("header"), arrayRequest.getTransFlag(), arrayRequest.getAskFlag(), date);
                                JSONObject object = new JSONObject();
                                List<AirConData> airConDataList = airTaskService.scanAirConditionOnTime(String.valueOf(arrayRequest.getMac().get(i)));
                                object.put("Mac", arrayRequest.getMac().get(i));
                                object.put("info", airConDataList);
                                jsonArray.add(object);
                            }
                        //}
                        break;
                    case "iot_7_"+ Device.KONGTIAO_MAC_API:
                        if (!(arrayRequest == null)) {
                            log.debug("获取空调Mac列表接口");
                            log.debug("Mac:{},Mid:{},Header:{},TransFlag:{},AskFlag:{},Time:{}", arrayRequest.getMac(), arrayRequest.getMid(), headers.getFirst("header"), arrayRequest.getTransFlag(), arrayRequest.getAskFlag(), date);
                            List<String> rowKeyList = null;
                            rowKeyList = airTaskService.scanRowKey();

                            jsonObject.put("Mac", rowKeyList);
                            jsonArray.add(jsonObject);
                        }
                        break;
                    case "iot_7_"+ Device.CHUSHIJI_MAC_API:
                        if (!(arrayRequest == null)) {
                            log.debug("获取除湿机Mac列表接口");
                            log.debug("Mac:{},Mid:{},Header:{},TransFlag:{},AskFlag:{},Time:{}", arrayRequest.getMac(), arrayRequest.getMid(), headers.getFirst("header"), arrayRequest.getTransFlag(), arrayRequest.getAskFlag(), date);
                            List<String> rowKeyList = null;
                            rowKeyList = dehumidifierTaskService.scanRowKey();

                            jsonObject.put("Mac", rowKeyList);
                            jsonArray.add(jsonObject);
                        }
                        break;
                    case "iot_7_"+ Device.CHUSHIJI_DEVICE:
                            log.debug("real除湿机接口");
                            log.debug("Mac:{},Mid:{},Header:{},TransFlag:{},AskFlag:{},BeginTime:{},EndTime:{}", arrayRequest.getMac(), arrayRequest.getMid(), headers.getFirst("header"), arrayRequest.getTransFlag(), arrayRequest.getAskFlag());
                            for (int i = 0; i < arrayRequest.getMac().size(); i++) {
                                log.debug("Mac:{},Mid:{},Header:{},TransFlag:{},AskFlag:{},Time:{}", arrayRequest.getMac().get(i), arrayRequest.getMid(), headers.getFirst("header"), arrayRequest.getTransFlag(), arrayRequest.getAskFlag(), date);
                                JSONObject object = new JSONObject();
                                List<DehumidifierData> dehumidifierDataList = dehumidifierTaskService.scanDehumiditifierOnTime(String.valueOf(arrayRequest.getMac().get(i)));
                                object.put("Mac", arrayRequest.getMac().get(i));
                                object.put("info", dehumidifierDataList);
                                jsonArray.add(object);
                            }
                        break;
                }
            }catch (Exception e){
                log.debug("real controller error:{}",e.getMessage());
            }finally {
                return RespEntity.success(jsonArray);
            }
        }else {
            jsonResult.setRespFlag("iot_1");
            jsonResult.setTransFlag(false);
            jsonResult.setDate(DATEF.format(new Date()));
            return RespEntity.fail(jsonResult);
        }
    }
}


