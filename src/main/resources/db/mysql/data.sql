INSERT INTO palcole.roles VALUES (1, "ADMIN"), (2, "USER");

INSERT INTO palcole.users (roles_role_id, username, email, password, account_expiration_date, credentials_expiration_date) 
VALUES (1, "joaquin", "admin@localhost.es", "$2a$10$bOY/2//tFFoJwliab9gyHubB6kFEdhIS9SIFS4aeOcVqj3ZFQZVa.", "2999-12-31 23:59:59", "2999-12-31 23:59:59");

INSERT INTO palcole.users (roles_role_id, username, email, password, account_expiration_date, credentials_expiration_date) 
VALUES (2, "manuel", "manuel@localhost.es", "$2a$10$bOY/2//tFFoJwliab9gyHubB6kFEdhIS9SIFS4aeOcVqj3ZFQZVa.", "2999-12-31 23:59:59", "2999-12-31 23:59:59");
