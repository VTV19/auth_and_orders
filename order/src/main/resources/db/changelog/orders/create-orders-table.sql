create table if not exists orders (
    id serial not null constraint order_pk primary key,
    user_id int not null,
    from_station_id int not null references orders.public.station on delete cascade,
    to_station_id int not null references orders.public.station on delete cascade,
    status int not null,
    created timestamp default now()
);