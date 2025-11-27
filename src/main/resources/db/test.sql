SELECT A.id
     , A.title
     , A.content
     , A.post_type
     , A.summary
     , A.view_count
     , A.is_published_yn
     , A.published_at
     , C.key
FROM posts A
         LEFT JOIN attachments B ON B.record_id = TEXT(A.id)
    AND B.record_type = 'post'
    AND B.name = 'thumbnail'
         LEFT JOIN blobs C ON C.id = B.blob_id
