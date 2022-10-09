package com.davidpoza;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/alumno")
public class AlumnoController {

  @RequestMapping
  public String showIndex() {
    return "index";
  }
  
  @RequestMapping("/sign-up")
  public String showForm(Model model) {
    Alumno alumno = new Alumno();
    model.addAttribute("alumno", alumno);
    return "signUpForm";
  }
  
  @RequestMapping("/process-form")
  public String processForm(@ModelAttribute("alumno") Alumno alumno) {
    
    return "confirmation";
  }
}
