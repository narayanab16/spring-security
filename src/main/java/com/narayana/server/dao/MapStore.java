package com.narayana.server.dao;

import com.narayana.server.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class MapStore {
    Logger LOG = LoggerFactory.getLogger(MapStore.class);
    private Map<Integer, User> map = new LinkedHashMap<>();
    public User addUser(User user) {
        map.put(user.id(), user);
        LOG.info(user + " , user add successful to map store");
        return user;
    }
    public User getUserById(Integer id) {
        LOG.info("find user by id: " + id);
        return map.get(id);
    }

    public List<User> getUsers() {
        LOG.info("get all Users ");
        List<User> users = map.entrySet().stream().map(e -> e.getValue()).collect(Collectors.toList());
        return users;
    }

}
