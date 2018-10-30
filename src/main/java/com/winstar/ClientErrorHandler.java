package com.winstar;

import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;

/**
 * client error handler
 *
 * @author gradle
 */
public class ClientErrorHandler implements ResponseErrorHandler {
    @Override
    public boolean hasError(ClientHttpResponse clientHttpResponse) throws IOException {
        return true;
    }

    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
        int status = response.getRawStatusCode();
        if (status == HttpStatus.NOT_FOUND.value()) {
            //TODO:metrics加入错误信息

        }
        if (status == HttpStatus.BAD_REQUEST.value()) {
            //TODO:metrics加入错误信息
        }
    }
}
