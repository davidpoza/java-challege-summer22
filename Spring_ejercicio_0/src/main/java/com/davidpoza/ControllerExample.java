package com.davidpoza;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ControllerExample {
  @RequestMapping
  public String showPage() {
    return "example";
  }
}
