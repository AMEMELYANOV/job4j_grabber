CREATE TABLE company
(
    id integer NOT NULL,
    name character varying,
    CONSTRAINT company_pkey PRIMARY KEY (id)
);

CREATE TABLE person
(
    id integer NOT NULL,
    name character varying,
    company_id integer,
    CONSTRAINT person_pkey PRIMARY KEY (id)
);

insert into company (id, name) values (1, 'Company1');
insert into company (id, name) values (2, 'Company2');
insert into company (id, name) values (3, 'Company3');
insert into company (id, name) values (4, 'Company4');
insert into company (id, name) values (5, 'Company5');

insert into person (id, name, company_id) values (1, 'Person1', 1);
insert into person (id, name, company_id) values (2, 'Person2', 1);
insert into person (id, name, company_id) values (3, 'Person3', 2);
insert into person (id, name, company_id) values (4, 'Person4', 2);
insert into person (id, name, company_id) values (5, 'Person5', 3);
insert into person (id, name, company_id) values (6, 'Person6', 3);
insert into person (id, name, company_id) values (7, 'Person7', 4);
insert into person (id, name, company_id) values (8, 'Person8', 4);
insert into person (id, name, company_id) values (9, 'Person9', 5);
insert into person (id, name, company_id) values (10, 'Person10', 5);
insert into person (id, name, company_id) values (11, 'Person11', 5);

select * from company;
select * from person;

-- 1. В одном запросе получить
-- - имена всех person, которые не состоят в компании с id = 5;
-- - название компании для каждого человека.
select p.name as name, c.name as company 
from person as p join company as c 
on p.company_id = c.id 
where c.id <> 5;

-- 2. Необходимо выбрать название компании с максимальным количеством человек 
-- + количество человек в этой компании.
select c.name as company, count(p.name) as quantity 
from company as c join person as p 
on p.company_id = c.id 
group by (c.name) 
order by quantity 
desc limit 1;
