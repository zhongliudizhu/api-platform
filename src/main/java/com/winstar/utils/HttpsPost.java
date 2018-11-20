package com.winstar.utils;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author shoo on 2018/5/10 9:42.
 *         --
 */
public class HttpsPost {

    public  String testPost(String sendxml,String URL) {
        try {
            String u=URL;
//            System.setProperty("javax.net.ssl.trustStore", "src/main/resources/test2.jks");
//            System.setProperty("javax.net.ssl.trustStorePassword","123456");
//            System.setProperty("javax.net.ssl.keyStoreType","JKS");
//            System.setProperty("javax.net.ssl.keyStore","src/main/resources/test2.jks") ;
//            System.setProperty("javax.net.ssl.keyStorePassword","123456") ;
         /*   System.getProperties().put("proxySet", "true");
		 	System.getProperties().put("proxyHost", "10.1.27.102");
		 	System.getProperties().put("proxyPort", "8080");*/

            URL url = new URL(u);
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setDoOutput(true);
            http.setDoInput(true);
            http.setRequestMethod("POST");
            OutputStreamWriter out = new OutputStreamWriter(http.getOutputStream());
            String xmlInfo = sendxml;
//            System.out.println("xmlInfo=" + xmlInfo);
            out.write(xmlInfo);
            out.flush();
            out.close();
            BufferedReader br = new BufferedReader(new InputStreamReader(http
                    .getInputStream()));
            String line = "";
            for (line = br.readLine(); line != null; line = br.readLine()) {
                // System.out.println(line);
                return line;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  null;
    }
//    public  void main(String[] args) throws Exception{
//        InvoiceController invoiceController = new InvoiceController();
//        String xmlInfo = invoiceController.getXmlInfo();
//        String T1 =  testPost(xmlInfo,"http://120.27.226.192:8120/FPService/services/FPServiceAPI?wsdl");
//        System.out.println(T1);
//    }
public String TestHelloWrold (String sendxml,String URL) throws IOException {
    StringBuffer sb = new StringBuffer("");
    sb.append(sendxml);
    int timeout = 10000;

    // HttpClient发送SOAP请求
    System.out.println("HttpClient 发送SOAP请求");
    HttpClient client = new HttpClient();
    PostMethod postMethod = new PostMethod(URL);
    // 设置连接超时
    client.getHttpConnectionManager().getParams().setConnectionTimeout(timeout);
    // 设置读取时间超时
    client.getHttpConnectionManager().getParams().setSoTimeout(timeout);
    // 然后把Soap请求数据添加到PostMethod中

    RequestEntity requestEntity = new StringRequestEntity(sb.toString(), "text/xml", "UTF-8");
    //设置请求头部，否则可能会报 “no SOAPAction header” 的错误
    postMethod.setRequestHeader("SOAPAction","");
    // 设置请求体
    postMethod.setRequestEntity(requestEntity);
    int status = client.executeMethod(postMethod);
    // 打印请求状态码
    System.out.println("status:" + status);
    // 获取响应体输入流
    InputStream is = postMethod.getResponseBodyAsStream();
    // 获取请求结果字符串
    String result = org.apache.cxf.helpers.IOUtils.toString(is);
    System.out.println("result: " + result);



    // HttpURLConnection 发送SOAP请求
    System.out.println("HttpURLConnection 发送SOAP请求");
    URL url = new URL(URL);
    HttpURLConnection conn = (HttpURLConnection) url.openConnection();

    conn.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
    conn.setRequestMethod("POST");
    conn.setUseCaches(false);
    conn.setDoInput(true);
    conn.setDoOutput(true);
    conn.setConnectTimeout(timeout);
    conn.setReadTimeout(timeout);

    DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
    dos.write(sb.toString().getBytes("utf-8"));
    dos.flush();


    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
    String line = null;
    StringBuffer strBuf = new StringBuffer();
    while ((line = reader.readLine()) != null) {
        strBuf.append(line);
    }
    dos.close();
    reader.close();

    System.out.println(strBuf.toString());
    return  strBuf.toString();
}
}
