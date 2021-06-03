package com.vetri.poc.filechunk;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vetri.poc.filechunk.model.ContentRange;
import com.vetri.poc.filechunk.utils.ServletUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.assertj.core.internal.Bytes;
import org.junit.Assert;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StreamUtils;
import org.springframework.web.client.*;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class FileUploaderApplicationTest {

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    @DisplayName("Test upload file via chunk uploader")
    public void testUpload() throws Exception {

//        String accessToken= "<the oauth 2 token>";
//        RestTemplate restTemplate = new RestTemplateBuilder(rt-> rt.getInterceptors().add((request, body, execution) -> {
//            request.getHeaders().add("Authorization", "Bearer "+accessToken);
//            return execution.execute(request, body);
//        })).build();


//        RestTemplate restTemplate = new RestTemplate();
//        HttpHeaders headers = new HttpHeaders();
//        String url = "http://httpbin.org/get";
//
//        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        headers.add("user-agent", "Mozilla/5.0 Firefox/26.0");
//        headers.set("user-key", "your-password-123"); // optional - in case you auth in headers
//        HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
//        ResponseEntity<String> respEntity = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
//
//        System.out.println(respEntity.toString());

//        String url = "http://localhost:8080/api/uploadFile";
//        HttpHeaders headers = new HttpHeaders();
////        headers.add("Authorization", UUID.randomUUID().toString().replace("-",""));
////        headers.add("Content-Type", "application/json");
//        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
//        MultiValueMap<String, Object> body
//                = new LinkedMultiValueMap<>();
//        body.add("files", new File("D:\\tmp\\joins\\0000_Smita's Devotional Album Ishana on Lord Shiva\\2021\\4\\26\\8f5ed791-a508-49f8-9843-167408f6a42b.png"));
//
//        HttpEntity<MultiValueMap<String, Object>> requestEntity
//                = new HttpEntity<>(body, headers);
//
//        String serverUrl = "http://httpbin.org/post";
//
//        RestTemplate restTemplate = new RestTemplate();
//        ResponseEntity<String> response = restTemplate
//                .postForEntity(serverUrl, requestEntity, String.class);
//
//        System.out.println("response > "+response.getBody());
    }

    private String createSalt() {
        String ts = String.valueOf(System.currentTimeMillis());
        String rand = UUID.randomUUID().toString();
        return DigestUtils.sha1Hex(ts + rand);
    }

    String token = UUID.randomUUID().toString().replace("-", "");
    String hash = UUID.randomUUID().toString().replace("-", "");
    String uploadId = createSalt();
    String serverUrl = "http://localhost:8080/api/uploadFile";
    //    String serverUrl = "http://httpbin.org/post";
    File file = new File("D:\\tmp\\small.mp4");

    @Test
    @DisplayName("Test upload file via chunk uploader v1")
    public void testUploadFile1() throws Exception {
        String chunkUrl = callMetaDataApi();
//        //chunkUrl = "http://httpbin.org/put";
//        callChunkApi(chunkUrl);

        // 1048576 bit = 128 kB.
        int chunkSize = 1 * 8 * 128 * 128; // 1 Megabyte
        FileChannel fileChannel = FileChannel.open(Paths.get("D:\\tmp\\small.mp4"), StandardOpenOption.READ, StandardOpenOption.WRITE);
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
        System.out.println("\nread " + sum + " bytes total");
    }

    private static class StringFromHeadersExtractor implements ResponseExtractor<String> {

        public String extractData(ClientHttpResponse response) {
            String data = null;
            try {
                HttpHeaders headers = response.getHeaders();
                //headers.keySet().stream().forEach(k -> System.out.println(headers.get(k)));

                System.out.println("chunkUrl.statusCode > " + response.getStatusCode());
                System.out.println("chunkUrl.headers > " + response.getHeaders());
                //System.out.println("chunkUrl.response > "+response.getBody());

                data = IOUtils.toString(response.getBody());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return data;
        }
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

    private void callChunkApi(String chunkUrl, int chunkNo, int chunkLength, Long size, byte [] chunk) throws Exception {

        InputStream inputStream = new ByteArrayInputStream(chunk);

        final RequestCallback requestCallback = request -> {
            request.getHeaders().add("Content-type", "application/octet-stream");
            request.getHeaders().add("Authorization", "Bearer " + token);
            request.getHeaders().add("Content-Length", String.valueOf(size));
            int rangeStart = chunkNo * chunkLength;
            int rangeEnd = rangeStart+chunk.length;
            String contentLength =  String.format("bytes %d-%d/%d",rangeStart,rangeEnd,size);
            request.getHeaders().add("Content-Range",contentLength);

            File tmp = new File("D:\\tmp");
            File tempFile = File.createTempFile("download", "tmp", tmp);
            FileOutputStream fos = new FileOutputStream(tempFile);
            fos.write(chunk);
            byte[] buf = Files.readAllBytes(tempFile.toPath());
            fos.close();

            request.getBody().write(buf);

//            IOUtils.copy(inputStream, request.getBody());
        };

        RestTemplate restTemplate = new RestTemplate();
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setBufferRequestBody(false);
        restTemplate.setRequestFactory(requestFactory);
//        final HttpMessageConverterExtractor<InputStream> responseExtractor =
//                new HttpMessageConverterExtractor<InputStream>(InputStream.class, restTemplate.getMessageConverters());

//        StringFromHeadersExtractor responseExtractor1 =  new StringFromHeadersExtractor();        StringFromHeadersExtractor responseExtractor1 =  new StringFromHeadersExtractor();
        ResponseExtractor responseExtractor1 = (ResponseExtractor<String>) clientHttpResponse -> {
            String data = null;
            try {
                HttpHeaders headers = clientHttpResponse.getHeaders();
                //headers.keySet().stream().forEach(k -> System.out.println(headers.get(k)));

                System.out.println("chunkUrl.statusCode > " + clientHttpResponse.getStatusCode());
                System.out.println("chunkUrl.headers > " + clientHttpResponse.getHeaders());
                //System.out.println("chunkUrl.response > "+response.getBody());

                data = IOUtils.toString(clientHttpResponse.getBody());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return data;
        };
        restTemplate.execute(chunkUrl, HttpMethod.PUT, requestCallback, responseExtractor1);

    }
//
//    @Test
//    public void test1() throws Exception{
//        FileChannel fileChannel = FileChannel.open(Paths.get("D:\\tmp\\small.mp4"), StandardOpenOption.READ, StandardOpenOption.WRITE);
//        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
//        int n;
//        long sum = 0L;
//        while ((n = fileChannel.read(byteBuffer)) != -1) {
//            sum += n;
//            byteBuffer.flip();
////            while (byteBuffer.hasRemaining()) {
////                char c = (char) byteBuffer.get();
////                System.out.print(c);
////            }
//            byte[] chunk = new byte[n];
//            byteBuffer.get(chunk);
//
//            System.out.println("read " + chunk.length  + " bytes");
//            byteBuffer.clear();
//        }
//        System.out.println("\nread " + sum + " bytes total");
//    }

}