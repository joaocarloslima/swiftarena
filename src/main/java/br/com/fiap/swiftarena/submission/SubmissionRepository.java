package br.com.fiap.swiftarena.submission;

import br.com.fiap.swiftarena.mission.Mission;
import br.com.fiap.swiftarena.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubmissionRepository extends JpaRepository<Submission, Long> {
    int countByUserAndMission(User user, Mission mission);

    boolean existsByUserAndMission(User user, Mission mission);

    boolean existsByUserAndMissionAndPassed(User user, Mission mission, boolean passed);

    List<Submission> findByUserAndMission(User user, Mission mission);
}
