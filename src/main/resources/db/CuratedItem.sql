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