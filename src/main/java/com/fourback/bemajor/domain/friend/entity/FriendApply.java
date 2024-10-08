package com.fourback.bemajor.domain.friend.entity;

import com.fourback.bemajor.domain.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.*;

/**
 * 스터디 그룹 초대 엔티티
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FriendApply {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Long id;
  @ManyToOne
  private UserEntity user;
  @ManyToOne
  private UserEntity friend;
}
