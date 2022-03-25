create table g_sql_etc_users
(
    idx             int auto_increment comment '사용자 고유번호'
        primary key,
    id              varchar(10)                        null comment '사용자 사번',
    name            varchar(30)                        null comment '사용자 이름',
    auth            char                               null comment '사용자 권한(U:사용자, A:관리자)',
    last_login_date datetime default CURRENT_TIMESTAMP not null comment '사용자 최근 로그인 시간',
    reg_date        datetime default CURRENT_TIMESTAMP not null comment '사용자 계정 생성 일시',
    update_date     datetime                           null comment '사용자 계정 업데이트 일시',
    constraint g_sql_etc_users_id_uindex
        unique (id)
)
    comment '사용자 정보 테이블' charset = utf8;

INSERT INTO ucb.g_sql_etc_users (idx, id, name, auth, last_login_date, reg_date, update_date) VALUES (4, 'MS06584', null, null, '2021-11-25 17:13:43', '2021-11-23 16:49:14', null);
INSERT INTO ucb.g_sql_etc_users (idx, id, name, auth, last_login_date, reg_date, update_date) VALUES (11, 'MS06635', null, null, '2021-11-25 17:14:37', '2021-11-25 15:27:44', null);