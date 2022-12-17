package com.example.sns.service;


import com.example.sns.exception.ErrorCode;
import com.example.sns.exception.SnsApplicationException;
import com.example.sns.model.Alarm;
import com.example.sns.model.User;
import com.example.sns.model.entity.AlarmEntity;
import com.example.sns.model.entity.UserEntity;
import com.example.sns.repository.AlarmEntityRepository;
import com.example.sns.repository.UserEntityRepository;
import com.example.sns.util.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final BCryptPasswordEncoder encoder;
    private final UserEntityRepository userEntityRepository;

    private final AlarmEntityRepository alarmEntityRepository;

    //컨피규레이션 값 = yaml 파일
    @Value("${jwt.secret-key}")
    private String secretKey;

    @Value("${jwt.token-expired-time-ms}")
    private Long expiredTimeMs;


    public User loadUserByUserName(String userName) {
        return userEntityRepository.findByUserName(userName).map(User::fromEntity).orElseThrow(() ->
                new SnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s not founded", userName)));
    }


    @Transactional
    public User join(String userName, String password) {
        //회원가입하려는 userName으로 회원가입된 user가 있는지 체크, Optional<T> 클래스일 경우 ifPresent 사용가능, JPA 기본 메서드인 findById 등은 기본이 Optional
        userEntityRepository.findByUserName(userName).ifPresent(it -> {
            throw new SnsApplicationException(ErrorCode.DUPLICATED_USER_NAME, String.format("%s is duplicated", userName)); //각 예외에 대한 이유가 다르다.
        });

        //회원가입 진행, 비밀번호 저장할 때 암호화에서 저장
        UserEntity userEntity = userEntityRepository.save(UserEntity.of(userName, encoder.encode(password)));
        return User.fromEntity(userEntity);
    }

    public String login(String userName, String password) {
        //회원가입 여부 체크, Optional<T> 클래스일 경우 orElseThrow 사용가능
        UserEntity userEntity = userEntityRepository.findByUserName(userName).orElseThrow(() ->
                new SnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s not founded", userName)));

        //비밀번호 체크
        if(!encoder.matches(password, userEntity.getPassword())) {
            throw new SnsApplicationException(ErrorCode.INVALID_PASSWORD);
        }

        //토큰 생성 후 반환
        String token = JwtTokenUtils.generateToken(userName, secretKey, expiredTimeMs);
        return token;
    }

    public Page<Alarm> alarmList(String userName, Pageable pageable) {
        UserEntity userEntity = userEntityRepository.findByUserName(userName).orElseThrow(() ->
                new SnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s not founded", userName)));

        return alarmEntityRepository.findAllByUser(userEntity, pageable).map(Alarm::fromEntity);
    }
}
