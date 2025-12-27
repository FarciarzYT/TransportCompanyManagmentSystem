package com.kiedywakacje.transport;

import com.kiedywakacje.transport.models.UserRole;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserRepository {
    private Map<String, User> usersByUsername;
    private Map<String, User> usersByEmail;
    private List<Client> clients;
    private List<Driver> drivers;
    private List<Admin> admins;

    public UserRepository() {
        this.usersByUsername = new HashMap<>();
        this.usersByEmail = new HashMap<>();
        this.clients = new ArrayList<>();
        this.drivers = new ArrayList<>();
        this.admins = new ArrayList<>();
    }

    public boolean addUser(User user) {
        if (user == null) {
            return false;
        }

        if (usersByUsername.containsKey(user.getUsername()) || 
            usersByEmail.containsKey(user.getEmail())) {
            return false;
        }

        usersByUsername.put(user.getUsername(), user);
        usersByEmail.put(user.getEmail(), user);

        if (user instanceof Client) {
            clients.add((Client) user);
        } else if (user instanceof Driver) {
            drivers.add((Driver) user);
        } else if (user instanceof Admin) {
            admins.add((Admin) user);
        }

        return true;
    }

    public User findByUsername(String username) {
        return usersByUsername.get(username);
    }

    public User findByEmail(String email) {
        return usersByEmail.get(email);
    }

    public boolean usernameExists(String username) {
        return usersByUsername.containsKey(username);
    }

    public boolean emailExists(String email) {
        return usersByEmail.containsKey(email);
    }

    public List<Client> getAllClients() {
        return new ArrayList<>(clients);
    }

    public List<Driver> getAllDrivers() {
        return new ArrayList<>(drivers);
    }

    public List<Admin> getAllAdmins() {
        return new ArrayList<>(admins);
    }

    public List<User> getAllUsers() {
        return new ArrayList<>(usersByUsername.values());
    }

    public boolean removeUser(String username) {
        User user = usersByUsername.remove(username);
        if (user != null) {
            usersByEmail.remove(user.getEmail());
            
            if (user instanceof Client) {
                clients.remove(user);
            } else if (user instanceof Driver) {
                drivers.remove(user);
            } else if (user instanceof Admin) {
                admins.remove(user);
            }
            return true;
        }
        return false;
    }

    public int getUserCount() {
        return usersByUsername.size();
    }
}

