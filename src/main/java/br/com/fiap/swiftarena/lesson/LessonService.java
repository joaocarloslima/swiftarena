package br.com.fiap.swiftarena.lesson;

import br.com.fiap.swiftarena.submission.Submission;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class LessonService {

    private final LessonRepository lessonRepository;

    public LessonService(LessonRepository lessonRepository) {
        this.lessonRepository = lessonRepository;
    }

    public List<Lesson> getAllLessons() {
        return lessonRepository.findAllByOrderByNumber();
    }

    public boolean isLessonActive(Long lessonId) {
        return lessonRepository.findById(lessonId).map(Lesson::isActive).orElse(false);
    }

    public Lesson getLessonById(Long aLong) {
        return lessonRepository.findById(aLong).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Lesson not found"));
    }
}
