package com.whatsapp.send.model;

import java.util.List;

public record MessageBody(String messageTemplate, List<String> sendingNumbers) {
    // No need to explicitly define constructors, getters, setters, or toString() method
}
