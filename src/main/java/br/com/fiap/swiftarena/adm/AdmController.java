package br.com.fiap.swiftarena.adm;

import br.com.fiap.swiftarena.user.Role;
import br.com.fiap.swiftarena.user.User;
import br.com.fiap.swiftarena.user.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AdmController {

    private final UserRepository userRepository;
    private final AdmService admService;

    private record ObservationRequest(String observation){}

    public AdmController(UserRepository userRepository, AdmService admService) {
        this.userRepository = userRepository;
        this.admService = admService;
    }

    @GetMapping("/adm")
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

}
