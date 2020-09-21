package com.roshan.moviesearch.Repository;

import org.springframework.data.repository.CrudRepository;

import com.roshan.moviesearch.Models.User;



public interface UserRepository extends CrudRepository<User, Integer> {
    Integer countByUserName(String userName);
    User findByUserName(String userName);
}