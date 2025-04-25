package com.example.Project.service;

import java.util.List;

import com.example.Project.model.User;

public interface IUserService {

    public User authenticateUser(String username, String password);

    public boolean createUser(String username, String password);

    public List<User> getAllUsers();

    public boolean approvePremium(int id);

    public boolean rejectPremium(int id);

    public boolean downgrade(int id);

    public boolean requestPremium(int id);
}
