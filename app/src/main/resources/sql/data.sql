

INSERT INTO roles (name, created, updated) VALUES
       ('ROLE_USER', NOW(), NOW()),
       ('ROLE_ADMIN', NOW(), NOW());


INSERT INTO users (username, first_name, last_name, email, password, phone, status, created, updated) VALUES
            ('john_doe', 'John', 'Doe', 'john@example.com', '$2a$10$XURPshQe1L9pLh4L4QbZ2O8V9A8QwX7Y6Z5V4U3T2S1R0O9I8U7Y', '+1234567890', 'ACTIVE', NOW(), NOW()),
            ('jane_smith', 'Jane', 'Smith', 'jane@example.com', '$2a$10$XURPshQe1L9pLh4L4QbZ2O8V9A8QwX7Y6Z5V4U3T2S1R0O9I8U7Y', '+1234567891', 'ACTIVE', NOW(), NOW()),
            ('admin', 'Admin', 'User', 'admin@example.com', '$2a$10$XURPshQe1L9pLh4L4QbZ2O8V9A8QwX7Y6Z5V4U3T2S1R0O9I8U7Y', '+1234567899', 'ACTIVE', NOW(), NOW());

