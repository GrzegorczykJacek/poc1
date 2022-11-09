package dev.jacek.grzegorczyk.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.jacek.grzegorczyk.model.ApiMessageDTO;
import dev.jacek.grzegorczyk.model.ApiMessageDTOOut;
import dev.jacek.grzegorczyk.service.ApiMessageService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ApiMessageController.class)
class ApiMessageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ApiMessageService apiMessageService;

    @Test
    void shouldSaveMessage() throws Exception {
        // Given
        ApiMessageDTO apiMessageDTO = new ApiMessageDTO();
        apiMessageDTO.setMessage("Test message...");
        apiMessageDTO.setAuthor("George Clooney");

        ApiMessageDTOOut apiMessageDTOOut = new ApiMessageDTOOut();
        apiMessageDTOOut.setMessage(apiMessageDTO.getMessage());
        apiMessageDTOOut.setAuthor(apiMessageDTO.getAuthor());

        given(apiMessageService.create(any(ApiMessageDTO.class))).willReturn(apiMessageDTOOut);
        ArgumentCaptor<ApiMessageDTO> apiMessageDTOArgumentCaptor = ArgumentCaptor.forClass(ApiMessageDTO.class);

        // When and Then
        this.mockMvc.perform(post("/api/v1/messages")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(apiMessageDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message", is(apiMessageDTO.getMessage())))
                .andExpect(jsonPath("$.author", is(apiMessageDTO.getAuthor())));
        verify(apiMessageService, times(1)).create(apiMessageDTOArgumentCaptor.capture());
    }
}