delete from t_file where id in (select id from t_patient_file);

delete from t_doctor_specialty where 1;

delete from t_file where id in (select id from t_doctor);
