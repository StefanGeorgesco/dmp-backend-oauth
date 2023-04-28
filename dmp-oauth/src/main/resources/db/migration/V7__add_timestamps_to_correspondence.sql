alter table t_correspondence add column created_date timestamp;
update t_correspondence set created_date=now() where created_date is null;
alter table t_correspondence add column last_modified_date timestamp;
update t_correspondence set last_modified_date=now() where last_modified_date is null;