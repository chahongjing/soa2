package com.zjy.mongodb.master;

import com.zjy.mongodb.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MasterRep extends MongoRepository<User, String> {
}