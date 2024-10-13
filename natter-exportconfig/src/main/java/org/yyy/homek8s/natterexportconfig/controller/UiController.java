package org.yyy.homek8s.natterexportconfig.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

import static org.yyy.homek8s.natterexportconfig.NatterExportconfigApplication.STORAGE;

@Controller
public class UiController {




    @GetMapping("/update-url")
    public String showUpdateUrlPage(@RequestParam(value = "token", required = false) String token, Model model) {
        model.addAttribute("storage", STORAGE);
        model.addAttribute("token", token); // 将 token 添加到模型中
        return "update-url"; // 返回 Thymeleaf 模板名称
    }

    @PostMapping("/update-url")
    public String updateUrl(@RequestParam("key") String key,
                            @RequestParam("value") String newValue,
                            @RequestParam("token") String token) {
        STORAGE.put(key, newValue); // 更新 HashMap 中的值
        // 构建重定向 URL，并附加 token 参数
        return "redirect:/update-url?token=" + token; // 重定向到 GET 请求，并携带 token
    }

}