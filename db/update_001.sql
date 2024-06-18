create table person (
                        id serial primary key not null,
                        login varchar(2000) unique,
                        password varchar(2000)
);

insert into person (login, password) values ('Vlad', '123');
insert into person (login, password) values ('Marina', '123');
insert into person (login, password) values ('Artem', '123');
insert into person (login, password) values ('Maksim', '123');