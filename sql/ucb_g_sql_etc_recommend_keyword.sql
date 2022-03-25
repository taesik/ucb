create table g_sql_etc_recommend_keyword
(
    idx      int auto_increment comment '추천키워드 고유번호'
        primary key,
    keyword  varchar(50)                        not null comment '키워드',
    reg_date datetime default CURRENT_TIMESTAMP not null comment '키워드 등록 시각'
)
    comment '추천키워드 테이블' charset = utf8;

INSERT INTO ucb.g_sql_etc_recommend_keyword (idx, keyword, reg_date) VALUES (1, '신용카드 추천', '2021-11-23 11:10:35');
INSERT INTO ucb.g_sql_etc_recommend_keyword (idx, keyword, reg_date) VALUES (2, '대방 상품', '2021-11-23 11:10:35');
INSERT INTO ucb.g_sql_etc_recommend_keyword (idx, keyword, reg_date) VALUES (3, '해외 직구', '2021-11-23 11:10:35');