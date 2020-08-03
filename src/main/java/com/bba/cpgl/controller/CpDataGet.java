package com.bba.cpgl.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * @progrem: com.bba.cpgl.controller
 * @author: ZhouSuwen'sdadi
 * @date: 2020-07-15 17:47
 **/
public class CpDataGet {
    public static void main(String[] args) {
        Map<String, String> map = new HashMap<String, String>();
        String con;
        try {
            for(int j=5;j<6;j++){
                String url = "http://datachart.500.com/dlt/history/history.shtml";
                List<String> links = getAllLinks(url);
                for(int i=1;i<links.size();i++){
                    if(map.get(links.get(i))==null){
                        map.put(links.get(i), "true");
                        con = GetWebContent(links.get(i),"utf-8",10000);
                        Document doc = Jsoup.parse(con);
                        String title = getTitle(doc);
                        String content = getContent(doc);
                        System.out.println(title);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    /**
     *网页抓取方法
     * @param urlString      要抓取的url地址
     * @param charset        网页编码方式
     * @param timeout        超时时间
     * @return                抓取的网页内容
     * @throws IOException   抓取异常
     */
    public static String GetWebContent(String urlString, final String charset, int timeout) throws IOException {
        if (urlString == null || urlString.length() == 0) {
            return null;
        }
        urlString = (urlString.startsWith("http://") || urlString.startsWith("https://")) ? urlString : ("http://" + urlString).intern();
        URL url = new URL(urlString);


        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestProperty(
                "User-Agent",
                "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 5.2; Trident/4.0; .NET CLR 1.1.4322; .NET CLR 2.0.50727)");//增加报头，模拟浏览器，防止屏蔽
        conn.setRequestProperty("Accept", "text/html");//只接受text/html类型，当然也可以接受图片,pdf,*/*任意，就是tomcat/conf/web里面定义那些

        conn.setConnectTimeout(timeout);
        try {
            if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        InputStream input = conn.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(input,
                charset));
        String line = null;
        StringBuffer sb = new StringBuffer();
        while ((line = reader.readLine()) != null) {
            sb.append(line).append("\r\n");
        }
        if (reader != null) {
            reader.close();
        }
        if (conn != null) {
            conn.disconnect();
        }
        return sb.toString();
    }

    public static String getContent(Document doc){
        String content = "";
        try {
            String link = ".article_content";
            content = doc.select(link).html();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return content;
    }

    public static String getTitle(Document doc){
        String content = "";
        try {
            String link = "h3";
            content=  doc.select(link).text();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return content;
    }
    public static List<String> getAllLinks(String url){
        List<String> links = new ArrayList<String>();
        try {
            String con = GetWebContent(url,"utf-8",15000);
            Document doc = Jsoup.parse(con);
            String link = "#tdata .cfont2";
            String href = "abs:href";
            Elements ele = doc.select(link);
            for (Element el : ele) {
                String path = el.select("td").val();
                links.add(path);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return links;
    }
}
