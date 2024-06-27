package hhplecture.lectures;

import hhplecture.lectures.entity.Lecture;
import hhplecture.lectures.entity.LectureApplication;
import hhplecture.lectures.repository.LectureApplicationRepository;
import hhplecture.lectures.repository.LectureRepository;
import hhplecture.lectures.service.LectureService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.*;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
public class LectureServiceImplTest {
    @Autowired
    private LectureService lectureService;

    @Autowired
    private LectureRepository lectureRepository;

    @Autowired
    private LectureApplicationRepository lectureApplicationRepository;

    @BeforeEach
    public void setup() {
        lectureApplicationRepository.deleteAll();
        lectureRepository.deleteAll();

//        Lecture lecture = new Lecture();
//        lecture.setTitle("tdd");
//        lecture.setDate(new Date());
//        lectureRepository.save(lecture);
    }

    @Test
    @Transactional
    @DisplayName("모든 특강 불러오기")
    public  void getAllLectureTest() {
        Lecture lecture = new Lecture();
        lecture.setTitle("tdd");
        lecture.setDate(new Date());
        lectureRepository.save(lecture);

        List<Lecture> lectures = lectureService.getAllLectures();
        assertThat(lectures).hasSize(1);
        assertThat(lectures.get(0).getTitle()).isEqualTo("tdd");


    }

    @Test
    @Transactional
    @DisplayName("강의 신청 성공")
    public void applyLectureTest() {
        Lecture lecture = new Lecture();
        lecture.setTitle("tdd");
        lecture.setDate(new Date());
        lectureRepository.save(lecture);

        boolean success = lectureService.applyLecture(1L, lecture.getId());
        assertThat(success).isTrue();

    }

    @Test
    @Transactional
    @DisplayName("중복강의 신청 불가")
    public void applyLectureDupFail() {
        Lecture lecture = new Lecture();
        lecture.setTitle("tdd");
        lecture.setDate(new Date());
        lectureRepository.save(lecture);

        lectureService.applyLecture(1L, lecture.getId());
        boolean success = lectureService.applyLecture(1L, lecture.getId());
        assertThat(success).isFalse();

        List<LectureApplication> applicationList = lectureApplicationRepository.findByUserId(1L);
        assertThat(applicationList).hasSize(1);

    }

    @Test
    @DisplayName("비관적락을 사용한 동시성 테스트")
    public void pessimisticTest () throws InterruptedException, ExecutionException {



        Lecture lecture = lectureRepository.findAll().get(0);

        int cnt = 35;
        ExecutorService executorService = Executors.newFixedThreadPool(cnt);
        List<Callable<Boolean>> tasks = new ArrayList<>();

        for (int i = 1; i<=cnt; i++) {
            final  long userId = i;
            tasks.add(() -> {
                try {
                    return lectureService.applyLecture(userId, lecture.getId());
                } catch (Exception e) {
                    return false;
                }
            });
        }

        List<Future<Boolean>> results = executorService.invokeAll(tasks);

        int successCnt = 0;
        int failCnt = 0;

        for(Future<Boolean> result : results) {
            if(result.get()) {
                successCnt++;
            } else {
                failCnt++;
            }
        }

        executorService.shutdown();

        assertThat(successCnt).isEqualTo(30);
        assertThat(failCnt).isEqualTo(5);


    }


}
