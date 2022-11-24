package hello.tumblbug.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/project/category")
public class CategoryController {

    @GetMapping("/book")
    public String categoryBook() {
        return "project/list";
    }

    @GetMapping("/film")
    public String categoryFilm() {
        return "project/list";
    }

    @GetMapping("/goods")
    public String categoryGoods() {
        return "project/list";
    }
}
