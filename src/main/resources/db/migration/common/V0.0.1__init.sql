-- common_user definition

CREATE TABLE common_user
(
    username   varchar(30)  NOT NULL,
    "name"     varchar(50)  NOT NULL,
    "password" varchar(250) NOT NULL,
    CONSTRAINT common_user_pkey PRIMARY KEY (username)
);

-- tenant definition

CREATE TABLE tenant
(
    tenant_id varchar(255) NOT NULL,
    "name"    varchar(100) NOT NULL,
    slogan    varchar(100) NOT NULL,
    "type"    varchar(100) NOT NULL,
    phone     varchar(100) NOT NULL,
    email     varchar(100) NOT NULL,
    website   varchar(100) NOT NULL,
    address   varchar(100) NOT NULL,
    logo      varchar(100) NOT NULL,
    CONSTRAINT tenant_pkey PRIMARY KEY (tenant_id)
);
CREATE INDEX idxdcxf3ksi0gyn1tieeq0id96lm ON tenant USING btree (name);

-- tenant_user definition

CREATE TABLE tenant_user
(
    tenant_id varchar(255) NOT NULL,
    username  varchar(255) NOT NULL,
    disabled  bool         NOT NULL,
    CONSTRAINT tenant_user_pkey PRIMARY KEY (tenant_id, username)
);
-- tenant_user foreign keys
ALTER TABLE tenant_user
    ADD CONSTRAINT fk98prq0pdpjuwlntl7ruqorfjr FOREIGN KEY (username) REFERENCES common_user (username);
ALTER TABLE tenant_user
    ADD CONSTRAINT fkjbyohwto7pt48xywupgf4vjc7 FOREIGN KEY (tenant_id) REFERENCES tenant (tenant_id);


INSERT INTO common_user (username, "name", "password")
VALUES ('root', 'Manager', '$2a$10$8c76Jfm15Ib7IOVh7l1DJOKtKUxeHvSlE/oj39W3uKbNyS3oK7BbK');