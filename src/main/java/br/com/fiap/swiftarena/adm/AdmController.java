package br.com.fiap.swiftarena.adm;

import br.com.fiap.swiftarena.mission.Mission;
import br.com.fiap.swiftarena.mission.MissionRequest;
import br.com.fiap.swiftarena.mission.TestCaseDto;
import br.com.fiap.swiftarena.user.Role;
import br.com.fiap.swiftarena.user.User;
import br.com.fiap.swiftarena.user.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AdmController {

    private final UserRepository userRepository;
    private final AdmService admService;
    private final ObjectMapper objectMapper;

    private record ObservationRequest(String observation){}

    public AdmController(UserRepository userRepository, AdmService admService, ObjectMapper objectMapper) {
        this.userRepository = userRepository;
        this.admService = admService;
        this.objectMapper = objectMapper;
    }

    @GetMapping("/adm2")
    public String adm(Model model, @AuthenticationPrincipal User user) {
        model.addAttribute("students", admService.getStudentCards());
        model.addAttribute("user", user);
        return "adm";
    }

    @GetMapping("/obs/{userId}")
    @ResponseBody
    public String getObs(@PathVariable Long userId){
        var user = userRepository.findById(userId).orElseThrow(
                () -> new IllegalArgumentException("Usuário não encontrado")
        );
        return user.getObservation();
    }

    @PostMapping("obs/{userId}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void saveObs(@PathVariable Long userId, @RequestBody ObservationRequest observation){
        var user = userRepository.findById(userId).orElseThrow(
                () -> new IllegalArgumentException("Usuário não encontrado")
        );
        user.setObservation(observation.observation);
        userRepository.save(user);
    }

    @GetMapping("lessons")
    public String lessons(Model model, @AuthenticationPrincipal User user){
        model.addAttribute("missions", admService.getAllMissions());
        model.addAttribute("user", user);
        return "lessons";
    }

    @GetMapping("lessons/form")
    public String lessonsForm(Model model, @AuthenticationPrincipal User user, @RequestParam(required = false) Long id) throws JsonProcessingException {
        model.addAttribute("user", user);
        if (id != null) {
            Mission mission = admService.getMissionById(id);
            model.addAttribute("mission", mission);
            // Convert test cases to DTOs to avoid circular reference, then serialize to JSON
            var testCaseDtos = mission.getTests().stream()
                    .map(TestCaseDto::from)
                    .toList();
            String testCasesJson = objectMapper.writeValueAsString(testCaseDtos);
            model.addAttribute("testCasesJson", testCasesJson);
        } else {
            model.addAttribute("mission", new Mission());
            model.addAttribute("testCasesJson", "[]");
        }
        return "lessons-form";
    }

    @PostMapping("lessons/form")
    public String saveLesson(MissionRequest missionRequest) {

        admService.saveMission(missionRequest);
        return "redirect:/lessons";
    }

}
