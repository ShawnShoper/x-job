package org.x.job.registry.zookeeper;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class ZookeeperRegistryApplication {
    public static void main(String[] args) {
        SpringApplication.run(ZookeeperRegistryApplication.class, args);
    }
}
