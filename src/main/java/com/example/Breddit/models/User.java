package com.example.Breddit.models;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Data
@Entity
@Table(name = "Breddit Users")

public class User extends UserTemplate{
    @Id
    @GeneratedValue
    private Long id;

    public User(){
        super();
    }
    
    

} 
