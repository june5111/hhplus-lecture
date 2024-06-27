package hhplecture.lectures.repository;

import hhplecture.lectures.entity.Lecture;
import hhplecture.lectures.entity.LectureApplication;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.util.List;

public interface LectureApplicationRepository extends JpaRepository<LectureApplication,Long> {
    List<LectureApplication> findByUserId(Long userId);//
    long countByLectureId(Long lectureId);
    boolean existsByUserIdAndLectureId(Long userId, Long lectureId);



}
