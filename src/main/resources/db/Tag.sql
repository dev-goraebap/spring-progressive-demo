create table tags
(
    id          serial
        constraint "PK_e7dc17249a1148a1970748eda99"
            primary key,
    name        varchar(100)            not null
        constraint tags_name_unique
            unique,
    description varchar(500)            not null,
    created_at  timestamp default now() not null,
    updated_at  timestamp default now() not null,
    user_id     integer                 not null
        constraint tags_user_id_users_id_fk
            references users
            on delete cascade
);

alter table tags
    owner to devgoraebap;