package zin.rashidi.web.xss.greet;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author Rashidi Zin
 */
@Controller
class GreetResource {

    @GetMapping("/greet")
    public String greet(@RequestParam String name, Model model) {
        model.addAttribute("name", name);

        return "greet";
    }

}
