package com.zlikun.learning.sdk;

import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * https://www.elastic.co/guide/en/elasticsearch/client/java-api/current/java-docs-get.html
 */
@Slf4j
public class GetTest {

    TransportClient client;

    @Before
    public void init() throws UnknownHostException {
        client = new PreBuiltTransportClient(Settings.builder()
                .put("cluster.name" ,"elasticsearch")
                .build())
                .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("es.zlikun.com"), 9300));
    }

    @Test
    public void api() throws IOException {

        GetResponse response = client.prepareGet("twitter", "tweet", "1")
                .setOperationThreaded(false)
                .get();

        log.info("_index = {} ,_type = {} ,_id = {} ,_version = {}"
                , response.getIndex()
                , response.getType()
                , response.getId()
                , response.getVersion());

        log.info("_source = {}", response.getSourceAsString());

    }

    @After
    public void destroy() {
        client.close();
    }

}
