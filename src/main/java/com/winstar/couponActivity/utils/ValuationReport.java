package com.winstar.couponActivity.utils;

import com.alibaba.fastjson.JSON;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.winstar.couponActivity.vo.CarCondition;
import com.winstar.couponActivity.vo.recentDeal;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author shoo on 2018/4/2 18:00.
 *         --
 */
public class ValuationReport {



    //获取汽车未来价格趋势（按月份）
    public static String getCarHistoricalPrice(String modelId,String zone,String regDate,String mile,RestTemplate restTemplate) throws Exception {

        String getCarHistoricalPrice = "";
        String url="https://api.che300.com/service/common/eval?oper=getMonthResidualTrend&modelId="+modelId+"&zone="+zone+"&regDate="+regDate+"&mile="+mile+"&token=27e73d6efa5df6544ab4e3fe714e957a";
        Map<String ,Object> urlVariables = new HashMap<String ,Object>();
        ResponseEntity<String> StringBody = RequestServerUtil.getRequest(restTemplate,url,urlVariables);
        if(StringBody.getStatusCode().value()==200){
            getCarHistoricalPrice = StringBody.getBody().toString();
        }
        return   getCarHistoricalPrice;
    }
    //获取汽车未来价格趋势预测（按年）
    public static String getCarFuturePrice(String modelId,String zone,String regDate,String mile,RestTemplate restTemplate){

        //示例
        //https://api.che300.com/service/common/eval?oper=getUsedCarResidualAnalysis&modelId=1&zone=11&regDate=2016-07&mile=0.5&token=
        String getCarFuturePrice = "";
        String url="https://api.che300.com/service/common/eval?oper=getUsedCarResidualAnalysis&modelId="+modelId+"&zone="+zone+"&regDate="+regDate+"&mile="+mile+"&token=&token=27e73d6efa5df6544ab4e3fe714e957a";
        Map<String ,Object> urlVariables = new HashMap<String ,Object>();
        ResponseEntity<String> StringBody = RequestServerUtil.getRequest(restTemplate,url,urlVariables);
        if(StringBody.getStatusCode().value()==200){
            getCarFuturePrice = StringBody.getBody().toString();
        }
        return   getCarFuturePrice;
    }

    //获取汽车详细配置
    public static String getModelParameters(String modelId,RestTemplate restTemplate){

        //示例
        //https://api.che300.com/service/common/eval?oper=getUsedCarResidualAnalysis&modelId=1&zone=11&regDate=2016-07&mile=0.5&token=
        String getCarFuturePrice = "";
        String url="http://api.che300.com/service/getModelParameters?modelId="+modelId+"&token=&token=27e73d6efa5df6544ab4e3fe714e957a";
        Map<String ,Object> urlVariables = new HashMap<String ,Object>();
        ResponseEntity<String> StringBody = RequestServerUtil.getRequest(restTemplate,url,urlVariables);
        if(StringBody.getStatusCode().value()==200){
            getCarFuturePrice = StringBody.getBody().toString();
        }
        return   getCarFuturePrice;
    }
    //获取汽车未来价格趋势预测（按年）
    public static String getUsedCarPrice(String modelId,String zone,String regDate,String mile,RestTemplate restTemplate){

        //示例
        //https://api.che300.com/service/common/eval?oper=getUsedCarResidualAnalysis&modelId=1&zone=11&regDate=2016-07&mile=0.5&token=
        String getUsedCarPrice = "";
        String url="http://testapi.che300.com/service/getUsedCarPrice?&modelId="+modelId+"&zone="+zone+"&regDate="+regDate+"&mile="+mile+"&token=&token=2d59efa689a9c6a7d99913a28a633410";
        Map<String ,Object> urlVariables = new HashMap<String ,Object>();
        ResponseEntity<String> StringBody = RequestServerUtil.getRequest(restTemplate,url,urlVariables);
        if(StringBody.getStatusCode().value()==200){
            getUsedCarPrice = StringBody.getBody().toString();
        }
        return   getUsedCarPrice;
    }

