package com.zlikun.learning.http;

import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * @author zlikun <zlikun-dev@hotmail.com>
 * @date 2017/9/15 11:44
 */
@Slf4j
public class PutTest {

    private String elastic_uri_prefix = "http://docker.zlikun.com:9200/" ;

    private OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(3 , TimeUnit.SECONDS)
            .build() ;

    /**
     * 创建一个用户(文档)，在DevTools使用`GET /user/hero/1`请求查看创建的文档信息
     */
    @Test
    public void put_user() throws InterruptedException {

        // 构建请求
        Request request = new Request.Builder()
                // user 表示索引
                // employee 表示类型
                // 1 表示ID
                .url(elastic_uri_prefix + "user/hero/1?pretty")
                .put(RequestBody.create(MediaType.parse("application/json")
                        ,"{\"name\":\"小乔\",\"age\":21,\"gender\":\"FEMALE\",\"about\":\"小乔要努力变强\"}"))
                .build() ;

        // 执行请求，好吧，是个馊主意，这TM是个异步请求
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                log.error("哟 ,出错了!" ,e);
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                log.info("{}" ,response.body().string());
                response.close();
            }
        });

        Thread.currentThread().join(1000);

    }

}
