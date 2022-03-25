create table g_sql_etc_users_keyword
(
    user_idx int                                not null comment '사용자 고유번호',
    keyword  varchar(50)                        not null comment '키워드',
    reg_date datetime default CURRENT_TIMESTAMP not null comment '키워드 등록 시각',
    primary key (user_idx, keyword)
)
    comment '사용자의 나의 구독 키워드 테이블' charset = utf8;

create index g_sql_etc_users_keyword_users_idx_index
    on g_sql_etc_users_keyword (user_idx);

INSERT INTO ucb.g_sql_etc_users_keyword (user_idx, keyword, reg_date) VALUES (11, '테스트1', '2021-11-25 16:13:59');