package edu.udacity.java.nano.tests;

import edu.udacity.java.nano.WebSocketChatApplication;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.core.StringContains.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = WebSocketChatApplication.class)
public class ChatRoomTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void whenValidLogin_thenReturns200() throws Exception {
        this.mockMvc.perform(get("/"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("login"))
                .andExpect(content().contentType("text/html;charset=UTF-8"));
    }

    @Test
    void whenValidUserJoin_thenReturns200() throws Exception {
        this.mockMvc.perform(get("/index?username=felipebonezi"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(content().string(containsString("felipebonezi")));
    }

    @Test
    void whenValidChat_thenReturns200() throws Exception {
        this.mockMvc.perform(get("/index?username=felipebonezi"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(content().string(containsString("Send (enter)")))
                .andExpect(view().name("chat"));
    }

}
