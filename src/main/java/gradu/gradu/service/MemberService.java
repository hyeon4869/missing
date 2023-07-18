package gradu.gradu.service;

import gradu.gradu.domain.Member;
import gradu.gradu.dto.MemberDTO;
import gradu.gradu.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;
    private final JavaMailSender mailSender;

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
//회원정보 수정 기능
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

//아이디 찾기 기능
    public Member findId(MemberDTO memberDTO) {
        Optional<Member> byMember = memberRepository.findByUserNameAndUserEmail(memberDTO.getUserName(), memberDTO.getUserEmail());
        if(byMember.isPresent()){
            Member member = byMember.get();
            return member;
        } else {
            throw new IllegalArgumentException("일치하는 사용자가 없습니다.");
        }
    }
//비밀번호 찾기 기능
    public Member findPwd(MemberDTO memberDTO) {
        Optional<Member> byMember=memberRepository.findByUserNameAndUserEmailAndUserID(memberDTO.getUserName(),memberDTO.getUserEmail(),memberDTO.getUserID());
        if(byMember.isPresent()) {
            Member member = byMember.get();
            return member;
        } else{
            throw new IllegalArgumentException("일치하는 사용자가 없습니다.");
        }
    }

    //임시 비밀번호 생성
    public String generateTemporaryPassword(){

        //비밀번호 자릿수
        int length = 8;
        //사용한 문자들
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

        StringBuilder temporaryPassword = new StringBuilder();
        Random random = new Random();
        for(int i = 0; i < length; i++){
            //character의 길이만큼의 수에서 랜덤한 숫자를 index에 저장
            int index = random.nextInt(characters.length());
            //character의 index값 위치에있는 값을 문자로 변환 후 임시 비밀번호에 저장
            temporaryPassword.append(characters.charAt(index));
        }
        //임시 비밀번호에 문자로 저장된 것을 문자열로 변환
        return temporaryPassword.toString();
    }
    @Transactional
    public void resetPassword(MemberDTO memberDTO, String temporaryPassword) {
        Optional<Member> byMember=memberRepository.findByUserID(memberDTO.getUserID());
        if(byMember.isPresent()){
            Member member = byMember.get();
            member.setResetPassword(temporaryPassword);
        }
    }
}

