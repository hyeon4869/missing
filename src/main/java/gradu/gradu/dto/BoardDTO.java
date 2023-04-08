package gradu.gradu.dto;


import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BoardDTO {

    private Long id;
    private String missingName;
    private String missingAge;
    private String missingGender;
    private String missingPlace;
    private String missingDate;
    private LocalDateTime boardTime;

    private MultipartFile boardFile;//파일을 담는 용도
    private String originalFileName;//원본 파일 이름
    private String storedFileName;//서버 저장용 파일 이름
    private int fileAttached;//파일 첨부 여부
}
