package hello.tumblbug.controller;

import hello.tumblbug.domain.Member;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home(@SessionAttribute(value = SessionConst.LOGIN_MEMBER, required = false) Member loginMember, Model model) {
        if (loginMember != null) {
            model.addAttribute("member", loginMember);
        }
        return "home";
    }
}
