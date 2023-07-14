package gradu.gradu.dto;

import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class MemberDTO {

    private Long id;

    private String userID;
    private String userPassID;
    private String userName;
    private String userGender;
    private String userEmail;
    private String userCode;
    private String pwd1;
    private String pwd2;
    private String pwd3;

}
