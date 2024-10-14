package com.tpisoftware.org.stlucia.ecommerce.controller;

import com.tpisoftware.org.stlucia.ecommerce.dto.user.UserCreateDTO;
import com.tpisoftware.org.stlucia.ecommerce.dto.user.UserDTO;
import com.tpisoftware.org.stlucia.ecommerce.mapper.UserMapper;
import com.tpisoftware.org.stlucia.ecommerce.model.User;
import com.tpisoftware.org.stlucia.ecommerce.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/register")
    public String getBlankInfo(Model model) {
        model.addAttribute("user", new UserCreateDTO());
        model.addAttribute("editable", true);
        return "user";
    }

    @PostMapping
    public String create(@ModelAttribute UserCreateDTO userDTO) {
        // 確保 UserDTO 中有 password 欄位
        User user = UserMapper.toModel(userDTO);

        userService.register(user);
        return "login";
    }

    @GetMapping(value = "{id}")
    public String browse(@PathVariable("id") Long id,
                         @RequestParam(name = "editable", defaultValue = "false") Boolean editable,
                         Model model) {
        User user = userService.findById(id);
        UserDTO userDTO = UserMapper.toDto(user);

        model.addAttribute("user", userDTO);
        model.addAttribute("editable", editable);

        return "user";
    }

    @PostMapping("login")
    public String login(@RequestParam("email") String email,
                        @RequestParam("password") String password,
                        Model model,
                        HttpSession session) {
        // 驗證用戶是否存在並匹配密碼
        User user = userService.findByEmail(email);
        UserDTO userDTO = UserMapper.toDto(user);

        if (user != null && user.getPassword().equals(password)) {
            session.setAttribute("loginUser", userDTO);
            model.addAttribute("user", userDTO);
            // 驗證成功，重定向到會員資料頁面
            return "redirect:/user/" + user.getId();
        } else {
            // 驗證失敗，回到登入頁面並顯示錯誤訊息
            model.addAttribute("error", "無效的電子郵件或密碼");
            return "login";
        }
    }

    @PutMapping
    public String update(UserDTO userDTO, HttpSession session) {
        User user = new User();
        user.setName(userDTO.getName());
        user.setAddress(userDTO.getAddress());

        userService.updateUser(userDTO.getId(), user);

        UserDTO loginUser = (UserDTO) session.getAttribute("loginUser");
        if (userDTO.getId().equals(loginUser.getId())) {
            loginUser.setName(userDTO.getName());
            loginUser.setAddress(userDTO.getAddress());
            session.setAttribute("loginUser", loginUser);
        }
        // 驗證成功，重定向到會員資料頁面
        return "redirect:/user/" + userDTO.getId();
    }

}