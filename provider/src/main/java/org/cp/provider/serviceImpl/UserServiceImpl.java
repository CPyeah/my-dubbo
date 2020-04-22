package org.cp.provider.serviceImpl;

import com.alibaba.dubbo.config.annotation.Service;

import org.cp.dubboapi.entities.User;
import org.cp.dubboapi.service.UserService;

import java.util.ArrayList;
import java.util.List;

/**
 * create by CP on 2020/4/16 0016.
 */
@Service(interfaceClass = UserService.class)
public class UserServiceImpl implements UserService {
    @Override
    public List<User> getUsers() {
        List<User> users = new ArrayList<>();
        users.add(new User(1L, "cp", "a111"));
        users.add(new User(2L, "zj", "a222"));
        return users;
    }

    @Override
    public User getUserById(Long id) {
        if (1 == id) {
            return new User(1L, "cp20881", "a111");
        }
        if (2 == id) {
            return new User(2L, "zj", "a222");
        }
        return null;
    }

    @Override
    public String sayHello() {
        return "server_001";
    }
}
