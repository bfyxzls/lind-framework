package com.lind.common.core.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class MachineCodeUtils {

	private static final String LINUX = "LINUX";

	private static final String WINDOWS = "WINDOWS";

	/**
	 * @param 打印
	 **/
	public static void print() {
		System.out.print("当前机器码输出:  ");
		System.out.println(getSn());
	}

	/**
	 * @param
	 * @return java.lang.String
	 * @Author Guoqiang.Bai
	 * @Description TODO
	 * @Date 15:08 2020/5/6
	 **/
	public static String getSn() {
		String os = System.getProperty("os.name");
		os = os.toUpperCase();

		String mainBoardNumber = "";
		if (LINUX.equals(os)) {

			// linux选择 cpuId + 主板id + 网卡地址
			String cpuid = getSerialNumber("dmidecode -t processor | grep 'ID'", "ID", ":", "");
			String boardNumber = getSerialNumber("dmidecode | grep UUID", "UUID", ":", "");
			String mac = getSerialNumber("ifconfig -a", "ether", " ", "ether");
			mainBoardNumber = cpuid + boardNumber + mac;

		}
		else if (os.contains(WINDOWS)) {

			// windows环境取 cpuId + 硬盘(系统盘) + 网卡地址
			mainBoardNumber = getCPUSerial() + getHardDiskSN() + getMac();

		}
		else {
			return "不支持当前系统的获取.";
		}

		// 拿到机器码之后做md5加密,再返回后8位
		String encStr = stringToMD5(mainBoardNumber);
		return encStr.substring(encStr.length() - 8);
	}

	/**
	 * @return java.lang.String
	 * @Author GuoQiang.Bai
	 * @Date 14:33 2019/12/16
	 * @Description
	 * @Param [cmd, record, symbol]
	 **/
	private static String getSerialNumber(String cmd, String record, String symbol, String macMark) {
		String execResult = executeLinuxCmd(cmd);
		String[] infos = execResult.split("\n");

		for (String info : infos) {
			info = info.trim();
			if (info.contains(record)) {

				// 不是获取网卡标识，那么需要做replace的替换操作
				if (!macMark.equals("ether")) {
					info = info.replace(" ", "");
				}

				String[] sn = info.split(symbol);
				return sn[1];
			}
		}

		return null;
	}

	/**
	 * @return java.lang.String
	 * @Author GuoQiang.Bai
	 * @Date 14:37 2019/12/16
	 * @Description 执行linux命令
	 * @Param [cmd]
	 **/
	private static String executeLinuxCmd(String cmd) {
		try {
			Runtime run = Runtime.getRuntime();
			Process process;
			process = run.exec(cmd);
			InputStream in = process.getInputStream();
			StringBuffer out = new StringBuffer();
			byte[] b = new byte[8192];
			for (int n; (n = in.read(b)) != -1;) {
				out.append(new String(b, 0, n));
			}
			in.close();
			process.destroy();
			return out.toString();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取C盘序列号
	 * @return
	 */
	public static String getHardDiskSN() {
		// 默认取c盘
		String drive = "c";
		String result = "";
		try {
			File file = File.createTempFile("realhowto", ".vbs");
			file.deleteOnExit();
			FileWriter fw = new FileWriter(file);

			String vbs = "Set objFSO = CreateObject(\"Scripting.FileSystemObject\")\n"
					+ "Set colDrives = objFSO.Drives\n" + "Set objDrive = colDrives.item(\"" + drive + "\")\n"
					+ "Wscript.Echo objDrive.SerialNumber"; // see note
			fw.write(vbs);
			fw.close();
			String path = file.getPath().replace("%20", " ");
			Process p = Runtime.getRuntime().exec("cscript //NoLogo " + path);
			BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String line;
			while ((line = input.readLine()) != null) {
				result += line;
			}
			input.close();
		}
		catch (Exception e) {
			e.printStackTrace();
			result = "";
		}
		return result.trim();
	}

	/**
	 * 获取本机MAC 通过jdk自带的方法,先获取本机所有的ip,然后通过NetworkInterface获取mac地址
	 * @return
	 */
	public static String getMac() {
		try {
			String resultStr = "";
			List<String> ls = getLocalHostLANAddress();
			for (String str : ls) {
				// 获取本地IP对象
				InetAddress ia = InetAddress.getByName(str);
				// 获得网络接口对象（即网卡），并得到mac地址，mac地址存在于一个byte数组中。
				byte[] mac = NetworkInterface.getByInetAddress(ia).getHardwareAddress();
				if (mac == null) {
					continue;
				}
				// 下面代码是把mac地址拼装成String
				StringBuilder sb = new StringBuilder();
				for (int i = 0; i < mac.length; i++) {
					if (i != 0) {
						sb.append("-");
					}
					// mac[i] & 0xFF 是为了把byte转化为正整数
					String s = Integer.toHexString(mac[i] & 0xFF);
					sb.append(s.length() == 1 ? 0 + s : s);
				}
				// 把字符串所有小写字母改为大写成为正规的mac地址并返回
				resultStr += sb.toString().toUpperCase() + ",";
			}
			return resultStr;
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		return "";
	}

	/**
	 * @param
	 * @return java.util.List<java.lang.String>
	 * @Author Guoqiang.Bai
	 * @Description 获取本机所有的ip
	 * @Date 11:39 2020/4/26
	 **/
	private static List<String> getLocalHostLANAddress() throws Exception {
		List<String> ips = new ArrayList<String>();
		Enumeration<NetworkInterface> interfs = NetworkInterface.getNetworkInterfaces();
		while (interfs.hasMoreElements()) {
			NetworkInterface interf = interfs.nextElement();
			Enumeration<InetAddress> addres = interf.getInetAddresses();
			while (addres.hasMoreElements()) {
				InetAddress in = addres.nextElement();
				if (in instanceof Inet4Address) {
					if (!"127.0.0.1".equals(in.getHostAddress())) {
						ips.add(in.getHostAddress());
					}
				}
			}
		}
		return ips;
	}

	/**
	 * 获取CPU序列号
	 * @return
	 */
	public static String getCPUSerial() {
		String result = "";
		try {
			File file = File.createTempFile("tmp", ".vbs");
			file.deleteOnExit();
			FileWriter fw = new FileWriter(file);
			String vbs = "Set objWMIService = GetObject(\"winmgmts:\\\\.\\root\\cimv2\")\n"
					+ "Set colItems = objWMIService.ExecQuery _ \n" + "   (\"Select * from Win32_Processor\") \n"
					+ "For Each objItem in colItems \n" + "    Wscript.Echo objItem.ProcessorId \n"
					+ "    exit for  ' do the first cpu only! \n" + "Next \n";

			fw.write(vbs);
			fw.close();
			String path = file.getPath().replace("%20", " ");
			Process p = Runtime.getRuntime().exec("cscript //NoLogo " + path);
			BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String line;
			while ((line = input.readLine()) != null) {
				result += line;
			}
			input.close();
			file.delete();
		}
		catch (Exception e) {
			e.fillInStackTrace();
		}
		if (result.trim().length() < 1 || result == null) {
			result = "";
		}
		return result.trim();
	}

	/**
	 * @param plainText
	 * @return java.lang.String
	 * @Author Guoqiang.Bai
	 * @Description java自动md5加密
	 * @Date 8:53 2020/4/27
	 **/
	public static String stringToMD5(String plainText) {
		byte[] secretBytes = null;
		try {
			secretBytes = MessageDigest.getInstance("md5").digest(plainText.getBytes());
		}
		catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("没有这个md5算法！");
		}
		String md5code = new BigInteger(1, secretBytes).toString(16);
		for (int i = 0; i < 32 - md5code.length(); i++) {
			md5code = "0" + md5code;
		}
		return md5code;
	}

}
