package com.nikitiuk.javabeansinitializer.server.utils;

public enum MimeType {

    MULTIPART_FORM_DATA {
        @Override
        public String mimeTypeName() {
            return "multipart/form-data";
        }
    },

    APPLICATION_JSON {
        @Override
        public String mimeTypeName() {
            return "application/json";
        }
    },

    TEXT_HTML {
        @Override
        public String mimeTypeName() {
            return "text/html";
        }
    };

    public String mimeTypeName() {
        return "no Mime Type";
    }

}
