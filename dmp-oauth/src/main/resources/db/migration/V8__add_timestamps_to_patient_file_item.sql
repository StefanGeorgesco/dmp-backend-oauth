alter table t_patient_file_item add column created_date timestamp;
update t_patient_file_item set created_date=now() where created_date is null;
alter table t_patient_file_item add column last_modified_date timestamp;
update t_patient_file_item set last_modified_date=now() where last_modified_date is null;