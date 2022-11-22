package hello.tumblbug.dto;

import hello.tumblbug.domain.Category;
import hello.tumblbug.domain.Member;
import hello.tumblbug.domain.Reward;
import hello.tumblbug.file.UploadFile;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
public class ProjectUploadDto {

    private String title;

    private Category category;

    private Member creator;

    private UploadFile mainImage;

    private List<UploadFile> subImages;

    private String description;

    private int targetSponsorship;

    private Reward reward1;

    private Reward reward2;

    private LocalDateTime deadline;
}
