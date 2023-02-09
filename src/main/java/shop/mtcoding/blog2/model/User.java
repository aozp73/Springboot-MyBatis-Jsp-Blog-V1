package shop.mtcoding.blog2.model;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
public class User {
    private int id;
    private String username;
    private String password;
    private String email;
    private Timestamp createdAt;
}
