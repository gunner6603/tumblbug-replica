package hello.tumblbug;

import hello.tumblbug.interceptor.LoginInterceptor;
import hello.tumblbug.interceptor.NavProfileInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginInterceptor())
                .order(1)
                .addPathPatterns("/member/{memberId}/edit"); //Get으로 호출 가능한 URL만 등록 -> 팔로우, 후원버튼은 Post만 가능하므로 제외

        registry.addInterceptor(new NavProfileInterceptor())
                .order(2)
                .addPathPatterns("/**");
    }
}
