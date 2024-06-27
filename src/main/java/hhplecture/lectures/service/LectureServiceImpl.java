package hhplecture.lectures.service;

import hhplecture.lectures.entity.Lecture;
import hhplecture.lectures.entity.LectureApplication;
import hhplecture.lectures.repository.LectureApplicationRepository;
import hhplecture.lectures.repository.LectureRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class LectureServiceImpl implements LectureService{

    @Autowired
    private LectureRepository lectureRepository;
    @Autowired
    private LectureApplicationRepository lectureApplicationRepository;
    @PersistenceContext
    private EntityManager entityManager;

    /**
     * 특강 목록 가져오기
     * @return
     */
    @Override
    public List<Lecture> getAllLectures() {
        return lectureRepository.findAll();
    }

    /**
     * 강의 신청 (비관적 락 사용.)dd
     * @param userId
     * @param lectureId
     * @return
     */
    @Override
    @Transactional
    public boolean applyLecture(Long userId, Long lectureId) {
        Lecture lecture = entityManager.find(Lecture.class, lectureId, LockModeType.PESSIMISTIC_WRITE);
//        Lecture lecture = lectureRepository.findByIdWithLock(lectureId);

        //userId, 와 lectureId로 조회 시 존재하면 이미 신청한 강의이므로 false반환.
        if(lectureApplicationRepository.existsByUserIdAndLectureId(userId,lectureId)) {
            return false;
        }

        //신청자 수가 30이 넘으면 false반환
        long applicationCnt = lectureApplicationRepository.countByLectureId(lectureId);
        if(applicationCnt >= 30) {
            return false;
        }

        // 2개의 if문을 타지 않으면 특강 신청. 좀더 보완 필요... 시간이 없다..ㅠ
        LectureApplication lectureApplication = new LectureApplication();
        lectureApplication.setUserId(userId);
        lectureApplication.setLecture(lecture);
        lectureApplication.setSuccess(true);

        lectureApplicationRepository.save(lectureApplication);

        return true;
    }

    /**
     * 특강 신청 성공여부
     * @param userId
     * @return
     */
    @Override
    public boolean checkApplicationStatus(Long userId) {
        return lectureApplicationRepository.findByUserId(userId).stream().anyMatch(LectureApplication::isSuccess);
    }

    /**
     * 특강 등록
     * @param lecture
     * @return
     */
    @Override
    public Lecture registerLecture(Lecture lecture) {
        return lectureRepository.save(lecture);
    }
}
