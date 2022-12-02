package hello.tumblbug.controller;

import hello.tumblbug.domain.Category;
import hello.tumblbug.dto.PagingDto;
import hello.tumblbug.dto.PagingQueryDto;
import hello.tumblbug.dto.SimpleProjectDto;
import hello.tumblbug.service.ProjectService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/project/category")
public class CategoryController {

    private final ProjectService projectService;

    @GetMapping("/BOOK")
    public String categoryBook(@ModelAttribute PagingQueryDto queryDto, Model model) {
        PagingDto<SimpleProjectDto> pagingDto = projectService.findByCategory(Category.BOOK, queryDto);
        List<List<SimpleProjectDto>> projectGrid = ProjectController.makeGrid(pagingDto.getElements(), 4);
        model.addAttribute("pagingDto", pagingDto);
        model.addAttribute("projectGrid", projectGrid);
        return "project/list";
    }

    @GetMapping("/FILM")
    public String categoryFilm(@ModelAttribute PagingQueryDto queryDto, Model model) {
        PagingDto<SimpleProjectDto> pagingDto = projectService.findByCategory(Category.FILM, queryDto);
        List<List<SimpleProjectDto>> projectGrid = ProjectController.makeGrid(pagingDto.getElements(), 4);
        model.addAttribute("pagingDto", pagingDto);
        model.addAttribute("projectGrid", projectGrid);
        return "project/list";
    }

    @GetMapping("/GOODS")
    public String categoryGoods(@ModelAttribute PagingQueryDto queryDto, Model model) {
        PagingDto<SimpleProjectDto> pagingDto = projectService.findByCategory(Category.GOODS, queryDto);
        List<List<SimpleProjectDto>> projectGrid = ProjectController.makeGrid(pagingDto.getElements(), 4);
        model.addAttribute("pagingDto", pagingDto);
        model.addAttribute("projectGrid", projectGrid);
        return "project/list";
    }
}
