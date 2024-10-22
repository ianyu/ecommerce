package com.tpisoftware.org.stlucia.ecommerce.controller;

import com.tpisoftware.org.stlucia.ecommerce.dto.CartItemDTO;
import com.tpisoftware.org.stlucia.ecommerce.dto.user.UserCreateDTO;
import com.tpisoftware.org.stlucia.ecommerce.dto.user.UserLoginDTO;
import com.tpisoftware.org.stlucia.ecommerce.mapper.UserMapper;
import com.tpisoftware.org.stlucia.ecommerce.model.User;
import com.tpisoftware.org.stlucia.ecommerce.service.CartService;
import com.tpisoftware.org.stlucia.ecommerce.service.UserService;
import com.tpisoftware.org.stlucia.ecommerce.util.JwtUtil;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserService userService;

    @Autowired
    private CartService cartService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(UserCreateDTO dto) {
        dto.setPassword(passwordEncoder.encode(dto.getPassword()));
        User user = UserMapper.toModel(dto);
        userService.register(user);

        return "redirect:/auth/login";
    }

    @GetMapping("/login")
    public String showLoginForm(@RequestParam(value = "errorMessage", required = false) String errorMessage,
//                                @CookieValue(value = "token", required = false) String token,
                                HttpSession session,
                                Model model) {
        String result = "login";

        String jwtToken = (String) session.getAttribute("jwtToken");
        if (jwtToken != null && jwtToken.length() > 0) {
            result = "redirect:/product/list";
        } else {
            if (errorMessage != null) {
                model.addAttribute("errorMessage", errorMessage);
            }
        }
        return result;
    }

    @PostMapping("/login")
    @ResponseBody
    public ResponseEntity<?> login(@RequestBody UserLoginDTO userLoginDTO,
//                                   HttpServletResponse response,
                                   HttpSession session) {
        try {
            authenticate(userLoginDTO.getUsername(), userLoginDTO.getPassword());

            // 生成JWT Token
            User user = userService.findByEmail(userLoginDTO.getUsername());
            String jwtToken = jwtUtil.generateToken(user);

            session.setAttribute("jwtToken", jwtToken);


            List<CartItemDTO> sessionCartItems = (List<CartItemDTO>) session.getAttribute("cartItems");
            if (sessionCartItems != null) {
                Long loginUserId = jwtUtil.extractUserId(jwtToken);
                cartService.clearCart(loginUserId);
                sessionCartItems.forEach(item ->
                        cartService.addToCart(loginUserId, item.getProductId(), item.getQuantity()));
                session.removeAttribute(CartItemController.ATTRIBUTE_NAME);
            }

            return ResponseEntity.ok("Login successful");
        } catch (Exception e) {
            return ResponseEntity.status(401).body(e.getMessage());
        }
    }

    private boolean authenticate(String username, String password) {
        try {
            User user = userService.findByEmail(username);
            if (user == null) {
                throw new RuntimeException("User not found: " + username);
            } else if (!passwordEncoder.matches(password, user.getPassword())) {
                throw new RuntimeException("invalid password");
            }
        } catch (Exception e) {
            throw new RuntimeException("invalid username");
        }
        return true;
    }
}