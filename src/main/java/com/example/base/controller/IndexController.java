package com.example.base.controller;

import com.example.base.po.ThymeleafUserPO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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

    @GetMapping("/thymeleaf")
    public String index(Model model) {
        ThymeleafUserPO thymeleafUserPo = new ThymeleafUserPO("张三", 20, true, "法外狂徒");
        model.addAttribute("user", thymeleafUserPo);
        model.addAttribute("booleans", new boolean[]{true, false, false, true});
        return "thymeleaf";
    }
}
