-- =====================================================
-- Initial Database Schema
-- =====================================================

-- -----------------------------------------------------
-- Table: users
-- -----------------------------------------------------
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

-- -----------------------------------------------------
-- Table: tags
-- -----------------------------------------------------
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

-- -----------------------------------------------------
-- Table: series
-- -----------------------------------------------------
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

-- -----------------------------------------------------
-- Table: posts
-- -----------------------------------------------------
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

-- -----------------------------------------------------
-- Table: post_tags (relation)
-- -----------------------------------------------------
create table post_tags
(
    post_id integer not null
        constraint post_tags_post_id_posts_id_fk
            references posts
            on update cascade on delete cascade,
    tag_id  integer not null
        constraint post_tags_tag_id_tags_id_fk
            references tags,
    constraint post_tags_post_id_tag_id_pk
        primary key (post_id, tag_id)
);

alter table post_tags
    owner to devgoraebap;

-- -----------------------------------------------------
-- Table: series_posts (relation)
-- -----------------------------------------------------
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

-- -----------------------------------------------------
-- Table: blobs
-- -----------------------------------------------------
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

-- -----------------------------------------------------
-- Table: attachments
-- -----------------------------------------------------
create table attachments
(
    id          serial
        constraint "PK_5e1f050bcff31e3084a1d662412"
            primary key,
    name        varchar(100)            not null,
    record_type varchar(100)            not null,
    record_id   varchar(100)            not null,
    blob_id     integer                 not null
        constraint attachments_blob_id_blobs_id_fk
            references blobs,
    created_at  timestamp default now() not null
);

alter table attachments
    owner to devgoraebap;

-- -----------------------------------------------------
-- Table: comments
-- -----------------------------------------------------
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

-- -----------------------------------------------------
-- Table: curated_sources
-- -----------------------------------------------------
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

-- -----------------------------------------------------
-- Table: curated_items
-- -----------------------------------------------------
create table curated_items
(
    id         serial
        primary key,
    title      varchar(500)            not null,
    link       text                    not null
        constraint curated_items_link_unique
            unique,
    guid       text                    not null
        constraint curated_items_guid_unique
            unique,
    snippet    text,
    pub_date   timestamp               not null,
    source     varchar(100)            not null,
    source_id  integer
        constraint curated_items_source_id_curated_sources_id_fk
            references curated_sources
            on delete set null,
    created_at timestamp default now() not null
);

alter table curated_items
    owner to devgoraebap;

-- -----------------------------------------------------
-- Table: blocked_ips
-- -----------------------------------------------------
create table blocked_ips
(
    id           serial
        constraint "PK_e86c3986ac081ad24d5443bb6c5"
            primary key,
    ip_address   inet                                            not null,
    reason       text,
    blocked_by   varchar(20) default 'manual'::character varying not null,
    expires_at   timestamp with time zone,
    created_at   timestamp   default now()                       not null,
    updated_at   timestamp   default now()                       not null,
    is_active_yn varchar(1)  default 'Y'::character varying      not null
);

alter table blocked_ips
    owner to devgoraebap;

create unique index "IDX_d9a4a34a43215adb2f0c361283"
    on blocked_ips (ip_address);

create index "IDX_6ee844ff70dc272825209c1e9e"
    on blocked_ips (is_active_yn, expires_at);

-- -----------------------------------------------------
-- Table: app_logs
-- -----------------------------------------------------
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
