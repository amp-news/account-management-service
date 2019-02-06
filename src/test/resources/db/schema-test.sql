-- TABLES CREATION
CREATE TABLE IF NOT EXISTS public."role" (
	id bigserial NOT NULL,
	"name" varchar(100) NOT NULL,
	description varchar(1000) NULL,
	CONSTRAINT role_pk PRIMARY KEY (id)
)
WITH (
	OIDS=FALSE
) ;


CREATE TABLE IF NOT EXISTS public.account (
	id bigserial NOT NULL,
	nickname varchar(100) NOT NULL,
	password varchar(2024) NOT NULL,
	first_name varchar(100) NULL,
	last_name varchar(100) NULL,
	email varchar(100) NOT NULL,
	status varchar(50) NOT NULL,
	role_id bigint NOT NULL,
	CONSTRAINT account_pk PRIMARY KEY (id),
	CONSTRAINT account_role_fk FOREIGN KEY (role_id) REFERENCES role(id)
)
WITH (
	OIDS=FALSE
) ;


-- Permissions
ALTER TABLE public.account OWNER TO admin;
ALTER TABLE public."role" OWNER TO admin;


