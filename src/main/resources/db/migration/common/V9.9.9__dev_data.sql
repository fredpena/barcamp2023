INSERT INTO common_user (username, "name", "password")
VALUES ('f.pena', 'Fred Pena', '$2a$10$8c76Jfm15Ib7IOVh7l1DJOKtKUxeHvSlE/oj39W3uKbNyS3oK7BbK'),
       ('m.perez', 'Maria Perez', '$2a$10$8c76Jfm15Ib7IOVh7l1DJOKtKUxeHvSlE/oj39W3uKbNyS3oK7BbK');


INSERT INTO tenant (tenant_id, "name")
VALUES ('tenant_01', 'Tenant 1'),
       ('tenant_02', 'Tenant 2'),
       ('tenant_03', 'Tenant 3');


INSERT INTO tenant_user (tenant_id, username, disabled)
VALUES ('tenant_01', 'root', false),
       ('tenant_02', 'root', false),
       ('tenant_03', 'root', false),
       ('tenant_01', 'f.pena', false),
       ('tenant_03', 'f.pena', false),
       ('tenant_02', 'm.perez', false);
