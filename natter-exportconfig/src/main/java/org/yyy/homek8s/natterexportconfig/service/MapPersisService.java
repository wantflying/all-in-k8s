package org.yyy.homek8s.natterexportconfig.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import static org.yyy.homek8s.natterexportconfig.NatterExportconfigApplication.FILE_NAME;
import static org.yyy.homek8s.natterexportconfig.NatterExportconfigApplication.STORAGE;

@Service
@Slf4j
public class MapPersisService {

    // 定时任务，每 10 分钟执行
    @Scheduled(fixedRate = 10 * 60 * 1000) // 600000 毫秒 = 10 分钟
    public void scheduleTask() {
        saveToFile(); // 持久化 HashMap
    }


    // 持久化 HashMap 到文件
    public void saveToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(STORAGE);
            log.info("HashMap 已持久化到文件:",STORAGE.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}