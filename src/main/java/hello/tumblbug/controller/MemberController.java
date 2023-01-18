package hello.tumblbug.controller;

import hello.tumblbug.controller.form.MemberEditForm;
import hello.tumblbug.domain.Member;
import hello.tumblbug.dto.SimpleProjectDto;
import hello.tumblbug.file.FileStore;
import hello.tumblbug.service.MemberService;
import hello.tumblbug.service.ProjectService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;
    private final ProjectService projectService;
    private final FileStore fileStore;

    @GetMapping("/{memberId}")
    public String profileMain(@PathVariable Long memberId, Model model, @SessionAttribute(value = SessionConst.LOGIN_MEMBER, required = false) Member loginMember) {
        Member member = memberService.findOne(memberId);
        model.addAttribute("member", member);
        followButtonControl(model, loginMember, memberId);
        return "profile/main";
    }

    @GetMapping("/{memberId}/edit")
    public String profileEditForm(@PathVariable Long memberId, Model model, HttpServletResponse response, @SessionAttribute(value = SessionConst.LOGIN_MEMBER) Member loginMember) throws IOException {
        if (!loginMember.getId().equals(memberId)) {
            response.sendError(403);
        }
        Member member = memberService.findOne(memberId);
        MemberEditForm form = new MemberEditForm(null, member.getUsername(), member.getPassword(), member.getInfo());
        model.addAttribute("form", form);
        return "profile/editForm";
    }

    @PostMapping("/{memberId}/edit")
    public String profileEdit(@PathVariable Long memberId, @Valid @ModelAttribute("form") MemberEditForm form, BindingResult bindingResult, @SessionAttribute(SessionConst.LOGIN_MEMBER) Member loginMember, HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (!loginMember.getId().equals(memberId)) {
            response.sendError(403);
            return null;
        }

        if (bindingResult.hasErrors()) {
            return "profile/editForm";
        }

        Member member = memberService.updateMember(memberId, fileStore.storeFile(form.getUserImage()), form.getUsername(), form.getPassword(), form.getInfo());
        HttpSession session = request.getSession(false);
        session.setAttribute(SessionConst.LOGIN_MEMBER, member);

        return "redirect:/member/{memberId}";
    }

    @GetMapping("/{memberId}/createdProject")
    public String createdProjectList(@PathVariable Long memberId, Model model, @SessionAttribute(value = SessionConst.LOGIN_MEMBER, required = false) Member loginMember) {
        Member member = memberService.findOne(memberId);
        List<SimpleProjectDto> createdProject = projectService.findCreatedProject(memberId);
        List<List<SimpleProjectDto>> projectGrid = ProjectController.makeGrid(createdProject, 4);
        model.addAttribute("member", member);
        model.addAttribute("projectNum", createdProject.size());
        model.addAttribute("projectGrid", projectGrid);
        followButtonControl(model, loginMember, memberId);
        return "profile/createdProjects";
    }

    @GetMapping("/{memberId}/backedProject")
    public String sponsoredProjectList(@PathVariable Long memberId, Model model, @SessionAttribute(value = SessionConst.LOGIN_MEMBER, required = false) Member loginMember) {
        Member member = memberService.findOne(memberId);
        List<SimpleProjectDto> sponsoredProject = projectService.findSponsoredProject(memberId);
        List<List<SimpleProjectDto>> projectGrid = ProjectController.makeGrid(sponsoredProject, 4);
        model.addAttribute("member", member);
        model.addAttribute("projectNum", sponsoredProject.size());
        model.addAttribute("projectGrid", projectGrid);
        followButtonControl(model, loginMember, memberId);
        return "profile/backedProjects";
    }

    @GetMapping("/{memberId}/follower")
    public String followerList(@PathVariable Long memberId, Model model, @SessionAttribute(value = SessionConst.LOGIN_MEMBER, required = false) Member loginMember) {
        Member member = memberService.findOne(memberId);
        model.addAttribute("member", member);
        followButtonControl(model, loginMember, memberId);
        return "profile/follower";
    }

    @GetMapping("/{memberId}/following")
    public String followingList(@PathVariable Long memberId, Model model, @SessionAttribute(value = SessionConst.LOGIN_MEMBER, required = false) Member loginMember) {
        Member member = memberService.findOne(memberId);
        model.addAttribute("member", member);
        followButtonControl(model, loginMember, memberId);
        return "profile/following";
    }

    @PostMapping("/{memberId}/follow")
    public String followMember(@PathVariable Long memberId, @SessionAttribute(value = SessionConst.LOGIN_MEMBER, required = false) Member loginMember, @RequestParam(defaultValue = "/") String redirectURI, HttpServletResponse response) throws IOException {
        if (loginMember == null) {
            return "redirect:/login?redirectURI=" + redirectURI;
        }
        if (loginMember.getId().equals(memberId)) {
            response.sendError(404); //self-following not allowed
            return null;
        }
        memberService.followOrStopFollowingMember(loginMember.getId(), memberId);
        return "redirect:" + redirectURI;
    }

    public void followButtonControl(Model model, Member loginMember, Long profileMemberId) {
        if (loginMember == null) {
            model.addAttribute("followButtonActive", true);
        } else if (loginMember.getId().equals(profileMemberId)) {
        } else {
            boolean follows = memberService.follows(loginMember.getId(), profileMemberId);
            if (follows) {
                model.addAttribute("followingButtonActive", true);
            } else {
                model.addAttribute("followButtonActive", true);
            }
        }
    }
}
