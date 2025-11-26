create table posts
(
    id              serial
        constraint "PK_2829ac61eff60fcec60d7274b9e"
            primary key,
    slug            varchar(255)                                   not null
        constraint posts_slug_unique
            unique,
    title           varchar(255)                                   not null,
    summary         varchar(500)                                   not null,
    content         text                                           not null,
    post_type       varchar(20)  default 'post'::character varying not null,
    view_count      integer      default 0                         not null,
    created_at      timestamp    default now()                     not null,
    updated_at      timestamp    default now()                     not null,
    published_at    timestamp    default now()                     not null,
    user_id         integer                                        not null
        constraint posts_user_id_users_id_fk
            references users
            on delete cascade,
    is_published_yn published_yn default 'N'::published_yn         not null
);

alter table posts
    owner to devgoraebap;