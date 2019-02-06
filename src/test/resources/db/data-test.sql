-- CLEANUP:
DELETE FROM public."role"
DELETE FROM public."account"


-- RESET SEQUENCES:
setval('account_id_seq', 1)
setval('role_id_seq', 1)


-- ROLE INSERTS:
INSERT INTO public."role"
("name", description)
VALUES('ROLE_ADMIN', 'Admin role.');

INSERT INTO public."role"
("name", description)
VALUES('ROLE_MENTOR', 'Mentor role.');

INSERT INTO public."role"
("name", description)
VALUES('ROLE_MENTEE', 'Mentee role.');

INSERT INTO public."role"
("name", description)
VALUES('ROLE_JMP_USER', 'JMP User role.');


-- ACCOUNT INSERTS:
INSERT INTO public.account
(nickname, password, first_name, last_name, email, status, role_id)
VALUES('adminka', 'agjld;kagk', 'mr', 'adminos', 'smwht@gmail.com', 'ACTIVE', 1);

INSERT INTO public.account
(nickname, password, first_name, last_name, email, status, role_id)
VALUES('jmpuser', 'sfgla;jgkadg;kj', 'mr', 'jmp', 'jmp@gmail.com', 'ACTIVE', 6);

INSERT INTO public.account
(nickname, password, first_name, last_name, email, status, role_id)
VALUES('mentor', 'aslkflagha', 'mr', 'mentor', 'mentor@gmail,com', 'INACTIVE', 2);
