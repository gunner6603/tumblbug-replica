package hello.tumblbug.controller.form;

import hello.tumblbug.domain.DBConst;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
public class MemberEditForm {

    private MultipartFile userImage;

    @Size(min = 2, max = DBConst.MEMBER_USERNAME_MAX_LENGTH)
    private String username;

    @Size(min = 6, max = DBConst.MEMBER_PASSWORD_MAX_LENGTH)
    private String password;

    @Size(min = 0, max = DBConst.MEMBER_INFO_MAX_LENGTH)
    private String info;
}