package br.com.fiap.swiftarena.mission;

import br.com.fiap.swiftarena.lesson.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MissionRepository extends JpaRepository<Mission, Long> {
    List<Mission> findByLesson(Lesson lesson);

    @Query("SELECT m FROM Mission m LEFT JOIN FETCH m.tests WHERE m.id = :id")
    Optional<Mission> findByIdWithTests(@Param("id") Long id);
}
