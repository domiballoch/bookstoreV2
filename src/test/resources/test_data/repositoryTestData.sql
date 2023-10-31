--books creation

INSERT INTO book (isbn, category, title, author, price, stock)
VALUES
(1001, 'FICTION', 'Tall Tales', 'Mr Fredrikson', 4.99, 25),
(1002, 'FICTION', 'Short Tales', 'Mr Fredrikson', 24.99, 50),
(1003, 'TRAVEL', 'A Foreign Land', 'Mr Kite', 11.99, 10),
(1004, 'ROMANCE', 'An English Rose', 'Mrs Juliet', 7.99, 20),
(1005, 'HORROR', 'The Crab Man', 'Mr Gustavo', 14.99, 100);

--users creation

INSERT INTO users (user_id, first_name, last_name, address_line_1, address_line_2, post_code)
VALUES
(2001, 'John', 'Smith', '10 Something Road', 'London', 'SW1'),
(2002, 'Paul', 'Roberts', '20 Long Road', 'London', 'SW2'),
(2003, 'Mary', 'Klein', '30 Anywhere Street', 'London', 'SW3'),
(2004, 'Jane', 'Terry', '40 Lost Avenue', 'London', 'SW4'),
(2005, 'Jack', 'Murry', '50 Somewhere Close', 'London', 'SW5');

--basket creation

INSERT INTO basket (basket_id)
VALUES
(4001),
(4002),
(4003),
(4004),
(4005);

--basket items creation

INSERT INTO basket_item (basket_item_id, fk_basket_id, fk_isbn, quantity, total_price)
VALUES
(3001, 4001, 1001, 1, 4.99),
(3002, 4002, 1002, 1, 24.99),
(3003, 4003, 1003, 1, 11.99),
(3004, 4004, 1004, 1, 7.99),
(3005, 4005, 1005, 1, 14.99);

--order details creation

INSERT INTO order_details (order_details_id, fk_user_id, total_order_price, order_date)
VALUES
(6001, 2001, 4.99, '2022-03-01 09:00:00'),
(6002, 2002, 24.99, '2022-03-02 10:00:00'),
(6003, 2003, 11.99, '2022-03-03 11:00:00'),
(6004, 2004, 7.99, '2022-03-04 12:00:00'),
(6005, 2005, 14.99, '2022-03-05 13:00:00');

--order items creation

INSERT INTO order_item (order_item_id, fk_order_details_id, fk_isbn, quantity, total_price)
VALUES
(5001, 6001, 1001, 1, 4.99),
(5002, 6002, 1002, 1, 24.99),
(5003, 6003, 1003, 1, 11.99),
(5004, 6004, 1004, 1, 7.99),
(5005, 6005, 1005, 1, 14.99);
