package shop.mtcoding.blog2.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import shop.mtcoding.blog2.dto.user.UserReq.UserJoinReqDto;
import shop.mtcoding.blog2.dto.user.UserReq.UserLoginReqDto;
import shop.mtcoding.blog2.handler.ex.CustomException;
import shop.mtcoding.blog2.model.User;
import shop.mtcoding.blog2.model.UserRepository;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public void 회원가입(UserJoinReqDto userJoinReqDto) {
        // 아이디 존재 확인
        User principal = userRepository.findByUsername(userJoinReqDto.getUsername());
        if (principal != null) {
            throw new CustomException("이미 존재하는 아이디입니다");
        }
        // 가입
        try {
            userRepository.insert(userJoinReqDto.getUsername(), userJoinReqDto.getPassword(),
                    userJoinReqDto.getEmail());
        } catch (Exception e) {
            throw new CustomException("일시적인 서버 문제가 발생했습니다", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public User 로그인(UserLoginReqDto userLoginReqDto) {
        // username 일치 확인
        User principal = userRepository.findByUsername(userLoginReqDto.getUsername());
        if (principal == null) {
            throw new CustomException("아이디를 확인해주세요");
        }
        // password 일치 확인
        if (!principal.getPassword().equals(userLoginReqDto.getPassword())) {
            throw new CustomException("패스워드를 확인해주세요");
        }

        // session에 넣을 User 반환
        return principal;
    }
}
