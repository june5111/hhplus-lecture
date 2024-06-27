package hhplecture.lectures.service;

import hhplecture.lectures.entity.Lecture;
import org.springframework.stereotype.Service;

import java.util.List;


public interface LectureService {
    List<Lecture> getAllLectures();
    boolean applyLecture(Long userId, Long lectureId);
    boolean checkApplicationStatus(Long userId);
    Lecture registerLecture(Lecture lecture);
}
