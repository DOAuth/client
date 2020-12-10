package com.github.doauth.client.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.HashMap;
import java.util.Map;

@Controller
public class WebController {
    @GetMapping
    public String index() {
        return "index";
    }

    @GetMapping("/access")
    public String access(Model model) {
        Map<String, String> scope = new HashMap<>();
        scope.put("scope1", "Scope 1");
        scope.put("scope2", "Scope 2");
        scope.put("scope3", "Scope 3");
        scope.put("scope4", "Scope 4");

        model.addAttribute("scope", scope);
        return "access";
    }

    @GetMapping("/sign")
    public String sign() {
        return "sign";
    }
}
