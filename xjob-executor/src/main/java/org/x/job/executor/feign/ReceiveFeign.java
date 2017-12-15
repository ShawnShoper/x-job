package org.x.job.executor.feign;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.x.job.commons.transfer.Fenshou;

@FeignClient(name = "receiveFeign")
public interface ReceiveFeign {
    @RequestMapping(value = "/executor/doreceive", method = RequestMethod.POST)
    Boolean doReceive(Fenshou fenshou) throws Exception;
}
