package com.drone.poc;

import org.h2.tools.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.sql.SQLException;

@SpringBootApplication
public class DronePocApplication {
    Logger logger = LoggerFactory.getLogger(DronePocApplication.class);
    @Value("${h2.server.tcp.port:9090}")
    private int h2TcpPort;
    @Value("${h2.server.tcp.enabled:false}")
    private boolean tcpServerEnabled;

    private Server h2DatabaseTcpServer;

    public static void main(String[] args) {
        SpringApplication.run(DronePocApplication.class, args);
    }

    @PostConstruct
    public void startH2DatabaseTcpServer() {
        if (tcpServerEnabled) {
            try {
                this.h2DatabaseTcpServer = Server.createTcpServer("-tcp", "-tcpAllowOthers", "-tcpPort", String.valueOf(h2TcpPort)).start();
                logger.info("Running H2 database server at : " + this.h2DatabaseTcpServer.getURL());
            } catch (SQLException exception) {
                logger.error("Failed to start H2 database server - " + exception.getMessage());
            }
        }
    }

    @PreDestroy
    public void stopH2DatabaseTcpServer() {
        if (h2DatabaseTcpServer != null)
            this.h2DatabaseTcpServer.stop();
    }
}
