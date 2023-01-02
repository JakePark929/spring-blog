package com.jake.blog.controller;

import com.jake.blog.service.BoardService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class BoardController {
    private final BoardService boardService;

    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    @GetMapping({"","/"})
    public String index(
//            @AuthenticationPrincipal PrincipalDetail principal
            Model model,
            @PageableDefault(size=3, sort="id", direction = Sort.Direction.DESC) Pageable pageable
    ) {
//        System.out.println("principal.getUsername() = " + principal.getUsername());
        model.addAttribute("boards", boardService.listBoard(pageable));
        return "index"; // viewResolver 작동! model 을 들고감
    }

    @GetMapping("/board/{id}")
    public String findById(@PathVariable Long id, Model model) {
        model.addAttribute("board", boardService.detailBoard(id));
        return "board/detail";
    }

    @GetMapping("/board/saveForm")
    public String saveFrom() {
        return "board/saveForm";
    }

    @GetMapping("/board/{id}/updateForm")
    public String updateForm(@PathVariable Long id, Model model) {
        model.addAttribute("board", boardService.detailBoard(id));
        return "board/updateForm";
    }
}
