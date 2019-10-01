package com.nikitiuk.javabeansinitializer.server;

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
    };

    public String mimeTypeName() {
        return "no Mime Type";
    }

}
