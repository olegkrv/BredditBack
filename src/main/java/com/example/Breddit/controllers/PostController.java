package com.example.Breddit.controllers;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.apache.coyote.BadRequestException;
import org.springframework.transaction.annotation.Transactional;
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
import org.springframework.web.client.HttpClientErrorException.BadRequest;

import com.example.Breddit.models.Post;
import com.example.Breddit.models.User;
import com.example.Breddit.service.UsersService;
import com.example.Breddit.service.JPA.PostServiceJPA;

import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/br/posts")
@RequiredArgsConstructor
@CrossOrigin("*")
public class PostController {
    private final PostServiceJPA service;

    private final UserController user_controller;
    // private final UsersService user_service;

    @GetMapping
    public List<Post> findAllPosts(){
        return service.findAllPosts();
    }

    @PostMapping("/add_post")
    public String addPost(@RequestBody Post post){
        try{
           if (user_controller.CURRENT.getId() == null) return "Необходимо войти в аккаунт, чтобы сделать пост!";
           
        else if (post.getTitle().split(" ").length == 0 || post.getTitle().equals("")) return "Заголовок не может быть пустым!";
        service.addPost(post);
        System.out.println(post.getTitle().split("") + "=="+post.getTitle().split("").length + "==" + (post.getTitle().split("").length == 0));
        return "Пост успешно добавлен!"; 
        }
        
        
        catch (Exception e) {return "Ошибка добавления поста: " + e;}
    }

    @PutMapping("/update_post")
    public Post updatePost(@RequestBody Post post){
        return service.updatePost(post);
    }

    @GetMapping("/{id}")
    public Post findById(@PathVariable Long id){
        return service.findById(id);
    }

    @Transactional
    @DeleteMapping("delete_post/{id}")
    public String deletePost(@PathVariable Long id){
        if (service.findById(id) != null) {
            if (user_controller.CURRENT.getId() == null) return "Вы даже не авторизовались!";
            if (!(service.findById(id).getAuthor().equals(user_controller.CURRENT.getId()))){
            return "Удалять можно лишь свои посты!";
        }
        if (service.deletePost(id)){
            return "Пост успешно удалён!";
        }
    }
        return "Поста с таким id не существует!";
    }
}
