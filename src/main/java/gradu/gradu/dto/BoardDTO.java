package gradu.gradu.dto;


import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.GeneratedValue;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BoardDTO {

    @GeneratedValue
    private Long id;
    private String missingName;
    private String missingAge;
    private String missingGender;
    private String missingNum;
    private String missingPlace;
    private String missingDate;
    private LocalDateTime boardTime;
    private int fileAttached;//파일 첨부 여부
    private BoardFileDTO boardFileDTO;


}
