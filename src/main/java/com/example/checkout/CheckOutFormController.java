package com.example.checkout;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
public class CheckOutFormController {
    // 고객이 최종 결제 정보를 입력할 Form Controller

    @GetMapping("/checkOutForm")
    public String checkOutForm(Model model) {
        log.info("checkOutForm...");
        return "checkOutForm";
    }

}
