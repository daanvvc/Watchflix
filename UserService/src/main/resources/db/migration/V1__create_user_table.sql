CREATE TABLE roles (
    id INTEGER PRIMARY KEY,
    name VARCHAR(100) UNIQUE NOT NULL
);

CREATE TABLE users (
    id UUID PRIMARY KEY,
    email VARCHAR(254) UNIQUE NOT NULL,
    username VARCHAR(254) UNIQUE NULL,
    role_id INTEGER REFERENCES roles(id)
);

insert into roles VALUES
    (0, 'USER'),
    (1, 'ADMIN');

insert into users VALUES
     ('6df588f1-854d-4939-b800-bc6eb5cc450c', '96DdrQFtjgHn2NXvIRbjKSvt3ufS3OnkU5ayadZsqATHcK50',
      'john.doe', 0),
     ('4bf2a1b1-4550-44ad-9f90-0f2a6f2862d8', '+a7UrVlskxLG2M7lKSboIWTn3aTPS0ytKNcT4UO0n95ZLgrK7CIn',
      'daanvervaecke', 1);