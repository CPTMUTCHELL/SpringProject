-- create table boot.users (id serial primary key , login varchar(255),password varchar (255));
-- create table boot.roles (id serial primary key , name varchar (255));
-- create table boot.user_role (user_id int, role_id int, constraint user_id_to_id foreign key (user_id) references boot.users (id),
-- constraint role_id_to_id foreign key (role_id) references boot.roles(id));
select u.login,ur.role_id,r.name from boot.users u inner join boot.user_role ur on u.id = ur.user_id join boot.roles r on r.id = ur.role_id

