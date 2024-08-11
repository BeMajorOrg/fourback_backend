package com.fourback.bemajor.domain.image.entity;

import com.fourback.bemajor.domain.community.entity.Post;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@DiscriminatorValue("POST")
@Setter
@Getter
public class PostImage extends Image{
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;
}
