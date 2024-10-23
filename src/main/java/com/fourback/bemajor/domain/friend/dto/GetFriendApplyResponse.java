package com.fourback.bemajor.domain.friend.dto;

import com.fourback.bemajor.domain.friend.entity.FriendApply;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 스터디 그룹 초대 응답
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class GetFriendApplyResponse {
  private Long applyId;
  private Long userId;
  private Long friendId;
  private String friendName;
  private String friendImage;

  public static GetFriendApplyResponse from(FriendApply friendApply) {
    return new GetFriendApplyResponse(
            friendApply.getId(),
            friendApply.getFriend().getId(),
            friendApply.getUser().getId(),
            friendApply.getUser().getUserName(),
            friendApply.getUser().getImageUrl());
  }
}
