package dev.jacek.grzegorczyk.messages;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@NoArgsConstructor
public class Schema {

    private String type;

    ArrayList<Object> fields = new ArrayList<>();

    private boolean optional;

    private String name;

    private float version;
}
