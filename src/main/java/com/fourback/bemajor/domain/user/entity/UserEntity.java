package com.fourback.bemajor.domain.user.entity;

import com.fourback.bemajor.domain.studygroup.entity.StudyJoined;
import com.fourback.bemajor.domain.user.dto.request.UserUpdateRequestDto;
import com.fourback.bemajor.domain.user.dto.response.UserResponseDto;

import org.hibernate.annotations.SQLDelete;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@AllArgsConstructor
@SQLDelete(sql = "update user set is_deleted=true where user_id = ?")
@Builder
@Table(name = "user")
public class UserEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "user_id")
  private Long userId;

  @Column(name = "name")
  private String userName;

  @Column(name = "email")
  private String email;

  @Column(name = "birth")
  private String birth;

  @Column(name = "oauth2_id")
  private String oauth2Id;

  @Column(name = "role")
  private String role;

  @Column(name = "belong")
  private String belong;

  @Column(name = "department")
  private String department;

  @Column(name = "bobby")
  private String hobby;

  @Column(name = "objective")
  private String objective; //희망 분야

  @Column(name = "address")
  private String address;

  @Column(name = "tech_stack")
  private String techStack;

  @Setter
  @Column(name = "is_deleted")
  private boolean isDeleted;

  @Setter
  @Column(name = "image_url")
  private String imageUrl;

  @OneToMany(mappedBy = "user")
  private List<StudyJoined> studyJoineds;

  public UserResponseDto toUserResponseDto() {
    return UserResponseDto.builder()
                          .userId(this.userId)
                          .userName(this.userName)
                          .address(this.address)
                          .department(this.department)
                          .imageUrl(this.imageUrl)
                          .email(this.email)
                          .hobby(this.hobby)
                          .birth(this.birth)
                          .belong(this.belong)
                          .objective(this.objective)
                          .techStack(this.techStack)
                          .isDeleted(this.isDeleted)
                          .build();
  }

  public void update(UserUpdateRequestDto userUpdateRequestDto) {
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
