package com.tpisoftware.org.stlucia.ecommerce.controller;

import com.tpisoftware.org.stlucia.ecommerce.dto.UserDTO;
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

    // 去新增使用者的頁面
    @GetMapping("/register")
    public String viewCreatePage(Model model) {
        model.addAttribute("user", new UserDTO());
        model.addAttribute("editable", true);
        return "user";
    }

    // 去瀏覽使用者的頁面
    @GetMapping(value = "{id}")
    public String viewEditPage(@PathVariable("id") Long id,
                             @RequestParam(name = "editable", defaultValue = "false") Boolean editable,
                             Model model) {
        User user = userService.findById(id);

        UserDTO userDTO = new UserDTO(user.getId(), user.getEmail(), user.getPassword(), user.getName(), user.getAddress());
        model.addAttribute("user", userDTO);
        model.addAttribute("editable", editable);
        return "user";
    }

    // 註冊(新增使用者)
    @PostMapping
    public String create(@ModelAttribute UserDTO userDTO) {
        // 確保 UserDTO 中有 password 欄位
        User user = new User();
        user.setEmail(userDTO.getEmail());
        user.setName(userDTO.getName());
        user.setPassword(userDTO.getPassword());  // 修正為 userDTO.getPassword()
        user.setAddress(userDTO.getAddress());

        userService.register(user);
        return "login";
    }

    @PostMapping("login")
    public String login(@RequestParam("email") String email,
                        @RequestParam("password") String password,
                        Model model,
                        HttpSession session) {
        // 驗證用戶是否存在並匹配密碼
        User user = userService.findByEmail(email);
        if (user != null && user.getPassword().equals(password)) {
            session.setAttribute("loginUser", user.getName());
            model.addAttribute("user", user);
            // 驗證成功，重定向到會員資料頁面
            return "redirect:/user/" + user.getId();
        } else {
            // 驗證失敗，回到登入頁面並顯示錯誤訊息
            model.addAttribute("error", "無效的電子郵件或密碼");
            return "login";
        }
    }

    @PutMapping
    public String update(UserDTO userDTO, Model model) {
        User user = new User();
        user.setName(userDTO.getName());
        user.setAddress(userDTO.getAddress());

        userService.updateUser(userDTO.getId(), user);

        // 驗證成功，重定向到會員資料頁面
        return "redirect:/user/" + userDTO.getId();
    }

}