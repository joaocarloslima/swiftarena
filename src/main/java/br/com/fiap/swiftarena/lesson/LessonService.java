package br.com.fiap.swiftarena.lesson;

import br.com.fiap.swiftarena.submission.Submission;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LessonService {

    private final LessonRepository lessonRepository;

    public LessonService(LessonRepository lessonRepository) {
        this.lessonRepository = lessonRepository;
    }

    public List<Lesson> getAllLessons() {
        return lessonRepository.findAll();
    }

    public boolean isLessonActive(Long lessonId) {
        return lessonRepository.findById(lessonId).map(Lesson::isActive).orElse(false);
    }
}
