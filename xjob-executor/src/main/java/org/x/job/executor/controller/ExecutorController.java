package org.x.job.executor.controller;

import feign.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.x.job.commons.transfer.Fenshou;
import org.x.job.executor.pipeline.PipelineExecutor;
import org.x.job.executor.receive.MessageReceiver;
import org.x.job.executor.send.MessageSender;

import java.util.List;

@Controller("/executor")
public class ExecutorController {

    @Autowired
    private MessageSender messageSender;
    @Autowired
    private PipelineExecutor pipelineExecutor;
    @Autowired
    private MessageReceiver messageReceiver;


    @PostMapping(value = "/innersend")
    public void innerSend(String addr, List<String> jobs) throws Exception {
        messageSender.innerSend(addr, jobs);
    }

    @PostMapping(value = "/doit")
    public void doIt() throws Exception{
        pipelineExecutor.doIt();
    }

    @PostMapping(value = "/doreceive")
    public void doReceive(Fenshou fenshou) throws Exception {
        messageReceiver.doReceive(fenshou);
    }
}
