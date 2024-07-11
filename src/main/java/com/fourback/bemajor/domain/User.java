package com.fourback.bemajor.domain;

import com.fourback.bemajor.dto.UserDto;
import com.fourback.bemajor.dto.UserWithImageDto;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@SQLDelete(sql = "update user set is_deleted=true, user_name='<알 수 없음>' where user_id = ?")
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long userId;
    String userName;
    String email;
    String birth;
    String oauth2Id;
    String role;
    String belong;
    String department;
    String hobby;
    String objective; //희망 분야
    String address;
    String techStack;
    @Setter
    boolean isDeleted;
    @OneToMany(mappedBy = "user")
    List<StudyJoined> studyJoineds = new ArrayList<>();

    @Setter
    @OneToOne(mappedBy = "user",cascade = CascadeType.REMOVE, orphanRemoval = true)
    UserImage userImage;

    public UserWithImageDto toUserWithImageDto() {
        UserWithImageDto userWithImageDto = new UserWithImageDto();
        userWithImageDto.setUserName(this.userName);
        userWithImageDto.setEmail(this.email);
        userWithImageDto.setBirth(this.birth);
        userWithImageDto.setBelong(this.belong);
        userWithImageDto.setDepartment(this.department);
        userWithImageDto.setHobby(this.hobby);
        userWithImageDto.setObjective(this.objective);
        userWithImageDto.setAddress(this.address);
        userWithImageDto.setTechStack(this.techStack);
        if(userImage!=null)
            userWithImageDto.setImageName(userImage.getFileName());
        userWithImageDto.setDeleted(this.isDeleted);
        return userWithImageDto;
    }

    public UserDto toUserDto() {
        return new UserDto(this.userName
                , this.email, this.birth, this.belong, this.department, this.hobby, this.objective, this.address, this.techStack, this.isDeleted);
    }

    public void setUserDto(UserDto userDto) {
        this.userName = userDto.getUserName();
        this.email = userDto.getEmail();
        this.birth = userDto.getBirth();
        this.belong = userDto.getBelong();
        this.department = userDto.getDepartment();
        this.hobby = userDto.getHobby();
        this.objective = userDto.getObjective();
        this.address = userDto.getAddress();
        this.techStack = userDto.getTechStack();
        this.isDeleted=userDto.isDeleted();
    }
}
