package com.jt.httpClient;

import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class TestHttpClient {

    @Test
    public void testGet() throws IOException {
        //1.实例化对象
        HttpClient httpClient = HttpClients.createDefault();
        //2.定义请求网址
        String url = "http://www.baidu.com";  //html代码
        //3.定义请求方式
        HttpGet get = new HttpGet(url);
        //4.发送请求
        HttpResponse httpResponse = httpClient.execute(get);
        //5.判断状态码
        int status = httpResponse.getStatusLine().getStatusCode();
        if(status == 200) {
            //6.获取数据
            HttpEntity httpEntity = httpResponse.getEntity();
            String html = EntityUtils.toString(httpEntity, "UTF-8");
            System.out.println(html);
            //7.对业务数据进行封装

        }
    }






}
