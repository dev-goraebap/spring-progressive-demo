create table blobs
(
    id           serial
        constraint "PK_fe61649fa345f685eb31b949e4c"
            primary key,
    key          varchar(255)                 not null
        constraint blobs_key_unique
            unique,
    filename     varchar(255)                 not null,
    content_type varchar(100)                 not null,
    service_name varchar(50)                  not null,
    byte_size    integer                      not null,
    checksum     varchar(255)                 not null
        constraint blobs_checksum_unique
            unique,
    created_by   varchar(100)                 not null,
    created_at   timestamp default now()      not null,
    metadata     text      default '{}'::text not null
);

alter table blobs
    owner to devgoraebap;
