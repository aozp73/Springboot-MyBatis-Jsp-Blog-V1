package shop.mtcoding.blog2.model;

import java.sql.Timestamp;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Board {
    private int id;
    private String title;
    private String content;
    private String thumbnail;
    private int userId;
    private Timestamp createdAt;
}
