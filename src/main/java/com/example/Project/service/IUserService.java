package com.example.Project.service;

import com.example.Project.model.User;

public interface IUserService {

    public User authenticateUser(String username, String password);

    public boolean createUser(String username, String password);
}
