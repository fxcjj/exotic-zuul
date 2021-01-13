package com.vic.user1.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author victor
 * date: 2020/11/30 19:26
 */
@RestController
@RequestMapping("test")
public class TestController {

    @GetMapping("test1")
    public String test1() {
        return "user1 test1";
     }
}
