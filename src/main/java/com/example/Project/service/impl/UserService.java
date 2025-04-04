package com.example.Project.service.impl;

import java.util.List;
import java.util.Optional;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Project.model.User;
import com.example.Project.repository.IUserRepository;
import com.example.Project.service.IUserService;

@Service
public class UserService implements IUserService {
    @Autowired
    private IUserRepository _userRepository;

    @Override
    public User authenticateUser(String username, String password) {
        Optional<User> userOptional = _userRepository.findByUsername(username);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            return BCrypt.checkpw(password, user.getPasswordHash()) ? user : null;
        }
        return null;
    }

    @Override
    public boolean createUser(String username, String password) {
        Optional<User> userOptional = _userRepository.findByUsername(username);

        if (userOptional.isPresent()) {
            return false;
        }

        String passwordHash = BCrypt.hashpw(password, BCrypt.gensalt());
        User user = new User();
        user.setUsername(username);
        user.setPasswordHash(passwordHash);
        _userRepository.save(user);
        return true;
    }

    @Override
    public List<User> getAllUsers() {
        return _userRepository.findAll();
    }

    @Override
    public boolean approvePremium(int id) {
        Optional<User> userOptional = _userRepository.findById(id);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setRequirePremium(false);
            user.setPremium(true);
            _userRepository.save(user);
            return true;
        }

        return false;
    }

    @Override
    public boolean rejectPremium(int id) {
        Optional<User> userOptional = _userRepository.findById(id);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setRequirePremium(false);
            _userRepository.save(user);
            return true;
        }

        return false;
    }

    @Override
    public boolean downgrade(int id) {
        Optional<User> userOptional = _userRepository.findById(id);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setPremium(false);
            _userRepository.save(user);
            return true;
        }

        return false;
    }
}
