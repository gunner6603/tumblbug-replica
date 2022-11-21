package hello.tumblbug.controller;

import hello.tumblbug.controller.form.ProjectUploadForm;
import hello.tumblbug.domain.Category;
import hello.tumblbug.domain.Member;
import hello.tumblbug.domain.Project;
import hello.tumblbug.domain.Reward;
import hello.tumblbug.dto.ProjectUploadDto;
import hello.tumblbug.file.FileStore;
import hello.tumblbug.file.UploadFile;
import hello.tumblbug.service.ProjectService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
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
    public String upload(@ModelAttribute("form") ProjectUploadForm form, @SessionAttribute(value = SessionConst.LOGIN_MEMBER, required = false) Member loginMember, RedirectAttributes redirectAttributes) throws IOException {
        log.info("form={}", form);

        UploadFile mainImage = fileStore.storeFile(form.getMainImage());
        List<UploadFile> subImages = fileStore.storeFiles(form.getSubImages());

        Reward reward1 = new Reward(form.getReward1Price(), form.getReward1Description());
        Reward reward2 = new Reward(form.getReward2Price(), form.getReward2Description());

        ProjectUploadDto projectUploadDto = new ProjectUploadDto(form.getTitle(), form.getCategory(), loginMember, mainImage, subImages, form.getDescription(), reward1, reward2, form.getDeadline());

        Long projectId = projectService.createProject(projectUploadDto);
        redirectAttributes.addAttribute("projectId", projectId);

        return "redirect:/project/{projectId}";
    }

    @GetMapping("/{projectId}")
    public String projectDetail(@PathVariable Long projectId, Model model) {
        Project project = projectService.findOne(projectId);
        model.addAttribute("project", project);
        return "project/detail";
    }
}
