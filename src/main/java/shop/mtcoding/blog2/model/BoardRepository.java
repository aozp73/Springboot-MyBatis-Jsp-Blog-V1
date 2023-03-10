package shop.mtcoding.blog2.model;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import shop.mtcoding.blog2.dto.board.BoardResp.BoardDetailRespDto;
import shop.mtcoding.blog2.dto.board.BoardResp.BoardMainRespDto;

@Mapper
public interface BoardRepository {
        public List<Board> findAll();

        public BoardDetailRespDto findByIdWithUsername(int id);

        public List<BoardMainRespDto> findAllWithUsername();

        public Board findById(int id);

        public int insert(@Param("title") String title, @Param("content") String content,
                        @Param("thumbnail") String thumbnail,
                        @Param("userId") int userId);

        public int updateById(@Param("id") int id, @Param("title") String title,
                        @Param("content") String content, @Param("thumbnail") String thumbnail);

        public int deleteById(int id);
}