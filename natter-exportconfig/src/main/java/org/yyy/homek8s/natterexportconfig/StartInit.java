package org.yyy.homek8s.natterexportconfig;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.concurrent.ConcurrentHashMap;

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


    @SuppressWarnings("unchecked")
    public void loadFromFile() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            STORAGE = (ConcurrentHashMap<String, String>) ois.readObject();
            System.out.println("HashMap 已从文件加载。");
        } catch (FileNotFoundException e) {
            System.out.println("持久化文件未找到，使用空 HashMap。");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}