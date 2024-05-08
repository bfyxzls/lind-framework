package com.lind.common.http;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lind
 * @date 2023/2/27 17:08
 * @since 1.0.0
 */
public class XXLPost {

	private static final TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
		@Override
		public java.security.cert.X509Certificate[] getAcceptedIssuers() {
			return new java.security.cert.X509Certificate[] {};
		}

		@Override
		public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
		}

		@Override
		public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
		}
	} };

	private static Logger logger = LoggerFactory.getLogger(XXLPost.class);

	// trust-https start
	private static void trustAllHosts(HttpsURLConnection connection) {
		try {
			SSLContext sc = SSLContext.getInstance("TLS");
			sc.init(null, trustAllCerts, new java.security.SecureRandom());
			SSLSocketFactory newFactory = sc.getSocketFactory();

			connection.setSSLSocketFactory(newFactory);
		}
		catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		connection.setHostnameVerifier(new HostnameVerifier() {
			@Override
			public boolean verify(String hostname, SSLSession session) {
				return true;
			}
		});
	}

	public static void postBody(String url, int timeout, Map<String, String> requestObj) {
		HttpURLConnection connection = null;
		BufferedReader bufferedReader = null;
		try {
			// connection
			URL realUrl = new URL(url);
			connection = (HttpURLConnection) realUrl.openConnection();

			// trust-https
			boolean useHttps = url.startsWith("https");
			if (useHttps) {
				HttpsURLConnection https = (HttpsURLConnection) connection;
				trustAllHosts(https);
			}

			// connection setting
			connection.setRequestMethod("POST");
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setUseCaches(false);
			connection.setReadTimeout(timeout * 1000);
			connection.setConnectTimeout(3 * 1000);
			// Keep-Alive是HTTP协议中的一个首部字段，它的作用是保持客户端和服务器之间的持久连接，使得客户端可以在一个TCP连接上发送多个HTTP请求而不必每次发送都要建立一个新的连接。这样可以节省网络的带宽和资源，提高网络的效率。
			connection.setRequestProperty("connection", "Keep-Alive");
			// Content-Type用于指定发送给服务器的数据的类型和格式，例如：application/json、text/html等。
			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;");
			// Accept-Charset用于指定客户端希望接收到的字符集，也可以有响应格式，例如utf-8、gb2312、application/json;等。
			connection.setRequestProperty("Accept-Charset", "application/json;charset=UTF-8");

			// do connection
			connection.connect();

			// write requestBody
			if (requestObj != null) {
				StringBuffer requestBody = new StringBuffer();
				requestObj.forEach((i, o) -> {
					requestBody.append(String.format("%s=%s&", i, o));
				});
				String result = requestBody.toString();
				result = result.substring(0, result.length() - 1);
				DataOutputStream dataOutputStream = new DataOutputStream(connection.getOutputStream());
				dataOutputStream.write(result.getBytes("UTF-8"));
				dataOutputStream.flush();
				dataOutputStream.close();
			}

			/*
			 * byte[] requestBodyBytes = requestBody.getBytes("UTF-8");
			 * connection.setRequestProperty("Content-Length",
			 * String.valueOf(requestBodyBytes.length)); OutputStream outwritestream =
			 * connection.getOutputStream(); outwritestream.write(requestBodyBytes);
			 * outwritestream.flush(); outwritestream.close();
			 */

			// valid StatusCode
			int statusCode = connection.getResponseCode();
			if (statusCode != 200) {
				logger.error("error code" + statusCode);
			}

			// result
			bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
			StringBuilder result = new StringBuilder();
			String line;
			while ((line = bufferedReader.readLine()) != null) {
				result.append(line);
			}
			String resultJson = result.toString();

			System.out.println("resultJson=" + resultJson);

		}
		catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		finally {
			try {
				if (bufferedReader != null) {
					bufferedReader.close();
				}
				if (connection != null) {
					connection.disconnect();
				}
			}
			catch (Exception e2) {
				logger.error(e2.getMessage(), e2);
			}
		}
	}

	@Test
	public void post() {
		Map<String, String> map = new HashMap<>();
		map.put("client_id", "democlient");
		map.put("client_secret", "ec0fd1c6-68b0-4c39-a9fa-c3be25c8ef01");
		map.put("token",
				"eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJFXzZpaDM1eVRMSk1pZUkwdnFnOU1tVFFySjZSY1VTeGlYZU5kY01hb1lrIn0.eyJleHAiOjE2Nzc0OTA4MDMsImlhdCI6MTY3NzQ4OTYwMywianRpIjoiM2YyOWVhOGMtZmM0NS00YmY2LTg2MjctMDVjZDZlMGUyMzJmIiwiaXNzIjoiaHR0cDovLzE5Mi4xNjguNC4yNjo4MDgwL2F1dGgvcmVhbG1zL2ZhYmFvIiwiYXVkIjpbInBrdWxhdyIsInJlYWxtLW1hbmFnZW1lbnQiLCJhY2NvdW50Il0sInN1YiI6ImQyMWE1ZWM4LTMwYzUtNGRiNi1hMjZhLTE3YWM0YjNmY2ZkNCIsInR5cCI6IkJlYXJlciIsImF6cCI6InZ1ZSIsInNlc3Npb25fc3RhdGUiOiI1OThjOWQ1Mi0wMTlmLTQwMzUtYjVjMi00MDA0MjhmMzFjNTAiLCJhY3IiOiIxIiwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbImNhcnNpIl19LCJyZXNvdXJjZV9hY2Nlc3MiOnsicGt1bGF3Ijp7InJvbGVzIjpbInVtYV9wcm90ZWN0aW9uIl19LCJyZWFsbS1tYW5hZ2VtZW50Ijp7InJvbGVzIjpbInZpZXctcmVhbG0iLCJ2aWV3LWlkZW50aXR5LXByb3ZpZGVycyIsIm1hbmFnZS1pZGVudGl0eS1wcm92aWRlcnMiLCJpbXBlcnNvbmF0aW9uIiwicmVhbG0tYWRtaW4iLCJjcmVhdGUtY2xpZW50IiwibWFuYWdlLXVzZXJzIiwicXVlcnktcmVhbG1zIiwidmlldy1hdXRob3JpemF0aW9uIiwicXVlcnktY2xpZW50cyIsInF1ZXJ5LXVzZXJzIiwibWFuYWdlLWV2ZW50cyIsIm1hbmFnZS1yZWFsbSIsInZpZXctZXZlbnRzIiwidmlldy11c2VycyIsInZpZXctY2xpZW50cyIsIm1hbmFnZS1hdXRob3JpemF0aW9uIiwibWFuYWdlLWNsaWVudHMiLCJxdWVyeS1ncm91cHMiXX0sImFjY291bnQiOnsicm9sZXMiOlsibWFuYWdlLWFjY291bnQiLCJ2aWV3LWFwcGxpY2F0aW9ucyIsInZpZXctY29uc2VudCIsIm1hbmFnZS1hY2NvdW50LWxpbmtzIiwibWFuYWdlLWNvbnNlbnQiLCJkZWxldGUtYWNjb3VudCIsInZpZXctcHJvZmlsZSJdfX0sInNjb3BlIjoicm9sZXMgZXh0ZW5zaW9uLXJvbGVzIGVtYWlsIHByb2ZpbGUiLCJlbWFpbF92ZXJpZmllZCI6ZmFsc2UsIm5pY2tuYW1lIjoi5byg5LiJIiwiaXNHcm91cFVzZXIiOjAsImV4dGVuc2lvbl9yb2xlcyI6eyJ3ZWl4aW4iOlsiZXhhbSIsImJhbmt0b3BpYyJdfSwicHJlZmVycmVkX3VzZXJuYW1lIjoidGVzdCJ9.QBEA3hLWxICzgIB4tRFgwqzihouMtbxSbMYS4t5MpX2V7wihXii6V9X2JySDw6Lji-PYa-WNZaivAwzUmdHWmnA8mQwhzkw4AgOnqnrm4vcuZbuOMKHntbvNCFipmuDw48gjrQjVo9k6YDM8ZnIhmy433RFwzOeT6kLDle62a9XZR3YlRUdL78FnGDvsHNriqfZbGs-jgKgqLskbGIUFXvlT0A8UGX1plhlAmnwPTiA5yhfAr_K5pKb2KToBDedXqAOl0m_Q7Fja9od9IEiziNVSi17fKwRE0-SgJ0rd5Qx1Fq5Ni5cSALwGRZC2_uCA7fgKdgoM9u2q-k56e3srhg");

		postBody("http://192.168.4.26:8080/auth/realms/fabao/protocol/openid-connect/token/introspect", 3, map);
	}

}
