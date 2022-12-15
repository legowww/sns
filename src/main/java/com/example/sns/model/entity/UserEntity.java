package com.example.sns.model.entity;


import com.example.sns.model.UserRole;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.Instant;

/**
 * - @Entity(name ="")의 경우 말그대로 엔티티의 이름을 정할때 사용됩니다. 이는 HQL에서 엔티티를 식별할 이름을 정합니다.
 * - @Table(name ="")의 경우 Database에 생성될 table의 이름을 지정할때 사용됩니다.
 * - @Table이 없고 @Entity(name ="")만 존재하는 경우, @Entity의 name 속성에 의해, Entity와 Table 이름이 모두 결정됩니다.
 */
@Entity
@Table(name = "\"user\"")
@Getter
@Setter
@SQLDelete(sql = "UPDATE user SET deleted_at = NOW() where id=?")
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
