package shop.mtcoding.blog2.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import shop.mtcoding.blog2.dto.board.BoardReq.BoardSaveReqDto;
import shop.mtcoding.blog2.dto.board.BoardReq.BoardUpdateReqDto;
import shop.mtcoding.blog2.handler.ex.CustomApiException;
import shop.mtcoding.blog2.model.Board;
import shop.mtcoding.blog2.model.BoardRepository;
import shop.mtcoding.blog2.util.Htmlparser;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class BoardService {

    private final BoardRepository boardRepository;

    @Transactional
    public void 글쓰기(BoardSaveReqDto boardSaveReqDto, int userId) {
        String thumbnail = Htmlparser.getThumbnail(boardSaveReqDto.getContent());

        try {
            boardRepository.insert(boardSaveReqDto.getTitle(), boardSaveReqDto.getContent(), thumbnail, userId);
        } catch (Exception e) {
            throw new CustomApiException("일시적인 서버 문제가 발생했습니다", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional
    public void 게시글삭제(int boardId, int principalId) {
        Board boardPS = boardRepository.findById(boardId);
        // 게시글 존재 확인
        if (boardPS == null) {
            throw new CustomApiException("게시글이 존재하지 않습니다");
        }

        // 게시글 userId, 로그인 id 일치 여부
        if (boardPS.getUserId() != principalId) {
            throw new CustomApiException("게시글 삭제 권한이 없습니다.", HttpStatus.FORBIDDEN);
        }

        // 삭제
        try {
            boardRepository.deleteById(boardId);
        } catch (Exception e) {
            throw new CustomApiException("일시적인 서버 문제가 발생했습니다", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    public void 게시글수정(BoardUpdateReqDto boardUpdateReqDto, int principalId) {
        // 게시글 존재 유무
        Board boardPS = boardRepository.findById(boardUpdateReqDto.getId());
        if (boardPS == null) {
            throw new CustomApiException("게시물이 존재하지 않습니다");
        }

        // 게시글 userId, 세션 id 비교
        if (boardPS.getUserId() != principalId) {
            throw new CustomApiException("수정 권한이 없습니다", HttpStatus.UNAUTHORIZED);
        }

        String thumbnail = Htmlparser.getThumbnail(boardUpdateReqDto.getContent());
        try {
            boardRepository.updateById(boardUpdateReqDto.getId(), boardUpdateReqDto.getTitle(),
                    boardUpdateReqDto.getContent(), thumbnail);
        } catch (Exception e) {
            throw new CustomApiException("일시적인 서버 문제가 발생했습니다", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