    //获取汽车所有信息
    public  String getCarNews(String modelId,String zone,String regDate,String mile) throws Exception {

        //车型id
        //String modelId="1";

       /* http://api.che300.com/service/getUsedCarPrice?
       token=27e73d6efa5df6544ab4e3fe714e957a&
       modelId=1
       &regDate=2015-03        上牌日期
       &mile=23                英里
       &zone=42                ok
       */

        //示例
        //http://api.che300.com/service/getUsedCarPrice?token=27e73d6efa5df6544ab4e3fe714e957a&modelId=1&regDate=2015-03&mile=23&zone=42
        StringBuilder json = new StringBuilder();
        String url="http://api.che300.com/service/getUsedCarPrice?token=27e73d6efa5df6544ab4e3fe714e957a&modelId="+modelId+"&regDate="+regDate+"&mile="+mile+"&zone="+zone;

        //获取汽车成交记录 json  数据
        String CarDealRecord=null;
        //获取汽车历史价格趋势 json  数据
        String CarHistoricalPrice=null;
        //获取汽车未来价格趋势预测  json  数据
        String CarFuturePrice=null;

        try {
            JSONObject jo = new JSONObject(json.toString());
            String URL =(String)jo.get("url");
            ValuationReport c= new ValuationReport();
            //获取汽车成交记录 json  数据
            CarDealRecord =c.getCarDealRecord(URL);
            //获取汽车历史价格趋势 json  数据
//            CarHistoricalPrice= c.getCarHistoricalPrice(modelId,zone,regDate,mile);
            //获取汽车未来价格趋势预测  json  数据
//            CarFuturePrice=c.getCarFuturePrice(modelId,zone,regDate,mile,restTemplate);

        }catch (JSONException e) {
            e.printStackTrace();
        }
        //返回json
        return  requestURLUtil(url)+CarDealRecord+CarHistoricalPrice+CarFuturePrice;
    }



    //获取车况说明
    public String getCarCondition(String modelId,String Grade){

        //外观状况
        //surface
        //车辆内饰
        //interior
        //工况状况
        //work_state


//        [外观状况]：
//        分为优，良，中，差四个条件
//        [优]: 原厂漆，漆面轻微瑕疵；车窗玻璃光洁。
//        [良]: 几乎无色差，喷漆不超过2个面；车窗玻璃完好。
//        [中]: 喷漆不超过4个面，钣金不超过2处，略有色差；车窗玻璃有轻微裂痕。
//        [差]: 全车多处喷漆或钣金，色差较为明显，或者车身多处凹陷、裂痕或锈迹。
//
//        [车辆内饰]：
//        分为优，良，中，差四个条件
//        [优]: 方向盘及按键无磨损；座椅及内饰崭新；车内无异味。
//        [良]: 方向盘及按键轻微磨损；座椅及内饰轻微磨损；车内轻微异味。
//        [中]: 方向盘及按键有明显磨损；座椅及内饰有几处较为明显的破损、污渍或霉斑；车内有异味。
//        [差]: 方向盘及按键有破损或者缺失；座椅及内饰有多处破损、污渍或者霉斑；车内有明显异味。
//
//        [工况状况]：
//        分为优，良，中，差四个条件
//        [优]: 发动机及变速箱运行良好且无维修；底盘及电气系统运行良好；按时保养且记录完整。
//        [良]: 发动机运行正常且无维修；变速箱、底盘及电气系统运行正常；保养记录较为完整。
//        [中]: 发动机工况一般，或有启动困难、轻微渗油、异常抖动等；变速箱及底盘无明显故障或异响；电气系统偶发故障但不影响安全行驶。
//        [差]: 发动机即将大修或者已经大修；变速箱及底盘某些部件老化需更换；电气系统易发故障且某些故障较难修复。
        CarCondition c =new CarCondition();
        c.setcId(modelId);
        //外观状况  车辆内饰   工况状况  判断
        if(Grade=="优"){
            //  "优":
            c.setSurface("原厂漆，漆面轻微瑕疵；车窗玻璃光洁。");
            c.setInterior("方向盘及按键无磨损；座椅及内饰崭新；车内无异味。");
            c.setWork_state("发动机及变速箱运行良好且无维修；底盘及电气系统运行良好；按时保养且记录完整。");
        } else if(Grade=="良") {
            //"良":
            c.setSurface("几乎无色差，喷漆不超过2个面；车窗玻璃完好。");
            c.setInterior("方向盘及按键轻微磨损；座椅及内饰轻微磨损；车内轻微异味。");
            c.setWork_state("发动机运行正常且无维修；变速箱、底盘及电气系统运行正常；保养记录较为完整。");
        }else if(Grade=="中") {
            //  "中":
            c.setSurface("喷漆不超过4个面，钣金不超过2处，略有色差；车窗玻璃有轻微裂痕。");
            c.setInterior("方向盘及按键有明显磨损；座椅及内饰有几处较为明显的破损、污渍或霉斑；车内有异味。");
            c.setWork_state("发动机工况一般，或有启动困难、轻微渗油、异常抖动等；变速箱及底盘无明显故障或异响；电气系统偶发故障但不影响安全行驶。");
        }  else if(Grade=="差") {
            // "差":
            c.setSurface("全车多处喷漆或钣金，色差较为明显，或者车身多处凹陷、裂痕或锈迹。");
            c.setInterior("方向盘及按键有破损或者缺失；座椅及内饰有多处破损、污渍或者霉斑；车内有明显异味。");
            c.setWork_state("发动机即将大修或者已经大修；变速箱及底盘某些部件老化需更换；电气系统易发故障且某些故障较难修复。");
        }
        return  JSON.toJSONString(c);
    }

