package com.nikitiuk.javabeansinitializer.server;

public enum MimeType {

    MULTIPART_FORM_DATA {
        @Override
        public String mimeTypeName() {
            return "multipart/form-data";
        }
    };

    public String mimeTypeName() {
        return "no Mime Type";
    }

}
