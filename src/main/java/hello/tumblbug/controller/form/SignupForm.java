package hello.tumblbug.controller.form;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class SignupForm {

    @NotEmpty
    private String username;

    @NotEmpty
    private String loginId;

    @NotEmpty
    private String password;
}
