package hello.tumblbug.controller.form;

import hello.tumblbug.domain.Category;
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

    @NotEmpty
    @Size(max = 40)
    private String title;

    @NotNull
    private Category category;

    @NotNull
    private MultipartFile mainImage;

    private List<MultipartFile> subImages = new ArrayList<>();

    @NotEmpty
    @Size(min = 10, max = 2000)
    private String description;

    @Range(min = 1000, max = 100000000)
    private int targetSponsorship;

    @Range(min = 1000, max = 100000000)
    private int reward1Price;

    @NotEmpty
    @Size(max = 500)
    private String reward1Description;

    @Range(min = 1000, max = 100000000)
    private int reward2Price;

    @NotEmpty
    @Size(max = 500)
    private String reward2Description;

    @NotEmpty
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate deadlineDate;
}
