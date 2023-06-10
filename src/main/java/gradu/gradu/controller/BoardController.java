package gradu.gradu.controller;

import gradu.gradu.domain.Board;
import gradu.gradu.domain.BoardFile;
import gradu.gradu.domain.Finder;
import gradu.gradu.dto.BoardDTO;
import gradu.gradu.dto.BoardFileDTO;
import gradu.gradu.repository.BoardFileRepository;
import gradu.gradu.repository.BoardRepository;
import gradu.gradu.service.BoardService;
import gradu.gradu.service.FinderService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
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
import org.springframework.web.socket.*;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;


import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.Timer;
import java.util.TimerTask;

import static gradu.gradu.controller.BoardController.MyWebSocketHandler.handleImageAddedEvent;


@Controller
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardController {

    private final BoardService boardService;
    private final BoardFileRepository boardFileRepository;
    private final FinderService finderService;

    @GetMapping("/bbs3")
    public String findForm(Model model, @PageableDefault(page = 0, size = 20, sort = "findTime", direction = Sort.Direction.DESC) Pageable pageable, String searchKey, String searchGender, String h_area1) {
        Page<Finder> list =null;

        list = finderService.findAll(pageable);

        int nowPage= pageable.getPageNumber()+1;
        int startPage=Math.max(nowPage -4,1);
        int endPage=Math.min(nowPage +5, list.getTotalPages());

        model.addAttribute("finderList", list);
        model.addAttribute("nowPage", nowPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        return "bbs3";
    }

    @GetMapping("/write")
    public String saveForm(){
        return "write";
    }

    @Transactional
    @PostMapping("/write")
    public String save(BoardFileDTO boardFileDTO, BoardDTO boardDTO, Model model) throws IOException {
        Long saveId =boardService.save(boardFileDTO, boardDTO);
        model.addAttribute("message","등록이 완료되었습니다");
        model.addAttribute("searchUrl", "/view?id="+saveId);
        return "message";
    }

    @GetMapping("/bbs")
    public String listForm(Model model, @PageableDefault(page = 0, size = 20, sort = "id", direction = Sort.Direction.DESC) Pageable pageable, String searchKey, String searchGender, String h_area1) {
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







/////////////////////////////////////////////////////////////웹소켓을 통해 메시지 알림창 띄우기



    public static class MyWebSocketHandler implements WebSocketHandler {

        private static WebSocketSession session;

        @Override
        public void afterConnectionEstablished(WebSocketSession session) throws Exception {
            this.session = session;
        }

        @Override
        public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {

        }

        @Override
        public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {

        }

        @Override
        public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {

        }

        @Override
        public boolean supportsPartialMessages() {
            return false;
        }

        // 이미지 파일 추가시 알림 메시지를 클라이언트로 전송하는 메서드
        private static void sendImageAddedNotification(Board board) {
            try {
                if (session != null && session.isOpen()) {
                    String message = "실종자를 발견했습니다. \n번호 : "+board.getId()+"\n실종자 이름 : "+board.getMissingName()+"\n실종자 발견 페이지에서 확인해주세요."; // 알림 메시지
                    session.sendMessage(new TextMessage(message)); // 클라이언트로 메시지 전송
                    Thread.sleep(1000); // 1초 대기
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        public static void handleImageAddedEvent(Board board) {
            // 파일 추가 알림 로직 수행
            sendImageAddedNotification(board);
            // 클라이언트에게 알림을 보내는 등의 작업을 수행하면 됩니다.
            // 예: WebSocket을 사용하여 클라이언트에게 실시간으로 알림을 보냄
        }

    }
    /////////////////////////////////////////////////////////////////

    @GetMapping("/view")
    public String bbsView(Model model, Long id) {
        Board board = boardService.findById(id);
        BoardFile boardFile = boardFileRepository.findByBoard_Id(id);

        if (boardFile != null) {
            String storedFileName = boardFile.getStoredFileName();
            String imageSrc = "/upload/" +board.getMissingName()+board.getMissingNum()+"/"+ storedFileName;

            String imageSrc2= null;
            imageSrc2 = "/upload/"+board.getMissingName()+board.getMissingNum()+"/"+"라즈베리탐색이미지.png";

            model.addAttribute("imageSrc", imageSrc);
            model.addAttribute("imageSrc2", imageSrc2);
        }

        model.addAttribute("board", board);


        Path folderPath = Paths.get("c:/temp/"+board.getMissingName()+board.getMissingNum()+"/");

        try {
            WatchService watchService = FileSystems.getDefault().newWatchService();

            WatchKey watchKey = folderPath.register(watchService, StandardWatchEventKinds.ENTRY_CREATE);

            Thread watchThread = new Thread(() -> {
                try {
                    while (true) {
                        WatchKey key = watchService.take();

                        if (key.isValid()) {
                            for (WatchEvent<?> event : key.pollEvents()) {
                                WatchEvent.Kind<?> kind = event.kind();

                                if (kind == StandardWatchEventKinds.ENTRY_CREATE) {
                                    WatchEvent<Path> ev = (WatchEvent<Path>) event;
                                    Path filename = ev.context();
                                    System.out.println("실종자를 찾았습니다.");


                                    // 파일 추가 알림 로직 수행
                                    // 클라이언트에게 알림을 보내는 등의 작업을 수행하면 됩니다.
                                    // 예: WebSocket을 사용하여 클라이언트에게 실시간으로 알림을 보냄
                                    handleImageAddedEvent(board);

                                    finderService.save(board);


                                }
                            }

                            key.reset();
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });

            watchThread.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "bbs2";

    }



}
