insert into users(username, password) values ('user', '$2a$12$rfG90Tzo1Aw8lJjWNSZtE.eAkESMBjtiOJb0OYjUeAqqEuwTwKX9.');

insert into items(id, item_name, description, price) values (100, 'Test Item 1', 'Proin a orci et ipsum accumsan porttitor ac non est.', 12);
insert into items(id, item_name, description, price) values (200, 'Test Item 2', 'Description', 234234.7);
insert into items(id, item_name, description, price) values (300, 'Test Item 3', 'Test', 1200);

insert into orders(id, username) values (100, 'user');
insert into orders(id, username) values (200, 'user');
insert into orders(id, username) values (300, 'user');

insert into orders_items(order_id, item_id, count) values (100, 100, 10);
insert into orders_items(order_id, item_id, count) values (100, 200, 5);
insert into orders_items(order_id, item_id, count) values (100, 300, 3);
insert into orders_items(order_id, item_id, count) values (200, 100, 1);
insert into orders_items(order_id, item_id, count) values (200, 200, 1);
insert into orders_items(order_id, item_id, count) values (300, 100, 1);