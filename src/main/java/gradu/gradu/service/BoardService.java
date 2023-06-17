package gradu.gradu.service;

import gradu.gradu.domain.Board;
import gradu.gradu.domain.BoardFile;
import gradu.gradu.dto.BoardDTO;
import gradu.gradu.dto.BoardFileDTO;
import gradu.gradu.repository.BoardFileRepository;
import gradu.gradu.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.Instant;
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
    public Long save(BoardFileDTO boardFileDTO, BoardDTO boardDTO) throws IOException {


        Board board = Board.builder()
                .missingName(boardDTO.getMissingName())
                .missingAge(boardDTO.getMissingAge())
                .missingGender(boardDTO.getMissingGender())
                .missingPlace(boardDTO.getMissingPlace())
                .missingDate(boardDTO.getMissingDate())
                .missingNum(boardDTO.getMissingNum())
                .boardTime(LocalDateTime.now())
                .fileAttached(boardFileDTO.getBoardFile() == null || boardFileDTO.getBoardFile().isEmpty() ? 0 : 1)
                .build();
        Long saveId = boardRepository.save(board).getId();

        String savePath = null;
        if (boardFileDTO.getBoardFile() != null && !boardFileDTO.getBoardFile().isEmpty()) {
            //첨부 파일있음
            MultipartFile boardFile = boardFileDTO.getBoardFile();
            String originalFilename = boardFile.getOriginalFilename();
            String storedFileName = System.currentTimeMillis() + "_" + originalFilename;


            savePath = "C:/temp/"+board.getMissingName()+board.getMissingNum()+"/"+storedFileName;

            // C:/temp/ 디렉토리가 존재하지 않는다면 생성
            File dir = new File("C:/temp/"+board.getMissingName()+board.getMissingNum()+"/");
            if (!dir.exists()) {
                dir.mkdirs();
            }
            boardFile.transferTo(new File(savePath));

            BoardFile toboardFile = BoardFile.builder()
                    .originalFileName(originalFilename)
                    .board(board)
                    .storedFileName(storedFileName)
                    .boardTime(board.getBoardTime())
                    .build();


            boardFileRepository.save(toboardFile);

            // src/main/resources/static/images/ 디렉토리에 이미지 파일 복사
            String destinationPath = "D:/boot/gradu/photo.jpg" ;

            Path sourcePath = Paths.get(savePath);
            Path destPath = Paths.get(destinationPath);
            try {
                Files.copy(sourcePath, destPath, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //////////////////////////////////////
                String photo = "D:\\boot\\gradu\\photo.jpg";
                List<String> listData = List.of(boardDTO.getMissingName(), boardDTO.getMissingGender(), boardDTO.getMissingAge(), boardDTO.getMissingPlace(), boardDTO.getMissingDate(), boardDTO.getMissingNum());

                try {
                    // 사진 복사
                    Files.copy(Path.of(photo), Path.of("D:\\boot\\gradu\\data\\photo.jpg"), StandardCopyOption.REPLACE_EXISTING);

                    // 리스트 저장
                    FileWriter writer = new FileWriter("D:\\boot\\gradu\\data\\list_data.txt");
                    for (String item : listData) {
                        writer.write(item.toString() + "\n");
                    }
                    writer.close();

                    // 파일 시간 스탬프 업데이트
                    Thread.sleep(1000); // 소스 코드 파일 실행 시간 고려
                    FileWriter timestampWriter = new FileWriter("data/timestamp.txt");
                    timestampWriter.write(String.valueOf(Instant.now().getEpochSecond()));
                    timestampWriter.close();
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
return saveId;


    }

    public Page<Board> findAll(Pageable pageable) {
        return boardRepository.findAll(pageable);
    }

    public Page<Board> findBySearchGender(String searchGender, Pageable pageable){
        return boardRepository.findByMissingGenderContaining(searchGender, pageable);
    }

    public Page<Board> findBySearchArea(String h_area1, Pageable pageable){
        return boardRepository.findByMissingPlaceContaining(h_area1, pageable);
    }



    public Page<Board> findBySearchGenderAndArea(String searchGender, String h_area1, Pageable pageable) {
        return boardRepository.findByMissingGenderContainingAndMissingPlaceContaining(searchGender,h_area1, pageable);
    }

    public Board findById(Long id) {
        return boardRepository.findById(id).get();
    }


    public Page<Board> findBySearchKey(String searchKey, Pageable pageable) {
        return boardRepository.findByMissingNameContaining(searchKey, pageable);
    }

    public Page<Board> findBySearchKeyAndSearchGender(String searchKey, String searchGender, Pageable pageable) {
        return boardRepository.findByMissingNameContainingAndMissingGenderContaining(searchKey,searchGender,pageable);
    }

    public Page<Board> findBySearchKeyAndSearchGenderAndArea(String searchKey, String searchGender, String h_area1, Pageable pageable) {
        return boardRepository.findByMissingNameContainingAndMissingGenderContainingAndMissingPlaceContaining(searchKey,searchGender,h_area1,pageable);
    }

    public Page<Board> findBySearchKeyAndSearchArea(String searchKey, String h_area1, Pageable pageable) {
        return boardRepository.findByMissingNameContainingAndMissingPlaceContaining(searchKey, h_area1, pageable);
    }
}
