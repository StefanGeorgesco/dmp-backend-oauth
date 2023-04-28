alter table t_file add column created_date timestamp;
update t_file set created_date=now() where created_date is null;
alter table t_file add column last_modified_date timestamp;
update t_file set last_modified_date=now() where last_modified_date is null;