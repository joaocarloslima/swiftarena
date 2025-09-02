package br.com.fiap.swiftarena.adm;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdmController {

    @GetMapping("/adm")
    public String adm() {
        return "adm";
    }
}
