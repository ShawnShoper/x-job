package test;

import basetest.BaseTest;
import ch.qos.logback.core.net.SyslogOutputStream;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.x.job.commons.transfer.Fenshou;
import org.x.job.executor.receive.MessageReceiver;

import java.util.Arrays;
import java.util.List;

public class SchedulerInput extends BaseTest {

    @Autowired
    private MessageReceiver messageReceiver;

    @Test
    public void t() throws Exception {
       Boolean suceess = messageReceiver.doReceive(initFenshou());
        System.out.println("Job is done? " + suceess);
    }

    public Fenshou initFenshou(){
        Fenshou fenshou = new Fenshou();

        String jobs = "job1,job2,job3";
        String paths = "path1,path2,path3";

        List<String> job = Arrays.asList(jobs.split(","));
        List<String> machines = Arrays.asList(paths.split(","));

        fenshou.setJob(job);
        fenshou.setMachines(machines);
        return fenshou;
    }

}
