package org.x.job.executor.feign;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "pipelineExecutorFeign")
public interface PipelineExecutorFeign {
    @RequestMapping(value = "/executor/doit", method = RequestMethod.POST)
    void doIt();
}
