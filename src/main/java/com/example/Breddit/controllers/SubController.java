package com.example.Breddit.controllers;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;


import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Breddit.models.Sub;
import com.example.Breddit.service.JPA.SubServiceJPA;

import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/br/")
@RequiredArgsConstructor
@CrossOrigin("*")
public class SubController {
    private final SubServiceJPA service;
    private final UserController user_controller;

    @GetMapping
    public List<Sub> findAllSubs(){
        return service.findAllSubs();
    }

    @PostMapping("/add_subbreddit")
    public String addSub(@RequestBody Sub sub){
        try{
           if (user_controller.CURRENT.getId() == null) return "Необходимо войти в аккаунт, чтобы создать саббреддит!";
           
        else if (sub.getTitle().split(" ").length == 0 || sub.getTitle().equals("")) return "Название саббреддита не может быть пустым!";
        
        else if (!(sub.getTitle().matches("^[a-zA-Z0-9]+$"))) return "Можно использовать только латинские буквы!";

        if (service.addSub(sub) != null) return "Саббреддит успешно добавлен!";
        else return "".format("Саббреддит %s уже есть в базе данных!", sub.getTitle());
        }

        catch (Exception e) {return "Ошибка создания саббреддита: " + e;}
        
    }

    @PatchMapping("/update_subbreddit")
    public String updateSub(@RequestBody Long id, @RequestBody String title, @RequestBody String description){
         if (user_controller.CURRENT.getId() == null) return "Необходимо войти в аккаунт, чтобы обновить саббреддит!";

        else if (!(user_controller.CURRENT.getId().equals(service.findById(id).getMain_admin()) ||
        service.findById(id).getAdmins().contains(user_controller.CURRENT.getId()))) return "У вас нет прав администратора, чтобы обновить данный Саббреддит!";
        Sub sub = service.findById(id);
        sub.setTitle(title);
        sub.setDescription(description);
        service.addSub(sub);
        sub = null;
        return "Саббреддит успешно обновлён!";
    }

    @GetMapping("/{title}")
    public Sub findById(@PathVariable String title){
        return service.findByTitle(title);
    }

    @DeleteMapping("/delete_subbreddit/{id}")
    public String deleteSub(@PathVariable Long id){
        if (user_controller.CURRENT.getId() == null) return "Необходимо войти в аккаунт, чтобы удалить саббреддит!";

        else if (!(user_controller.CURRENT.getId().equals(service.findById(id).getMain_admin()) ||
        service.findById(id).getAdmins().contains(user_controller.CURRENT.getId()))) return "У вас нет прав администратора, чтобы удалить данный Саббреддит!";

        if (service.deleteSub(id)) return "Саббреддит успешно удалён!";
        return "Саббреддита с таким id не существует!";
    }

    @PutMapping("/{title}/add_admin/{id}")
    public String addAdmin(@PathVariable String title, @PathVariable Long id){
        if (user_controller.CURRENT.getId() == null) return "Необходимо войти в аккаунт, чтобы добавить нового админа Саббреддита!";

        else if (!(user_controller.CURRENT.getId().equals(service.findByTitle(title).getMain_admin()))) return "У вас нет прав главного администратора, чтобы добавить нового админа Саббреддита!";

        service.addAdmin(service.findByTitle(title).getId(), id);
        return "".format("Теперь %s часть команды.", user_controller.findUserbyId(id).getNickname());
    }

    @PutMapping("/{title}/remove_admin/{id}")
    public String removeAdmin(@PathVariable String title, @PathVariable Long id){
        if (user_controller.CURRENT.getId() == null) return "Необходимо войти в аккаунт, чтобы удалить админа Саббреддита!";

        else if (!(user_controller.CURRENT.getId().equals(service.findByTitle(title).getMain_admin()))) return "У вас нет прав главного администратора, чтобы удалить админа Саббреддита!";

        service.removeAdmin(service.findByTitle(title).getId(), id);
        return "".format("Теперь %s изгнан из вышего уютного уголка.", user_controller.findUserbyId(id).getNickname());
    }
}
