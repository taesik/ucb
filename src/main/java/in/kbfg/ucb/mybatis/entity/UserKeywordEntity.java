package in.kbfg.ucb.mybatis.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserKeywordEntity {
    private int userIdx;
    private String keyword;
    private LocalDateTime regDate;
}
