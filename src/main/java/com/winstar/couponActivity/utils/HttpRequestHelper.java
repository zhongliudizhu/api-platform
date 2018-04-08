package com.winstar.couponActivity.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;

public class HttpRequestHelper {

    private final static Logger logger = LoggerFactory.getLogger(HttpRequestHelper.class);




    public String send() {
        return null;
    }

    public static String sendRequestPost(String urlString, String data) throws Exception {
        String result = "";
        OutputStream os = null;
        InputStream is = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        HttpURLConnection conn = null;
        try {
            logger.info("开始请求中心接口：" + urlString);
            URL url = new URL(urlString);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setConnectTimeout(30000);
            conn.setReadTimeout(30000);
            conn.setInstanceFollowRedirects(true);
            conn.setRequestProperty("content-type", "application/json");
             String  userName="test";
            String  passWord="test";
            //Basic认证
            if (true) {
                byte[] token = (userName + ":" + passWord).getBytes("utf-8");
                String authorization = "Basic " + new String(Base64.getEncoder().encode(token), "utf-8");
                conn.setRequestProperty("Authorization", authorization);
            }

            conn.connect();// 握手
            os = conn.getOutputStream();// 拿到输出流
            os.write(data.getBytes("utf-8"));
            os.flush();
            logger.info("发送数据");
            is = conn.getInputStream();// 拿到输入流
            logger.info("发送完毕");
            if (conn.getResponseCode() == 200) {
                logger.info("返回成功");
                isr = new InputStreamReader(is);
                br = new BufferedReader(isr);
                String s = br.readLine();
                result = new String(s.getBytes(), "UTF-8");
                logger.info("读取完成:" + result);
            }
        } catch (Exception e) {
            logger.error("调用接口失败：", e);
            throw e;
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
                if (br != null) {
                    br.close();
                }
                if (isr != null) {
                    isr.close();
                }
                if (is != null) {
                    is.close();
                }
                conn.disconnect();
            } catch (Exception e) {
                logger.error("关闭链接失败：", e);
            }
        }
        return result;
    }

    public static String sendRequestGet(String urlString) throws Exception {
        String result = "";
        OutputStream os = null;
        InputStream is = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        HttpURLConnection conn = null;
        try {
            logger.info("开始请求中心接口：" + urlString);
            URL url = new URL(urlString);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setConnectTimeout(30000);
            conn.setReadTimeout(30000);
            conn.setInstanceFollowRedirects(true);
            conn.setRequestProperty("content-type", "application/json");

            conn.connect();// 握手
            is = conn.getInputStream();// 拿到输入流
            logger.info("发送完毕");
            if (conn.getResponseCode() == 200) {
                logger.info("返回成功");
                isr = new InputStreamReader(is);
                br = new BufferedReader(isr);
                String s = br.readLine();
                result = new String(s.getBytes(), "UTF-8");
                logger.info("读取完成:" + result);
            }
        } catch (Exception e) {
            logger.error("调用接口失败：", e);
            throw e;
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
                if (br != null) {
                    br.close();
                }
                if (isr != null) {
                    isr.close();
                }
                if (is != null) {
                    is.close();
                }
                conn.disconnect();
            } catch (Exception e) {
                logger.error("关闭链接失败：", e);
            }
        }
        return result;
    }
}
