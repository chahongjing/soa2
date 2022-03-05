package com.zjy.mongodb.config;

import com.zjy.mongodb.User;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author neo
 */
public interface MasterRepository extends MongoRepository<User, String> {
}
