package com.tundalabs.store.users;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;


@AllArgsConstructor
@Getter
@ToString
public class UserDto {
//    @JsonProperty("user_id")
    private Long id;
    private String name;
    private String email;
//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
//    private LocalDateTime createdAt;
}
