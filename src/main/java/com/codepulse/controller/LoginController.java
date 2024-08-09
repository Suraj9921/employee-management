package com.codepulse.controller;

import com.codepulse.model.User;
import com.codepulse.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Collection;

@Controller
public class LoginController {
    private UserService userService;

    public LoginController(UserService userService) {
        this.userService = userService;
    }

    @InitBinder
    public void initBinder(WebDataBinder dataBinder) {
        StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
        dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
    }

    @GetMapping("/")
    public String home(Authentication authentication) {
        // Check if the user is authenticated
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return "index"; // Show the index page if not authenticated
        }

        // Redirect based on user roles
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        for (GrantedAuthority grantedAuthority : authorities) {
            if (grantedAuthority.getAuthority().equals("USER")) {
                return "redirect:/home";
            } else if (grantedAuthority.getAuthority().equals("ADMIN")) {
                return "redirect:/dashboard";
            }
        }

        // Fallback redirect if no roles match (this case should generally not happen)
        return "index";
    }


    @GetMapping("/sigin")
    public String loginForm(HttpServletRequest request, Authentication authentication) {
        // Check if the user is already authenticated
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return "sigin"; // Show the login page if not authenticated
        }

        // If the user is authenticated, retrieve their session
        HttpSession session = request.getSession();
        Object attribute = session.getAttribute("userLoggedIn");

        // Check if the user is already logged in and stored in session
        if (attribute != null) {
            // Redirect based on user roles
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            for (GrantedAuthority grantedAuthority : authorities) {
                if (grantedAuthority.getAuthority().equals("USER")) {
                    return "redirect:/home";
                } else if (grantedAuthority.getAuthority().equals("ADMIN")) {
                    return "redirect:/dashboard";
                }
            }
        }

        // Default redirect if user is authenticated but not found in session
        return "redirect:/home";
    }


    @GetMapping("/register")
    public String showRegistrationForm(Model model){
        User user = new User();
        model.addAttribute("user", user);
        return "register";
    }

    @PostMapping("/register/save")
    public String registration(@Valid @ModelAttribute("user") User user, BindingResult result, Model model){
        User existing = userService.findByEmail(user.getEmail());
        if (existing != null) {
            result.rejectValue("email", null, "There is already an account registered with that email");
        }
        if (result.hasErrors()) {
            model.addAttribute("user", user);
            return "register";
        }
        userService.saveUser(user);
        return "redirect:/register?success";
    }
}
