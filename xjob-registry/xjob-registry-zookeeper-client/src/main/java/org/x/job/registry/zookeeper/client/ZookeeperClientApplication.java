package org.x.job.registry.zookeeper.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableDiscoveryClient
@RestController
public class ZookeeperClientApplication {
    public static void main(String[] args) {
        SpringApplication.run(ZookeeperClientApplication.class, args);
    }
    @Autowired
    private LoadBalancerClient loadBalancer;

    @Autowired
    private DiscoveryClient discovery;

    @RequestMapping("/discovery")
    public Object discovery() {

        return discovery.getServices();
    }

    @RequestMapping("/all")
    public Object all() {
        return discovery.getServices();
    }

}
