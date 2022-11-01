delete from t_file where id in (select id from t_patient_file)

delete from t_doctor_specialty

delete from t_file where id in (select id from t_doctor)
