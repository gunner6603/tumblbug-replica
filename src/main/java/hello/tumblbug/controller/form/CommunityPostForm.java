package hello.tumblbug.controller.form;

import lombok.Data;

import javax.validation.constraints.Size;

@Data
public class CommunityPostForm {

    @Size(min = 5, max = 1000)
    private String content;
}
