package gradu.gradu.controller;

import gradu.gradu.domain.Member;
import gradu.gradu.dto.MemberDTO;
import gradu.gradu.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/")
    public String indexForm() {
        return "main";
    }

    @GetMapping("/main")
    public String mainForm() {
        return "main";
    }

    @GetMapping("/join")
    public String joinForm(){
        return "join";
    }

    @PostMapping("/auth/join")
    public String join(MemberDTO memberDTO, Model model){

        memberService.save(memberDTO);
        model.addAttribute("message", "회원가입을 성공하셨습니다");
        model.addAttribute("searchUrl","/");

        return "message";

    }
    //예외처리 핸들러
    @ExceptionHandler(IllegalArgumentException.class)
    public String handlerException(IllegalArgumentException ex, Model model) {

        if(ex.getMessage().equals("해당 아이디는 이미 존재합니다.")) {
            model.addAttribute("message", ex.getMessage());
            model.addAttribute("searchUrl", "/join");
            return "message";
        }
        else if(ex.getMessage().equals("현재 비밀번호가 맞지 않습니다.")||ex.getMessage().equals("변경할 비밀번호가 일치하지 않습니다.")) {
            model.addAttribute("message", ex.getMessage());
            model.addAttribute("searchUrl", "/mypage/update");
            return "message";}
        else{
            return "message";
        }
    }



    @GetMapping("/login")
    public String loginForm(){
     return "login";
    }

    @PostMapping("auth/login")
    public String login(@ModelAttribute MemberDTO memberDTO, HttpSession session, Model model){
        MemberDTO loginResult= memberService.login(memberDTO);
        if(loginResult!=null) {
            session.setAttribute("userID", loginResult.getUserID());
            model.addAttribute("message","로그인 성공");
            model.addAttribute("searchUrl","/write");
            return "message";
        } else {
            model.addAttribute("message","아이디 혹은 비밀번호가 틀립니다");
            model.addAttribute("searchUrl","/login");
         return "message";
        }
    }

    @GetMapping("/logoutAction")
    public String logout(HttpSession session,Model model) {
        session.getAttribute(session.getId());
        session.invalidate();
        return "redirect:/";
    }

    @PostMapping("/userIdCheck")
    public @ResponseBody String userIdCheck(@RequestParam String userId){
    String checkResult = memberService.userIdCheck(userId);
    return checkResult;
    }

    @GetMapping("/mypage")
    public String myPageForm(Model model, HttpSession session) {
        String myUserId = (String) session.getAttribute("userID");
        Member byMember = memberService.findByUserId(myUserId);
        model.addAttribute("memberList",byMember);
        return "mypage";
    }
//회원수정 폼
    @GetMapping("/mypage/update")
    public String updateForm(Model model, HttpSession session){
        String myUserId = (String) session.getAttribute("userID");
        Member byMember = memberService.findByUserId(myUserId);
        model.addAttribute("memberList", byMember);
        return "update";
    }
//회원수정 기능
    @PostMapping("/mypage/update")
    public String update(MemberDTO memberDTO, HttpSession session){
        String myUserId = (String) session.getAttribute("userID");
        memberService.update(memberDTO, myUserId);
        session.getAttribute(session.getId());
        session.invalidate();
        return "redirect:/login";
    }

    //아이디 찾기 폼
    @GetMapping("/findid")
    public String findidForm() {
        return "findid";
    }

    //아이디 찾기 기능
    @PostMapping("/findid")
    public String findid(){
     return "login";
    }
}
