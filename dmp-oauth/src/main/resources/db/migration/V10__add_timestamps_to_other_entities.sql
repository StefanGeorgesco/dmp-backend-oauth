alter table t_disease add column created_date timestamp;
update t_disease set created_date=now() where created_date is null;
alter table t_disease add column last_modified_date timestamp;
update t_disease set last_modified_date=now() where last_modified_date is null;

alter table t_medical_act add column created_date timestamp;
update t_medical_act set created_date=now() where created_date is null;
alter table t_medical_act add column last_modified_date timestamp;
update t_medical_act set last_modified_date=now() where last_modified_date is null;

alter table t_specialty add column created_date timestamp;
update t_specialty set created_date=now() where created_date is null;
alter table t_specialty add column last_modified_date timestamp;
update t_specialty set last_modified_date=now() where last_modified_date is null;