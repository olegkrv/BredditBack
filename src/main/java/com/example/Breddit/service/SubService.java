package com.example.Breddit.service;
import java.util.List;

import com.example.Breddit.models.Post;
import com.example.Breddit.models.Sub;

public interface SubService {
    List<Sub> findAllSubs();

    Sub addSub(Sub sub);

    Sub updateSub(Sub post);

    Sub findByTitle(String title);

    Sub findById(Long id);

    boolean deleteSub(Long id);

    void addAdmin(Long sub_id, Long user_id);

    void removeAdmin(Long sub_id, Long user_id);
}
