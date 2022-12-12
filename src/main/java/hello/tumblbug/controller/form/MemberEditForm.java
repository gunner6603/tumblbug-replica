package hello.tumblbug.controller.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
public class MemberEditForm {

    private MultipartFile userImage;

    @Size(min = 2, max = 8)
    private String username;

    @Size(min = 6, max = 12)
    private String password;

    @Size(min = 0, max = 1000)
    private String info;
}