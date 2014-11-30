package pl.edu.wszib.msmolen.mt.client.utils;

import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class SSLUtils {

	private static boolean mZezwalajNaSsl = false;
	
	public static void zezwalajNaPolaczeniaSsl() {
		if (mZezwalajNaSsl)
			return;
		
		TrustManager[] manager = new TrustManager[]{
				new X509TrustManager() {
					
					@Override
					public X509Certificate[] getAcceptedIssuers() {
						return null;
					}
					
					@Override
					public void checkServerTrusted(X509Certificate[] certs, String authType)	throws CertificateException {
					}
					
					@Override
					public void checkClientTrusted(X509Certificate[] certs, String authType) throws CertificateException {
					}
				}
		};
		
		try {
			SSLContext context = SSLContext.getInstance("SSL");
			context.init(null, manager, new SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory());
		} catch (Exception e) {
		}
		
		HostnameVerifier verifier = new HostnameVerifier() {
			
			@Override
			public boolean verify(String arg0, SSLSession arg1) {
				return true;
			}
		};
		
		HttpsURLConnection.setDefaultHostnameVerifier(verifier);
		mZezwalajNaSsl = true;
	}
	
}
