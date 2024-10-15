package org.yyy.homek8s.natterexportconfig.service;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class QLServer {
    private static final Logger log = LoggerFactory.getLogger(QLServer.class);

    private String address;
    private String id;
    private String secret;
    private String auth;
    private final Gson gson;

    @Autowired
    public QLServer(QLServerProperties properties) {
        this.address = properties.getAddress();
        this.id = properties.getId();
        this.secret = properties.getSecret();
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        login();
    }


    private void login() {
        String url = String.format("%s/open/auth/token?client_id=%s&client_secret=%s", address, id, secret);
        try {
            HttpResponse response = HttpRequest.get(url).execute();
            String responseBody = response.body();
            JsonObject jsonObject = JsonParser.parseString(responseBody).getAsJsonObject();
            if (jsonObject.has("code") && jsonObject.get("code").getAsInt() == 200) {
                JsonObject data = jsonObject.getAsJsonObject("data");
                this.auth = data.get("token_type").getAsString() + " " + data.get("token").getAsString();
            } else {
                log.info("登录失败：{}", jsonObject.has("message") ? jsonObject.get("message").getAsString() : "未知错误");
            }
        } catch (Exception e) {
            log.info("登录失败：{}", e.getMessage());
        }
    }

    private void updateAuth() {
        login(); // 每次请求前更新 token
    }

    public List<Map<String, Object>> getEnvs() {
        updateAuth(); // 确保使用最新的 token
        String url = String.format("%s/open/envs?searchValue=", address);
        try {
            HttpResponse response = HttpRequest.get(url)
                    .header("Authorization", this.auth)
                    .execute();
            String responseBody = response.body();
            JsonObject jsonObject = JsonParser.parseString(responseBody).getAsJsonObject();
            if (jsonObject.has("code") && jsonObject.get("code").getAsInt() == 200) {
                return new Gson().fromJson(jsonObject.getAsJsonArray("data"), List.class);
            } else {
                log.info("获取环境变量失败：{}", jsonObject.has("message") ? jsonObject.get("message").getAsString() : "未知错误");
            }
        } catch (Exception e) {
            log.info("获取环境变量失败：{}", e.getMessage());
        }
        return null;
    }

    public String findEnvId(String key, String value) {
        List<Map<String, Object>> envs = getEnvs();
        if (envs != null) {
            for (Map<String, Object> env : envs) {
                if (env.get("name").equals(key) && env.get("value").equals(value)) {
                    log.info("找到环境变量：{}", gson.toJson(env)); // 打印找到的环境变量
                    return String.valueOf(env.get("id")); // 假设返回的环境变量中有 id 字段
                }
            }
        }
        log.info("未找到符合条件的环境变量：key={}，value={}", key, value);
        return null;
    }

    public boolean addEnv(List<Map<String, Object>> envs) {
        updateAuth(); // 确保使用最新的 token
        String url = String.format("%s/open/envs", address);
        try {
            HttpResponse response = HttpRequest.post(url)
                    .header("Authorization", this.auth)
                    .header("Content-Type", "application/json")
                    .body(gson.toJson(envs))
                    .execute();
            String responseBody = response.body();
            JsonObject jsonObject = JsonParser.parseString(responseBody).getAsJsonObject();
            if (jsonObject.has("code") && jsonObject.get("code").getAsInt() == 200) {
                log.info("新增环境变量成功：{}", gson.toJson(envs));
                return true;
            } else {
                log.error("错误返回：{}",responseBody);
                log.error("新增环境变量失败：{}", jsonObject.has("message") ? jsonObject.get("message").getAsString() : "未知错误");
                return false;
            }
        } catch (Exception e) {
            log.info("新增环境变量失败：{}", e.getMessage());
            return false;
        }
    }

    public boolean updateOrDeleteEnvByKey(String key, Map<String, Object> newEnv, boolean isDelete) {
        updateAuth(); // 确保使用最新的 token
        List<Map<String, Object>> envs = getEnvs(); // 获取当前的环境变量列表

        for (Map<String, Object> env : envs) {
            if (env.get("name").equals(key)) {
                if (isDelete) {
                    // 删除操作
                    String url = String.format("%s/open/envs", address); // 删除不需要 ID
                    try {
                        Double id = (Double) env.get("id");
                        HttpResponse response = HttpRequest.delete(url)
                                .header("Authorization", this.auth)
                                .header("Content-Type", "application/json")
                                .body(gson.toJson(List.of(id))) // 传入要删除的环境变量
                                .execute();
                        String responseBody = response.body();
                        JsonObject jsonObject = JsonParser.parseString(responseBody).getAsJsonObject();
                        if (jsonObject.has("code") && jsonObject.get("code").getAsInt() == 200) {
                            log.info("删除环境变量成功：{}", gson.toJson(env));
                            return true;
                        } else {
                            log.error("错误返回：{}",responseBody);
                            log.info("删除环境变量失败：{}", jsonObject.has("message") ? jsonObject.get("message").getAsString() : "未知错误");
                            return false;
                        }
                    } catch (Exception e) {
                        log.info("删除环境变量失败：{}", e.getMessage());
                        return false;
                    }
                } else {
                    // 更新操作
                    String url = String.format("%s/open/envs", address);// 更新不需要 ID
                    String id = String.valueOf(env.get("id"));
                    newEnv.putIfAbsent("id",id);
                    try {
                        HttpResponse response = HttpRequest.put(url)
                                .header("Authorization", this.auth)
                                .header("Content-Type", "application/json")
                                .body(gson.toJson(newEnv)) // 传入新的环境变量数据
                                .execute();
                        String responseBody = response.body();
                        JsonObject jsonObject = JsonParser.parseString(responseBody).getAsJsonObject();
                        if (jsonObject.has("code") && jsonObject.get("code").getAsInt() == 200) {
                            log.info("更新环境变量成功：{}", gson.toJson(newEnv));
                            return true;
                        } else {
                            log.error("错误返回：{}",responseBody);
                            log.info("更新环境变量失败：{}", jsonObject.has("message") ? jsonObject.get("message").getAsString() : "未知错误");
                            return false;
                        }
                    } catch (Exception e) {
                        log.info("更新环境变量失败：{}", e.getMessage());
                        return false;
                    }
                }
            }
        }
        log.info("未找到匹配的环境变量：key={}", key);
        return false; // 如果没有找到匹配的环境变量
    }

//    public static void main(String[] args) {
//        String address = "http://192.168.1.1:5700";
//        String clientId = "_5sssss-";
//        String clientSecret = "ssssss";
//
//        QLServer qlServer = new QLServer(address, clientId, clientSecret);
//
//        // 示例：新增环境变量
//        List<Map<String, Object>> newEnvs = List.of(
//                Map.of("name", "xinzengceshi", "value", "value")
//        );
//        boolean addResult = qlServer.addEnv(newEnvs);
//        log.info("新增结果：{}", addResult);
//
//
//        // 示例：更新环境变量
//        HashMap<String, Object> updateEnv = new HashMap<>();
//        updateEnv.put("name", "xinzengceshi");
//        updateEnv.put("value", "更新完");
//        boolean updateResult = qlServer.updateOrDeleteEnvByKey("xinzengceshi", updateEnv, false);
//        log.info("更新结果：{}", updateResult);
//
//        // 示例：删除环境变量
//        boolean deleteResult = qlServer.updateOrDeleteEnvByKey("xinzengceshi", null, true);
//        log.info("删除结果：{}", deleteResult);
//    }
}