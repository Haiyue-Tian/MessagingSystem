package com.haiyue.messaging.response;

import com.haiyue.messaging.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Optional;

@Data
@AllArgsConstructor
public class UserResponse{
    private int userId;
    private Optional<String> username;
    private Optional<String> nickname;

    public static UserResponse from(User user){
        return new UserResponse(
                user.getId(),
                Optional.ofNullable(user.getUsername()),
                Optional.ofNullable(user.getNickname()));
    }
}
