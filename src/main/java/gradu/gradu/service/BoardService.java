package gradu.gradu.service;

import gradu.gradu.domain.Board;
import gradu.gradu.domain.BoardFile;
import gradu.gradu.dto.BoardDTO;
import gradu.gradu.repository.BoardFileRepository;
import gradu.gradu.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardService {

    private final BoardRepository boardRepository;
    private final BoardFileRepository boardFileRepository;

    @Transactional
    public void save(BoardDTO boardDTO) throws IOException {

        if(boardDTO.getBoardFile().isEmpty()){
            //첨부파일 없음
            Board board = Board.builder()
                    .missingName(boardDTO.getMissingName())
                    .missingAge(boardDTO.getMissingAge())
                    .missingGender(boardDTO.getMissingGender())
                    .missingPlace(boardDTO.getMissingPlace())
                    .missingDate(boardDTO.getMissingDate())
                    .boardTime(LocalDateTime.now())
                    .fileAttached(0)
                    .build();
            boardRepository.save(board);
        }else{
            //첨부 파일있음
            MultipartFile boardFile = boardDTO.getBoardFile();
            String originalFilename = boardFile.getOriginalFilename();
            String storedFileName=System.currentTimeMillis()+"_"+originalFilename;
            String savePath= "c:/springboot_img/" + storedFileName;
            boardFile.transferTo(new File(savePath));

            Board board = Board.builder()
                    .missingName(boardDTO.getMissingName())
                    .missingAge(boardDTO.getMissingAge())
                    .missingGender(boardDTO.getMissingGender())
                    .missingPlace(boardDTO.getMissingPlace())
                    .missingDate(boardDTO.getMissingDate())
                    .boardTime(LocalDateTime.now())
                    .fileAttached(1)
                    .build();
            Long saveId=boardRepository.save(board).getId();

            Board Byboard= boardRepository.findById(saveId).get();
            BoardFile toboardFile = BoardFile.builder()
                    .originalFileName(originalFilename)
                    .board(board)
                    .storedFileName(storedFileName)
                    .boardTime(board.getBoardTime())
                    .build();
            boardFileRepository.save(toboardFile);
        }

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<BoardDTO> request = new HttpEntity<>(boardDTO, headers);
        String url = "http://localhost:4000/api/boards";
        ResponseEntity<Void> response = restTemplate.postForEntity(url, request, Void.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            // 성공적으로 처리된 경우
            System.out.println("성공!");
        } else {
            // 오류가 발생한 경우
            System.out.println("실패!");
        }

    }

    public List<BoardDTO> findAll() {
        List<Board> boards=boardRepository.findAll();
        List<BoardDTO> boardDTOS=new ArrayList<>();
        for(Board board: boards) {
            BoardDTO boardDTO = BoardDTO.builder()
                    .id(board.getId())
                    .missingName(board.getMissingName())
                    .missingAge(board.getMissingAge())
                    .missingGender(board.getMissingGender())
                    .missingPlace(board.getMissingPlace())
                    .missingDate(board.getMissingDate())
                    .boardTime(board.getBoardTime())
                    .build();
            boardDTOS.add(boardDTO);
        }
        return boardDTOS;
    }
}
