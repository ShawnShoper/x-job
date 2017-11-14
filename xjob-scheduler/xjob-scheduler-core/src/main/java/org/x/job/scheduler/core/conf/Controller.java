package org.x.job.scheduler.core.conf;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {
    @RequestMapping("/aaaa")
    public A cc() {
        return new A();
    }

    class A {
        private String name = "\"asds";

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
