package gradu.gradu.controller;

import gradu.gradu.dto.BoardDTO;
import gradu.gradu.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.IOException;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @GetMapping("/write")
    public String saveForm(){
        return "/write";
    }

    @PostMapping("/write")
    public String save(BoardDTO boardDTO) throws IOException {
        boardService.save(boardDTO);
        return "redirect:/bbs";
    }

    @GetMapping("/bbs")
    public String listForm(Model model) {
        List<BoardDTO> boardDTOS = boardService.findAll();
        model.addAttribute("boardList", boardDTOS);
        return "bbs";
    }

}
