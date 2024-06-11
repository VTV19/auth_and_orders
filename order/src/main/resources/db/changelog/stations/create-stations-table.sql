create table if not exists station (
    id serial not null constraint station_pk primary key,
    station varchar(50) not null
);