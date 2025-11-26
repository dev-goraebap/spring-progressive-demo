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