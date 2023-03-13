insert into users (username, password, first_name,last_name, age) values ('admin', '$2a$10$6I3YxvPMTfZSVPveOufk4ei6pPWM3NYzsXX7FEKgVfZwiQ7GSVIC6', 'Ivan', 'Ivanov', 32);
insert into users (username, password, first_name,last_name, age) values ('user', '$2a$10$zzs1n336kRt47mHBfTUVb.NmKvuih.ZNegDSWmrqnlFGRY3ZKnyTq', 'Petr', 'Semenov', 26);

insert into roles (role_name) values ('ROLE_ADMIN');
insert into roles (role_name) values ('ROLE_USER');

insert into users_roles values(1, 1);
insert into users_roles values(2, 2);


insert into cafe (name, city, email, phone) values ('La_Donna', 'London', 'LaDonna@london.com', '555');
insert into cafe (name, city, email, phone) values ('La_Luna', 'Berlin', 'LaLuna@berlin.com', '111');
insert into cafe (name, city, email, phone) values ('La_Pizza', 'Rome', 'LaPizza@rome.com', '777');

insert into pizza (name, pizza_size, key_ingredients, price, cafe_id) values ('Margarita', 'L', 'Cheese, Tomatos', '15', 1);
insert into pizza (name, pizza_size, key_ingredients, price, cafe_id) values ('Pepperoni', 'L', 'Ham, Cheese', '17', 1);
insert into pizza (name, pizza_size, key_ingredients, price, cafe_id) values ('4_cheese', 'L', 'Parmesan, Dor_blue', '18', 2);
insert into pizza (name, pizza_size, key_ingredients, price, cafe_id) values ('Hawaian', 'L', 'Tomato, Pineapple', '19', 2);
insert into pizza (name, pizza_size, key_ingredients, price, cafe_id) values ('Manhatten', 'L', 'Proscutto, Mozarella', '20', 3);