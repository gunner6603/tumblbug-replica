package hello.tumblbug.controller.form;

import hello.tumblbug.domain.Category;
import hello.tumblbug.domain.DBConst;
import lombok.Data;
import org.hibernate.validator.constraints.Range;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class ProjectUploadForm {

    @Size(min = 5, max = DBConst.PROJECT_TITLE_MAX_LENGTH)
    private String title;

    @NotNull
    private Category category;

    private MultipartFile mainImage;

    @Size(min = 0, max = 3)
    private List<MultipartFile> subImages = new ArrayList<>();

    @Size(min = 10, max = DBConst.PROJECT_DESCRIPTION_MAX_LENGTH)
    private String description;

    @Range(min = 1000, max = 100000000)
    private int targetSponsorship;

    @Range(min = 1000, max = 100000000)
    private int reward1Price;

    @Size(min = 10, max = DBConst.REWARD_DESCRIPTION_MAX_LENGTH)
    private String reward1Description;

    @Range(min = 1000, max = 100000000)
    private int reward2Price;

    @Size(min = 10, max = DBConst.REWARD_DESCRIPTION_MAX_LENGTH)
    private String reward2Description;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate deadlineDate;
}
