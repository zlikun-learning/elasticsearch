package com.zlikun.learning.sdk;

import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.DocWriteRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

/**
 * https://www.elastic.co/guide/en/elasticsearch/client/java-api/current/java-docs-index.html
 */
@Slf4j
public class IndexTest {

    TransportClient client;

    @Before
    public void init() throws UnknownHostException {
        // https://www.elastic.co/guide/en/elasticsearch/client/java-api/current/transport-client.html
        client = new PreBuiltTransportClient(Settings.EMPTY)
                // 允许添加多个，以构成集群
                .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("es.zlikun.com"), 9300));
    }

    @Test
    public void api() throws IOException {

        XContentBuilder builder = jsonBuilder()
                .startObject()
                .field("user", "zlikun")
                .field("postDate", new Date())
                .field("message", "trying out Elasticsearch")
                .endObject();

        String json = builder.string();
        log.info("build message is {}", json);

        IndexResponse response = client.prepareIndex("twitter", "tweet")
                .setOpType(DocWriteRequest.OpType.CREATE)
                .setSource(json, XContentType.JSON)
                .get();

        log.info("_index = {} ,_type = {} ,_id = {} ,_version = {}"
                , response.getIndex()
                , response.getType()
                , response.getId()
                , response.getVersion());

        // status has stored current instance statement.
        RestStatus status = response.status();
        log.info("_status = {}", status.getStatus());
    }

    @After
    public void destroy() {
        client.close();
    }

}
