//package com.vetri.poc.filechunk;
//
//import org.apache.commons.io.IOUtils;
//import org.assertj.core.api.Assertions;
//import org.junit.Assert;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpMethod;
//import org.springframework.http.client.SimpleClientHttpRequestFactory;
//import org.springframework.util.StreamUtils;
//import org.springframework.web.client.RequestCallback;
//import org.springframework.web.client.ResponseExtractor;
//import org.springframework.web.client.RestTemplate;
//
//import java.io.*;
//import java.nio.file.Files;
//
//public class Test1 {
//
//    @Test
//    @DisplayName("Test upload file via chunk uploader")
//    public void t1()throws Exception{
//        System.out.println("Vetri");
//
//        // String to bytes
//        // call rest api with octet-stream
//
//        RestTemplate restTemplate = new RestTemplate();
//        String data = "Vetri";
//        byte [] chunk = data.getBytes("UTF-8");
//        String api = "http://localhost:8080/api/ngx-upload/f778246f27478596fea72c3d820f4a4f1c164e91";
//        int range = 10;
//        int rangeStart = 0;
//        int rangeEnd = chunk.length;
//        int size = chunk.length;
//
////        HttpHeaders headers = restTemplate.headForHeaders(api);
////        long contentLength = headers.getContentLength();
//        long contentLength = chunk.length;
//        File file = restTemplate.execute(api, HttpMethod.PUT
//                ,clientHttpRequest -> clientHttpRequest
//                        .getHeaders()
//                        //.set("Range", String.format("bytes=0-%d", range - 1))
//                        .set("Content-Range", String.format("bytes %d-%d/%d",rangeStart,rangeEnd,size))
//                , clientHttpResponse -> {
//            File ret = File.createTempFile("download", "tmp");
//            StreamUtils.copy(clientHttpResponse.getBody(), new FileOutputStream(ret));
//            return ret;
//        });
//
//        Assert.assertNotNull(file);
//        Assertions
//                .assertThat(file.length())
//                .isEqualTo(contentLength);
//    }
//}
