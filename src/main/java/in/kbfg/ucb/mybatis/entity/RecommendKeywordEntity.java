package in.kbfg.ucb.mybatis.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RecommendKeywordEntity {
    private int idx;
    private String keyword;
    private LocalDateTime regDate;
}
