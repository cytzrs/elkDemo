package com.elastic.stack.demo.elkDemo.sms;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang.time.DateFormatUtils;
import org.springframework.web.client.RestTemplate;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;


public class CScode {

	// 算法名称
	public static final String KEY_ALGORITHM = "DES";
	// 算法名称/加密模式/填充方式
	public static final String CIPHER_MODE = "DES/CBC/PKCS5Padding";
	//private static final String EncryptOrDecrypt = null;

	/**
	 * 加密
	 * 对消息的加密算法：Base64(DES(MD5(消息体)+消息体))
	 * @param message 消息明文
	 * @param key 密钥，系统生成的密钥为24位的Base64字符，由两部分构成：12位Key(密钥密码)＋12位的IV(初始化向量)。
	 * @return String 消息密文
	 * @throws Exception
	 */
	public static String encryptString(String message, String key) throws Exception {


		//
		//System.out.println("传入message参数:"+message);
		//System.out.println("传入24位key："+key);
		if(message == null || message.length() == 0){
			throw new IllegalArgumentException("传入message参数不正确。");
		}
		else if(key == null || key.length() == 0 || key.length() != 24){
			throw new IllegalArgumentException("传入key参数不正确。");
		}


		//获取key前12位字节数组
		byte[] keyArray = getKey(key);
		//System.out.println("key前12位字节数组长度： "+keyArray.length);
		//MD5加密+消息明文
		String s = md5(message) + message;

		Cipher cipher = Cipher.getInstance(CIPHER_MODE);
		DESKeySpec desKeySpec = new DESKeySpec(keyArray);
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(KEY_ALGORITHM);
		SecretKey secretKey = keyFactory.generateSecret(desKeySpec);


		//获取key后12位字节数组
		byte[] ivArray = getIV(key);
		IvParameterSpec iv = new IvParameterSpec(ivArray);
		//System.out.println("key前12后字节数组长度： "+ivArray.length);
		cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);

