package com.lind.common.core.http;

import okhttp3.*;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

public class OkHttp3Util {

	private static OkHttpClient okHttpClient;

	static {
		okHttpClient = OkHttpConfiguration.okHttpClient();
	}

	/**
	 * get
	 * @param url 请求的url
	 * @param queries 请求的参数，在浏览器？后面的数据，没有可以传null
	 * @return
	 */
	public String get(String url, Map<String, String> queries, Map<String, String> header) throws IOException {
		String responseBody = "";
		StringBuffer sb = new StringBuffer(url);
		if (queries != null && queries.keySet().size() > 0) {
			boolean firstFlag = true;
			Iterator iterator = queries.entrySet().iterator();
			while (iterator.hasNext()) {
				Map.Entry entry = (Map.Entry<String, String>) iterator.next();
				if (firstFlag) {
					sb.append("?" + entry.getKey() + "=" + entry.getValue());
					firstFlag = false;
				}
				else {
					sb.append("&" + entry.getKey() + "=" + entry.getValue());
				}
			}
		}
		Request.Builder requestBuilder = new Request.Builder();
		if (header != null) {
			for (Map.Entry<String, String> entry : header.entrySet()) {
				requestBuilder.header(entry.getKey(), entry.getValue());
			}
		}
		Request request = requestBuilder.url(sb.toString()).build();
		Response response = null;
		try {
			response = okHttpClient.newCall(request).execute();
			int status = response.code();
			if (status == 200) {
				return response.body().string();
			}
		}
		catch (IOException e) {
			throw e;
		}
		finally {
			if (response != null) {
				response.close();
			}
		}
		return responseBody;
	}

	/**
	 * post
	 * @param url 请求的url
	 * @param params post form 提交的参数
	 * @return
	 */
	public String post(String url, Map<String, Object> params, Map<String, String> header) throws IOException {
		String responseBody = "";
		FormBody.Builder builder = new FormBody.Builder();
		// 添加参数
		if (params != null && params.keySet().size() > 0) {
			for (String key : params.keySet()) {
				Object o = params.get(key);
				if (o != null) {
					builder.add(key, params.get(key).toString());
				}
			}
		}
		Headers headers = Headers.of(header);
		Request request = new Request.Builder().headers(headers).url(url).post(builder.build()).build();
		Response response = null;
		try {
			response = okHttpClient.newCall(request).execute();
			int status = response.code();
			if (status == 200) {
				return response.body().string();
			}
		}
		catch (IOException e) {
			throw e;
		}
		finally {
			if (response != null) {
				response.close();
			}
		}
		return responseBody;
	}

	public String postJson(String url, String json, Map<String, String> header) throws IOException {
		String responseBody = "";
		RequestBody body = RequestBody.create(MediaType.parse("application/json"), json);
		Headers headers = Headers.of(header);
		Request request = new Request.Builder().headers(headers).url(url).post(body).build();
		Response response = null;
		try {
			response = okHttpClient.newCall(request).execute();
			int status = response.code();
			// if (status == 200) {
			// String res = new String(response.body().bytes());
			// return res;
			// }

			return new String(response.body().bytes());

		}
		catch (IOException e) {
			throw e;
		}
		finally {
			if (response != null) {
				response.close();
			}
		}
		// return responseBody;
	}

	/**
	 * post 上传文件
	 * @param url
	 * @param params
	 * @param fileType
	 * @return
	 */
	public String postFile(String url, Map<String, Object> params, String fileType) throws Exception {
		String responseBody = "";
		MultipartBody.Builder builder = new MultipartBody.Builder();
		// 添加参数
		if (params != null && params.keySet().size() > 0) {
			for (String key : params.keySet()) {
				if (params.get(key) instanceof File) {
					File file = (File) params.get(key);
					builder.addFormDataPart(key, file.getName(), RequestBody.create(MediaType.parse(fileType), file));
					continue;
				}
				builder.addFormDataPart(key, params.get(key).toString());
			}
		}
		Request request = new Request.Builder().url(url).post(builder.build()).build();
		Response response = null;
		try {
			response = okHttpClient.newCall(request).execute();
			int status = response.code();
			if (status == 200) {
				return response.body().string();
			}
		}
		catch (Exception e) {
			throw e;
		}
		finally {
			if (response != null) {
				response.close();
			}
		}
		return responseBody;
	}

	/**
	 * @param url 请求的url
	 * @param queries 请求的参数，在浏览器？后面的数据，没有可以传null
	 * @param header header中的数据，没有可以传null
	 * @return
	 * @description 调用get请求(加强版)
	 */
	public String getEnhancedVersion(String url, Map<String, String> queries, Map<String, String> header)
			throws IOException {

		Headers headers = null;
		Headers.Builder headersbuilder = new Headers.Builder();
		if (header != null) {
			for (Map.Entry<String, String> entry : header.entrySet()) {
				headersbuilder.add(entry.getKey(), entry.getValue());
			}
		}
		headers = headersbuilder.build();

		Request request = new Request.Builder().url(url).headers(headers).build();
		return execute(request);
	}

	/**
	 * 发起请求获取结果
	 * @param request
	 * @return
	 */
	private String execute(Request request) throws IOException {
		try (Response response = okHttpClient.newCall(request).execute()) {
			if (response.isSuccessful()) {
				return response.body().string();
			}
		}
		catch (Exception e) {
			throw e;
		}
		return "";
	}

	/**
	 * Post请求发送JSON数据....{"name":"zhangsan","pwd":"123456"} 参数一：请求Url 参数二：请求的JSON
	 * 参数三：请求头参数
	 */
	public String jsonPostWithHeader(String url, String jsonParams, Map<String, String> headers) throws IOException {
		RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonParams);

		Request.Builder requestBuilder = new Request.Builder();
		if (headers != null) {
			for (Map.Entry<String, String> entry : headers.entrySet()) {
				requestBuilder.header(entry.getKey(), entry.getValue());
			}
		}
		Request request = requestBuilder.url(url).post(requestBody).build();
		return execute(request);
	}

	/**
	 * @return java.lang.String
	 * @Author Guoqiang.Bai
	 * @Description 发起delete方式的请求
	 * @Date 18:50 2022/11/16
	 **/
	public String deleteReq(String url, String jsonParams, Map<String, String> headers) throws IOException {
		RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonParams);

		Request.Builder requestBuilder = new Request.Builder();
		if (headers != null) {
			for (Map.Entry<String, String> entry : headers.entrySet()) {
				requestBuilder.header(entry.getKey(), entry.getValue());
			}
		}
		Request request = requestBuilder.url(url).delete(requestBody).build();
		return execute(request);
	}

}
