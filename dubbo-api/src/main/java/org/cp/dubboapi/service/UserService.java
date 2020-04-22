package org.cp.dubboapi.service;

import org.cp.dubboapi.entities.User;

import java.util.List;

/**
 * create by CP on 2020/4/16 0016.
 */
public interface UserService {

    List<User> getUsers();

    User getUserById(Long id);

    String sayHello();

}
