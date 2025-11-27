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