CREATE TABLE IF NOT EXISTS source(
	id SERIAL PRIMARY KEY,
	name TEXT UNIQUE,
	description TEXT
	);

CREATE TABLE IF NOT EXISTS uws_user(
	id SERIAL PRIMARY KEY,
	name TEXT UNIQUE,
	password TEXT,
	owner BOOL,
	admin BOOL,
	create_post BOOL,
	create_source BOOL,
	edit_source BOOL,
	edit_group BOOL
	);

CREATE TABLE IF NOT EXISTS mail_setting(
	id SERIAL PRIMARY KEY,
	uid INT,
	mail_addr TEXT,
	daily BOOL,
	weekly BOOL,
	day_of_week SMALLINT,
	last_daily TIMESTAMP,
	last_weekly TIMESTAMP,

	CONSTRAINT fk_user
		FOREIGN KEY (uid)
		REFERENCES uws_user(id)
	);