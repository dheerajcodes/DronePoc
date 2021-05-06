package com.drone.poc;

import org.h2.tools.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.sql.SQLException;

@SpringBootApplication
public class DronePocApplication {
    @Value("${h2.server.tcp.port:9090}")
    private int h2TcpPort;
    @Value("${h2.server.tcp.enabled:false}")
    private boolean tcpServerEnabled;

    private Server h2DatabaseTcpServer;

    public static void main(String[] args) {
        SpringApplication.run(DronePocApplication.class, args);
    }

    @PostConstruct
    public void startH2DatabaseTcpServer() throws SQLException {
        if (tcpServerEnabled) {
            this.h2DatabaseTcpServer = Server.createTcpServer("-tcp", "-tcpAllowOthers", "-tcpPort", String.valueOf(h2TcpPort)).start();
        }
    }

    @PreDestroy
    public void stopH2DatabaseTcpServer() {
        if (h2DatabaseTcpServer != null)
            this.h2DatabaseTcpServer.stop();
    }
}
