package com.fourback.bemajor.domain.studygroup.entity;

import com.fourback.bemajor.domain.user.entity.UserEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "study_group_notification")
public class StudyGroupNotification {
    @Id
    @Column(name = "notification_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private StudyGroup studyGroup;

    @ManyToOne(fetch = FetchType.LAZY)
    private UserEntity user;
}
