create table if not exists api_user (
    id serial not null constraint user_pk primary key,
    nickname varchar(50) not null,
    email varchar(100) unique not null,
    password varchar(255) not null,
    created timestamp default now()
);