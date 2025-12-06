
with media as (
    select a.record_type
         , a.record_id
         , b.key
         , b.metadata
    from attachments a
    inner join blobs b on b.id = a.blob_id
    where a.name = 'thumbnail'
),
comment_counts as (
      select post_id, count(*) as cnt
      from comments
      where deleted_at is null
      group by post_id
),
post_list as (
    select p.slug
         , p.id
         , p.title
         , p.summary
         , p.view_count
         , m.key as thumbnail_key
         , m.metadata as thumbnail_metadata
         , coalesce(cc.cnt, 0) as comment_count
    from posts p
    left join media m on m.record_id = text(p.id) and m.record_type = 'post'
    left join comment_counts cc on cc.post_id = p.id
    where p.is_published_yn = 'Y'
)
select s.id
    , s.slug
    , s.name
    , s.status
    , s.published_at
    , m.key as series_thumbnail_key
    , m.metadata as series_thumbnail_metadata
    , p.slug as post_slug
    , p.title as post_title
    , p.summary as post_summary
    , p.view_count as post_view_count
    , p.comment_count as post_comment_count
    , p.thumbnail_key as post_thumbnail_key
    , p.thumbnail_metadata as post_thumbnail_metadata
from series s
left join media m on m.record_id = text(s.id) and m.record_type = 'series'
left join series_posts sp on sp.series_id = s.id
left join post_list p on p.id = sp.post_id
where
    s.slug = 'lib-lab'
    and s.is_published_yn = 'Y'
order by sp.order