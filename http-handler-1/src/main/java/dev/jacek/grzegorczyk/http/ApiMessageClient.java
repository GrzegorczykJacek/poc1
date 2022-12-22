package dev.jacek.grzegorczyk.http;

import dev.jacek.grzegorczyk.models.ApiMessageDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(value = "apiMessageClient", url = "localhost:8085/api/v1")
public interface ApiMessageClient {
    @PostMapping(value = "/messages", consumes = "application/json")
    void send(ApiMessageDTO apiMessageDTO);
}
