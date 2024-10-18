package org.yyy.homek8s.natterexportconfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.yyy.homek8s.natterexportconfig.service.MapPersisService;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@SpringBootApplication
@EnableScheduling  //表示开启定时任务
public class NatterExportconfigApplication {

    public static final String FILE_NAME = "natterconfig.dat";

    public static Map<String, String> STORAGE = new ConcurrentHashMap<>();

    @Autowired
    private MapPersisService mapPersisService;


    public static void main(String[] args) {
        SpringApplication.run(NatterExportconfigApplication.class, args);
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
