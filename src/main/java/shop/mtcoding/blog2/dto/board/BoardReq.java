package shop.mtcoding.blog2.dto.board;

import lombok.Getter;
import lombok.Setter;

public class BoardReq {

    @Getter
    @Setter
    public static class BoardSaveReqDto {
        private String title;
        private String content;
        private String thumbnail;
    }

    @Getter
    @Setter
    public static class BoardUpdateReqDto {
        private int id;
        private String title;
        private String content;
    }
}
