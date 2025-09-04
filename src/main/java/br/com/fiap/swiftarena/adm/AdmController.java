package br.com.fiap.swiftarena.adm;

import br.com.fiap.swiftarena.user.Role;
import br.com.fiap.swiftarena.user.User;
import br.com.fiap.swiftarena.user.UserRepository;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdmController {

    private final UserRepository userRepository;
    private final AdmService admService;

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
}
