package gradu.gradu.controller;

import gradu.gradu.dto.MemberDTO;
import gradu.gradu.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/")
    public String indexForm() {
        return "main";
    }


    @GetMapping("/join")
    public String joinForm(){
        return "join";
    }

    @PostMapping("/auth/join")
    public String join(MemberDTO memberDTO){
        memberService.save(memberDTO);
        return "login";
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
            return "redirect:/";
        } else {
            model.addAttribute("error","불일치");
         return "redirect:/login";
        }
    }

    @GetMapping("/logoutAction")
    public String logout(HttpSession session) {
        session.getAttribute(session.getId());
        session.invalidate();
        return "redirect:/";
    }

}
