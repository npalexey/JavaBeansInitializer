package com.nikitiuk.javabeansinitializer.server.response;

public enum ResponseCode {

    HTTP_200_OK{
        @Override
        public String getStringValue() {
            return "200 OK";
        }
    },
    HTTP_204_NO_CONTENT{
        @Override
        public String getStringValue() {
            return "204 No Content";
        }
    },
    HTTP_404_NOT_FOUND{
        @Override
        public String getStringValue() {
            return "204 No Content";
        }
    };

    public String getStringValue() {
        return "no code";
    }
}
