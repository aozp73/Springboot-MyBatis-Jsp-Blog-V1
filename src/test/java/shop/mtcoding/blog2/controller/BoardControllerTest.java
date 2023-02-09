package shop.mtcoding.blog2.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import shop.mtcoding.blog2.dto.board.BoardReq.BoardSaveReqDto;
import shop.mtcoding.blog2.dto.board.BoardResp.BoardMainRespDto;
import shop.mtcoding.blog2.model.User;

@Transactional
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
public class BoardControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper om;

    private MockHttpSession mockSession;

    @BeforeEach
    public void setUp() {
        User user = new User(1, "ssar", "1234", "ssar@nate.com",
                Timestamp.valueOf(LocalDateTime.now()));
        mockSession = new MockHttpSession();
        mockSession.setAttribute("principal", user);
    }

    @Test
    public void main_test() throws Exception {
        // given

        // when
        ResultActions resultActions = mvc.perform(get("/"));
        Map<String, Object> map = resultActions.andReturn().getModelAndView().getModel();
        List<BoardMainRespDto> dtos = (List<BoardMainRespDto>) map.get("dtos");

        String model = om.writeValueAsString(dtos);
        System.out.println("테스트 : " + model);

        // then
        resultActions.andExpect(status().isOk());
        assertThat(dtos.get(0).getTitle()).isEqualTo("1번째 제목");
        // resultActions.andExpect(jsonPath("$[0].title").value("1번째 제목"));

    }

    @Test
    public void save_test() throws Exception {
        // given
        BoardSaveReqDto boardSaveReqDto = new BoardSaveReqDto();
        boardSaveReqDto.setTitle("제목");
        boardSaveReqDto.setContent("내용");

        String requestBody = om.writeValueAsString(boardSaveReqDto);

        // when
        ResultActions resultActions = mvc.perform(post("/board")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .session(mockSession));

        // then
        resultActions.andExpect(status().isCreated());
    }
}
