package dev.jacek.grzegorczyk.web;

import dev.jacek.grzegorczyk.http.ApiMessageClient;
import dev.jacek.grzegorczyk.models.ApiMessageDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.sleuth.Span;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/api/v1/messages")
@RequiredArgsConstructor
public class ApiMessageController {

    private final Tracer tracer;

    private final ApiMessageClient apiMessageClient;

    @PostMapping
    public void create(@RequestBody @Valid ApiMessageDTO apiMessage) {
        log.info("API received a new message: {}", apiMessage);
        Span span = tracer.currentSpan();
        if (span != null) {
            log.info("Trace ID {}", span.context().traceId());
            log.info("Span ID {}", span.context().spanId());
        }

        apiMessageClient.send(apiMessage);

    }
}
