package com.example.base.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author benben
 * @program base
 * @Description
 * @date 2021-05-28 9:53
 */
@Controller
public class IndexController {

    @GetMapping({"/", "index"})
    public String index() {
        return "index";
    }
}
