package br.com.fiap.swiftarena.adm;

import br.com.fiap.swiftarena.lesson.LessonRepository;
import br.com.fiap.swiftarena.submission.Submission;
import br.com.fiap.swiftarena.submission.SubmissionRepository;
import br.com.fiap.swiftarena.user.Role;
import br.com.fiap.swiftarena.user.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AdmService {

    private final UserRepository userRepository;
    private final LessonRepository lessonRepository;
    private final SubmissionRepository submissionRepository;

    public AdmService(UserRepository userRepository, LessonRepository lessonRepository, SubmissionRepository submissionRepository) {
        this.userRepository = userRepository;
        this.lessonRepository = lessonRepository;
        this.submissionRepository = submissionRepository;
    }

    public List<StudentCardDto> getStudentCards() {
        var studentCards = new ArrayList<StudentCardDto>();
        var students = userRepository.findAllByRoleOrderByName(Role.ROLE_STUDENT);
        for (var student : students) {
            var studentCard = new StudentCardDto();
            studentCard.setId(student.getId());
            studentCard.setName(student.getName());
            studentCard.setAvatarUrl(student.getAvatarUrl());

            var lessons = lessonRepository.findAll();
            var missionCards = new ArrayList<MissionCardDto>();
            for (var lesson : lessons) {
                var missionCard = new MissionCardDto();
                missionCard.setLesson(lesson);
                var missions = lesson.getMissions();
                var submissionCards = new ArrayList<SubmissionCardDto>();
                for (var mission: missions){
                    var submissions = submissionRepository.findByUserAndMission(student, mission);
                    var submissionCard = new SubmissionCardDto();
                    submissionCard.setAttempts(submissions.size());
                    submissionCard.setPassed(submissions.stream().anyMatch(Submission::isPassed));
                    submissionCard.setClassName("badge-neutral");
                    var opacity = 30;
                    if(!submissionCard.isPassed() && submissionCard.getAttempts()>0) {
                        submissionCard.setClassName("badge-error");
                        opacity = 90;
                    }
                    if (submissionCard.getAttempts() > 0 && submissionCard.isPassed()){
                        opacity = 100 - (submissionCard.getAttempts() - 1) * 20;
                        submissionCard.setClassName("badge-success");
                    }
                    if (opacity < 20) opacity = 20;
                    submissionCard.setOpacity(opacity);
                    submissionCards.add(submissionCard);
                }
                missionCard.setSubmissions(submissionCards);
                missionCards.add(missionCard);
            }
            studentCard.setMissions(missionCards);

            studentCard.setTotalScore(calculateTotalScore(missionCards));

            studentCards.add(studentCard);

        }
        return studentCards;
    }

    private int calculateTotalScore(List<MissionCardDto> missionCards) {
        int totalMissions = 0;
        int totalPoints = 0;

        for (var missionCard : missionCards) {
            for (var submissionCard : missionCard.getSubmissions()) {
                totalMissions++;

                if (submissionCard.isPassed()) {
                    totalPoints += 10;
                    totalPoints -= (submissionCard.getAttempts() - 1) * 2;
                } else {
                    totalPoints -= submissionCard.getAttempts() * 2;
                }
            }
        }

        int maxPoints = totalMissions * 10;
        int scorePercent = maxPoints > 0 ? (int) Math.round(100.0 * totalPoints / maxPoints) : 0;
        if (scorePercent < 0) scorePercent = 0;

        return  scorePercent;
    }

}
