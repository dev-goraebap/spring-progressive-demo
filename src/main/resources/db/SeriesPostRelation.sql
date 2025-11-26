create table series_posts
(
    id         serial
        constraint "PK_6a0c831e35b38b6517fd12e4191"
            primary key,
    "order"    integer   default 999   not null,
    created_at timestamp default now() not null,
    updated_at timestamp default now() not null,
    series_id  integer                 not null
        constraint series_posts_series_id_series_id_fk
            references series
            on delete cascade,
    post_id    integer                 not null
        constraint series_posts_post_id_posts_id_fk
            references posts
            on delete cascade,
    constraint series_posts_series_id_post_id_unique
        unique (series_id, post_id)
);

alter table series_posts
    owner to devgoraebap;
