package com.lelandyan.ct.web.controller;

import com.lelandyan.ct.web.bean.Calllog;
import com.lelandyan.ct.web.service.CalllogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
