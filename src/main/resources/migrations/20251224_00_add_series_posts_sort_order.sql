-- series_posts 테이블에 sort_order 컬럼 추가
ALTER TABLE series_posts ADD COLUMN IF NOT EXISTS sort_order integer DEFAULT 999;

-- 기존 order 컬럼 데이터를 sort_order로 마이그레이션
UPDATE series_posts SET sort_order = "order" WHERE "order" IS NOT NULL;
UPDATE series_posts SET sort_order = 999 WHERE sort_order IS NULL;

ALTER TABLE series_posts ALTER COLUMN sort_order SET NOT NULL;

-- 기존 order 컬럼 삭제
ALTER TABLE series_posts DROP COLUMN IF EXISTS "order";

COMMIT;