package org.yyy.homek8s.natterexportconfig.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.yyy.homek8s.natterexportconfig.service.QLServer;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/qlapi")
public class QLController {

    @Autowired
    private QLServer qlServer; // 假设 QLServer 是一个 Spring Bean

    @GetMapping("/updateJDCookie")
    public String updateCookie( String ptPin, String ptKey) {
        // 从 QLServer 获取 JD_COOKIE 环境变量
        List<Map<String, Object>> envs = qlServer.getEnvs();
        String jdCookie = null;
        // 查找 JD_COOKIE
        if (envs != null) {
            for (Map<String, Object> env : envs) {
                if ("JD_COOKIE".equals(env.get("name"))) {
                    jdCookie = (String) env.get("value");
                    break;
                }
            }
        }

        if (jdCookie == null) {
            return "JD_COOKIE 未找到";
        }

        // 将多行 COOKIE 数据拆分成列表
        List<String> cookieLines = List.of(jdCookie.split("\n"));

        // 创建新的 COOKIE 行
        String newCookieLine = String.format("pt_key=%s;pt_pin=%s;", ptKey, ptPin);

        // 查找是否存在指定的 pt_pin
        AtomicBoolean found = new AtomicBoolean(false);
        List<String> updatedCookieLines = cookieLines.stream()
                .map(line -> {
                    if (line.contains("pt_pin=" + ptPin)) {
                        found.set(true); // 标记为找到
                        return newCookieLine; // 替换行
                    }
                    return line; // 保留原行
                })
                .collect(Collectors.toList());

        // 如果没有找到，新增 COOKIE 行
        if (!found.get()) {
            updatedCookieLines.add(newCookieLine); // 添加新的 COOKIE 行
        }

        // 将更新后的 COOKIE 数据合并成字符串
        String updatedJD_COOKIE = String.join("\n", updatedCookieLines);
        // 获取当前时间并格式化为字符串
        String currentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        // 准备更新的环境变量
        HashMap<String, Object> newEnv = new HashMap<>();
        newEnv.put("name", "JD_COOKIE");
        newEnv.put("value", updatedJD_COOKIE);
        newEnv.put("remarks", "updateTime"+currentTime);
        // 调用 QLServer 更新环境变量
        boolean result = qlServer.updateOrDeleteEnvByKey("JD_COOKIE", newEnv, false);

        return result ? "更新成功" : "更新失败";
    }
}
