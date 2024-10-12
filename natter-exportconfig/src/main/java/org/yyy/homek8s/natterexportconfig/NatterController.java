package org.yyy.homek8s.natterexportconfig;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class NatterController {

    @GetMapping("/getFSLLink")
    public ResponseEntity<String> getLink() {
        // 创建 RestTemplate
        RestTemplate restTemplate = new RestTemplate();

        // 设置 User-Agent
        HttpHeaders headers = new HttpHeaders();
        headers.set("User-Agent", "Mozilla/5.0 (iPhone; CPU iPhone OS 14_0 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/14.0 Mobile/15E148 Safari/604.1");
        HttpEntity<String> entity = new HttpEntity<>(headers);

        // 调用第三方接口
        String url = "https://balesp.xyz/NBk7H/FQoZg"; // 替换为实际的 API URL
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

        // 返回结果
        return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
    }
}