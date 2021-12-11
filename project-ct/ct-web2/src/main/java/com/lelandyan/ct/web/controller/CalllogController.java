package com.lelandyan.ct.web.controller;
import com.lelandyan.ct.web.bean.Calllog;
import com.lelandyan.ct.web.service.CalllogService;
import com.lelandyan.ct.web.service.CalllogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class CalllogController {
    @Autowired
    private CalllogService calllogService;

    @RequestMapping("/query")
    public String query() {
        return "query";
    }

    @RequestMapping("/view")
    public String view(String tel, String calltime, Model model){
        System.out.println("come in view");
        List<Calllog> logs = calllogService.queryMonthDatas(tel, calltime);
        System.out.println(logs.size());
        model.addAttribute("calllogs",logs);
        return "view";
    }
}
