create table curated_sources
(
    id           serial
        primary key,
    name         varchar(100)                     not null,
    url          text                             not null,
    is_active_yn active_yn default 'Y'::active_yn not null,
    created_at   timestamp default now()          not null,
    updated_at   timestamp default now()          not null
);

alter table curated_sources
    owner to devgoraebap;