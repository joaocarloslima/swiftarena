package br.com.fiap.swiftarena.mission;

import br.com.fiap.swiftarena.lesson.Lesson;
import br.com.fiap.swiftarena.submission.SubmissionRepository;
import br.com.fiap.swiftarena.user.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MissionService {

    private final MissionRepository missionRepository;
    private final SubmissionRepository submissionRepository;

    public MissionService(MissionRepository missionRepository, SubmissionRepository submissionRepository) {
        this.missionRepository = missionRepository;
        this.submissionRepository = submissionRepository;
    }

    public List<Mission> getMissionsByLessonId(Long lessonId) {
        return missionRepository.findByLesson(Lesson.builder().id(lessonId).build());
    }

    public Mission getMissionById(Long missionId) {
        return missionRepository.findByIdWithTests(missionId).orElseThrow(() -> new RuntimeException("Mission not found"));
    }

    public int getNextAttemptNumber(User user, Mission mission) {
        return submissionRepository.countByUserAndMission(user, mission) + 1;
    }

    public Object getMissionsStepByLessonId(Long lessonId, User user) {
        var missions = getMissionsByLessonId(lessonId);
        return missions.stream().map(mission -> {
            boolean haveSubmission = submissionRepository.existsByUserAndMission(user, mission);
            boolean completed = submissionRepository.existsByUserAndMissionAndPassed(user, mission, true);
            return new MissionStep(
                    mission.getId(),
                    mission.getTitle(),
                    mission.isChallenge(),
                    mission.getLesson(),
                    completed,
                    haveSubmission
            );
        }).toList();
    }

    public List<Mission> getAllMissions() {
        return missionRepository.findAll();
    }
}
