package com.fourback.bemajor.domain.user.entity;

import com.fourback.bemajor.domain.image.entity.UserImage;
import com.fourback.bemajor.domain.studygroup.entity.StudyJoined;
import com.fourback.bemajor.domain.user.dto.request.UserRequestDto;
import com.fourback.bemajor.domain.user.dto.response.UserResponseDto;
import com.fourback.bemajor.domain.user.dto.response.UserWithImageResponseDto;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@SQLDelete(sql = "update user set is_deleted=true, name='<알 수 없음>' where user_id = ?")
@Builder
@Table(name = "user")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    Long userId;

    @Column(name = "name")
    String userName;

    @Column(name = "email")
    String email;

    @Column(name = "birth")
    String birth;

    @Column(name = "oauth2_id")
    String oauth2Id;

    @Column(name = "role")
    String role;

    @Column(name = "belong")
    String belong;

    @Column(name = "department")
    String department;

    @Column(name = "bobby")
    String hobby;

    @Column(name = "objective")
    String objective; //희망 분야

    @Column(name = "address")
    String address;

    @Column(name = "tech_stack")
    String techStack;

    @Setter
    @Column(name = "is_deleted")
    boolean isDeleted;

    @OneToMany(mappedBy = "user")
    List<StudyJoined> studyJoineds = new ArrayList<>();

    @Setter
    @OneToOne(mappedBy = "user",cascade = CascadeType.REMOVE,
            orphanRemoval = true, fetch = FetchType.LAZY)
    UserImage userImage;

    public UserWithImageResponseDto toUserWithImageDto() {
        UserWithImageResponseDto userWithImageResponseDto = new UserWithImageResponseDto(
                this.userName, this.email, this.birth, this.belong, this.department,
                this.hobby, this.objective, this.address, this.techStack, this.isDeleted);
        if (userImage != null)
            userWithImageResponseDto.setImageName(userImage.getFileName());
        return userWithImageResponseDto;
    }

    public UserResponseDto toUserResponseDto() {
        return new UserResponseDto(this.userName, this.email, this.birth,
                this.belong, this.department, this.hobby, this.objective,
                this.address, this.techStack, this.isDeleted);
    }

    public void setUserEntity(UserRequestDto userRequestDto) {
        this.userName = userRequestDto.getUserName();
        this.email = userRequestDto.getEmail();
        this.birth = userRequestDto.getBirth();
        this.belong = userRequestDto.getBelong();
        this.department = userRequestDto.getDepartment();
        this.hobby = userRequestDto.getHobby();
        this.objective = userRequestDto.getObjective();
        this.address = userRequestDto.getAddress();
        this.techStack = userRequestDto.getTechStack();
    }
}
