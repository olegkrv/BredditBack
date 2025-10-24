package com.example.Breddit.service.JPA;

import java.util.ArrayList;
import java.util.List;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import com.example.Breddit.controllers.UserController;
import com.example.Breddit.models.CurrentUser;
import com.example.Breddit.models.Post;
import com.example.Breddit.models.Sub;
import com.example.Breddit.models.User;
import com.example.Breddit.repository.PostReposiroty;
import com.example.Breddit.repository.SubRepository;
import com.example.Breddit.service.SubService;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Primary
public class SubServiceJPA implements SubService{
    private final SubRepository repository; 
    private final PostReposiroty post_repository;
    private final UserController user_controller;

    @Override
    public List<Sub> findAllSubs(){
        return repository.findAll();
    }

    @Override
    public Sub addSub(Sub sub){
        try {
            Sub new_sub = repository.save(sub);
            new_sub.setMain_admin(user_controller.CURRENT.getId());
            repository.save(new_sub);
            return new_sub;
        }
        catch (Exception exception) {
            System.out.println(exception);
            return null;}
    }

    @Override
    public Sub updateSub(Sub sub){
        return repository.save(sub);
    }

    @Override
    public Sub findByTitle(String title){
        return repository.findBytitle(title);
    }

    @Override
    public Sub findById(Long id){
        return repository.findByid(id);
    }

    @Override
    @Transactional
    public boolean deleteSub(Long id){
        if (repository.findByid(id) != null) {
            for (Long post: repository.findByid(id).getPosts()) post_repository.deleteByid(post);
        
            repository.deleteByid(id); 
            return true;
        }
        return false;
    }

    @Override
    public void addAdmin(Long sub_id, Long user_id){
        Sub this_sub = repository.findByid(sub_id);
        System.out.println(this_sub);
        ArrayList<Long> admins = this_sub.getAdmins();
        System.out.println(admins);
        admins.add(user_id);
        System.out.println(admins);
        this_sub.setAdmins(admins);
        repository.save(this_sub);
        this_sub = null;
    }

    @Override
    public void removeAdmin(Long sub_id, Long user_id){
        
    }
}
