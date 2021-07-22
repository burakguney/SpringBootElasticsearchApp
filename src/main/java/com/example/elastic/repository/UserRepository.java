package com.example.elastic.repository;

import java.util.List;

import com.example.elastic.entity.User;

import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends ElasticsearchRepository<User, String> {

    @Query("{\"bool\": {\"must\": [{\"match\": {\"name\": \"?0\"}}]}}")
    List<User> getByCustomQuery(String search);

    List<User> findByNameLikeOrSurnameLike(String name, String surname);

}
