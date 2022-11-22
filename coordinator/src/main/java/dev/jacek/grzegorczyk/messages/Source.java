package dev.jacek.grzegorczyk.messages;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Source {

    private String version;
    private String connector;
    private String name;
    private float ts_ms;
    private String snapshot;
    private String db;
    private String sequence;
    private String schema;
    private String table;
    private float txId;
    private float lsn;
    private String xmin = null;
}