    // 根据URL 获取汽车成交记录
    public  String getCarDealRecord(String URL)throws IOException{
        // 测试网站
        // String baseUrlPath ="https://www.che300.com/pinggu/v12c42m1r2015-03g23?from=youjiaxing&_s=987cd3a9991c2f8e&rt=1522284855783";
        //获取网页HTML
        WebClient webClient = new WebClient(BrowserVersion.CHROME);
        webClient.getOptions().setJavaScriptEnabled(true);
        webClient.getOptions().setCssEnabled(false);
        webClient.getOptions().setRedirectEnabled(true);
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        webClient.getOptions().setTimeout(5000);
        //HtmlPage rootPage =webClient.getPage(baseUrlPath);
        HtmlPage rootPage =webClient.getPage(URL);
        webClient.waitForBackgroundJavaScript(1000);
        String info = rootPage.asXml();

        //去掉html的空格与回车
        info = info.replaceAll("\r|\n| ", "").trim();

        //将html 的\与y除去
        info=info.replaceAll("\"","").trim();
        info=info.replaceAll("y","<");

        //匹配规则
        //匹配 成交车型
        Pattern p3 = Pattern.compile("(?<=<le=width:30%title=)(.+?)(?=>)");
        //匹配 新车指导,上牌时间，公里，城市，类别
        Pattern p4 = Pattern.compile("(?<=<le=width:10%>)(.+?)(?=</span>)");
        //匹配 成交日期
        Pattern p5 = Pattern.compile("(?<=<le=width:10%;color:#f60;font-size:20px>)(.+?)(?=</span>)");

        Matcher buf_m3=p3.matcher(info);
        Matcher buf_m4=p4.matcher(info);
        Matcher buf_m5=p5.matcher(info);
        //用于存放页面信息
        List<String> list =new ArrayList<>();

        while (buf_m3.find()) {
            list.add(buf_m3.group());
            //System.out.println(buf_m3.group());
        }

        while (buf_m4.find()) {
            list.add(buf_m4.group());
            //System.out.println(buf_m4.group());
        }

        while (buf_m5.find()) {
            list.add(buf_m5.group());
            //System.out.println(buf_m5.group());
        }

        //存放 近期成交记录 recentDeal 对象
        List<recentDeal> arr =new ArrayList<>();
        for(int i=0;i<=3;i++){
            recentDeal r =new recentDeal();
            r.setrId(i);
            arr.add(r);
        }

        for (int i=0;i<=arr.size();i++){
            if (i==0){
                arr.get(i).setModels(list.get(i));
                arr.get(i).setNew_car_guidance_price(list.get(11));
                arr.get(i).setTransaction_price(list.get(35));
                arr.get(i).setOn_the_card_time(list.get(12));
                arr.get(i).setKilometre(list.get(13));
                arr.get(i).setTransaction_time(list.get(14));
                arr.get(i).setCity(list.get(15));
                arr.get(i).setCategory(list.get(16));
            }else if (i==1){
                arr.get(i).setModels(list.get(i));
                arr.get(i).setNew_car_guidance_price(list.get(17));
                arr.get(i).setTransaction_price(list.get(36));
                arr.get(i).setOn_the_card_time(list.get(18));
                arr.get(i).setKilometre(list.get(19));
                arr.get(i).setTransaction_time(list.get(20));
                arr.get(i).setCity(list.get(21));
                arr.get(i).setCategory(list.get(22));
            }else if (i==2){
                arr.get(i).setModels(list.get(i));
                arr.get(i).setNew_car_guidance_price(list.get(23));
                arr.get(i).setTransaction_price(list.get(37));
                arr.get(i).setOn_the_card_time(list.get(24));
                arr.get(i).setKilometre(list.get(25));
                arr.get(i).setTransaction_time(list.get(26));
                arr.get(i).setCity(list.get(27));
                arr.get(i).setCategory(list.get(28));
            }else if (i==3){
                arr.get(i).setModels(list.get(i));
                arr.get(i).setNew_car_guidance_price(list.get(29));
                arr.get(i).setTransaction_price(list.get(38));
                arr.get(i).setOn_the_card_time(list.get(30));
                arr.get(i).setKilometre(list.get(31));
                arr.get(i).setTransaction_time(list.get(32));
                arr.get(i).setCity(list.get(33));
                arr.get(i).setCategory(list.get(34));
            }
        }
        return  JSON.toJSONString(arr);
    }


    private String requestURLUtil(String requestUrl){
        URL url = null;
        URLConnection urlConnection = null;
        StringBuilder json = new StringBuilder();
        try {
            url = new URL(requestUrl);
            urlConnection = url.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    urlConnection.getInputStream(),"utf-8"));//防止乱码
            String inputLine = null;
            while ((inputLine = in.readLine()) != null) {
                json.append(inputLine);
            }
            in.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {

        }
        return  json.toString();
    }
}
