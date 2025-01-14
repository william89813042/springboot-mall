package com.systex.william.springbootmall.dao.impl;

import com.systex.william.springbootmall.dao.UserDao;
import com.systex.william.springbootmall.dto.UserRegisterRequest;
import com.systex.william.springbootmall.model.User;
import com.systex.william.springbootmall.rowmapper.UserRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class UserDaoImpl implements UserDao {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public UserDaoImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public User getUserById(Integer userId) {
        String sql = """
            SELECT * FROM user
            WHERE user_id = :userId
            """;

        Map<String, Object> map = new HashMap<>(); // <key, value >
        map.put("userId", userId);

        List<User> userList = namedParameterJdbcTemplate.query(sql, map, new UserRowMapper());

        if (userList.size() > 0 ) {
            return userList.get(0);
        } else {
            return null;
        }
    }

    @Override
    public User getUserByEmail(String email) {
        String sql = """
            SELECT * FROM user
            WHERE email = :email
            """;

        Map<String, Object> map = new HashMap<>(); // <key, value >
        map.put("email", email);

        List<User> userList = namedParameterJdbcTemplate.query(sql, map, new UserRowMapper());

        if (userList.size() > 0 ) {
            return userList.get(0);
        } else {
            return null;
        }
    }


    @Override
    public Integer createUser(UserRegisterRequest userRegisterRequest) {
        String sql = """
            INSERT INTO user (email, password, created_date, last_modified_date)  -- 這裡的 email, password, created_date, last_modified_date 是對應到我們的 User Table 的欄位
            VALUES (:email, :password, :createdDate, :lastModifiedDate)   -- 這裡的 :email, :password, :createdDate, :lastModifiedDate 是對應到我們的 Map 的 key
            """;

        Map<String, Object> map = new HashMap<>();
        map.put("email", userRegisterRequest.getEmail());
        map.put("password", userRegisterRequest.getPassword());

        Date now = new Date();
        map.put("createdDate", now);
        map.put("lastModifiedDate", now);

        KeyHolder keyHolder = new GeneratedKeyHolder(); //去接住 MySQL 資料庫自動生成的 userId

        namedParameterJdbcTemplate.update(sql, new MapSqlParameterSource(map), keyHolder);

        int userId = keyHolder.getKey().intValue();

        return userId;
    }
}
