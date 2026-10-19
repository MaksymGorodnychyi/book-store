INSERT INTO categories (id, name, description, is_deleted)
VALUES (1, 'Keto', 'Keto diet books', false);

INSERT INTO books (id, title, author, isbn, price, description, cover_image, is_deleted)
VALUES (1, 'KetoDiet', 'Test Author', '9781234567897', 100.00, 'Test description', 'cover.jpg', false);

INSERT INTO books_categories (book_id, category_id)
VALUES (1, 1);
