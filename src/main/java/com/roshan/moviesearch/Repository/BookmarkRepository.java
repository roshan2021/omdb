package com.roshan.moviesearch.Repository;

import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;

import com.roshan.moviesearch.Models.Bookmark;


public interface BookmarkRepository extends CrudRepository<Bookmark, Integer> {
    ArrayList<Bookmark> findByOwnerId(Integer ownerId);
}