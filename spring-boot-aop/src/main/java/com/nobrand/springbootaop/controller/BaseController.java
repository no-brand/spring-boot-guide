package com.nobrand.springbootaop.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BaseController {

    @RequestMapping(value = "/")
    public String index() {
        return "index page";
    }

    @RequestMapping(value = "/exception")
    public String throwException() throws Exception {
        throw new Exception("exception");
    }

}
