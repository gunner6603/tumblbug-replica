package hello.tumblbug.init;

import hello.tumblbug.domain.Category;
import hello.tumblbug.domain.Member;
import hello.tumblbug.domain.Project;
import hello.tumblbug.domain.Reward;
import hello.tumblbug.file.UploadFile;
import hello.tumblbug.repository.MemberRepository;
import hello.tumblbug.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;

@Controller
@RequiredArgsConstructor
public class Init {

    private final MemberRepository memberRepository;
    private final ProjectRepository projectRepository;

    @GetMapping("/init")
    public String init() {

        for (int i = 1; i <= 100; i++) {
            Member member = new Member("테스트 멤버" + i, "test" + i, "test" + i + "!");
            member.setInfo("테스트 문자열 테스트 문자열 테스트 문자열 테스트 문자열 테스트 문자열 테스트 문자열 테스트 문자열 테스트 문자열 테스트 문자열 테스트 문자열 테스트 문자열 테스트 문자열 테스트 문자열 테스트 문자열 테스트 문자열 테스트 문자열 테스트 문자열 테스트 문자열 테스트 문자열 테스트 문자열 테스트 문자열 ");
            memberRepository.save(member);
            String title = "테스트 프로젝트" + i + " 테스트 프로젝트" + i + " 테스트 프로젝트" + i;
            Category category = Category.BOOK;
            switch (i % 3) {
                case 0:
                    category = Category.BOOK;
                    break;
                case 1:
                    category = Category.FILM;
                    break;
                case 2:
                    category = Category.GOODS;
                    break;
            }
            UploadFile mainImage = new UploadFile("perfume.png", "08ec9357-3ad7-4884-9f23-df86949e941d.png");
            int targetSponsorship = 1000000;
            String description = "테스트 프로젝트 내용 테스트 프로젝트 내용 테스트 프로젝트 내용 테스트 프로젝트 내용 테스트 프로젝트 내용 테스트 프로젝트 내용 테스트 프로젝트 내용 테스트 프로젝트 내용 테스트 프로젝트 내용 테스트 프로젝트 내용 테스트 프로젝트 내용";
            LocalDateTime deadline = LocalDateTime.now().plusMonths(2);
            Reward reward1 = new Reward(10000, "리워드1");
            Reward reward2 = new Reward(20000, "리워드2");
            Project project = new Project(title, category, member, mainImage, targetSponsorship, description, deadline, reward1, reward2);
            projectRepository.save(project);
        }


//        Member member = new Member("테스트 멤버", "test", "test");
//        memberRepository.save(member);
//
//        for (int i = 0; i < 10; i++) {
//            String title = "테스트 프로젝트" + i + " 테스트 프로젝트" + i + " 테스트 프로젝트" + i;
//            Category category = Category.GOODS;
//            UploadFile mainImage = new UploadFile("perfume.png", "08ec9357-3ad7-4884-9f23-df86949e941d.png");
//            int targetSponsorship = 1000000 * (i + 1);
//            String description = "테스트 프로젝트 내용 테스트 프로젝트 내용 테스트 프로젝트 내용 테스트 프로젝트 내용 테스트 프로젝트 내용 테스트 프로젝트 내용 테스트 프로젝트 내용 테스트 프로젝트 내용 테스트 프로젝트 내용 테스트 프로젝트 내용 테스트 프로젝트 내용";
//            LocalDateTime deadline = LocalDateTime.now().plusMonths(2);
//            Reward reward1 = new Reward(10000, "리워드1");
//            Reward reward2 = new Reward(20000, "리워드2");
//            Project project = new Project(title, category, member, mainImage, targetSponsorship, description, deadline, reward1, reward2);
//            projectRepository.save(project);
//        }
        return "redirect:/";
    }
}