		//DES加密后的字节数组
		byte[] encryptbyte = cipher.doFinal(s.getBytes());
		BASE64Encoder base64Encoder = new BASE64Encoder();
		//转为Base64返回
		return base64Encoder.encode(encryptbyte);
	}

	/**
	 * 解密
	 * 对消息的加密算法：Base64(DES(MD5(消息体)+消息体))
	 * @param message 消息密文
	 * @param key 密钥，系统生成的密钥为24位的Base64字符，由两部分构成：12位Key(密钥密码)＋12位的IV(初始化向量)。
	 * @return String 消息明文
	 * @throws Exception
	 */
	public static String decryptString(String message, String key) throws Exception {

		if(message == null || message.length() == 0){
			throw new IllegalArgumentException("传入message参数不正确。");
		}
		else if(key == null || key.length() == 0 || key.length() != 24){
			throw new IllegalArgumentException("传入key参数不正确。");
		}

		//获取key前12位字节数组
		byte[] keyArray = getKey(key);

		BASE64Decoder base64Decoder = new BASE64Decoder();
		//转为DES加密的字节数组
		byte[] bytesrc = base64Decoder.decodeBuffer(message);
		Cipher cipher = Cipher.getInstance(CIPHER_MODE);
		DESKeySpec desKeySpec = new DESKeySpec(keyArray);
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(KEY_ALGORITHM);
		SecretKey secretKey = keyFactory.generateSecret(desKeySpec);

		byte[] ivArray = getIV(key);
		IvParameterSpec iv = new IvParameterSpec(ivArray);
		cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);

		//获取MD5加密+消息明文的字节数组
		byte[] retByte = cipher.doFinal(bytesrc);
		String r = new String(retByte);
		//截取消息明文并返回
		return r.substring(32);
	}

	/**
	 * 获取key中的key(密钥密码)
	 * @param key 系统生成的密钥为24位的Base64字符，由两部分构成：12位Key(密钥密码)＋12位的IV(初始化向量)。
	 * @return 返回8位无符号字节数组
	 * @throws IOException
	 */
	private static byte[] getKey(String key) throws IOException{
		//截取key的后12位初始化向量字符串
		String keyStr = key.substring(0, 12);
		//System.out.println("截取key前12位"+keyStr);
		BASE64Decoder base64Decoder = new BASE64Decoder();
		byte[] keyArray = base64Decoder.decodeBuffer(keyStr);
		//System.out.println("截取key前12位base64："+keyArray);
		return keyArray;

	}

	/**
	 * 获取key中的IV(初始化向量)
	 * @param key 系统生成的密钥为24位的Base64字符，由两部分构成：12位Key(密钥密码)＋12位的IV(初始化向量)。
	 * @return 返回8位无符号字节数组
	 * @throws IOException
	 */
	private static byte[] getIV(String key) throws IOException{
		//截取key的后12位初始化向量字符串
		String ivStr = key.substring(12,24);
		//System.out.println("截取key后12位"+ivStr);
		BASE64Decoder base64Decoder = new BASE64Decoder();
		byte[] ivArray = base64Decoder.decodeBuffer(ivStr);
		//System.out.println("截取key后12位base64："+ivArray);
		return ivArray;
	}

	/**
	 * MD5加密
	 * @param message 明文
	 * @return String 密文
	 * @throws NoSuchAlgorithmException
	 */
	private static String md5(String message) throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("md5");
		md.update(message.getBytes());
		byte b[] = md.digest();
		int i;
		StringBuffer sb = new StringBuffer("");
		for (int offset = 0; offset < b.length; offset++) {
			i = b[offset];
			if (i < 0)
				i += 256;
			if (i < 16)
				sb.append("0");
			sb.append(Integer.toHexString(i));
		}
		return sb.toString();

	}



	//======================================================================================================
	//输入key  和字符串进行解密加密
	//==========================================================================================================

	private final static String key = "pF2P1VtPqK0=N6DgANZsvYA=";

	public static void main(String[] args) throws Exception {
		String json = DateFormatUtils.format(new Date(), "yyyyMMddHHmmssssssss");
		System.out.println(json);
		/*ECHead _head = new ECHead();
		_head.setBCode("EMI10001");
		_head.setAck("1");
		_head.setSqId("ec2017101217241110001515");
		_head.setECID("c00280");

		ECBody _body = new ECBody();
		_body.setECOrderId("104343399812");

		ECOrder _order = new ECOrder();
		_order.setPID("100021");

		List<String> phones = new ArrayList<>();
		phones.add("18621077935");
		_order.setPhones(phones);

		_body.setOrderList(_order);

		Map<String, String> convBody = BeanUtils.bean2Map(_body);
		String body = JSON.toJSONString(convBody);

		String bodyEncpy = processBody(body);
		System.out.println(bodyEncpy);

		_head.setCnxt(bodyEncpy);

		String head = JSON.toJSONString(_head);
		System.out.println(head);*/

		/*Map<String, String> mHead = new HashMap<>();
		mHead.put("BCode", "EMI10001");
		mHead.put("Ack", "1");
		mHead.put("SqId", "ec2017101217241110001515");
		mHead.put("ECID", "c00280");

		Map<String, String> mBody = new HashMap<>();

		Map<String, String> mOrders = new HashMap<>();
		List<Map<String, String>> dfdsfds = new ArrayList<>();
		mOrders.put("PID", "100021");
		mOrders.put("Phones", "18621077935");
		dfdsfds.add(mOrders);

		String mOrdersJson = JSON.toJSONString(dfdsfds);
		mBody.put("OrderList", mOrdersJson);
		mBody.put("ECOrderId", "104343399812");
		String mBodyJson = JSON.toJSONString(mBody);

		System.out.println(mBodyJson);
		String mBodyEncpy = processBody(mBodyJson);

		System.out.println(mBodyEncpy);*/

		/*String mmBody = "{\"ECOrderId\":\"104343399812\",\"OrderList\":[{\"PID\":\"100021\",\"Phones\":\"18621077935\"}]}";

		System.out.println(mmBody);

		String result = processBody(mmBody);
		System.out.println(result);*/

		/*String sfsdf = "3IP4de2Bcxp8sYoCi9QPG9elIVwiD1Iof+CewuuMNR3Bly3mqGNvbgC+ZE3wrRTAZfJsnlMXP7lIUNHHUWK3g1rRdcWbdmbm1pFQTkAFpvGyZ1bh2aYx2bS/gAcJKYm+goNrSuForAyHGEQwJhPns0y/m2qVTsTTSXI35riCoaEY6uhO1xCgg3xUqpy30mrX0pQQDEwX8x17yJzRkzgZ7qWFmdK/46RH3NNUabryRqTuQm7yAWS88g==";
		System.out.println(processBody(sfsdf));

		processBody("{\"BCode\":\"EMI1001\",\"Ack\":\"1\",\"ECID\":\"100172\",\"SqId\":\"201705311645123456\",\"Cnxt\":\"wPrUL/KKQrlk1juX6Sh3E/CxuGExtZKqH7Jc9AkhB6k3S/YSnuExQZ+OefXAGDggf2smE0CMdUl9\\r\\nAekE4LGFFh3RO5cwROBK9HH9ADRao1mLmzM7E4cb4njTariAuttTPvWrCPWREXGcnc0HSQ8+Zoik\\r\\nI8NTRYZAa0c99cjxKf+T+sqhJ9FGdJ/dIk0HuEAmMeB1mvFsHLT8QAtxs4Saui/9gj/yeKNe7KpX\\r\\n5d4tEs36N39Pa2YTDBbyY+MOIFLJ\"}");
*/
		String str = CScode.decryptString("mf1gpek0uUsDAi+qQ3AXYBKaXb4fKbEdJLmbB8a2PvBbzW4QTP3qNdBPM159dfMHoHWikfOOEuZ/YjXl3OD9rs3Ce9uh/dtd9m0sTNV+E5kQ7SMGHhp1lTYimfmaf+WR3QYq/A2L154O1+Bq2WMotaDDJzWtYgIkK4EcpTXsI3YFOTCAeM+uHJMcTVIccOVrxZ7hs4DbTMW8e7we79d9nRxEc5zAIx80", key);
		System.out.println("\n" + str);
		/*String url = "%7B%22BCode%22%3A%22EMI1003%22%2C%22Ack%22%3A%221%22%2C%22SqId%22%3A%222017101415151810005127%22%2C%22ECID%22%3A%22C00280%22%2C%22Cnxt%22%3A%22yAiX%2FdcfvHXxqNcBVoY%2F84kTgoVjd0T880SC6IzT1LdguZOhTnJYXyEVaqimv1pv6MLjCgE4V9H5K4QELRNAYfPHgimocPk3FKnl+w7SsuwXv59uRUZU8JdEeT6Opv4CrYU17QBELpWswxcCIh%2FMHfDOo4V9fp3L+F1nWbglmBHOOytNkg%2FakGIggjM8zJcHApJqRAgVBkfGvbKDMWsugY41o1nRAiSN%22%7D=";
		url = URLDecoder.decode(url);
		System.out.print(url);*/
	}

	private static String processHead(String sendString) throws Exception {
		String MSG = CScode.encryptString(sendString, key);
		//解决控制台输出/r/n 为转义字符
		String MSG1 = MSG;
		if (MSG.contains("\r\n"))
		{MSG1 = MSG.replace("\r\n", "");}
		String MSGM = CScode.decryptString(MSG, key);
		return MSG1;
	}

	private static String processBody(String sendString) throws Exception {
		String MSG = CScode.encryptString(sendString, key);
		//解决控制台输出/r/n 为转义字符
		String MSG1 = MSG;
		if (MSG.contains("\r\n"))
		{MSG1 = MSG.replace("\r\n", "");}
		String MSGM = CScode.decryptString(MSG, key);
		System.out.println(MSGM);
		return MSG1;
	}

	/*public void sendRequest_EMI1001() {
		RestTemplate template
	}*/
}



