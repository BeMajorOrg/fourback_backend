package com.fourback.bemajor.domain;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import lombok.Setter;

@Entity
@DiscriminatorValue("USER")
@Setter
public class UserImage  extends Image{
    @OneToOne
    private User user;
}
