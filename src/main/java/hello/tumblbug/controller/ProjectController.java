package hello.tumblbug.controller;

import hello.tumblbug.controller.form.CommunityPostForm;
import hello.tumblbug.controller.form.ProjectUploadForm;
import hello.tumblbug.domain.*;
import hello.tumblbug.dto.PagingDto;
import hello.tumblbug.dto.PagingQueryDto;
import hello.tumblbug.dto.ProjectUploadDto;
import hello.tumblbug.dto.SimpleProjectDto;
import hello.tumblbug.file.FileStore;
import hello.tumblbug.file.UploadFile;
import hello.tumblbug.service.MemberService;
import hello.tumblbug.service.ProjectService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.net.MalformedURLException;
import java.time.Duration;
import java.time.LocalDate;
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
    private final MemberService memberService;

    @GetMapping("/add")
    public String uploadForm(@ModelAttribute("form") ProjectUploadForm form, Model model) {
        model.addAttribute("categories", Category.values());
        return "project/uploadForm";
    }

    @PostMapping("/add")
    public String upload(@Valid @ModelAttribute("form") ProjectUploadForm form, BindingResult bindingResult, @SessionAttribute(value = SessionConst.LOGIN_MEMBER) Member loginMember, RedirectAttributes redirectAttributes, Model model) throws IOException {

        if (form.getMainImage().isEmpty()) {
            bindingResult.rejectValue("mainImage", "empty");
        }

        if (form.getDeadlineDate() != null && form.getDeadlineDate().isBefore(LocalDate.now())) {
            bindingResult.rejectValue("deadlineDate", "passedDate");
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("categories", Category.values());
            return "project/uploadForm";
        }

        UploadFile mainImage = fileStore.storeFile(form.getMainImage());
        List<UploadFile> subImages = fileStore.storeFiles(form.getSubImages());

        Reward reward1 = new Reward(form.getReward1Price(), form.getReward1Description());
        Reward reward2 = new Reward(form.getReward2Price(), form.getReward2Description());

        ProjectUploadDto projectUploadDto = new ProjectUploadDto(form.getTitle(), form.getCategory(), loginMember, mainImage, subImages, form.getDescription(), form.getTargetSponsorship(), reward1, reward2, form.getDeadlineDate().atTime(23, 59, 59));

        Long projectId = projectService.createProject(projectUploadDto);
        redirectAttributes.addAttribute("projectId", projectId);

        return "redirect:/project/{projectId}";
    }

    @GetMapping("/{projectId}")
    public String projectDetail(@PathVariable Long projectId, Model model, @SessionAttribute(value = SessionConst.LOGIN_MEMBER, required = false) Member loginMember) {
        Project project = projectService.findOneFetchCreatorAndReward(projectId);
        model.addAttribute("project", project);
        addFollowButtonInfoToModel(model, loginMember, project);
        addProjectInfoToModel(model, project);
        return "project/detail/main";
    }

    private void addProjectInfoToModel(Model model, Project project) {
        long leftDays = Duration.between(LocalDateTime.now(), project.getDeadline()).toDays();
        if (leftDays < 0) {
            leftDays = 0;
        }
        model.addAttribute("leftDays", leftDays);
        model.addAttribute("achievementRate", project.getAchievementRate());
    }

    private void addFollowButtonInfoToModel(Model model, Member loginMember, Project project) {
        if (loginMember == null) {
            model.addAttribute("followButtonActive", true);
        } else if (loginMember.getId().equals(project.getCreator().getId())) {
        } else {
            boolean follows = memberService.follows(loginMember.getId(), project.getCreator().getId());
            if (follows) {
                model.addAttribute("followingButtonActive", true);
            } else {
                model.addAttribute("followButtonActive", true);
            }
        }
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
    public String popularProjects(@ModelAttribute PagingQueryDto queryDto, Model model) {
        PagingDto<SimpleProjectDto> pagingDto = projectService.findMostPopularByPagingDto(queryDto);
        List<List<SimpleProjectDto>> projectGrid = makeGrid(pagingDto.getElements(), 4);
        model.addAttribute("pagingDto", pagingDto);
        model.addAttribute("projectGrid", projectGrid);
        return "project/list";
    }

    @GetMapping("/new")
    public String newProjects(@ModelAttribute PagingQueryDto queryDto, Model model) {
        PagingDto<SimpleProjectDto> pagingDto = projectService.findLatestByPagingDto(queryDto);
        List<List<SimpleProjectDto>> projectGrid = makeGrid(pagingDto.getElements(), 4);
        model.addAttribute("pagingDto", pagingDto);
        model.addAttribute("projectGrid", projectGrid);
        return "project/list";
    }

    @GetMapping("/imminent")
    public String imminentProjects(@ModelAttribute PagingQueryDto queryDto, Model model) {
        PagingDto<SimpleProjectDto> pagingDto = projectService.findMostImminentByPagingDto(queryDto);
        List<List<SimpleProjectDto>> projectGrid = makeGrid(pagingDto.getElements(), 4);
        model.addAttribute("pagingDto", pagingDto);
        model.addAttribute("projectGrid", projectGrid);
        return "project/list";
    }

    @PostMapping("/{projectId}/sponsor/{rewardId}")
    public String sponsorProject(@PathVariable Long projectId, @PathVariable Long rewardId, @SessionAttribute(value = SessionConst.LOGIN_MEMBER, required = false) Member loginMember) throws IOException {
        if (loginMember == null) {
            return "redirect:/login?redirectURI=/project/{projectId}#option";
        }
        Project project = projectService.findOne(projectId);
        if (LocalDateTime.now().isAfter(project.getDeadline())) {
            return "redirect:/project/{projectId}";
        }
        projectService.sponsorProject(loginMember.getId(), projectId, rewardId);
        return "redirect:/project/{projectId}";
    }

    @GetMapping("/search")
    public String searchProject(@RequestParam(defaultValue = "") String query, @ModelAttribute PagingQueryDto queryDto, Model model) {
        PagingDto<SimpleProjectDto> pagingDto = projectService.search(queryDto, query);
        List<List<SimpleProjectDto>> projectGrid = makeGrid(pagingDto.getElements(), 4);
        model.addAttribute("pagingDto", pagingDto);
        model.addAttribute("projectGrid", projectGrid);
        model.addAttribute("isSearchResultList", true);
        return "project/list";
    }


    @GetMapping("/{projectId}/community")
    public String communityPostList(@PathVariable Long projectId, Model model, @SessionAttribute(value = SessionConst.LOGIN_MEMBER, required = false) Member loginMember) {
        Project project = projectService.findOneFetchCreatorAndReward(projectId);
        List<CommunityPost> communityPosts = projectService.findCommunityPosts(projectId);
        model.addAttribute("project", project);
        model.addAttribute("communityPosts", communityPosts);
        addFollowButtonInfoToModel(model, loginMember, project);
        addProjectInfoToModel(model, project);
        return "project/detail/communityPostList";
    }

    @GetMapping("/{projectId}/community/add")
    public String communityPostAddForm(@PathVariable Long projectId, @ModelAttribute("form") CommunityPostForm form, Model model) {
        Project project = projectService.findOne(projectId);
        model.addAttribute("project", project);
        addProjectInfoToModel(model, project);
        return "project/detail/communityPostAddForm";
    }

    @PostMapping("/{projectId}/community/add")
    public String addCommunityPost(@PathVariable Long projectId, @SessionAttribute(value = SessionConst.LOGIN_MEMBER) Member loginMember, @Valid @ModelAttribute("form") CommunityPostForm form, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            Project project = projectService.findOne(projectId);
            model.addAttribute("project", project);
            addProjectInfoToModel(model, project);
            return "project/detail/communityPostAddForm";
        }
        projectService.addCommunityPost(projectId, loginMember.getId(), form.getContent());
        return "redirect:/project/{projectId}/community";
    }

    @PostMapping("/{projectId}/community/{postId}/delete")
    public String deleteCommunityPost(@PathVariable Long postId, @SessionAttribute(value = SessionConst.LOGIN_MEMBER, required = false) Member loginMember, HttpServletResponse response) throws IOException {
        CommunityPost communityPost = projectService.findCommunityPost(postId);
        if (loginMember == null || !loginMember.getId().equals(communityPost.getAuthor().getId())) {
            response.sendError(403);
            return null;
        } else {
            projectService.deleteCommunityPost(postId);
        }
        return "redirect:/project/{projectId}/community";
    }


    public static <T> List<List<T>> makeGrid(List<T> sequence, int colSize) {
        List<List<T>> projectGrid = new ArrayList<>();
        List<T> gridRow = new ArrayList<>();
        ;
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
