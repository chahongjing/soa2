package com.zjy.dao;

import com.zjy.po.User;
import org.springframework.stereotype.Repository;

public interface UserDao {
    User get(Long id);

    void update(User user);
}
