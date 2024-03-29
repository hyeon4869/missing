package gradu.gradu.domain;

import gradu.gradu.dto.MemberDTO;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Member {

    @Id @GeneratedValue
    @Column(name="member_id")
    private Long id;

    private String userID;

    private String userPassID;

    private String userName;

    private String userGender;

    private String userEmail;

    @OneToMany(mappedBy = "member")
    private List<Board>  boards = new ArrayList<>();

    public void setUserPassID(String newPassword) {
        this.userPassID= newPassword;
    }

    public void setResetPassword(String temp){
        this.userPassID = temp;
    }


}
