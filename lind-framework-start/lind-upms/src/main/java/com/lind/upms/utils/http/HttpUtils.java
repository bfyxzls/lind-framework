package com.lind.upms.utils.http;

import com.lind.upms.constant.Constants;
import com.lind.upms.utils.StringUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import javax.net.ssl.*;
import java.io.*;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Map;

/**
 * 通用http发送方法
 *
 * @author ruoyi
 */
public class HttpUtils {

	private static final Logger log = LoggerFactory.getLogger(HttpUtils.class);

	/**
	 * 向指定 URL 发送GET方法的请求
	 * @param url 发送请求的 URL
	 * @return 所代表远程资源的响应结果
	 */
	public static String sendGet(String url) {
		return sendGet(url, StringUtils.EMPTY);
	}

	/**
	 * 向指定 URL 发送GET方法的请求
	 * @param url 发送请求的 URL
	 * @param param 请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
	 * @return 所代表远程资源的响应结果
	 */
	public static String sendGet(String url, String param) {
		return sendGet(url, param, Constants.UTF8);
	}

	/**
	 * 向指定 URL 发送GET方法的请求
	 * @param url 发送请求的 URL
	 * @param param 请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
	 * @param contentType 编码类型
	 * @return 所代表远程资源的响应结果
	 */
	public static String sendGet(String url, String param, String contentType) {
		StringBuilder result = new StringBuilder();
		BufferedReader in = null;
		try {
			String urlNameString = StringUtils.isNotBlank(param) ? url + "?" + param : url;
			log.info("sendGet - {}", urlNameString);
			URL realUrl = new URL(urlNameString);
			URLConnection connection = realUrl.openConnection();
			connection.setRequestProperty("accept", "*/*");
			connection.setRequestProperty("connection", "Keep-Alive");
			connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			connection.connect();
			in = new BufferedReader(new InputStreamReader(connection.getInputStream(), contentType));
			String line;
			while ((line = in.readLine()) != null) {
				result.append(line);
			}
			log.info("recv - {}", result);
		}
		catch (ConnectException e) {
			log.error("调用HttpUtils.sendGet ConnectException, url=" + url + ",param=" + param, e);
		}
		catch (SocketTimeoutException e) {
			log.error("调用HttpUtils.sendGet SocketTimeoutException, url=" + url + ",param=" + param, e);
		}
		catch (IOException e) {
			log.error("调用HttpUtils.sendGet IOException, url=" + url + ",param=" + param, e);
		}
		catch (Exception e) {
			log.error("调用HttpsUtil.sendGet Exception, url=" + url + ",param=" + param, e);
		}
		finally {
			try {
				if (in != null) {
					in.close();
				}
			}
			catch (Exception ex) {
				log.error("调用in.close Exception, url=" + url + ",param=" + param, ex);
			}
		}
		return result.toString();
	}

