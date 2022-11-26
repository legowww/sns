package com.example.sns.model.entity;


import com.example.sns.model.UserRole;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.Instant;

@Entity
@Table(name = "\"user\"") //실제 테이블 이름 "user"
@Getter
@Setter
@SQLDelete(sql = "UPDATED \"user\" SET deleted_at = NOW() where id=?")
@Where(clause = "deleted_at is NULL") //삭제되지 않는 것만 where
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * username 필드명 사용시 에러 발생 (Reason: Failed to create query for method public abstract)
     * 이유: repository 의 메서드인 findByUserName 이랑 매칭이 되지 않는다.
     *  -> userName 으로 수정
     */
    @Column(name = "user_name")
    private String userName; //

    @Column(name = "password")
    private String password;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private UserRole role = UserRole.USER;

    @Column(name = "register_at")
    private Timestamp registeredAt;

    @Column(name = "updated_at")
    private Timestamp updatedAt;

    @Column(name = "deleted_at")
    private Timestamp deletedAt;

    @PrePersist //저장되기 전에 작동
    void registeredAt() {
        this.registeredAt = Timestamp.from(Instant.now());
    }

    @PreUpdate //수정하기 전에 작동
    void updatedAt() {
        this.updatedAt = Timestamp.from(Instant.now());
    }

    //새 엔티티를 만드는 메서드
    //서비스에서는 DTO를 통해서만 데이터를 다뤄야한다.
    public static UserEntity of(String userName, String password) {
        UserEntity userEntity = new UserEntity();
        userEntity.setUserName(userName);
        userEntity.setPassword(password);
        return userEntity;
    }
}
