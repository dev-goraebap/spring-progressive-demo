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