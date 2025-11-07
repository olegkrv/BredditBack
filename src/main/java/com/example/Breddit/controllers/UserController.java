package com.example.Breddit.controllers;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Breddit.BredditApplication;
import com.example.Breddit.models.CurrentUser;
import com.example.Breddit.models.User;
import com.example.Breddit.repository.SubRepository;
import com.example.Breddit.service.UsersService;

import lombok.RequiredArgsConstructor;
//import com.example.Breddit.BredditApplication;;

@RestController
@RequestMapping("/br/users")
@RequiredArgsConstructor
@CrossOrigin("*")
public class UserController{
    // @NonNull List<User>
    public CurrentUser CURRENT = new CurrentUser();
    private final UsersService service;
    private final SubRepository sub_repository;

    @GetMapping
    public List<User> findAllUsers(){
        //System.out.println("((( Заходи не бойся, выходи не плачь )))");
        return service.findAllUsers();
    }

    @PostMapping("/save_user")
    public String saveUser(@RequestBody User user){
        if (service.saveUser(user) != null){
            CURRENT.setUser(user);
            return "Пользователь успешно добавлен!";}
        else return "Эта почта уже используется!";
        
    }

    @GetMapping("/{id}")
    public User findUserbyId(@PathVariable Long id){
        return service.findUserbyId(id);
    }

    @PutMapping("/update_user")
    public User updateUser(@RequestBody User user){
       return service.updateUser(user);
    }

    @DeleteMapping("/delete_user")
    public String deleteUser(){
        System.out.println(CURRENT.getId() + "==" + !(CURRENT.getAdmined_subs().isEmpty()) + "==" + CURRENT.getAdmined_subs());
        if (CURRENT.getId() !=null && !(CURRENT.getAdmined_subs().isEmpty())) {
            
            String message = "Вы не уможете удалить свой аккаунт, так как являетесь главным админом в следующих Саббреддитах:";
            for (Long sub_id: CURRENT.getAdmined_subs()){
                message += "\n" + sub_repository.findByid(sub_id).getTitle();
            }
            message += "\n" +  "Передайте в данных Саббредитах права главного админа другому пользователю. ";
            return message;
        }
        else if (service.deleteUser(CURRENT.getId())){
            CURRENT.logOut();
            return "Пользователь успешно удалён.";
        }
        return "Вы не вошли в аккаунт, чтобы его удалять...";
    }

    @GetMapping("/current")
    public ArrayList<String> getCurrentUser(){return CURRENT.getUser();}

    @PostMapping("/authorization")
    public String authUser(@RequestBody User user){
        return service.authUser(user, CURRENT, service.findUserbyEmail(user.getEmail()));
    }

    @GetMapping("/logout")
    public String logOut(){
        return service.logOut(CURRENT);
    }
}
