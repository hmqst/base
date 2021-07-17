package com.example.base.po;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * (UserPO)实体类  PO
 *
 * @author benben
 * @since 2021-03-10 11:02:45
 */
@ToString
public class UserPO implements Serializable, UserDetails {
    private static final long serialVersionUID = 842648937120584750L;

    private Integer id;
    @ApiModelProperty("用户名")
    private String username;
    @ApiModelProperty("密码")
    private String password;
    @ApiModelProperty("是否禁用")
    private Boolean enabled = false;
    @ApiModelProperty("是否锁定")
    private Boolean locked = false;
    @JsonIgnore
    private List<RolePO> roles;

    public UserPO() {
    }

    public UserPO(String username, boolean enabled) {
        this.username = username;
        this.enabled = enabled;
    }

    //获取当前用户对象所具有的角色信息
    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        for (RolePO role : roles) {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        }
        return authorities;
    }
    //获取当前用户的密码
    @Override
    public String getPassword() {
        return password;
    }
    //获取当前用户的用户名
    @Override
    public String getUsername() {
        return username;
    }
    //当前账户是否未过期
    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return true;
    }
    //当前账户是否未锁定
    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return !locked;
    }
    //当前账户密码是否未过期
    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return true;
    }
    //当前账户是否可用
    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Boolean getLocked() {
        return locked;
    }

    public void setLocked(Boolean locked) {
        this.locked = locked;
    }

    public List<RolePO> getRoles() {
        return roles;
    }

    public void setRoles(List<RolePO> roles) {
        this.roles = roles;
    }
}
