package com.fourback.bemajor.domain.friend.entity;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import com.fourback.bemajor.domain.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.Instant;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Embeddable
public class FriendId implements Serializable {

    @Column(name = "account_id")
    private Long accountId;

    @Column(name = "friend_account_id")
    private Long friendAccountId;
}