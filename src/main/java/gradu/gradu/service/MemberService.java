package gradu.gradu.service;

//import gradu.gradu.config.SecurityConfig;
import gradu.gradu.domain.Member;
import gradu.gradu.dto.MemberDTO;
import gradu.gradu.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.aspectj.apache.bcel.classfile.Utility;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Security;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;
    //private final SecurityConfig securityConfig;

    @Transactional
    public Member save(MemberDTO memberDTO) {
        validateDuplicateMember(memberDTO);


        Member member = Member.builder()
                .userID(memberDTO.getUserID())
                .userPassID(memberDTO.getUserPassID())
                .userName(memberDTO.getUserName())
                .userEmail(memberDTO.getUserEmail())
                .userCode(memberDTO.getUserCode())
                .userGender(memberDTO.getUserGender()) // 수정된 부분
                .build();
        memberRepository.save(member);
        return member;
    }


    private void validateDuplicateMember(MemberDTO memberDTO) {
        Optional<Member> byUserID = memberRepository.findByUserID(memberDTO.getUserID());
        if(byUserID.isPresent()) {
            throw new IllegalArgumentException("해당 아이디는 이미 존재합니다.");
        }
    }

    public MemberDTO login(MemberDTO memberDTO) {
        Optional<Member> byUserID = memberRepository.findByUserID(memberDTO.getUserID());
        if(byUserID.isPresent()){
            Member  member = byUserID.get();
            if(member.getUserPassID().equals( memberDTO.getUserPassID())) {
                MemberDTO DTO = MemberDTO.builder()
                         .userID(member.getUserID())
                         .userPassID(member.getUserPassID())
                         .userEmail(member.getUserEmail())
                         .userGender(member.getUserGender())
                         .build();
                return DTO;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }
//이메일 중복체크
    public String userIdCheck(String userId) {
        Optional<Member> optionalMember= memberRepository.findByUserID(userId);
        if(optionalMember.isEmpty()){
            return "ok";
        } else {
            return "no";
        }
    }
}

