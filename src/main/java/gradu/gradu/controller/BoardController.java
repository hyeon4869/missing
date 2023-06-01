package gradu.gradu.controller;

import gradu.gradu.domain.Board;
import gradu.gradu.domain.BoardFile;
import gradu.gradu.dto.BoardDTO;
import gradu.gradu.dto.BoardFileDTO;
import gradu.gradu.repository.BoardFileRepository;
import gradu.gradu.repository.BoardRepository;
import gradu.gradu.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;


@Controller
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardController {

    private final BoardService boardService;
    private final BoardFileRepository boardFileRepository;

    @GetMapping("/write")
    public String saveForm(){
        return "/write";
    }

    @Transactional
    @PostMapping("/write")
    public String save(BoardFileDTO boardFileDTO, BoardDTO boardDTO, Model model) throws IOException {
        boardService.save(boardFileDTO, boardDTO);
        model.addAttribute("message","등록이 완료되었습니다");
        model.addAttribute("searchUrl", "/view?id=120 ");
        return "message";
    }

    @GetMapping("/bbs")
    public String listForm(Model model, @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable, String searchKey, String searchGender, String h_area1) {
        Page<Board> list =null;

        if (searchGender == null || "select".equals(searchGender) && "select".equals(h_area1) && searchKey.isEmpty()) {
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

    @GetMapping("/view")
    public String bbsView(Model model, Long id) {
        Board board = boardService.findById(id);
        BoardFile boardFile = boardFileRepository.findByBoard_Id(id);

        if (boardFile != null) {
            String storedFileName = boardFile.getStoredFileName();
            String imageSrc = "/upload/" + storedFileName;
            model.addAttribute("imageSrc", imageSrc);
        }

        model.addAttribute("board", board);

        return "bbs2";
    }




}
