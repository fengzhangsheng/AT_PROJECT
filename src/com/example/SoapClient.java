// SoapClient.java
package com.example;

import javax.net.ssl.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.cert.X509Certificate;

public class SoapClient {
    private static void disableSSLVerification() {
        try {
            TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        public X509Certificate[] getAcceptedIssuers() { return null; }
                        public void checkClientTrusted(X509Certificate[] certs, String authType) {}
                        public void checkServerTrusted(X509Certificate[] certs, String authType) {}
                    }
            };

            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

            HostnameVerifier allHostsValid = (hostname, session) -> true;
            HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
        } catch (Exception e) {
            SimpleLogger.error("SSL disabling failed: " + e.getMessage());
        }
    }

    public static String sendSoap(String soapUrl, String soapAction, String soapRequestBody) {
        if (soapUrl == null || soapUrl.isEmpty()) {
            SimpleLogger.error("Invalid URL provided");
            return "ERROR: Empty URL";
        }

        try {
            if (soapUrl.startsWith("https")) {
                disableSSLVerification();
            }

            URL url = new URL(soapUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
            connection.setRequestProperty("SOAPAction", soapAction);
            connection.setConnectTimeout(30000);
            connection.setReadTimeout(60000);
            connection.setDoOutput(true);

            try (OutputStream os = connection.getOutputStream()) {
                os.write(soapRequestBody.getBytes(StandardCharsets.UTF_8));
            }

            int httpStatus = connection.getResponseCode();
            SimpleLogger.info("HTTP Status: " + httpStatus + " for URL: " + soapUrl);

            StringBuilder response = new StringBuilder();
            try (InputStream is = httpStatus >= 400 ? connection.getErrorStream() : connection.getInputStream();
                 BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {

                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
            }

            return response.toString();
        } catch (Exception e) {
            SimpleLogger.error("SOAP request failed: " + e.getMessage());
            return "ERROR: " + e.getMessage();
        }
    }
}