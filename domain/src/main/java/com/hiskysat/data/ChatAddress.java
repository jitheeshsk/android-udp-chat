package com.hiskysat.data;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(staticName = "of")
@Getter
public class ChatAddress {
    private final String ipAddress;
    private final int port;
}
