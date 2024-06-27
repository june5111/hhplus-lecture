package hhplecture.lectures.repository;

import hhplecture.lectures.entity.Lecture;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

public interface LectureRepository extends JpaRepository<Lecture, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Lecture findByIdWithLock(Long id);
}