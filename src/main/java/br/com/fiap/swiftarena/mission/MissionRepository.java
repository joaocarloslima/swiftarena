package br.com.fiap.swiftarena.mission;

import br.com.fiap.swiftarena.lesson.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MissionRepository extends JpaRepository<Mission, Long> {
    List<Mission> findByLesson(Lesson lesson);
}
