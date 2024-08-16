package com.example.backend.security.principals;

import com.example.backend.model.entity.Role;
import com.example.backend.model.entity.User;
import com.example.backend.repository.IUserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Set;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private IUserRepo IUserRepository;

    // chuyen doi user thanh UserDetail trong security
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = IUserRepository.findUserByEmail(email).orElseThrow(() -> new NoSuchElementException("Email khong ton tai"));
        return CustomUserDetails.builder()
                .id(user.getId())
                .status(user.getUserStatus())
                .email(user.getEmail())
                .fullName(user.getUsername())
                .password(user.getPassword())
                .authorities(mapRoleToAuthorties(user.getRoles()))
                .build();
    }

    private Collection<? extends GrantedAuthority> mapRoleToAuthorties(Set<Role> roles) {
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getRoleName())).toList();
    }
}
