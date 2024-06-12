package com.lind.common.core.http;

import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;

import javax.net.ssl.*;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

/**
 * @author lind
 * @date 2024/6/12 9:34
 * @since 1.0.0
 */
public class OkHttpConfiguration {

	public OkHttpConfiguration() {
	}

	private static X509TrustManager x509TrustManager() {
		return new X509TrustManager() {
			public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
			}

			public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
			}

			public X509Certificate[] getAcceptedIssuers() {
				return new X509Certificate[0];
			}
		};
	}

	private static SSLSocketFactory sslSocketFactory() {
		try {
			SSLContext sslContext = SSLContext.getInstance("TLS");
			sslContext.init((KeyManager[]) null, new TrustManager[] { x509TrustManager() }, new SecureRandom());
			return sslContext.getSocketFactory();
		}
		catch (NoSuchAlgorithmException var1) {
			NoSuchAlgorithmException e = var1;
			e.printStackTrace();
		}
		catch (KeyManagementException var2) {
			KeyManagementException e = var2;
			e.printStackTrace();
		}

		return null;
	}

	private static ConnectionPool pool() {
		return new ConnectionPool(200, 5L, TimeUnit.MINUTES);
	}

	public static OkHttpClient okHttpClient() {
		return (new OkHttpClient.Builder()).sslSocketFactory(sslSocketFactory(), x509TrustManager())
				.retryOnConnectionFailure(false).connectionPool(pool()).connectTimeout(30L, TimeUnit.SECONDS)
				.readTimeout(30L, TimeUnit.SECONDS).build();
	}

}
