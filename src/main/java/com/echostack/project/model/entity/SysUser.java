package com.echostack.project.model.entity;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data
public class SysUser extends BaseEntity implements UserDetails {

    private String username;

    private String password;

    private String email;

    private String mobile;

    private int sex;

    private String description;

    private String headImg;

    private String country;

    private String province;

    private String city;

    private String address;

    private String status;

    private boolean isAccountNonExpired;

    private boolean isAccountNonLocked;

    private boolean isCredentialsNonExpired;

    private boolean isEnabled;

    private List<SysRole> sysRoles;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (SysRole sysRole : sysRoles) {
            authorities.add(new SimpleGrantedAuthority(sysRole.getName()));
        }
        return authorities;
    }

    public SysUser(String username) {
        this.username = username;
    }

    public SysUser(){

    }
}
