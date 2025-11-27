create table users
(
    id         serial
        constraint "PK_a3ffb1c0c8416b9fc6f907b7433"
            primary key,
    email      varchar(254)            not null
        constraint users_email_unique
            unique,
    nickname   varchar(50)             not null,
    created_at timestamp default now() not null,
    updated_at timestamp default now() not null
);

alter table users
    owner to devgoraebap;