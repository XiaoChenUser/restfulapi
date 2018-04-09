package com.restfulapi.hbase;

import com.restfulapi.hbase.entity.AirConData;
import com.restfulapi.hbase.utils.StrUtils;
import com.restfulapi.service.AirService;
import com.restfulapi.service.AirTaskService;
import com.restfulapi.service.DehumidifierService;
import com.restfulapi.service.DehumidifierTaskService;
import org.apache.hadoop.hbase.filter.RegexStringComparator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mortbay.log.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RestfulapiApplicationTests {
	public static final Logger log = LoggerFactory.getLogger(RestfulapiApplicationTests.class);
	public static final SimpleDateFormat DATEROWKEY = new SimpleDateFormat("yyyyMMddHHmmss");

	public static final SimpleDateFormat DATEE = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	private static SimpleDateFormat DATETT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
	@Autowired
    AirService airService;
	@Autowired
    AirTaskService airTaskService;
	@Autowired
	DehumidifierService dehumidifierService;
	@Autowired
	DehumidifierTaskService dehumidifierTaskService;

	@Test
	public void kafka()throws Exception{
		airTaskService.testTask("2018022123");
	}
	@Test
	public void onTime()throws Exception{
		List<AirConData> airConDataList = new ArrayList<AirConData>();
//        airConDataList = scanDataAirCon(mac,starttime,endtime,mid,evt);
		try {
			airConDataList = airTaskService.scanAirConditionOnTime("f4911e10b010");
		} catch (Exception e) {
			e.printStackTrace();
		}
		for(int i=0;i<airConDataList.size();i++){
			String airConDataStr = StrUtils.airConToJson(airConDataList.get(i));
			System.out.println(airConDataStr);
		}

	}

	@Test
	public void rowKey()throws Exception{
        List list = dehumidifierTaskService.scanRowKey();
        System.out.println(list);
    }
	@Test
	public void contextLoads() throws Exception {
		String mac = "34ea3406bd42";
		String starttime ="2018-02-01 00:00:00";
		String endtime = "2018-02-10 00:00";
		String mid = "11000";
		//String mid =null;
		String evt = null;
		System.out.println(DATEROWKEY.format(DATEE.parse(starttime)));
//		dehumidifierService.scanDehumidifier(null,starttime,endtime,null,null);
		//airService.scanArrayAirCon(null,starttime,endtime,null,null);
		//hbaseService.test(mac,starttime,endtime,mid,evt);
	}
	@Test
	public void realDataTest()throws Exception{
		String startTime = airTaskService.getCalDate(-1);
		String endTime = airTaskService.getCalDate(0);
		airTaskService.scanRealData(startTime,endTime);
	}
	@Test
	public void tt() throws Exception{
		String mac = "34ea342eb2ca";
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY) - 8);
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		System.out.println("八个小时前的时间：" + df.format(calendar.getTime()));
		System.out.println("当前的时间：" + df.format(new Date()));
		LocalDateTime today = LocalDateTime.now();
		LocalDateTime date = LocalDateTime.of(today.getYear(),today.getMonth(),today.getDayOfMonth(),today.getHour()-8,today.getMinute(),today.getSecond());
		System.out.println(date);
		System.out.println(String.valueOf(date));
		List<AirConData> airConDataList = airTaskService.scanAirConditionOnTime(null);
		for(int i=0;i<airConDataList.size();i++){
			String airConDataStr = StrUtils.airConToJson(airConDataList.get(i));
			System.out.println(airConDataStr);
		}
    }

	@Test
	public void arraytest() throws Exception {
        ArrayList<String> mac = new ArrayList<>();
        ArrayList<String> mid = new ArrayList<>();
        String starttime ="2017-01-20 00:00:00";
		String endtime = "2018-02-03 00:00:00";
		//String mid = "10001";
        mac.add("34ea340022e9");
		mac.add("34ea340756f1");
		mid.add("10001");
		mid.add("10002");
        //mid.add("11000");
		String evt = null;
		//hbaseService.scanDataAirCon(null,starttime,endtime,null,evt);
//		for (int i = 0; i < mac.size(); i++) {
//			hbaseService.scanArrayAirCon(mac.get(i),null,null,null,evt);
//		}
		for (int i = 0; i < mid.size(); i++) {
			airService.scanArrayAirCon(null,starttime,endtime,null,evt);
		}

	}
	@Test
	public void test()throws Exception{
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -1);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE,0);
		cal.set(Calendar.SECOND,0);
		cal.set(Calendar.MILLISECOND,0);
		System.out.println(DATETT.format(cal.getTime()));
		String time = "2017-12-21 20:30";
		Date date = DATEE.parse(time);
		System.out.println(DATEE.parse(time));
		System.out.println(DATETT.format(date));
		System.out.println(DATETT.format(new Date()));
		System.out.println(StrUtils.tmGen(new Date()));
		Log.info("rowKey :" + String.valueOf(new RegexStringComparator(".*" + DATETT.format(new Date()))));
	}


	@Test
	public void ttt()throws Exception {
		String ss = "Session 0x0 for server null, unexpected error, closing socket connection and";
		String sss = "Started RestfulapiApplication in 10.842 seconds (JVM running for 11.63)";
		if (sss.toString().indexOf("closing socket connection") == -1) {
			try {
				System.out.println("service");
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}else {
			System.out.println("MMP");
		}
	}

}
