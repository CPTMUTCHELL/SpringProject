package com.example.demo.services;

import com.example.demo.entities.Role;
import com.example.demo.entities.User;
import com.example.demo.repositories.RoleRepo;
import com.example.demo.repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepo userRepository;
    @Autowired
    private RoleRepo roleRepository;
    @Autowired
    private BCryptPasswordEncoder encoder;
    public List<User> findAll(){
        return userRepository.findAll();
    }
    public boolean checkUser(String login){
        return  (userRepository.existsUserByLogin(login)) ;
    }
    public User findByLogin(String login){return userRepository.findByLogin(login);}
    public void save(User user){
        List<Role> roles=new ArrayList<>();
        roles.add(roleRepository.getOne(2L));
        user.setRoles(roles);
        user.setPassword(encoder.encode(user.getPassword()));
        userRepository.save(user);
    }
    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User user=userRepository.findByLogin(s);

        if (user==null){
            throw new UsernameNotFoundException("Invalid login or password");
        }
        System.out.println(user);
        return new org.springframework.security.core.userdetails.User(user.getLogin(),
                user.getPassword(),mapRolesToAuth(user.getRoles()));

    }
    private List<?extends GrantedAuthority> mapRolesToAuth(Collection<Role> roles){
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());
    }
}
