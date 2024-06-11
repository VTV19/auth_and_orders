create table if not exists session (
    id serial not null constraint session_pk primary key,
    user_id int not null references api_user,
    token varchar(255) not null,
    expires timestamp not null
);