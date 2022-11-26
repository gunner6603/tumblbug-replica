package hello.tumblbug.controller;

import hello.tumblbug.controller.form.ProjectUploadForm;
import hello.tumblbug.domain.Category;
import hello.tumblbug.domain.Member;
import hello.tumblbug.domain.Project;
import hello.tumblbug.domain.Reward;
import hello.tumblbug.dto.ProjectUploadDto;
import hello.tumblbug.dto.SimpleProjectDto;
import hello.tumblbug.file.FileStore;
import hello.tumblbug.file.UploadFile;
import hello.tumblbug.service.ProjectService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.net.MalformedURLException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/project")
public class ProjectController {

    private final ProjectService projectService;
    private final FileStore fileStore;

    @GetMapping("/add")
    public String uploadForm(@ModelAttribute("form") ProjectUploadForm form, Model model) {
        model.addAttribute("categories", Category.values());
        return "project/uploadForm";
    }

    @PostMapping("/add")
    public String upload(@ModelAttribute("form") ProjectUploadForm form, @SessionAttribute(value = SessionConst.LOGIN_MEMBER) Member loginMember, RedirectAttributes redirectAttributes) throws IOException {
        log.info("form={}", form);

        UploadFile mainImage = fileStore.storeFile(form.getMainImage());
        List<UploadFile> subImages = fileStore.storeFiles(form.getSubImages());

        Reward reward1 = new Reward(form.getReward1Price(), form.getReward1Description());
        Reward reward2 = new Reward(form.getReward2Price(), form.getReward2Description());

        ProjectUploadDto projectUploadDto = new ProjectUploadDto(form.getTitle(), form.getCategory(), loginMember, mainImage, subImages, form.getDescription(), form.getTargetSponsorship(), reward1, reward2, form.getDeadline());

        Long projectId = projectService.createProject(projectUploadDto);
        redirectAttributes.addAttribute("projectId", projectId);

        return "redirect:/project/{projectId}";
    }

    @GetMapping("/{projectId}")
    public String projectDetail(@PathVariable Long projectId, Model model) {
        Project project = projectService.findOne(projectId);
        long leftDays = Duration.between(LocalDateTime.now(), project.getDeadline()).toDays();
        int achievementRate = project.getCurrentSponsorship() * 100 / project.getTargetSponsorship();
        model.addAttribute("project", project);
        model.addAttribute("leftDays", leftDays);
        model.addAttribute("achievementRate", achievementRate);
        return "project/detail";
    }

    @ResponseBody
    @GetMapping("/images/{filename}")
    public Resource downloadImage(@PathVariable String filename) throws
            MalformedURLException {
        return new UrlResource("file:" + fileStore.getFullPath(filename));
    }

    @GetMapping("/home")
    public String home(Model model) {
        List<SimpleProjectDto> latestProjects = projectService.findLatestN(16);
        List<List<SimpleProjectDto>> projectGrid = makeGrid(latestProjects, 4);
        List<SimpleProjectDto> popularProjects = projectService.findMostPopularN(8);
        String timeNow = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yy.MM.dd HH:mm"));

        model.addAttribute("projectGrid", projectGrid);
        model.addAttribute("popularProjects", popularProjects);
        model.addAttribute("timeNow", timeNow);

        return "project/home";
    }

    @GetMapping("/popular")
    public String popularProjects() {
        return "project/list";
    }

    @GetMapping("/new")
    public String newProjects() {
        return "project/list";
    }

    @GetMapping("/imminent")
    public String imminentProjects() {
        return "project/list";
    }

    public static <T> List<List<T>> makeGrid(List<T> sequence, int colSize) {
        List<List<T>> projectGrid = new ArrayList<>();
        List<T> gridRow = new ArrayList<>();;
        for (int i = 0; i < sequence.size(); i++) {
            gridRow.add(sequence.get(i));
            if (i % colSize == colSize - 1) {
                projectGrid.add(gridRow);
                gridRow = new ArrayList<>();
            }
        }
        if (sequence.size() % colSize != 0) {
            projectGrid.add(gridRow);
        }
        return projectGrid;
    }
}
