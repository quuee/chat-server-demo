package com.demo.im.netty;


import jakarta.annotation.PreDestroy;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class IMServersLaunch implements CommandLineRunner {

    public static volatile long machineId = 0; // 机器id

    private List<IMServer> imServers;

    public IMServersLaunch(List<IMServer> imServers) {
        this.imServers = imServers;
    }

    @Override
    public void run(String... args) throws Exception {
        // 启动服务
        for (IMServer imServer : imServers) {
            imServer.start();
        }
    }


    @PreDestroy
    public void destroy() {
        // 停止服务
        for (IMServer imServer : imServers) {
            imServer.stop();
        }
    }

}
