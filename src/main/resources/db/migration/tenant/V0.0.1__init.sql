--   #####   #######  ######   #     #   #####   #######  #     #  ######   #######
--  #     #     #     #     #  #     #  #     #     #     #     #  #     #  #
--  #           #     #     #  #     #  #           #     #     #  #     #  #
--   #####      #     ######   #     #  #           #     #     #  ######   #####
--        #     #     #   #    #     #  #           #     #     #  #   #    #
--  #     #     #     #    #   #     #  #     #     #     #     #  #    #   #
--   #####      #     #     #   #####    #####      #      #####   #     #  #######

CREATE TABLE revision
(
    id            int4 NOT NULL,
    "timestamp"   int8 NOT NULL,
    ip_address    varchar(255) NULL,
    modifier_user varchar(255) NULL,
    CONSTRAINT revision_pkey PRIMARY KEY (id)
);

CREATE SEQUENCE revision_seq
    INCREMENT BY 50
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 1
	CACHE 1
	NO CYCLE;

-- userr definition

CREATE TABLE userr
(
    user_id            bigserial    NOT NULL,
    username           varchar(30)  NOT NULL,
    "password"         varchar(250) NOT NULL,
    "name"             varchar(50)  NOT NULL,
    email              varchar(50) NULL,
    deleted            bool         NOT NULL,
    "locked"           bool         NOT NULL,
    one_log_pwd        bool         NOT NULL,
    last_login_ts      timestamptz(6) NULL,
    profile_picture    oid NULL,
    created_by         varchar(255) NOT NULL,
    last_modified_by   varchar(255) NOT NULL,
    created_date       timestamptz(6) NOT NULL,
    last_modified_date timestamptz(6) NOT NULL,
    CONSTRAINT uk_n67vkjwdu5sqqnyg6m79gmvo8 UNIQUE (username),
    CONSTRAINT userr_pkey PRIMARY KEY (user_id)
);
CREATE INDEX idxfquhh9vhs8636tad5s6jvfvei ON userr USING btree (email);
CREATE INDEX idxn67vkjwdu5sqqnyg6m79gmvo8 ON userr USING btree (username);
CREATE INDEX idxsk89oerwq4vbeabcmutif1l5f ON userr USING btree (name);

-- userr_log definition

CREATE TABLE userr_log
(
    user_id             int8 NOT NULL,
    rev                 int4 NOT NULL,
    revtype             int2 NULL,
    deleted             bool NULL,
    deleted_mod         bool NULL,
    email               varchar(50) NULL,
    email_mod           bool NULL,
    "locked"            bool NULL,
    locked_mod          bool NULL,
    "name"              varchar(50) NULL,
    name_mod            bool NULL,
    one_log_pwd         bool NULL,
    one_log_pwd_mod     bool NULL,
    "password"          varchar(250) NULL,
    password_mod        bool NULL,
    profile_picture     bytea NULL,
    profile_picture_mod bool NULL,
    username            varchar(30) NULL,
    username_mod        bool NULL,
    CONSTRAINT userr_log_pkey PRIMARY KEY (rev, user_id)
);
-- userr_log foreign keys
ALTER TABLE userr_log
    ADD CONSTRAINT fk1y02m3un1plefo6yc2u7vlhfh FOREIGN KEY (rev) REFERENCES revision (id);

-- person definition

CREATE TABLE person
(
    code               bigserial    NOT NULL,
    first_name         varchar(50)  NOT NULL,
    last_name          varchar(50)  NOT NULL,
    email              varchar(100) NOT NULL,
    phone              varchar(50)  NOT NULL,
    date_of_birth      date         NOT NULL,
    occupation         varchar(100) NOT NULL,
    "role"             varchar(50)  NOT NULL,
    important          bool         NOT NULL,
    created_by         varchar(255) NOT NULL,
    last_modified_by   varchar(255) NOT NULL,
    created_date       timestamptz(6) NOT NULL,
    last_modified_date timestamptz(6) NOT NULL,
    CONSTRAINT person_pkey PRIMARY KEY (code)
);
CREATE INDEX idx7d9wr9eid9hei15m3t98w0lya ON person USING btree (last_name);
CREATE INDEX idxexqo3u81yamdnfwndpx8svr0w ON person USING btree (first_name);
CREATE INDEX idxfwmwi44u55bo4rvwsv0cln012 ON person USING btree (email);

-- person_log definition

CREATE TABLE person_log
(
    code              int8 NOT NULL,
    rev               int4 NOT NULL,
    revtype           int2 NULL,
    date_of_birth     date NULL,
    date_of_birth_mod bool NULL,
    email             varchar(100) NULL,
    email_mod         bool NULL,
    first_name        varchar(50) NULL,
    first_name_mod    bool NULL,
    important         bool NULL,
    important_mod     bool NULL,
    last_name         varchar(50) NULL,
    last_name_mod     bool NULL,
    occupation        varchar(100) NULL,
    occupation_mod    bool NULL,
    phone             varchar(50) NULL,
    phone_mod         bool NULL,
    "role"            varchar(50) NULL,
    role_mod          bool NULL,
    CONSTRAINT person_log_pkey PRIMARY KEY (rev, code)
);
-- person_log foreign keys
ALTER TABLE person_log
    ADD CONSTRAINT fkrwljtig2qn16svh3bxhgqvod1 FOREIGN KEY (rev) REFERENCES revision (id);


-- --  ######      #     #######     #
-- --  #     #    # #       #       # #
-- --  #     #   #   #      #      #   #
-- --  #     #  #     #     #     #     #
-- --  #     #  #######     #     #######
-- --  #     #  #     #     #     #     #
-- --  ######   #     #     #     #     #

INSERT INTO userr (username, "password", "name", email, last_login_ts, deleted, "locked", one_log_pwd,
                   profile_picture, created_by, last_modified_by, created_date, last_modified_date)
VALUES ('root', '$2a$10$8c76Jfm15Ib7IOVh7l1DJOKtKUxeHvSlE/oj39W3uKbNyS3oK7BbK', 'Manager',
        'info@fredpena.dev', NULL, FALSE, FALSE, FALSE, NULL, 'root', 'root', NOW(), NOW());