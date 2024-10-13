package org.yyy.homek8s.natterexportconfig;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@SpringBootApplication
public class NatterExportconfigApplication {

    public static Map<String, String> STORAGE = null;


    public static void main(String[] args) {
        STORAGE = new ConcurrentHashMap<>();
        STORAGE.put("fsl","https://balesp.xyz/NBB-c/Dc6vR");
        SpringApplication.run(NatterExportconfigApplication.class, args);
    }

}
