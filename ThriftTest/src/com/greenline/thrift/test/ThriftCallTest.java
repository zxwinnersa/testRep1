package com.greenline.thrift.test;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.transport.TSocket;

import com.cti.core.util.MD5Util;
import com.cti.service.remote.CtiServices;
import com.cti.service.remote.IRemoteServiceEntry;
import com.cti.service.remote.PageIn;
import com.cti.service.remote.ReturnObj;
import com.cti.service.remote.ThriftException;
import com.cti.service.remote.ThriftHeader;

public class ThriftCallTest {
	public static void main(String[] args) {
		List<String> list = new ArrayList<String>();
		// list.add("zx");
		// list.add("123123");

		// getHospitalList
		// list.add("ywhz");
		// list.add("");

		// getGuaHaoClinicsRecently
		list.add("44143784642010311A1001");
		list.add("542500197707173930");
		list.add("0");
		list.add("162");

		// list.add("44143784642010311A1001");
		// list.add("100");

		/* getWaitroomListByHCodeWithLoginNew 分页 */
		// list.add("44143784642010311A1001");
		// list.add("8dd87179-6c10-4be2-b088-e70c14a8cc55");
		// list.add("610401197402285322");
		// list.add("0");
		// list.add("");

		// getHealthCardList
		// list.add("542500197707173930");

		// 伪代码
		long startTime = System.currentTimeMillis(); // 获取开始时间
		// osgiHealthCardService
		// dataAnalysisService
		// osgiHealthUserService
		// ReturnObj returnObj = ThriftCallTest.callThrift("192.168.1.167",
		// 7912,
		// "osgiHealthUserService", "login", list);

		ReturnObj returnObj = ThriftCallTest.callThrift("192.168.1.167", 7912,
				"appservice", "getGuaHaoClinicsRecently", list);

		// ReturnObj returnObj = ThriftCallTest.callThrift("192.168.1.167",
		// 7912,
		// "appservice", "getWaitroomListByHCodeWithLoginNew", list);

		// ReturnObj returnObj = ThriftCallTest.callThrift("192.168.1.167",
		// 7912,
		// "appservice", "getWaitroomDetail", list);

		// ReturnObj returnObj = ThriftCallTest.callThrift("192.168.1.61", 7913,
		// "osgiGgffService", "getHospitalList", list);

		// ReturnObj returnObj = ThriftCallTest.callThrift("192.168.1.167",
		// 7912,
		// "osgiHealthCardService", "getHealthCardList", list);

		System.out.println(returnObj);
		long endTime = System.currentTimeMillis(); // 获取结束时间
		System.out.println("程序运行时间： " + (endTime - startTime) + "ms");

	}

	public static final ReturnObj callThrift(String url, int port,
			String serviceName, String methodName, List<String> parameters) {
		DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
		String time = df.format(new Date());
		String val = MD5Util.MD5("Oder7^ji&12k0OL1=+qwV1" + time, "utf-8");
		TSocket transport = new TSocket(url, port);
		ReturnObj returnObj = new ReturnObj();
		try {
			transport.open();
			TBinaryProtocol protocol = new TBinaryProtocol(transport);
			// Use the service we already defined
			IRemoteServiceEntry.Iface service = new IRemoteServiceEntry.Client(
					protocol);
			ThriftHeader thriftHeader = new ThriftHeader();
			thriftHeader.setClient("java000001");
			thriftHeader.setTime(time);
			thriftHeader.setKey(val);
			CtiServices ser = new CtiServices();
			ser.setServiceId(serviceName);
			ser.setMethodName(methodName);
			if (parameters != null) {
				ser.setParameters(parameters);
			}
			PageIn pageIn = new PageIn();
			pageIn.setPageAction(4);
			pageIn.setCurrentPage(1);
			pageIn.setPageIndex(1);
			pageIn.setPageSize(10);
			// pageIn.setSortField("");
			// pageIn.setSortOrder("");
			returnObj = service.invoke(thriftHeader, ser, null);
		} catch (TException e) {
			e.printStackTrace();
			returnObj.setCode(2);
			returnObj.setThriftException(new ThriftException(101, e
					.getMessage(), e.getLocalizedMessage()));
		} finally {
			transport.close();
		}
		return returnObj;
	}
}
