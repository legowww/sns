package com.example.sns.exception;


import com.example.sns.controller.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalControllerAdvice {


    @ExceptionHandler(SnsApplicationException.class) //이 에러를 처리하는 핸들러
    public ResponseEntity<?> applicationHandler(SnsApplicationException e) {
        /**
         e.getErrorCode()=DUPLICATED_USER_NAME
         e.e.getErrorCode().name()=DUPLICATED_USER_NAME

         e.getErrorCode().getStatus()=409 CONFLICT
         e.getErrorCode().getMessage()=User name is duplicated
         e.getMessage()=User name is duplicated. test120 is duplicated
         */
        log.error("Error occurs {}", e.toString());
        log.info("e.getErrorCode()={}", e.getErrorCode());
        log.info("e.e.getErrorCode().name()={}", e.getErrorCode().name());
        log.info("e.getErrorCode().getStatus()={}", e.getErrorCode().getStatus());
        log.info("e.getErrorCode().getMessage()={}", e.getErrorCode().getMessage());
        log.info("e.getMessage()={}", e.getMessage());


//        return new ResponseEntity(Response.error(e.getErrorCode().name()), e.getErrorCode().getStatus());
        return ResponseEntity.status(e.getErrorCode().getStatus())
                .body(Response.error(e.getErrorCode().name())); //위의 코드와 같다.
    }
}
