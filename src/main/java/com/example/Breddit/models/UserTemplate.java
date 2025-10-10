package com.example.Breddit.models;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;

import com.example.Breddit.service.UsersService;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import java.lang.reflect.Modifier;

@MappedSuperclass
@Getter
@Setter
public abstract class UserTemplate {
 
    private String nickname;
    private String password;
    @NonNull
    @Column(unique = true)
    private String email;

    private LocalDate date_of_birth;
     
    @Transient
    private Integer age;

    public int getAge() {
        return Period.between(date_of_birth, LocalDate.now()).getYears();
     }
    
    private ArrayList<Long> posts = new ArrayList<Long>();

    public void addPost(Long id){
        ArrayList<Long> my_posts = this.getPosts();
        System.out.println(my_posts);
        my_posts.add(id);
        this.setPosts(my_posts);
        my_posts = null;
    }
    
    public void deletePost(Long id){
        ArrayList<Long> my_posts = this.getPosts();
        System.out.println(my_posts);
        my_posts.remove(id);
        this.setPosts(my_posts);
        my_posts = null;
    }
    
    }



    

