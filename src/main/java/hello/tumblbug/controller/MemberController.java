package hello.tumblbug.controller;

import hello.tumblbug.controller.form.MemberEditForm;
import hello.tumblbug.domain.Member;
import hello.tumblbug.domain.Project;
import hello.tumblbug.dto.SimpleProjectDto;
import hello.tumblbug.repository.MemberRepository;
import hello.tumblbug.service.MemberService;
import hello.tumblbug.service.ProjectService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;
    private final ProjectService projectService;

    @GetMapping("/{memberId}")
    public String profileMain(@PathVariable Long memberId, Model model) {
        Member member = memberService.findOne(memberId);
        model.addAttribute("member", member);
        return "profile/main";
    }

    @GetMapping("/{memberId}/edit")
    public String profileEditForm(@PathVariable Long memberId, Model model) {
        Member member = memberService.findOne(memberId);
        MemberEditForm form = new MemberEditForm(member.getUsername(), member.getPassword(), member.getInfo());
        model.addAttribute("form", form);
        return "profile/editForm";
    }

    @PostMapping("/{memberId}/edit")
    public String profileEdit(@PathVariable Long memberId, @ModelAttribute("form") MemberEditForm form, HttpServletRequest request) {
        Member member = memberService.updateMember(memberId, form.getUsername(), form.getPassword(), form.getInfo());
        HttpSession session = request.getSession(false);
        session.setAttribute(SessionConst.LOGIN_MEMBER, member);
        return "redirect:/member/{memberId}";
    }

    @GetMapping("/{memberId}/createdProject")
    public String createdProjectList(@PathVariable Long memberId, Model model) {
        Member member = memberService.findOne(memberId);
        List<SimpleProjectDto> createdProject = projectService.findCreatedProject(memberId);
        List<List<SimpleProjectDto>> projectGrid = ProjectController.makeGrid(createdProject, 4);
        model.addAttribute("member", member);
        model.addAttribute("projectNum", createdProject.size());
        model.addAttribute("projectGrid", projectGrid);
        return "profile/createdProjects";
    }
}
