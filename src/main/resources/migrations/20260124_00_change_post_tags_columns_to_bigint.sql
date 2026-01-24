-- =====================================================
-- post_tags 테이블의 FK 컬럼 타입을 BIGINT로 변경
-- posts.id, tags.id가 BIGINT인데 post_tags의 FK가 INTEGER라서 타입 불일치
-- =====================================================

------------------------------------------------------MIGRATE-----------------------------------------------------------

-- 기존 FK 제약조건 삭제
alter table post_tags drop constraint post_tags_post_id_posts_id_fk;
alter table post_tags drop constraint post_tags_tag_id_tags_id_fk;

-- 컬럼 타입 변경
alter table post_tags alter column post_id type bigint;
alter table post_tags alter column tag_id type bigint;

-- FK 제약조건 재생성
alter table post_tags
    add constraint post_tags_post_id_posts_id_fk
        foreign key (post_id) references posts (id)
            on update cascade on delete cascade;

alter table post_tags
    add constraint post_tags_tag_id_tags_id_fk
        foreign key (tag_id) references tags (id)
            on delete cascade;

COMMIT;

------------------------------------------------------ROLLBACK----------------------------------------------------------

-- alter table post_tags drop constraint post_tags_post_id_posts_id_fk;
-- alter table post_tags drop constraint post_tags_tag_id_tags_id_fk;
-- alter table post_tags alter column post_id type integer;
-- alter table post_tags alter column tag_id type integer;
-- alter table post_tags
--     add constraint post_tags_post_id_posts_id_fk
--         foreign key (post_id) references posts (id)
--             on update cascade on delete cascade;
-- alter table post_tags
--     add constraint post_tags_tag_id_tags_id_fk
--         foreign key (tag_id) references tags (id);
