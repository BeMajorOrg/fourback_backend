package com.fourback.bemajor.domain;

import com.fourback.bemajor.dto.UserDto;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long userId;
    String userName;
    // 게시판 연동해줘야 함
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

    public UserDto toUserDto() {
        UserDto userDto = new UserDto();
        userDto.setUserName(this.userName);
        userDto.setEmail(this.email);
        userDto.setBirth(this.birth);
        userDto.setBelong(this.belong);
        userDto.setDepartment(this.department);
        userDto.setHobby(this.hobby);
        userDto.setObjective(this.objective);
        userDto.setAddress(this.address);
        userDto.setTechStack(this.techStack);
        return userDto;
    }
}
