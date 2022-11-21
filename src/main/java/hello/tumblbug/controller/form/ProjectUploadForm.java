package hello.tumblbug.controller.form;

import hello.tumblbug.domain.Category;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class ProjectUploadForm {

    private String title;

    private Category category;

    private MultipartFile mainImage;

    private List<MultipartFile> subImages = new ArrayList<>();

    private String description;

    private int reward1Price;

    private String reward1Description;

    private int reward2Price;

    private String reward2Description;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime deadline;
}