	/**
	 * 向指定 URL 发送POST方法的请求
	 * @param url 发送请求的 URL
	 * @param param 请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
	 * @return 所代表远程资源的响应结果
	 */
	public static String sendPost(String url, String param) {
		PrintWriter out = null;
		BufferedReader in = null;
		StringBuilder result = new StringBuilder();
		try {
			log.info("sendPost - {}", url);
			URL realUrl = new URL(url);
			URLConnection conn = realUrl.openConnection();
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			conn.setRequestProperty("Accept-Charset", "utf-8");
			conn.setRequestProperty("contentType", "utf-8");
			conn.setDoOutput(true);
			conn.setDoInput(true);
			out = new PrintWriter(conn.getOutputStream());
			out.print(param);
			out.flush();
			in = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
			String line;
			while ((line = in.readLine()) != null) {
				result.append(line);
			}
			log.info("recv - {}", result);
		}
		catch (ConnectException e) {
			log.error("调用HttpUtils.sendPost ConnectException, url=" + url + ",param=" + param, e);
		}
		catch (SocketTimeoutException e) {
			log.error("调用HttpUtils.sendPost SocketTimeoutException, url=" + url + ",param=" + param, e);
		}
		catch (IOException e) {
			log.error("调用HttpUtils.sendPost IOException, url=" + url + ",param=" + param, e);
		}
		catch (Exception e) {
			log.error("调用HttpsUtil.sendPost Exception, url=" + url + ",param=" + param, e);
		}
		finally {
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			}
			catch (IOException ex) {
				log.error("调用in.close Exception, url=" + url + ",param=" + param, ex);
			}
		}
		return result.toString();
	}

	public static String sendSSLPost(String url, String param) {
		StringBuilder result = new StringBuilder();
		String urlNameString = url + "?" + param;
		try {
			log.info("sendSSLPost - {}", urlNameString);
			SSLContext sc = SSLContext.getInstance("SSL");
			sc.init(null, new TrustManager[] { new TrustAnyTrustManager() }, new java.security.SecureRandom());
			URL console = new URL(urlNameString);
			HttpsURLConnection conn = (HttpsURLConnection) console.openConnection();
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			conn.setRequestProperty("Accept-Charset", "utf-8");
			conn.setRequestProperty("contentType", "utf-8");
			conn.setDoOutput(true);
			conn.setDoInput(true);

			conn.setSSLSocketFactory(sc.getSocketFactory());
			conn.setHostnameVerifier(new TrustAnyHostnameVerifier());
			conn.connect();
			InputStream is = conn.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			String ret = "";
			while ((ret = br.readLine()) != null) {
				if (ret != null && !"".equals(ret.trim())) {
					result.append(new String(ret.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8));
				}
			}
			log.info("recv - {}", result);
			conn.disconnect();
			br.close();
		}
		catch (ConnectException e) {
			log.error("调用HttpUtils.sendSSLPost ConnectException, url=" + url + ",param=" + param, e);
		}
		catch (SocketTimeoutException e) {
			log.error("调用HttpUtils.sendSSLPost SocketTimeoutException, url=" + url + ",param=" + param, e);
		}
		catch (IOException e) {
			log.error("调用HttpUtils.sendSSLPost IOException, url=" + url + ",param=" + param, e);
		}
		catch (Exception e) {
			log.error("调用HttpsUtil.sendSSLPost Exception, url=" + url + ",param=" + param, e);
		}
		return result.toString();
	}

	private static CloseableHttpClient wrapClient(String url) {
		CloseableHttpClient client = HttpClientBuilder.create().build();
		if (url.startsWith("https")) {
			client = getCloseableHttpsClients();
		}
		return client;
	}

	private static SSLContext createIgnoreVerifySSL() {
		// 创建套接字对象
		SSLContext sslContext = null;
		try {
			// 指定TLS版本
			sslContext = SSLContext.getInstance("TLSv1.2");
		}
		catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("[创建套接字失败:] " + e.getMessage());
		}
		// 实现X509TrustManager接口，用于绕过验证
		X509TrustManager trustManager = new X509TrustManager() {
			@Override
			public void checkClientTrusted(X509Certificate[] paramArrayOfX509Certificate, String paramString)
					throws CertificateException {
			}

			@Override
			public void checkServerTrusted(X509Certificate[] paramArrayOfX509Certificate, String paramString)
					throws CertificateException {
			}

			@Override
			public X509Certificate[] getAcceptedIssuers() {
				return null;
			}
		};
		try {
			// 初始化sslContext对象
			sslContext.init(null, new TrustManager[] { trustManager }, null);
		}
		catch (KeyManagementException e) {
			throw new RuntimeException("[初始化套接字失败:] " + e.getMessage());
		}
		return sslContext;
	}

	private static CloseableHttpClient getCloseableHttpsClients() {
		// 采用绕过验证的方式处理https请求
		SSLContext sslcontext = createIgnoreVerifySSL();
		// 设置协议http和https对应的处理socket链接工厂的对象
		Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
				.register("http", PlainConnectionSocketFactory.INSTANCE)
				.register("https", new SSLConnectionSocketFactory(sslcontext)).build();
		PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
		HttpClients.custom().setConnectionManager(connManager);
		// 创建自定义的httpsclient对象
		CloseableHttpClient client = HttpClients.custom().setConnectionManager(connManager).build();
		return client;
	}

	public static String doPostJson(String url, String jsonParam, Map<String, String> headers) {
		HttpPost httpPost = null;
		CloseableHttpResponse response = null;
		CloseableHttpClient httpClient = wrapClient(url);
		try {
			httpPost = new HttpPost(url);
			// addHeader，如果Header没有定义则添加，已定义则不变，setHeader会重新赋值
			httpPost.addHeader("Content-type", "application/json;charset=utf-8");// 响应
			httpPost.setHeader("Accept", "application/json");// 请求
			StringEntity entity = new StringEntity(jsonParam, StandardCharsets.UTF_8);
			httpPost.setEntity(entity);
			// 是否有header
			if (headers != null && headers.size() > 0) {
				for (Map.Entry<String, String> entry : headers.entrySet()) {
					httpPost.addHeader(entry.getKey(), entry.getValue());
				}
			}
			// 执行请求
			response = httpClient.execute(httpPost);
			// 判断返回状态是否为200
			if (response.getStatusLine().getStatusCode() == 200) {
				return EntityUtils.toString(response.getEntity(), "UTF-8");
			}

		}
		catch (Exception e) {
			throw new RuntimeException("[发送POST请求错误：]" + e.getMessage());
		}
		finally {
			try {
				httpPost.releaseConnection();
				response.close();
				if (httpClient != null) {
					httpClient.close();
				}
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public static String doPostFile(String url, MultipartFile file) {
		return doPostFile(url, file, null);
	}

	public static String doPostFile(String url, MultipartFile file, Integer size) {
		HttpPost httpPost = null;
		CloseableHttpResponse response = null;
		CloseableHttpClient httpClient = wrapClient(url);
		try {
			httpPost = new HttpPost(url);
			// addHeader，如果Header没有定义则添加，已定义则不变，setHeader会重新赋值
			httpPost.setHeader("Accept", "application/json;charset=utf-8");// 响应
			MultipartEntityBuilder builder = MultipartEntityBuilder.create();
			builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE); // 支持浏览器兼容的方式
			// if (size != null) {
			// BufferedImage resizedImage = createResizedImage(file.getInputStream(),
			// size, size);
			// BufferedImageMultipartFile file2 = new
			// BufferedImageMultipartFile(resizedImage,
			// file.getOriginalFilename(), file.getContentType());
			// builder.addBinaryBody("file", file2.getInputStream(),
			// ContentType.MULTIPART_FORM_DATA,
			// file.getOriginalFilename());
			// }
			// else {
			builder.addBinaryBody("file", file.getInputStream(), ContentType.MULTIPART_FORM_DATA,
					file.getOriginalFilename());
			// }

			httpPost.setEntity(builder.build());
			// 执行请求
			response = httpClient.execute(httpPost);
			// 判断返回状态是否为200
			if (response.getStatusLine().getStatusCode() == 200) {
				return EntityUtils.toString(response.getEntity(), "UTF-8");
			}

		}
		catch (Exception e) {
			throw new RuntimeException("[发送POST请求错误：]" + e.getMessage());
		}
		finally {
			try {
				httpPost.releaseConnection();
				response.close();
				if (httpClient != null) {
					httpClient.close();
				}
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	private static class TrustAnyTrustManager implements X509TrustManager {

		@Override
		public void checkClientTrusted(X509Certificate[] chain, String authType) {
		}

		@Override
		public void checkServerTrusted(X509Certificate[] chain, String authType) {
		}

		@Override
		public X509Certificate[] getAcceptedIssuers() {
			return new X509Certificate[] {};
		}

	}

	private static class TrustAnyHostnameVerifier implements HostnameVerifier {

		@Override
		public boolean verify(String hostname, SSLSession session) {
			return true;
		}

	}

}
