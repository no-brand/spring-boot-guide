package com.nobrand.springbootaop.controller;

import com.nobrand.springbootaop.aspect.CheckAround;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BaseController {

    private static final Logger log = LoggerFactory.getLogger(BaseController.class);

    @CheckAround(actions = {"action1"})
    @RequestMapping(value = "/")
    public String index() {
        log.info("/");
        return "index page";
    }

    @CheckAround(actions = {"action2, action3"})
    @RequestMapping(value = "/exception")
    public String throwException() throws Exception {
        log.info("/exception");
        throw new Exception("exception");
    }

}
