package com.whatsapp.send.model;

public record WhatsAppMessage(String messaging_product, String to, String type, Template template) {

    public record Template(String name, Language language) {

        public record Language(String code) {
        }
    }
}

