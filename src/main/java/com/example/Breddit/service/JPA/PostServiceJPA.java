package com.example.Breddit.service.JPA;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.usertype.UserCollectionType;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import com.example.Breddit.controllers.UserController;
import com.example.Breddit.models.CurrentUser;
import com.example.Breddit.models.Post;
import com.example.Breddit.models.User;
import com.example.Breddit.repository.PostReposiroty;
import com.example.Breddit.service.PostService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Primary
public class PostServiceJPA implements PostService{
    private final PostReposiroty reposiroty;
    private final UsersServiceJPA user_service;
    private final UserController user_controller;

    @Override
    public List<Post> findAllPosts(){
        return reposiroty.findAll();
    }

    @Override
    public void addPost(Post post){
        post.setAuthor(user_controller.CURRENT.getId());
        post.setDate(Instant.now());

        Long new_post_id = reposiroty.save(post).getId();

        user_controller.CURRENT.addPost(new_post_id);

        User posting_man = user_service.findUserbyId(user_controller.CURRENT.getId());
        posting_man.addPost(new_post_id);
        user_service.saveUser(posting_man);
            

          
    }

    @Override
    public Post updatePost(Post post){
        return reposiroty.save(post);
    }

    @Override
    public Post findById(Long id){
        return reposiroty.findByid(id);
    }

    @Override
    public boolean deletePost(Long id){
        try{
           user_service.findUserbyId(reposiroty.findByid(id).getAuthor()).deletePost(id);
        reposiroty.deleteByid(id);
        return true; 
        }
        catch (Exception e){
            System.out.println("Ошибка удаления поста: " + e);
            return false;
        }
    }
}
