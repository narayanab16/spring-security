package com.narayana.server.service;

import com.narayana.server.dao.MapStore;
import com.narayana.server.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private MapStore mapStore;
    @Autowired
    public UserService(MapStore mapStore) {
        this.mapStore = mapStore;
    }

    public User addUser(User user) {
        mapStore.addUser(user);
        return mapStore.getUserById(user.id());
    }
    public User getUserById(Integer id) {
        return mapStore.getUserById(id);
    }
    public List<User> getUsers() {
        return mapStore.getUsers();
    }
}
