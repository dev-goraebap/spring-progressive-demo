create table app_logs
(
    id            bigserial
        primary key,
    timestamp     timestamp with time zone default now() not null,
    level         varchar(10)                            not null,
    message       text                                   not null,
    method        varchar(10),
    url           text,
    status_code   integer,
    response_time integer,
    user_id       integer,
    session_id    varchar(128),
    ip_address    inet,
    request_id    uuid,
    error_message text,
    error_stack   text,
    metadata      jsonb,
    tags          text[]                   default '{}'::text[],
    created_at    timestamp with time zone default now() not null
);

alter table app_logs
    owner to devgoraebap;

create index idx_app_logs_timestamp
    on app_logs (timestamp desc);

create index idx_app_logs_level
    on app_logs (level);

create index idx_app_logs_user_id
    on app_logs (user_id)
    where (user_id IS NOT NULL);

create index idx_app_logs_level_timestamp
    on app_logs (level asc, timestamp desc);

create index idx_app_logs_metadata
    on app_logs using gin (metadata);

create index idx_app_logs_tags
    on app_logs using gin (tags);