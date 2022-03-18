package com.shop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/thymeleaf")   //클라이언트의 요청에 대해서 어떤 컨트롤러가 처리할지 매핑하는 어노테이션

public class ThymeleafExController {
@GetMapping(value = "/ex01")
    public String thymeleafExample01(Model model){
    model.addAttribute("data", "타임리프 예제입니당");
    return "thymeleafEx/thymeleafEx01"; //templates 폴더를 기준으로 뷰의 위치와 이름(thymeleafEx01.html)을 반환한다.
}
}
