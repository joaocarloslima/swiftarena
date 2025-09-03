package br.com.fiap.swiftarena.mission;

import br.com.fiap.swiftarena.lesson.LessonService;
import br.com.fiap.swiftarena.submission.SubmissionRepository;
import br.com.fiap.swiftarena.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/missions")
@RequiredArgsConstructor
public class MissionController {

    private static final String BASE_DIR = "submissions";
    private final MissionEvaluationService missionEvaluationService;
    private final LessonService lessonService;
    private final MissionService missionService;
    private final MissionRepository missionRepository;
    private final SubmissionRepository submissionRepository;

    @GetMapping
    public String showLessons(Model model, @AuthenticationPrincipal User user) {
        model.addAttribute("lessons", lessonService.getAllLessons());
        model.addAttribute("user", user);
        return "missions";
    }

    @GetMapping("/lessons/{lessonId}")
    public String showMissions(@PathVariable Long lessonId, Model model, @AuthenticationPrincipal User user) {
        if (!lessonService.isLessonActive(lessonId)) {
            return "redirect:/missions";
        }
        model.addAttribute("lessons", lessonService.getAllLessons());
        model.addAttribute("missions", missionService.getMissionsStepByLessonId(lessonId, user));
        model.addAttribute("user", user);
        model.addAttribute("activeLesson", lessonId);
        return "missions";
    }

    @GetMapping("/lessons/{lessonId}/{missionId}")
    public String showMissionDetail(@PathVariable Long lessonId, @PathVariable Long missionId, Model model, @AuthenticationPrincipal User user) {
        if (!lessonService.isLessonActive(lessonId)) {
            return "redirect:/missions";
        }

        model.addAttribute("lessons", lessonService.getAllLessons());
        model.addAttribute("missions", missionService.getMissionsStepByLessonId(lessonId, user));
        model.addAttribute("activeLesson", lessonId);
        model.addAttribute("mission", missionService.getMissionById(missionId));
        model.addAttribute("submissions", submissionRepository.findByUserAndMission(user, missionService.getMissionById(missionId)));
        model.addAttribute("user", user);
        return "missions";
    }

    @PostMapping
    public String submitCode(
            @RequestParam String code,
            @RequestParam String missionId,
            @AuthenticationPrincipal User user,
            RedirectAttributes redirectAttributes
    ) {
        var mission = missionRepository.findById(Long.parseLong(missionId))
                .orElseThrow(() -> new IllegalArgumentException("Mission not found"));

        var attempt = missionService.getNextAttemptNumber(user, mission);

        EvaluationResult result = missionEvaluationService.evaluate(code, user, mission, attempt);

        redirectAttributes.addFlashAttribute("success", result.success());
        redirectAttributes.addFlashAttribute("output", result.output());
        redirectAttributes.addFlashAttribute("code", code);

        return "redirect:/missions/lessons/" + mission.getLesson().getId() + "/" + missionId;
    }


}