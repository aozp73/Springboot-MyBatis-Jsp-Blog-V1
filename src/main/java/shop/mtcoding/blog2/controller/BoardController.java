package shop.mtcoding.blog2.controller;

import javax.servlet.http.HttpSession;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.RequiredArgsConstructor;
import shop.mtcoding.blog2.dto.ResponseDto;
import shop.mtcoding.blog2.dto.board.BoardReq.BoardSaveReqDto;
import shop.mtcoding.blog2.dto.board.BoardReq.BoardUpdateReqDto;
import shop.mtcoding.blog2.handler.ex.CustomApiException;
import shop.mtcoding.blog2.handler.ex.CustomException;
import shop.mtcoding.blog2.model.Board;
import shop.mtcoding.blog2.model.BoardRepository;
import shop.mtcoding.blog2.model.User;
import shop.mtcoding.blog2.service.BoardService;

@RequiredArgsConstructor
@Controller
public class BoardController {

    private final BoardService boardService;
    private final BoardRepository boardRepository;

    private final HttpSession session;

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable int id, @RequestBody BoardUpdateReqDto boardUpdateReqDto) {
        Board boardPS = boardRepository.findById(id);
        if (boardPS == null) {
            throw new CustomApiException("게시물이 존재하지 않습니다");
        }
        User principal = (User) session.getAttribute("principal");
        if (principal == null) {
            throw new CustomApiException("로그인이 필요합니다", HttpStatus.UNAUTHORIZED);
        }
        if (boardPS.getUserId() != principal.getId()) {
            throw new CustomApiException("게시글 수정 권한이 없습니다", HttpStatus.FORBIDDEN);
        }

        if (boardPS.getTitle() == null || boardPS.getTitle().isEmpty()) {
            throw new CustomApiException("제목을 입력해주세요");
        }
        if (boardPS.getContent() == null || boardPS.getContent().isEmpty()) {
            throw new CustomApiException("내용을 입력해주세요");
        }

        boardService.게시글수정(boardUpdateReqDto, principal.getId());

        return new ResponseEntity<>(new ResponseDto<>(1, "게시글 수정완료", null), HttpStatus.OK);
    }

    @GetMapping("/board/{id}/updateForm")
    public String update(@PathVariable int id, Model model) {
        Board boardPS = boardRepository.findById(id);
        if (boardPS == null) {
            throw new CustomException("게시물이 존재하지 않습니다");
        }
        User principal = (User) session.getAttribute("principal");
        if (principal == null) {
            throw new CustomException("로그인이 필요합니다", HttpStatus.UNAUTHORIZED);
        }
        if (boardPS.getUserId() != principal.getId()) {
            throw new CustomException("게시글 수정 권한이 없습니다", HttpStatus.FORBIDDEN);
        }

        model.addAttribute("board", boardPS);

        return "/board/updateForm";
    }

    @DeleteMapping("/board/{id}")
    public ResponseEntity<?> delete(@PathVariable int id) {
        // 인증
        User principal = (User) session.getAttribute("principal");
        if (principal == null) {
            throw new CustomApiException("로그인이 필요합니다", HttpStatus.UNAUTHORIZED);
        }

        boardService.게시글삭제(id, principal.getId());
        return new ResponseEntity<>(new ResponseDto<>(1, "게시글이 삭제되었습니다", null), HttpStatus.OK);
    }

    @GetMapping("/board/{id}")
    public String detail(@PathVariable int id, Model model) {
        model.addAttribute("dto", boardRepository.findByIdWithUsername(id));
        return "/board/detail";
    }

    @GetMapping({ "/", "/board" })
    public String main(Model model) {

        model.addAttribute("dtos", boardRepository.findAllWithUsername());
        return "/board/main";
    }

    @GetMapping("/board/saveForm")
    public String saveForm() {
        if (session.getAttribute("principal") == null) {
            throw new CustomException("로그인이 필요합니다");
        }

        return "/board/saveForm";
    }

    @PostMapping("/board")
    public @ResponseBody ResponseEntity<?> save(@RequestBody BoardSaveReqDto boardSaveReqDto) {
        // 인증
        User principal = (User) session.getAttribute("principal");
        if (principal == null) {
            throw new CustomApiException("로그인이 필요합니다");
        }

        // 유효성 검사
        if (boardSaveReqDto.getTitle() == null || boardSaveReqDto.getTitle().isEmpty()) {
            throw new CustomApiException("제목을 입력해주세요");
        }
        if (boardSaveReqDto.getContent() == null || boardSaveReqDto.getContent().isEmpty()) {
            throw new CustomApiException("내용을 입력해주세요");
        }
        if (boardSaveReqDto.getTitle().length() > 100) {
            throw new CustomApiException("제목은 100자 이하여야 합니다");
        }

        boardService.글쓰기(boardSaveReqDto, principal.getId());

        return new ResponseEntity<>(new ResponseDto<>(1, "게시글이 등록되었습니다", null), HttpStatus.CREATED);
    }
}
