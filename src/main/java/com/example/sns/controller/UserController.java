package com.example.sns.controller;

import com.example.sns.controller.request.UserJoinRequest;
import com.example.sns.controller.request.UserLoginRequest;
import com.example.sns.controller.response.AlarmResponse;
import com.example.sns.controller.response.Response;
import com.example.sns.controller.response.UserJoinResponse;
import com.example.sns.controller.response.UserLoginResponse;
import com.example.sns.model.User;
import com.example.sns.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @PostMapping("/join")
    public Response<UserJoinResponse> join(@RequestBody UserJoinRequest request) {
        User user = userService.join(request.getUserName(), request.getPassword());
        //응답 성공-실패 획일화된 클래스로 처리
        //받는 쪽에서 api 파싱 어려움 -> 획일화된 응답 형태 생성 Response.class
        return Response.success(UserJoinResponse.fromUser(user));
    }

    @PostMapping("/login")
    public Response<UserLoginResponse> login(@RequestBody UserLoginRequest request) {
        String token = userService.login(request.getUserName(), request.getPassword());
        return Response.success(new UserLoginResponse(token));
    }

    @GetMapping("/alarm")
    public Response<Page<AlarmResponse>> alarm(Pageable pageable, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return Response.success(userService.alarmList(user.getId(), pageable).map(AlarmResponse::fromAlarm));
    }
}
