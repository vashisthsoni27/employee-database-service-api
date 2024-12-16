-- Insert predefined roles
INSERT INTO role (id, name) VALUES (1, 'ADMIN'), (2, 'USER'), (3, 'MANAGER');

-- Insert employees associated with roles
INSERT INTO employee (first_name, surname, role_id) VALUES
('John', 'Doe', 1),
('Alice', 'Smith', 2),
('Bob', 'Johnson', 3);

-- Insert projects
INSERT INTO project (name, employee_id) VALUES
('Project Alpha', 1),
('Project Beta', 2),
('Project Gamma', 3);
