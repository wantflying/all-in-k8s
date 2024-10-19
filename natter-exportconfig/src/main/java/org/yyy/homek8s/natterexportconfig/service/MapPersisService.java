package org.yyy.homek8s.natterexportconfig.service;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Map;

import static org.yyy.homek8s.natterexportconfig.NatterExportconfigApplication.FILE_NAME;
import static org.yyy.homek8s.natterexportconfig.NatterExportconfigApplication.STORAGE;

@Service
@Slf4j
public class MapPersisService {

    private Gson gson = new Gson();


    // 定时任务，每 10 分钟执行
    @Scheduled(fixedRate = 1 * 60 * 1000) // 600000 毫秒 = 10 分钟
    public void scheduleTask() {
        saveToFile(); // 持久化 HashMap
    }


    // 持久化 Map 到文件
    public void saveToFile() {
        try (Writer writer = new FileWriter(FILE_NAME,false)) {
            for (Map.Entry<String, String> entry : STORAGE.entrySet()) {
                writer.write(entry.getKey() + "=" + entry.getValue() + "\n");
            }
            log.info("Map 已持久化到文件:{}",STORAGE.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}