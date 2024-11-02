package com.fourback.bemajor.domain.user.entity;

import com.fourback.bemajor.domain.studygroup.entity.StudyJoined;
import com.fourback.bemajor.domain.user.dto.request.UserUpdateRequestDto;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@Table(name = "user")
@AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@SQLDelete(sql = "update user set is_deleted=true, name='<알 수 없음>', email='', birth=''," +
        " belong='', department='', hobby='', objective='', address='', tech_stack='' where user_id = ?")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "name")
    private String userName;

    @Column(name = "email")
    private String email;

    @Column(name = "birth")
    private String birth;

    @Column(name = "oauth2_id", unique = true, nullable = false, updatable = false)
    private String oauth2Id;

    @Column(name = "role", nullable = false)
    private String role;

    @Column(name = "belong")
    private String belong;

    @Column(name = "department")
    private String department;

    @Column(name = "hobby")
    private String hobby;

    @Column(name = "objective")
    private String objective; //희망 분야

    @Column(name = "address")
    private String address;

    @Column(name = "tech_stack")
    private String techStack;

    @Setter
    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted;

    @Setter
    @Column(name = "image_url")
    private String imageUrl;

    @OneToMany(mappedBy = "user")
    private List<StudyJoined> studyJoinedList;

    public static UserEntity from(String oauth2Id) {
        return UserEntity.builder()
                .isDeleted(false)
                .role("ROLE_USER")
                .oauth2Id(oauth2Id)
                .studyJoinedList(new ArrayList<>())
                .build();
    }

    public void modify(UserUpdateRequestDto userUpdateRequestDto) {
        this.userName = userUpdateRequestDto.getUserName();
        this.email = userUpdateRequestDto.getEmail();
        this.birth = userUpdateRequestDto.getBirth();
        this.belong = userUpdateRequestDto.getBelong();
        this.department = userUpdateRequestDto.getDepartment();
        this.hobby = userUpdateRequestDto.getHobby();
        this.objective = userUpdateRequestDto.getObjective();
        this.address = userUpdateRequestDto.getAddress();
        this.techStack = userUpdateRequestDto.getTechStack();
    }
}
