create table if not exists post(
	id serial primary key,
	name varchar(255),
	text text,
	link varchar(255) unique,
	created varchar(50)
);