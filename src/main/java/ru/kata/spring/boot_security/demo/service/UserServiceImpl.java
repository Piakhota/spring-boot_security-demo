package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.dao.RoleDao;
import ru.kata.spring.boot_security.demo.dao.UserDao;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    private final RoleDao roleDao;
    private final UserDao userDao;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserServiceImpl(RoleDao roleDao, UserDao userDao, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.roleDao = roleDao;
        this.userDao = userDao;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public List<User> getAll() {
        return userDao.findAll();
    }

    @Override
    @Transactional
    public void save(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userDao.save(user);
    }

    @Override
    public User get(Long id) {
        return userDao.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        userDao.deleteById(id);
    }

    @PostConstruct
    public void initMethod() {
        Role roleUser = new Role();
        roleUser.setName("ROLE_USER");
        roleDao.save(roleUser);

        Role roleAdmin = new Role();
        roleAdmin.setName("ROLE_ADMIN");
        roleDao.save(roleAdmin);

        User user1 = new User();
        user1.setUsername("user1");
        user1.setPassword(bCryptPasswordEncoder.encode("100"));
        user1.setName("Ivan");
        user1.setLastName("Ivanov");
        user1.setAge((byte) 11);
        user1.setRoles(Set.of(roleUser));
        userDao.save(user1);

        User user2 = new User();
        user2.setUsername("user2");
        user2.setPassword(bCryptPasswordEncoder.encode("100"));
        user2.setName("Petr");
        user2.setLastName("Petrov");
        user2.setAge((byte) 12);
        user2.setRoles(Set.of(roleUser));
        userDao.save(user2);

        User user3 = new User();
        user3.setUsername("admin1");
        user3.setPassword(bCryptPasswordEncoder.encode("100"));
        user3.setName("Sidor");
        user3.setLastName("Sidorov");
        user3.setAge((byte) 21);
        user3.setRoles(Set.of(roleUser, roleAdmin));
        userDao.save(user3);
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = userDao.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }
        return user;
    }
}
