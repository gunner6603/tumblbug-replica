package hello.tumblbug.controller.form;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
public class SignupForm {

    @NotEmpty
    @Size(min = 2, max = 6)
    private String username;

    @NotEmpty
    @Size(min = 6, max = 12)
    private String loginId;

    @NotEmpty
    @Size(min = 6, max = 12)
    private String password;
}
