package org.example.security_jwt_oauth2.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.example.security_jwt_oauth2.dto.JoinDTO;
import org.example.security_jwt_oauth2.service.JoinService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@Log4j2
@RequiredArgsConstructor
public class JoinController {

    // 생성자 주입 방법
    private final JoinService joinService;


    @GetMapping("/join")
    public String join() {
        return "join";
    }

    @PostMapping("/joinProc")
    public String joinProc(@ModelAttribute JoinDTO joinDTO) {

        log.info("joinDTO={}", joinDTO.getUserName());
        joinService.joinProcess(joinDTO);

        return "redirect:/login";
    }
}
