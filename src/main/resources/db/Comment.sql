create table comments
(
    id         serial
        constraint "PK_8bf68bc960f2b69e818bdb90dcb"
            primary key,
    request_id varchar(255)            not null
        constraint comments_request_id_unique
            unique,
    avatar_no  integer                 not null,
    nickname   varchar(50)             not null,
    comment    varchar(1000)           not null,
    created_at timestamp default now() not null,
    deleted_at timestamp,
    post_id    integer                 not null
        constraint comments_post_id_posts_id_fk
            references posts
            on delete cascade
);

alter table comments
    owner to devgoraebap;
