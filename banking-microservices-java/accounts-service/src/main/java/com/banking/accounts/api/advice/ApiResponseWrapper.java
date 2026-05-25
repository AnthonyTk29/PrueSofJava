package com.banking.accounts.api.advice;

import com.banking.accounts.api.response.ApiResponse;
import com.banking.accounts.api.response.ErrorResponse;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@RestControllerAdvice
public class ApiResponseWrapper implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
                                   Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                   ServerHttpRequest request, ServerHttpResponse response) {
        if (body instanceof ApiResponse<?> || body instanceof ErrorResponse) {
            return body;
        }
        String path = request.getURI().getPath();
        if (path.startsWith("/v3/api-docs") || path.startsWith("/swagger-ui")
                || path.startsWith("/swagger-resources") || path.startsWith("/webjars")) {
            return body;
        }
        HttpServletResponse httpResponse = ((ServletServerHttpResponse) response).getServletResponse();
        int status = httpResponse.getStatus();
        return new ApiResponse<>(status, body);
    }
}
