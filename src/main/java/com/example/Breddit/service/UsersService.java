package com.example.Breddit.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.Breddit.controllers.PostController;
import com.example.Breddit.models.CurrentUser;
import com.example.Breddit.models.User;
import com.example.Breddit.repository.UserReposiotry;

import lombok.RequiredArgsConstructor;



public interface UsersService{


    List<User> findAllUsers();

    User saveUser(User user);

    User findUserbyId(Long id);

    User findUserbyEmail(String email);

    User updateUser(User user);

    boolean deleteUser(Long id);

    
    default String authUser(User user, CurrentUser CURRENT, User potetnial_user){

        if (potetnial_user == null) return "Такой почты не существует";
        
        String ur_password = user.getPassword();

        if (potetnial_user.getPassword().equals(ur_password) && ur_password != null){
            CURRENT.setUser(potetnial_user);
            return "".format("Рады видеть вас снова, %s", CURRENT.getNickname());
        }
        return "Неверный пароль";
    };

    default String logOut(CurrentUser CURRENT){
        try {
          CURRENT.logOut();
        return "Вы успешно вышли из аккаунта!";  
        }
        catch (Exception e){
            return "Что-то пошло не так: " + e;
        }
    }
}
    

