ALTER TABLE product ADD COLUMN dimensions VARCHAR(100);
-- لو كنتي عايزة تحذفي عمود chain_length القديم بالمرة:
-- ALTER TABLE product DROP COLUMN chain_length;