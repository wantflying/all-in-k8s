package org.yyy.homek8s.natterexportconfig.controller;

import ch.qos.logback.core.util.StringUtil;
import jakarta.annotation.Nullable;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import static org.yyy.homek8s.natterexportconfig.NatterExportconfigApplication.STORAGE;

@RestController()
public class NatterController {



    @GetMapping("/getFSLLink")
    public ResponseEntity<String> getFSLLink(@Nullable String url,@Nullable String type) {

        type = StringUtil.isNullOrEmpty(type) ? "" : "/"+type;

        // 创建 RestTemplate
        RestTemplate restTemplate = new RestTemplate();

        // 设置 User-Agent
        HttpHeaders headers = new HttpHeaders();
        headers.set("User-Agent", "Mozilla/5.0 (iPhone; CPU iPhone OS 13_2_3 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/13.0.3 Mobile/15E148 Safari/604.1");
        HttpEntity<String> entity = new HttpEntity<>(headers);
        // 调用第三方接口
        url = StringUtil.isNullOrEmpty(url) ? STORAGE.get("fsl") : url;// 替换为实际的 API URL
        if(!StringUtil.isNullOrEmpty(url)) STORAGE.put("fsl",url);

        url += type;
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

        // 返回结果
        return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
    }
}