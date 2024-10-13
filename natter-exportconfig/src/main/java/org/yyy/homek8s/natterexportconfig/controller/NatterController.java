package org.yyy.homek8s.natterexportconfig.controller;

import ch.qos.logback.core.util.StringUtil;
import jakarta.annotation.Nullable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.yyy.homek8s.natterexportconfig.utils.MD5Util;

import static org.yyy.homek8s.natterexportconfig.NatterExportconfigApplication.STORAGE;

@RestController()
@Slf4j
public class NatterController {



    @GetMapping("/getFSLLink")
    public ResponseEntity<String> getFSLLink(String key,@Nullable String url,@Nullable String type,@Nullable String base64 ) {

        type = StringUtil.isNullOrEmpty(type) ? "" : "/"+type;

        // 创建 RestTemplate
        RestTemplate restTemplate = new RestTemplate();

        // 设置 User-Agent
        HttpHeaders headers = new HttpHeaders();
        headers.set("User-Agent", "Mozilla/5.0 (iPhone; CPU iPhone OS 13_2_3 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/13.0.3 Mobile/15E148 Safari/604.1");
        HttpEntity<String> entity = new HttpEntity<>(headers);
        // 调用第三方接口
        url = StringUtil.isNullOrEmpty(url) ? STORAGE.get("fsl") : url;// 替换为实际的 API URL
        if(!StringUtil.isNullOrEmpty(url)) STORAGE.put(key,url);

        url += type;
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        log.info("请求成功");
        // 返回结果
        String body =  response.getBody();
        body = "true".equals(base64) ?  MD5Util.base64Decode(response.getBody()) : body;
        return ResponseEntity.status(response.getStatusCode()).body(body);
    }

    @GetMapping("/updateValues")
    public ResponseEntity<String> updateValues(@RequestParam("key") String key,
                                             @RequestParam("value") String newValue) {

        STORAGE.put(key, newValue); // 更新 HashMap 中的值
        log.info("values {}-{} update success",key,newValue);
        // 返回结果
        return new ResponseEntity<>("ok",HttpStatusCode.valueOf(200));
    }

    @GetMapping("/getHomeLink")
    public ResponseEntity<String> getHomeLink(@RequestParam("key") String key,
                                               @RequestParam("pwd") String pwd) {

        String tpl = "proxies:\n" +
                "    - { name: home, type: ss, server: %s, port: %s, cipher: aes-256-gcm, password: %s, udp: false }\n";
        // 需要替换的值
        String port = "8388"; // 示例端口
        String password = StringUtil.isNullOrEmpty(pwd) ? "your_password" : pwd; // 示例密码
        port = STORAGE.getOrDefault(key,"127.0.0.1:80");// 更新 HashMap 中的值
        String[] parts = port.split(":");

        // 使用 String.format 替换占位符
        String result = String.format(tpl, parts[0],parts[1], password);
        log.info("create url :\n {}",result);
        // 返回结果
        return new ResponseEntity<>(result,HttpStatusCode.valueOf(200));
    }
}