INSERT INTO common_user (username, "name", "password")
VALUES ('f.pena', 'Fred Pena', '$2a$10$8c76Jfm15Ib7IOVh7l1DJOKtKUxeHvSlE/oj39W3uKbNyS3oK7BbK'),
       ('m.perez', 'Maria Perez', '$2a$10$8c76Jfm15Ib7IOVh7l1DJOKtKUxeHvSlE/oj39W3uKbNyS3oK7BbK');


INSERT INTO tenant (tenant_id, "name", slogan, "type", phone, email, website, address, logo)
VALUES ('tenant_01', 'Innovative Solutions Inc.', 'Turning Ideas into Realities',
        'Innovation and Technology Consulting', '+1 (555) 123-4567', 'info@innovativesolutions.com',
        'www.innovativesolutions.com', 'Technology Avenue #123, Future Bright District, Progress City',
        'images/tenant_01.png'),
       ('tenant_02', 'EcoTech Sustainable Recycling LLC', 'For a Cleaner, Greener Future',
        'Recycling and Sustainability', '+1 (555) 987-6543', 'contact@ecotechsustainability.com',
        'www.ecotechsustainability.com', 'Recycler Street #456, EcoLife District, Sustainable City',
        'images/tenant_02.png'),
       ('tenant_03', 'Flavors of the World International Catering', 'Culinary Experiences Without Borders',
        'Catering and Events', '+1 (555) 789-0123', 'events@flavorsoftheworld.com', 'www.flavorsoftheworld.com',
        'Gastronomic Plaza #789, Exquisite Delights District, Gourmet City',
        'images/tenant_03.png');


INSERT INTO tenant_user (tenant_id, username, disabled)
VALUES ('tenant_01', 'root', false),
       ('tenant_02', 'root', false),
       ('tenant_03', 'root', false),
       ('tenant_01', 'f.pena', false),
       ('tenant_03', 'f.pena', false),
       ('tenant_02', 'm.perez', false);
