package org.lili.common.entity;

import java.io.Serializable;
import java.util.Date;


public class BaseUser implements Serializable {
    /**
     * 主键id
     */
    private Integer id;

    /**
     * 名字
     */
    private String username;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 
     */
    private String password;

    /**
     * 创建时间
     */
    private Date ctime;

    /**
     * 修改时间
     */
    private Date mtime;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public Date getCtime() {
        return ctime;
    }

    public void setCtime(Date ctime) {
        this.ctime = ctime;
    }

    public Date getMtime() {
        return mtime;
    }

    public void setMtime(Date mtime) {
        this.mtime = mtime;
    }

    public static class Builder {
        private BaseUser obj;

        public Builder() {
            this.obj = new BaseUser();
        }

        public Builder id(Integer id) {
            obj.id = id;
            return this;
        }

        public Builder username(String username) {
            obj.username = username;
            return this;
        }

        public Builder email(String email) {
            obj.email = email;
            return this;
        }

        public Builder password(String password) {
            obj.password = password;
            return this;
        }

        public Builder ctime(Date ctime) {
            obj.ctime = ctime;
            return this;
        }

        public Builder mtime(Date mtime) {
            obj.mtime = mtime;
            return this;
        }

        public BaseUser build() {
            return this.obj;
        }
    }
}