package com.vetri.poc.filechunk;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.*;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.UUID;

public class Test2 {

    String token = UUID.randomUUID().toString().replace("-", "");
    String hash = UUID.randomUUID().toString().replace("-", "");
    String uploadId = createSalt();
    String serverUrl = "http://localhost:8080/api/uploadFile";
    //    String serverUrl = "http://httpbin.org/post";
//    File file = new File("D:\\tmp\\Sample-jpg-image-2mb.jpg");
    File file = new File("D:\\tmp\\small.mp4");

    @Test
    @DisplayName("Test upload file via chunk uploader")
    public void testUploadFile() throws Exception {
        String chunkUrl = callMetaDataApi();
//        //chunkUrl = "http://httpbin.org/put";
//        callChunkApi(chunkUrl);

        // 1048576 bit = 128 kB.
        int chunkSize = 1048576; // 1048576 Bytes = 1 MB (in binary)
        chunkSize = 1048576/4 ;
        FileChannel fileChannel = FileChannel.open(file.toPath(), StandardOpenOption.READ, StandardOpenOption.WRITE);
        ByteBuffer byteBuffer = ByteBuffer.allocate(chunkSize);
        int n;
        long sum = 0L;
        int i=0;
        while ((n = fileChannel.read(byteBuffer)) != -1) {
            sum += n;
            byteBuffer.flip();
            byte[] chunk = new byte[n];
            byteBuffer.get(chunk);
            callChunkApi(chunkUrl,i++,chunkSize,file.length(),chunk);
            System.out.println("read " + n  + " bytes");
            byteBuffer.clear();
        }
        System.out.println("read " + sum + " bytes total");
    }

    private String createSalt() {
        String ts = String.valueOf(System.currentTimeMillis());
        String rand = UUID.randomUUID().toString();
        return DigestUtils.sha1Hex(ts + rand);
    }

    private String callMetaDataApi() throws Exception {
        // Call File Metadata API
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token);
        headers.add("Content-Type", "application/json");

        Long size = file.length();
        String name = file.getName();
        String mimeType = Files.probeContentType(file.toPath());
        String json = "{\n" +
                "  \"hash\": \"" + hash + "\",\n" +
                "  \"mimeType\": \"" + mimeType + "\",\n" +
                "  \"name\": \"" + name + "\",\n" +
                "  \"size\": " + size + ",\n" +
                "  \"uploadId\": \"" + uploadId + "\"\n" +
                "}";
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(json);
        HttpEntity<JsonNode> requestEntity = new HttpEntity<>(jsonNode, headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate
                .postForEntity(serverUrl, requestEntity, String.class);

        String chunkUrl = "http://localhost:8080/api/" + response.getHeaders().getFirst("Location").toString();

        System.out.println("metadata.chunkUrl > " + chunkUrl);
        System.out.println("metadata.headers > " + response.getHeaders());
        System.out.println("metadata.response > " + response.getBody());

        return chunkUrl;
    }

    private void callChunkApi(String chunkUrl, int chunkNo, int chunkSize, Long fileSize, byte [] chunk) throws Exception {

        InputStream inputStream = new ByteArrayInputStream(chunk);

        final RequestCallback requestCallback = request -> {
            request.getHeaders().add("Content-type", "application/octet-stream");
            request.getHeaders().add("Authorization", "Bearer " + token);
            int rangeStart = chunkNo * chunkSize;
            int rangeEnd = rangeStart+chunk.length-1;
            request.getHeaders().add("Content-Length", String.valueOf((rangeEnd-rangeStart)+1));
            String contentLength =  String.format("bytes %d-%d/%d",rangeStart,rangeEnd,fileSize);
            request.getHeaders().add("Content-Range",contentLength);
            System.out.println("Content-Range: <unit> <range-start>-<range-end>/<size> -> "+contentLength);
            request.getBody().write(chunk);

//            IOUtils.copy(inputStream, request.getBody());
        };

        RestTemplate restTemplate = new RestTemplate();
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setBufferRequestBody(false);
        restTemplate.setRequestFactory(requestFactory);
//        final HttpMessageConverterExtractor<InputStream> responseExtractor =
//                new HttpMessageConverterExtractor<InputStream>(InputStream.class, restTemplate.getMessageConverters());

//        StringFromHeadersExtractor responseExtractor1 =  new StringFromHeadersExtractor();        StringFromHeadersExtractor responseExtractor1 =  new StringFromHeadersExtractor();
        ResponseExtractor<String> responseExtractor1 = clientHttpResponse -> {
            String data = null;
            try {
                HttpHeaders headers = clientHttpResponse.getHeaders();
                //headers.keySet().stream().forEach(k -> System.out.println(headers.get(k)));

                System.out.println("chunkUrl.statusCode > " + clientHttpResponse.getStatusCode());
                System.out.println("chunkUrl.headers > " + clientHttpResponse.getHeaders());
                //System.out.println("chunkUrl.response > "+response.getBody());
                data = IOUtils.toString(clientHttpResponse.getBody());

                if(clientHttpResponse.getStatusCode() == HttpStatus.OK)
                    callNotifyCompletedApi(chunkUrl);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return data;
        };
        String res = restTemplate.execute(chunkUrl, HttpMethod.PUT, requestCallback, responseExtractor1);
        System.out.println("chunkUrl.res > "+res);
    }

    private String callNotifyCompletedApi(String chunkUrl) throws Exception {
        // Call File NotifyCompleted API
        String notifyCompletedUrl = "http://localhost:8080/api/notify-completed";

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token);
        headers.add("Content-Type", "application/json");

        Long size = file.length();
        String name = file.getName();
        String mimeType = Files.probeContentType(file.toPath());
        String json = "{\n" +
                "  \"contentType\": \""+mimeType+"\",\n" +
                "  \"currentChunk\": 0,\n" +
                "  \"hash\": \""+hash+"\",\n" +
                "  \"name\": \""+chunkUrl+"\",\n" +
                "  \"size\": "+size+"\n" +
                "}";
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(json);
        HttpEntity<JsonNode> requestEntity = new HttpEntity<>(jsonNode, headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate
                .postForEntity(notifyCompletedUrl, requestEntity, String.class);


        System.out.println("notifyCompleted.notifyCompletedUrl > " + notifyCompletedUrl);
        System.out.println("notifyCompleted.headers > " + response.getHeaders());
        System.out.println("notifyCompleted.response > " + response.getBody());

        return chunkUrl;
    }
}
