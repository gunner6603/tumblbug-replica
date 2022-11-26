package hello.tumblbug.controller.form;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MemberEditForm {

    private String username;

    private String password;

    private String info;
}
