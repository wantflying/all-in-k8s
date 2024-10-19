package org.yyy.homek8s.natterexportconfig;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import static org.yyy.homek8s.natterexportconfig.NatterExportconfigApplication.FILE_NAME;
import static org.yyy.homek8s.natterexportconfig.NatterExportconfigApplication.STORAGE;

@Component
@Slf4j
public class StartInit {


    @PostConstruct
    public void init() throws InterruptedException {
        log.info("从环境变量初始化hashmap");
        loadFromFile();
        log.info("从环境变量初始化完成hashmap:{}",STORAGE.toString());
    }



    // 从文件加载 Map
    public void loadFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("=", 2); // 只分割成两部分
                if (parts.length == 2) {
                    String key = parts[0].trim();
                    String value = parts[1].trim();
                    addOrUpdate(key,value);
                }
            }
            log.info("Map 已从文件加载。");
        } catch (FileNotFoundException e) {
            log.error("持久化文件未找到，使用空 Map。");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 添加或更新 Map 条目
    public void addOrUpdate(String key, String value) {
        if (!STORAGE.containsKey(key)) {
            STORAGE.put(key, value);
            System.out.println("添加条目: " + key + " = " + value);
        } else {
            System.out.println("条目已存在，未添加: " + key);
        }
    }
}