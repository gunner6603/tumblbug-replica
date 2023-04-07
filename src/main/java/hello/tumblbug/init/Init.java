package hello.tumblbug.init;

import hello.tumblbug.domain.*;
import hello.tumblbug.file.UploadFile;
import hello.tumblbug.repository.CommunityPostRepository;
import hello.tumblbug.repository.MemberProjectRepository;
import hello.tumblbug.repository.MemberRepository;
import hello.tumblbug.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class Init {

    private final MemberRepository memberRepository;
    private final ProjectRepository projectRepository;
    private final CommunityPostRepository communityPostRepository;
    private final MemberProjectRepository memberProjectRepository;

    @GetMapping("/init")
    @Transactional
    public String init() throws InterruptedException {

        //커뮤니티 포스트 게시용 멤버 생성 (4개)
        List<Member> communityMembers = new ArrayList<>();
        for (int i = 1; i < 5; i++) {
            Member member = new Member("커뮤니티멤버" + i, "community" + i, "community" + i + "!");
            memberRepository.save(member);
            communityMembers.add(member);
        }

        List<Member> testMembers = new ArrayList<>();
        List<Project> testProjects = new ArrayList<>();
        List<Reward> firstRewards = new ArrayList<>();
        List<Reward> secondRewards = new ArrayList<>();

        //테스트 멤버 및 프로젝트 생성 (99개씩)
        for (int i = 1; i < 100; i++) {
            Member member = new Member("테스트멤버" + i, "test" + i, "test" + i + "!");
            member.setInfo("테스트멤버" + i +"의 테스트 소개글입니다. 반갑습니다!" );
            memberRepository.save(member);
            testMembers.add(member);

            String title = "테스트 프로젝트" + i + "의 제목입니다.";
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
            UploadFile mainImage = new UploadFile("test-image-perfume-1.png");
            int targetSponsorship = 1000000;
            String description = "테스트 프로젝트" + i +"의 내용입니다.\n" + "테스트 프로젝트" + i +"의 내용입니다.\n" + "테스트 프로젝트" + i +"의 내용입니다.\n" + "테스트 프로젝트" + i +"의 내용입니다.\n" + "테스트 프로젝트" + i +"의 내용입니다.\n" + "테스트 프로젝트" + i +"의 내용입니다.\n" + "테스트 프로젝트" + i +"의 내용입니다.\n" + "테스트 프로젝트" + i +"의 내용입니다.\n" + "테스트 프로젝트" + i +"의 내용입니다.\n" + "테스트 프로젝트" + i +"의 내용입니다.\n" + "테스트 프로젝트" + i +"의 내용입니다.\n";
            LocalDateTime deadline = LocalDateTime.now().plusMonths(1).plusDays(i / 10 * 3 + i % 10 * 7);
            Reward reward1 = new Reward(10000, "테스트 프로젝트" + i + "의 리워드1 내용입니다.");
            Reward reward2 = new Reward(20000, "테스트 프로젝트" + i + "의 리워드2 내용입니다.");
            Project testProject = Project.builder()
                    .title(title)
                    .category(category)
                    .creator(member)
                    .mainImage(mainImage)
                    .targetSponsorship(targetSponsorship)
                    .description(description)
                    .deadline(deadline)
                    .build();
            testProject.addReward(reward1);
            testProject.addReward(reward2);
            projectRepository.save(testProject);
            testProjects.add(testProject);
            firstRewards.add(reward1);
            secondRewards.add(reward2);

            //테스트 커뮤니티 포스트 생성 (프로젝트당 4개씩)
            for (int j = 1; j < 5; j++) {
                Member communityMember = communityMembers.get(j - 1);
                CommunityPost communityPost = new CommunityPost(testProject, communityMember, "커뮤니티 포스트" + j + "의 내용입니다.\n" + "커뮤니티 포스트" + j + "의 내용입니다.\n" + "커뮤니티 포스트" + j + "의 내용입니다.\n");
                communityPostRepository.save(communityPost);
                Thread.sleep(1);
            }
        }

        //프로젝트에 후원내역 생성
        for (int i = 0; i < 99; i++) {
            int sponsor_count = (i % 10) * 3 + i / 10 + 7; //프로젝트당 후원 수를 적당히 조절
            for (int j = 1; j <= sponsor_count; j++) {
                //후원내역을 만들되 중복 후원이 없도록 생성
                Member member = testMembers.get((i + j * 13) % 99);
                Project project = testProjects.get(i);
                Reward reward = firstRewards.get(i);
                MemberProject testMemberProject = new MemberProject(member, project, reward);
                memberProjectRepository.save(testMemberProject);

                project.increaseSponsorCount();
                project.increaseCurrentSponsorship(reward.getPrice());
                reward.increaseSalesCount();
            }

            int second_sponsor_count = (sponsor_count % 10) * 5 + 4; //프로젝트당 후원 수를 적당히 조절
            for (int j = sponsor_count + 1; j <= sponsor_count + second_sponsor_count; j++) {
                //후원내역을 만들되 중복 후원이 없도록 생성
                Member member = testMembers.get((i + j * 13) % 99);
                Project project = testProjects.get(i);
                Reward reward = secondRewards.get(i);
                MemberProject testMemberProject = new MemberProject(member, project, reward);
                memberProjectRepository.save(testMemberProject);

                project.increaseSponsorCount();
                project.increaseCurrentSponsorship(reward.getPrice());
                reward.increaseSalesCount();
            }
        }

        return "redirect:/";
    }
}
