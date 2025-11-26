create table series
(
    id              serial
        constraint "PK_e725676647382eb54540d7128ba"
            primary key,
    name            varchar(255)                                   not null
        constraint series_name_unique
            unique,
    description     varchar(1000),
    status          varchar(20)  default 'PLAN'::character varying not null,
    created_at      timestamp    default now()                     not null,
    updated_at      timestamp    default now()                     not null,
    user_id         integer                                        not null
        constraint series_user_id_users_id_fk
            references users
            on delete cascade,
    slug            varchar(255)                                   not null
        constraint series_slug_unique
            unique,
    published_at    timestamp    default now()                     not null,
    is_published_yn published_yn default 'N'::published_yn         not null
);

alter table series
    owner to devgoraebap;