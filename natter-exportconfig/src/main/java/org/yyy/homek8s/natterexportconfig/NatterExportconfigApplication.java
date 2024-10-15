package org.yyy.homek8s.natterexportconfig;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@SpringBootApplication
@EnableScheduling  //表示开启定时任务
public class NatterExportconfigApplication {

    public static Map<String, String> STORAGE = new ConcurrentHashMap<>();


    public static void main(String[] args) {
        STORAGE.put("fsl","https://balesp.xyz/NBB-c/Dc6vR");
        SpringApplication.run(NatterExportconfigApplication.class, args);
    }

}
