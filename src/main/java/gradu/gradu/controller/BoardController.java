package gradu.gradu.controller;

import gradu.gradu.domain.Board;
import gradu.gradu.dto.BoardDTO;
import gradu.gradu.dto.BoardFileDTO;
import gradu.gradu.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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
    public String save(BoardFileDTO boardFileDTO, BoardDTO boardDTO, Model model) throws IOException {
        boardService.save(boardFileDTO, boardDTO);
        model.addAttribute("message","등록이 완료되었습니다");
        model.addAttribute("searchUrl", "/bbs");
        return "message";
    }



    @GetMapping("/bbs")
    public String listForm(Model model, @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable, String searchKey) {
        Page<Board> list = null;
        if(searchKey==null) {
            list = boardService.findAll(pageable);
        } else {
            list = boardService.findBySearchKey(searchKey, pageable);
        }
        int nowPage= pageable.getPageNumber()+1;
        int startPage=Math.max(nowPage -4,1);
        int endPage=Math.min(nowPage +5, list.getTotalPages());

        model.addAttribute("boardList", list);
        model.addAttribute("nowPage", nowPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        return "bbs";
    }

    @GetMapping("/bbs2")
    public String bbs2Form(){
        return "bbs2";
    }

}
