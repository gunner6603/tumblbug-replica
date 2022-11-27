package hello.tumblbug.controller.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
public class MemberEditForm {

    private MultipartFile userImage;

    private String username;

    private String password;

    private String info;
}
