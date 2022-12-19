package hello.tumblbug.controller.form;

import hello.tumblbug.domain.DBConst;
import lombok.Data;

import javax.validation.constraints.Size;

@Data
public class CommunityPostForm {

    @Size(min = 5, max = DBConst.COMMUNITY_POST_CONTENT_MAX_LENGTH)
    private String content;
}
