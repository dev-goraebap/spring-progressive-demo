SELECT A.id
     , A.title
     , A.content
     , A.post_type
     , A.summary
     , A.view_count
     , A.is_published_yn
     , A.published_at
     , C.key
     , C.metadata
FROM posts A
         LEFT JOIN attachments B ON B.record_id = TEXT(A.id)
    AND B.record_type = 'post'
    AND B.name = 'thumbnail'
         LEFT JOIN blobs C ON C.id = B.blob_id


with post_counts as (select series_id, count(*) as cnt
                     from series_posts sp
                              inner join posts p on p.id = sp.post_id
                     where p.is_published_yn = 'Y'
                     group by series_id),
     media as (select a.record_id
                    , b.key
                    , b.metadata
               from attachments a
                        left join blobs b on b.id = a.blob_id
               where a.record_type = 'series'
                 and a.name = 'thumbnail')
select s.slug
     , s.name
     , s.description
     , s.published_at
     , s.status
     , pc.cnt
     , m.key
     , m.metadata
from series s
         left join post_counts pc on pc.series_id = s.id
         left join media m on m.record_id = text(s.id)
order by case
             when s.status = 'COMPLETED' then 1
             when s.status = 'PROGRESS' then 2
             when s.status = 'PLAN' then 3
             else 4
             end,
         s.created_at desc
