drop table if exists User;
drop table if exists House;
drop table if exists Appointment;

create table if not exists User (
    username    varchar(256)    primary key,
    password    varchar(256)    not null,
    first_name  varchar(256)    not null,
    last_name   varchar(256)    not null,
    privileges  integer         not null
);

create table if not exists House (
    type        integer      not null,
    address     varchar(256) not null,
    price       integer      not null,
    floor       integer      not null,
    elevator    boolean      not null,
    balconies   integer      not null,
    terrace     integer      not null,
    garden      integer      not null,
    accessories integer      not null,
    bedrooms    integer      not null,
    sqm         integer      not null,
    description text         not null,
    image0      varchar(256),
    image1      varchar(256),
    image2      varchar(256)
);

create table if not exists Appointment (
    time    date            not null,
    buyer   varchar(256)    not null,
    agent   varchar(256)    not null,
    house   integer         not null,

    foreign key (buyer)
        references User(username)
        on delete cascade
        on update cascade,
    foreign key (agent)
        references User(username)
        on delete cascade
        on update cascade,
    foreign key (house)
        references House(ROWID)
        on delete cascade
        on update cascade
);
