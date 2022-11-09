package dev.jacek.grzegorczyk.web;

import dev.jacek.grzegorczyk.model.ApiMessageDTO;
import dev.jacek.grzegorczyk.model.ApiMessageDTOOut;
import dev.jacek.grzegorczyk.service.ApiMessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.CREATED;

@Slf4j
@RestController
@RequestMapping("/api/v1/messages")
@RequiredArgsConstructor
public class ApiMessageController {

    private final ApiMessageService apiMessageService;

    @PostMapping
    @ResponseStatus(CREATED)
    public ApiMessageDTOOut create(@RequestBody @Valid ApiMessageDTO apiMessage) {
        log.info("API received a new message: {}", apiMessage);
        return apiMessageService.create(apiMessage);
    }
}
