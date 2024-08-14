package com.fourback.bemajor.domain.image.entity;

import com.fourback.bemajor.domain.user.entity.UserEntity;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToOne;
import lombok.Setter;

@Entity
@DiscriminatorValue("USER")
@Setter
public class UserImage  extends Image{
    @OneToOne(fetch = FetchType.LAZY)
    private UserEntity user;
}
