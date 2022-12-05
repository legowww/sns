package com.example.sns.controller.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.stream.Stream;

@AllArgsConstructor
@Getter
public class Response<T> {

    private String resultCode; //어떤 결과값인지 나타내는 코드
    private T result;

    //error
    public static Response<Void> error(String errorCode) {
        return new Response(errorCode, null);
    }

    //success
    public static <T> Response<T> success(T result) {
        return new Response("success", result);
    }

    public static Response<Void> success() {
        return new Response<Void>("success", null);
    }


    public String toStream() {
        if (result == null) {
            return "{" +
                    "\"resultCode\": "+ "\"" + resultCode + "\"," +
                    "\"result\":" + null + "}";
        }
        return "{" +
                "\"resultCode\": "+ "\"" + resultCode + "\"," +
                "\"result\":" + result + "}";
    }
}

