package com.quinnox.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.quinnox.model.User;

@Repository
public interface UserRepository extends MongoRepository<User, String>{
}