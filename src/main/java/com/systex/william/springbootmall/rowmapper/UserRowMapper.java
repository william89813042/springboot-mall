package com.systex.william.springbootmall.rowmapper;

import com.systex.william.springbootmall.model.User;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/*
    將資料庫的結果轉換成一個User Object
 */
public class UserRowMapper implements RowMapper<User> {

    @Override
    public User mapRow(ResultSet rs, int i) throws SQLException {
        User user = new User();
        user.setUserid(rs.getInt("user_id"));
        user.setEmail(rs.getString("email"));
        user.setPassword(rs.getString("password"));
        user.setCreatedDate(rs.getTimestamp("created_date"));
        user.setLastModifiedDate(rs.getTimestamp("last_modified_date"));

        return user;
    }
}
