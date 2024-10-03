package com.fourback.bemajor.domain.friend.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "friend")
public class Friend {

    @EmbeddedId
    private FriendId id;
}