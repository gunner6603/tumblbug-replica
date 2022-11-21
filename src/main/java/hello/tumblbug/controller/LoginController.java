package hello.tumblbug.controller;

import hello.tumblbug.controller.form.LoginForm;
import hello.tumblbug.controller.form.SignupForm;
import hello.tumblbug.domain.Member;
import hello.tumblbug.service.LoginService;
import hello.tumblbug.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;
    private final MemberService memberService;

    @GetMapping("/signup")
    public String signupForm(@ModelAttribute("signupForm") SignupForm form) {
        return "login/signupForm";
    }

    @PostMapping("/signup")
    public String signup(@Valid @ModelAttribute("signupForm") SignupForm form, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "login/signupForm";
        }
        Long memberId = memberService.join(form.getUsername(), form.getLoginId(), form.getPassword());
        if (memberId == null) {
            bindingResult.reject("duplicateLoginId");
            return "login/signupForm";
        }
        return "redirect:/";
    }


    @GetMapping("/login")
    public String loginForm(@ModelAttribute("loginForm") LoginForm form) {
        return "login/loginForm";
    }

    @PostMapping("/login")
    public String login(@Valid @ModelAttribute("loginForm") LoginForm form, BindingResult bindingResult, HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            return "login/loginForm";
        }
        Member loginMember = loginService.login(form.getLoginId(), form.getPassword());
        if (loginMember == null) {
            bindingResult.reject("idPasswordMismatch");
            return "login/loginForm";
        }
        HttpSession session = request.getSession(true);
        session.setAttribute(SessionConst.LOGIN_MEMBER, loginMember);
        return "redirect:/";
    }

    @PostMapping("/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return "redirect:/";
    }
}
