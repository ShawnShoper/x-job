package org.x.job.executor.feign;

import feign.Param;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@FeignClient(name = "sendFeign")
public interface SendFeign {
    @RequestMapping(value = "/executor/innersend", method = RequestMethod.POST)
    void innerSend(@Param("addr") String addr, @Param("jobs") List<String> jobs);
}
