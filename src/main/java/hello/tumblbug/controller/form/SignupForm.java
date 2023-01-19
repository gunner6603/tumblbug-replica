package hello.tumblbug.controller.form;

import hello.tumblbug.domain.DBConst;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
public class SignupForm {

    @Size(min = 2, max = DBConst.MEMBER_USERNAME_MAX_LENGTH)
    private String username;

    @Size(min = 6, max = DBConst.MEMBER_LOGIN_ID_MAX_LENGTH)
    private String loginId;

    @Size(min = 6, max = DBConst.MEMBER_PASSWORD_MAX_LENGTH)
    private String password;
}
