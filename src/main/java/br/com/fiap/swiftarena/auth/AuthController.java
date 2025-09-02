package br.com.fiap.swiftarena.auth;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {

    @GetMapping("/login")
    public String loginPage(@RequestParam(value = "error", required = false) String error, Model model) {
        if (error != null)
            model.addAttribute("error", "E-mail ou senha inválidos.");
        return "login";
    }

    @GetMapping("logout")
    public String logoutPage() {
        return "logout";
    }


}
