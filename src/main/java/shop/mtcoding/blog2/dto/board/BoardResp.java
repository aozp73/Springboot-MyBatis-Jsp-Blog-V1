package shop.mtcoding.blog2.dto.board;

import lombok.Getter;
import lombok.Setter;

public class BoardResp {

    @Getter
    @Setter
    public static class BoardMainRespDto {
        private int id;
        private int userId;
        private String title;
        private String thumbnail;
        private String username;
    }
}
