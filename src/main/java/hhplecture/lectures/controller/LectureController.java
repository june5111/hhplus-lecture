package hhplecture.lectures.controller;

import hhplecture.lectures.entity.Lecture;
import hhplecture.lectures.service.LectureService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("lectures")
@RequiredArgsConstructor
public class LectureController {
    private LectureService lectureService;

    /**
     * 모든 특강 불러오기
     * @return
     */
    @GetMapping
    public List<Lecture> getAllLectures() {
        return lectureService.getAllLectures();
    }

    /**
     * 특강 신청
     * @param userId
     * @param lectureId
     * @return
     */
    @PostMapping("/apply")
    public String applyLecture(@RequestParam Long userId, @RequestParam Long lectureId) {
        boolean success = lectureService.applyLecture(userId,lectureId);
        if (success) {
            return "신청 성공";
        } else {
            return "신청 실패";
        }
    }

    /**
     * 특강 신청 여부
     * @param userId
     * @return
     */
    @GetMapping("/application/{userId}")
    public boolean checkApplicationStatus(@PathVariable Long userId) {
        return lectureService.checkApplicationStatus(userId);
    }

    /**
     * 특강 등록
     * @param lecture
     * @return
     */
    @GetMapping("/register")
    public Lecture registerLecture(@RequestBody Lecture lecture) {
        return lectureService.registerLecture(lecture);

    }

}
