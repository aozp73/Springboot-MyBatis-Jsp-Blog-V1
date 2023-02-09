package shop.mtcoding.blog2.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import lombok.RequiredArgsConstructor;
import shop.mtcoding.blog2.dto.user.UserReq.UserJoinReqDto;
import shop.mtcoding.blog2.handler.ex.CustomException;
import shop.mtcoding.blog2.service.UserService;

@RequiredArgsConstructor
@Controller
public class UserController {

    private final UserService userService;

    @GetMapping("/joinForm")
    public String joinForm() {
        return "user/joinForm";
    }

    @GetMapping("/loginForm")
    public String loginForm() {
        return "user/loginForm";
    }

    @PostMapping("/join")
    public String join(UserJoinReqDto userJoinReqDto) {
        if (userJoinReqDto.getUsername() == null || userJoinReqDto.getUsername().isEmpty()) {
            throw new CustomException("아이디를 입력해주세요");
        }
        if (userJoinReqDto.getPassword() == null || userJoinReqDto.getPassword().isEmpty()) {
            throw new CustomException("패스워드를 입력해주세요");
        }
        if (userJoinReqDto.getEmail() == null || userJoinReqDto.getEmail().isEmpty()) {
            throw new CustomException("이메일을 입력해주세요");
        }

        userService.회원가입(userJoinReqDto);

        return "redirect:/loginForm";
    }
}
