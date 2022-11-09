package dev.jacek.grzegorczyk.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ApiMessageDTOOut extends ApiMessageDTO {

    private Long id;
}
