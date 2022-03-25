package in.kbfg.ucb.mybatis.entity;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class UserEntity {
    private int idx;
    private String id;
    private String name;
    private String auth;
    private LocalDateTime lastLoginDate;
    private LocalDateTime regDate;
    private LocalDateTime updateDate;
}
