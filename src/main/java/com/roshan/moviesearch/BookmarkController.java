package com.roshan.moviesearch;

import java.util.ArrayList;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.roshan.moviesearch.Models.Bookmark;
import com.roshan.moviesearch.Repository.BookmarkRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("api")
@RestController
public class BookmarkController {
    @Autowired
    private BookmarkRepository bookmarkRepository;

    @RequestMapping(value = "/bookmark", method = RequestMethod.GET)
    public ArrayList<Bookmark> getBookmark(@RequestHeader("Authorization") String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256("secret");
            JWTVerifier verifier = JWT.require(algorithm).withIssuer("Roshan").build();
            DecodedJWT jwt = JWT.decode(token);
            if (jwt.getClaim("id").asInt() == null) {
                return new ArrayList<Bookmark>();
            } else {
                ArrayList<Bookmark> list = bookmarkRepository.findByOwnerId(jwt.getClaim("id").asInt());
                return list;
            }
        } catch (Exception e) {
            return new ArrayList<Bookmark>();
        }

    }

    @RequestMapping(value = "/bookmark", method = RequestMethod.POST)
    public String saveBookmark(@RequestHeader("Authorization") String token, @RequestParam String movieId) {

        try {
            Algorithm algorithm = Algorithm.HMAC256("secret");
            JWTVerifier verifier = JWT.require(algorithm).withIssuer("Roshan").build();
            DecodedJWT jwt = JWT.decode(token);
            Bookmark bookmark = new Bookmark();
            bookmark.setOmdbId(movieId);
            bookmark.setOwnerId(jwt.getClaim("id").asInt());
            bookmarkRepository.save(bookmark);
            return "Your bookmark got saved";

        } catch (JWTCreationException exception) {
            return "Credentials are outdated";
        }
    }

    @RequestMapping(value = "/bookmark", method = RequestMethod.DELETE)
    public String deleteBookmark(@RequestHeader("Authorization") String token, @RequestParam String movieId) {

        try {
            Algorithm algorithm = Algorithm.HMAC256("secret");
            JWTVerifier verifier = JWT.require(algorithm).withIssuer("Roshan").build();
            DecodedJWT jwt = JWT.decode(token);
            ArrayList<Bookmark> list = bookmarkRepository.findByOwnerId(jwt.getClaim("id").asInt());
            Bookmark bookmark = new Bookmark();
            for (Bookmark innerBookmark : list) {
                if (innerBookmark.getOmdbId().equals(movieId)) {
                    bookmark = innerBookmark;
                    break;
                }
            }
            bookmarkRepository.delete(bookmark);
            return "Your bookmark got deleted";

        } catch (JWTCreationException exception) {
            return "Credentials are outdated";
        }
    }
}

// .verify(token); String mapping =
// jwt.getClaims().keySet().stream().map(key -> key + "=" +
// jwt.getClaims().get(key)) .collect(Collectors.joining(", ", "{", "}"));
// return "" + .asString();