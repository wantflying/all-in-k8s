//package org.yyy.homek8s.natterexportconfig.controller;
//
//
//import io.fabric8.kubernetes.api.model.Namespaced;
//import io.fabric8.kubernetes.api.model.Pod;
//import io.fabric8.kubernetes.client.KubernetesClient;
//import io.fabric8.kubernetes.client.KubernetesClientBuilder;
//import io.fabric8.kubernetes.client.dsl.LogWatch;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.scheduling.annotation.EnableScheduling;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Controller;
//import org.springframework.stereotype.Service;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.nio.charset.StandardCharsets;
//import java.time.LocalDateTime;
//import java.util.concurrent.Executors;
//import java.util.concurrent.ScheduledExecutorService;
//
//import static org.yyy.homek8s.natterexportconfig.NatterExportconfigApplication.STORAGE;
//
//@EnableScheduling
//@Service
//@Controller
//@Slf4j
//public class PodLogMonitorController {
//
//    private final KubernetesClient client;
//    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
//
//    public PodLogMonitorController() {
//        this.client = new KubernetesClientBuilder().build();
//    }
//
//    @Scheduled(cron = "0 0/30 * * * ?")
//    public void monitorPodLogs() {
//        log.info("开始执行日期检测：{}", LocalDateTime.now());
//        String namespace= "natter";
//        client.pods().inNamespace(namespace).list().getItems().forEach(pod -> processPodLogs(pod,namespace));
//    }
//
//
//
//    private void processPodLogs(Pod pod,String namespace) {
//        String podName = pod.getMetadata().getName();
//        String deploymentName = pod.getMetadata().getLabels().get("app");
//
//        try {
//            // 获取 Pod 的日志，非流模式
//            String logOutput = client.pods().inNamespace(namespace).withName(podName).getLog();
////            System.out.println("Pod Logs: " + logOutput);
//
//            // 处理日志内容
//            logOutput.lines().forEach(logLine -> {
//                if (logLine.contains("[E]")) {
//                    log.info("错误日志：{}",logLine);
//                    // 这里可以添加重启 Pod 的逻辑
//                    client.pods().withName(podName).delete();
//                    log.info("重启 Pod: {} ", podName);
//                    System.out.println();
//                }
//                extractAndStoreIp(logLine, deploymentName);
//            });
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    // 在 processPodLogs 方法中调用 convertInputStreamToString
//    private void processPodLogsByStream(Pod pod,String namespace) {
//        String podName = pod.getMetadata().getName();
//        log.info("开始检索{}日志....",podName);
//        String deploymentName = pod.getMetadata().getLabels().get("app");
//
//        try {
//            LogWatch logWatch = client.pods().inNamespace(namespace).withName(podName).watchLog();
//            String logOutput = convertInputStreamToString(logWatch.getOutput());
//            System.out.println("Pod Logs: " + logOutput);
//
//            // 处理日志内容
//            logOutput.lines().forEach(logLine -> {
//                if (logLine.contains("[E]")) {
////                    client.pods().withName(podName).delete();
//                    log.info("重启 Pod:{} " , podName);
//                    return;
//                }
//                extractAndStoreIp(logLine, deploymentName);
//            });
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//
//    private void extractAndStoreIp(String logLine, String deploymentName) {
//        String regex = "<--Natter-->.*tcp://(\\d+\\.\\d+\\.\\d+\\.\\d+):(\\d+)";
//        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(regex);
//        java.util.regex.Matcher matcher = pattern.matcher(logLine);
//
//        while (matcher.find()) {
//            String ip = matcher.group(1);
//            String port = matcher.group(2);
//            String ipPort = ip + ":" + port;
//            String key = deploymentName + "-port";
//            STORAGE.put(key, ipPort);
//            log.info("更新 STORAGE: {}  = {}" , key , ipPort);
//        }
//    }
//
//    private String convertInputStreamToString(InputStream inputStream) {
//        StringBuilder stringBuilder = new StringBuilder();
//        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
//            String line;
//            while ((line = reader.readLine()) != null) {
//                stringBuilder.append(line).append(System.lineSeparator());
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return stringBuilder.toString();
//    }
//
//    public static void main(String[] args) {
//        PodLogMonitorController podLogMonitorController = new PodLogMonitorController();
//        podLogMonitorController.monitorPodLogs();
//    }
//}
