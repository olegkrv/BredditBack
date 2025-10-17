package com.example.Breddit.models;

import java.util.ArrayList;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

public class Sub {
    public Sub() {super();}

    @Id
    @GeneratedValue
    private Long id;
    @Column(unique = true)
    private String title;
    private String description;
    private Long main_admin;
    private ArrayList<Long> admins = new ArrayList<Long>();
}
