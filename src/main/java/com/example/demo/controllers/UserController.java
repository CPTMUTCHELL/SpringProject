package com.example.demo.controllers;

import com.example.demo.entities.User;
import com.example.demo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/loginPage")
    private String startPage(Model model){
        model.addAttribute("userToRegister",new User());
        return "startPage";
    }
    @PostMapping("/registration")
    private String registration(@ModelAttribute(name ="userToRegister")User userToRegister,
                                RedirectAttributes ra){

        boolean exist=userService.checkUser(userToRegister.getLogin());
        System.out.println(userToRegister);
        if (exist){
            ra.addFlashAttribute("exist",exist);
            return "redirect:/loginPage";
        }
        else {
            userService.save(userToRegister);
            ra.addFlashAttribute("success",true);
        }
        return "redirect:/loginPage";
    }

}
