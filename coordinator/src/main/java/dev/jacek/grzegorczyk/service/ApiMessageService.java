package dev.jacek.grzegorczyk.service;

import dev.jacek.grzegorczyk.model.ApiMessageDTO;
import dev.jacek.grzegorczyk.model.ApiMessageDTOOut;

public interface ApiMessageService {

    ApiMessageDTOOut create(ApiMessageDTO apiMessage);
}
