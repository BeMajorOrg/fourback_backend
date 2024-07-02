package com.fourback.bemajor.repository;

import com.fourback.bemajor.domain.StudyGroup;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudyGroupRepository extends JpaRepository<StudyGroup,Long> {
    Page<StudyGroup> findAll(Pageable pageable);
    Page<StudyGroup> findAllByCategory(String category,Pageable pageable);
}
