package com.davidpoza;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ControllerExample {
  @RequestMapping
  public String showPage() {
    return "example";
  }
  
  @RequestMapping("/form")
  public String showForm() {
    return "form";
  }
  
  @RequestMapping("/process-form")
  public String showResult() {
    return "result";
  }
  
  @RequestMapping("/form2")
  public String showForm2() {
    return "form2";
  }
  
  @RequestMapping("/process-form2")
  public String showResult2(HttpServletRequest request, Model model) {
    String name = request.getParameter("name");
    String upperCaseName = name.toUpperCase();
    model.addAttribute("upperCaseName", upperCaseName);
    return "result2";
  }
}
