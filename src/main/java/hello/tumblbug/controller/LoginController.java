package hello.tumblbug.controller;

import hello.tumblbug.controller.form.LoginForm;
import hello.tumblbug.controller.form.SignupForm;
import hello.tumblbug.domain.Member;
import hello.tumblbug.service.LoginService;
import hello.tumblbug.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;

@Slf4j
@Controller
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;
    private final MemberService memberService;

    @GetMapping("/signup")
    public String signupForm(@ModelAttribute("signupForm") SignupForm form, HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (request.getSession(false) != null) {
            response.sendError(400);
        }
        return "login/signupForm";
    }

    @PostMapping("/signup")
    public String signup(@Valid @ModelAttribute("signupForm") SignupForm form, BindingResult bindingResult, @RequestParam(defaultValue = "/") String redirectURI, HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (request.getSession(false) != null) {
            response.sendError(400);
        }
        if (bindingResult.hasErrors()) {
            return "login/signupForm";
        }
        Long memberId = memberService.join(form.getUsername(), form.getLoginId(), form.getPassword());
        if (memberId == null) {
            bindingResult.reject("duplicateLoginId");
            return "login/signupForm";
        }
        return "redirect:/login?redirectURI=" + redirectURI;
    }


    @GetMapping("/login")
    public String loginForm(@ModelAttribute("loginForm") LoginForm form, HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (request.getSession(false) != null) {
            response.sendError(400);
        }
        return "login/loginForm";
    }

    @PostMapping("/login")
    public String login(@Valid @ModelAttribute("loginForm") LoginForm form, BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response, @RequestParam(defaultValue = "/") String redirectURI) throws IOException {
        if (request.getSession(false) != null) {
            response.sendError(400);
        }
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
        log.info("loginMemberId={}", loginMember.getId());
        return "redirect:" + redirectURI;
    }

    @PostMapping("/logout")
    public String logout(HttpServletRequest request, @RequestParam(defaultValue = "/") String redirectURI) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return "redirect:" + redirectURI;
    }
}
