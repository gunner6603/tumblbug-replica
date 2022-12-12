package hello.tumblbug.controller.form;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
public class SignupForm {

    @Size(min = 2, max = 8)
    private String username;

    @Size(min = 6, max = 12)
    private String loginId;

    @Size(min = 6, max = 12)
    private String password;
}
