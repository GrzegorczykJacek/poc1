package dev.jacek.grzegorczyk.registrator.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.jacek.grzegorczyk.registrator.model.RegistrationDTO;
import dev.jacek.grzegorczyk.registrator.model.RegistrationDTOOut;
import dev.jacek.grzegorczyk.registrator.service.RegistrationService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RegistrationController.class)
class RegistrationControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RegistrationService registrationService;

    @Test
    void shouldRegisterRequest() throws Exception {
        // Given
        Long id = 1L;
        RegistrationDTO registrationDTO = new RegistrationDTO();
        registrationDTO.setContents("Test contents...");

        RegistrationDTOOut registrationDTOOut = new RegistrationDTOOut();
        registrationDTOOut.setContents(registrationDTO.toString());
        registrationDTOOut.setId(id);

        given(registrationService.create(any(RegistrationDTO.class))).willReturn(registrationDTOOut);
        ArgumentCaptor<RegistrationDTO> registrationDTOArgumentCaptor = ArgumentCaptor.forClass(RegistrationDTO.class);

        // When and Then
        this.mockMvc.perform(post("/api/v1/registrations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registrationDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.contents", is(registrationDTO.toString())));
        BDDMockito.verify(registrationService, times(1)).create(registrationDTOArgumentCaptor.capture());

    }
}