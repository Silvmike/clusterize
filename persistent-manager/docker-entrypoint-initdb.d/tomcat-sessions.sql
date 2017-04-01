CREATE USER tomcat;
ALTER USER tomcat with password 'tomcat';

CREATE DATABASE tomcat;
GRANT ALL PRIVILEGES ON DATABASE tomcat TO tomcat;

\c tomcat;

CREATE TABLE public.tomcat_sessions
(
  session_id character varying(100) NOT NULL,
  valid_session character(1) NOT NULL,
  max_inactive integer NOT NULL,
  last_access bigint NOT NULL,
  app_name character varying(255),
  session_data bytea,
  CONSTRAINT tomcat_sessions_pkey PRIMARY KEY (session_id)
);

CREATE INDEX app_name_index
  ON public.tomcat_sessions
  USING btree
  (app_name);

GRANT SELECT, INSERT, UPDATE, DELETE ON TABLE public.tomcat_sessions TO tomcat;
