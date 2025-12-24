-- ip_address 컬럼을 inet에서 varchar로 변경
ALTER TABLE blocked_ips ALTER COLUMN ip_address TYPE varchar(45) USING ip_address::text;
COMMIT;