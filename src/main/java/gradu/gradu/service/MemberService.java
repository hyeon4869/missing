package gradu.gradu.service;

import gradu.gradu.domain.Member;
import gradu.gradu.dto.MemberDTO;
import gradu.gradu.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    public Member save(MemberDTO memberDTO) {
        validateDuplicateMember(memberDTO);


        Member member = Member.builder()
                .userID(memberDTO.getUserID())
                .userPassID(memberDTO.getUserPassID())
                .userName(memberDTO.getUserName())
                .userEmail(memberDTO.getUserEmail())
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

    public Member findByUserId(String myUserId) {
        return memberRepository.findByUserID(myUserId).get();
    }

    @Transactional
    public void update(MemberDTO memberDTO, String myUserId) {
        Optional<Member> byMember = memberRepository.findByUserID(myUserId);

        if (byMember.isPresent()) {
            Member member = byMember.get();
            if (member.getUserPassID().equals(memberDTO.getPwd1())) {
                if (memberDTO.getPwd2().equals(memberDTO.getPwd3())) {
                    member.setUserPassID(memberDTO);
                    memberRepository.save(member);
                } else {
                    throw new IllegalArgumentException("변경할 비밀번호가 일치하지 않습니다.");
                }
            } else {
                throw new IllegalArgumentException("현재 비밀번호가 맞지 않습니다.");
            }
        }
    }


}

