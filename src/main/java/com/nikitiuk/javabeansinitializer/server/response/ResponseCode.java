package com.nikitiuk.javabeansinitializer.server.response;

public enum ResponseCode {

    HTTP_200_OK{
        @Override
        public String getStringValue() {
            return "200 OK";
        }
    },
    HTTP_201_CREATED{
        @Override
        public String getStringValue() {
            return "201 Created";
        }
    },
    HTTP_204_NO_CONTENT{
        @Override
        public String getStringValue() {
            return "204 No Content";
        }
    },
    HTTP_400_BAD_REQUEST{
        @Override
        public String getStringValue() {
            return "400 Bad Request";
        }
    },
    HTTP_401_UNAUTHORIZED{
        @Override
        public String getStringValue() {
            return "401 Unauthorized";
        }
    },
    HTTP_403_FORBIDDEN{
        @Override
        public String getStringValue() {
            return "403 Forbidden";
        }
    },
    HTTP_404_NOT_FOUND{
        @Override
        public String getStringValue() {
            return "404 Not Found";
        }
    },
    HTTP_500_INTERNAL_SERVER_ERROR {
        @Override
        public String getStringValue() {
            return "500 Internal Server Error";
        }
    };

    public String getStringValue() {
        return "no code";
    }
}
