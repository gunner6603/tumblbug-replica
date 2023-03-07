package hello.tumblbug.interceptor;

import hello.tumblbug.controller.SessionConst;
import hello.tumblbug.domain.Member;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class NavProfileInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

        HttpSession session = request.getSession(false);

        if (session != null && session.getAttribute(SessionConst.LOGIN_MEMBER) != null) {
            Member loginMember = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);
            request.setAttribute(SessionConst.LOGGED_IN, true);
            request.setAttribute(SessionConst.LOGIN_MEMBER_USERNAME, loginMember.getUsername());
            request.setAttribute(SessionConst.LOGIN_MEMBER_ID, loginMember.getId());
            request.setAttribute(SessionConst.LOGIN_MEMBER_IMAGE_STORE_FILENAME, loginMember.getUserImage().getStoreFileName());
        }

        return true;
    }
}
