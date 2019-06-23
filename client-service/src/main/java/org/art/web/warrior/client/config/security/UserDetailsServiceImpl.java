package org.art.web.warrior.client.config.security;

import org.art.web.warrior.client.service.client.api.UserServiceClient;
import org.art.web.warrior.client.util.ServiceResponseUtil;
import org.art.web.warrior.commons.users.dto.RoleDto;
import org.art.web.warrior.commons.users.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserServiceClient userServiceClient;

    @Autowired
    public UserDetailsServiceImpl(UserServiceClient userServiceClient) {
        this.userServiceClient = userServiceClient;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        ResponseEntity<UserDto> userServiceResponse = userServiceClient.findUserByEmail(email);
        if (ServiceResponseUtil.isUserServiceErrorResponse(userServiceResponse)) {
            throw new UsernameNotFoundException("No user found with email: " + email);
        }
        UserDto user = userServiceResponse.getBody();
        return new org.springframework.security.core.userdetails.User(
            user.getEmail(),
            user.getPassword(),
            user.isEnabled(),
            true,
            true,
            true,
            getAuthorities(user.getRoles()));

    }

    private List<GrantedAuthority> getAuthorities(List<RoleDto> roles) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (RoleDto role : roles) {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        }
        return authorities;
    }
}
