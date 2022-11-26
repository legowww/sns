package com.example.sns.service;


import com.example.sns.exception.ErrorCode;
import com.example.sns.exception.SnsApplicationException;
import com.example.sns.model.User;
import com.example.sns.model.entity.UserEntity;
import com.example.sns.repository.UserEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UserService {

    private final UserEntityRepository userEntityRepository;

    public User join(String username, String password) {
        //회원가입하려는 userName으로 회원가입된 user가 있는지 체크
        //Optional<T> 클래스일 경우 ifPresent 사용가능, JPA 기본 메서드인 findById 등은 기본이 Optional
        userEntityRepository.findByUserName(username).ifPresent(it -> {
            throw new SnsApplicationException(ErrorCode.DUPLICATED_USER_NAME, String.format("%s is duplicated", username)); //각 예외에 대한 이유가 다르다.
        });

        //회원가입 진행
        UserEntity userEntity = userEntityRepository.save(UserEntity.of(username, password));
        return User.fromEntity(userEntity);
    }

    public String login(String username, String password) {
        //존재하지 않는 아이디
        // Optional<T> 클래스일 경우 orElseThrow 사용가능
        UserEntity userEntity = userEntityRepository.findByUserName(username)
                .orElseThrow(() -> new SnsApplicationException(ErrorCode.DUPLICATED_USER_NAME, ""));

        //비밀번호 체크
        if (!userEntity.getPassword().equals(password)) {
            throw new SnsApplicationException(ErrorCode.DUPLICATED_USER_NAME, ""); //
        }

        //토큰 생성
        return "";
    }
}
