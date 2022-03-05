package com.zjy.mongodb.slave;

import com.zjy.mongodb.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SlaveRep extends MongoRepository<User, String> {
}