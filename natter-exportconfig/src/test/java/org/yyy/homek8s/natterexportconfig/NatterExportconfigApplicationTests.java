package org.yyy.homek8s.natterexportconfig;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.yyy.homek8s.natterexportconfig.controller.PodLogMonitorController;

@SpringBootTest
class NatterExportconfigApplicationTests {

    @Autowired
    PodLogMonitorController podLogMonitorController;

    @Test
    void contextLoads() {
        podLogMonitorController.monitorPodLogs();
    }

}
