CREATE TABLE IF NOT EXISTS source(
	id SERIAL PRIMARY KEY,
	name TEXT UNIQUE,
	description TEXT
	);

CREATE TABLE IF NOT EXISTS source_attr(
	id SERIAL PRIMARY KEY,
	source_id INT,
	name TEXT,
	value TEXT,

	CONSTRAINT fk_source
		FOREIGN KEY (source_id)
		REFERENCES source(id)
	);

CREATE TABLE IF NOT EXISTS source_update(
	id SERIAL PRIMARY KEY,
	source_id INT,
	update_time TIMESTAMP,
	value TEXT,

	CONSTRAINT fk_source
		FOREIGN KEY (source_id)
		REFERENCES source(id)
	);

CREATE TABLE IF NOT EXISTS sub_group(
	id SERIAL PRIMARY KEY,
	name TEXT UNIQUE,
	description TEXT,
	user_owned BOOL
	);

CREATE TABLE IF NOT EXISTS group_child(
	id SERIAL PRIMARY KEY,
	parent_id INT,
	child_id INT,

	CONSTRAINT fk_parent
		FOREIGN KEY (parent_id)
		REFERENCES sub_group(id),
	CONSTRAINT fk_child
		FOREIGN KEY (child_id)
		REFERENCES sub_group(id)
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
	subscribe INT,
	ignore INT,
	daily BOOL,
	weekly BOOL,
	day_of_week SMALLINT,
	last_daily TIMESTAMP,
	last_weekly TIMESTAMP,

	CONSTRAINT fk_user
		FOREIGN KEY (uid)
		REFERENCES uws_user(id),
	CONSTRAINT fk_subscribe
		FOREIGN KEY (subscribe)
		REFERENCES sub_group(id),
	CONSTRAINT fk_ignore
		FOREIGN KEY (ignore)
		REFERENCES sub_group(id)
	);

CREATE TABLE IF NOT EXISTS audit_log(
	id SERIAL PRIMARY KEY,
	uid INT,
	change_time TIMESTAMP,
	description TEXT,

	CONSTRAINT fk_user
		FOREIGN KEY (uid)
		REFERENCES uws_user(id)
	);

CREATE TABLE IF NOT EXISTS post(
	id SERIAL PRIMARY KEY,
	uid INT,
	title TEXT,
	content TEXT,
	created TIMESTAMP,

	CONSTRAINT fk_user
		FOREIGN KEY (uid)
		REFERENCES uws_user(id)
	);

CREATE TABLE IF NOT EXISTS post_comment(
	id SERIAL PRIMARY KEY,
	uid INT,
	post_id INT,
	content TEXT,
	created TIMESTAMP,

	CONSTRAINT fk_user
		FOREIGN KEY (uid)
		REFERENCES uws_user(id),
	CONSTRAINT fk_post
		FOREIGN KEY (post_id)
		REFERENCES post(id)
	);

CREATE TABLE IF NOT EXISTS poll_option(
	id SERIAL PRIMARY KEY,
	post_id INT,
	content TEXT,

	CONSTRAINT fk_post
		FOREIGN KEY (post_id)
		REFERENCES post(id)
	);

CREATE TABLE IF NOT EXISTS source_sub(
	id SERIAL PRIMARY KEY,
	group_id INT,
	source_id INT,

	CONSTRAINT fk_group
		FOREIGN KEY (group_id)
		REFERENCES sub_group(id),
	CONSTRAINT fk_source
		FOREIGN KEY (source_id)
		REFERENCES source(id)
	);

CREATE TABLE IF NOT EXISTS post_sub(
	id SERIAL PRIMARY KEY,
	group_id INT,
	uid INT,

	CONSTRAINT fk_group
		FOREIGN KEY (group_id)
		REFERENCES sub_group(id),
	CONSTRAINT fk_user
		FOREIGN KEY (uid)
		REFERENCES uws_user(id)
	);

CREATE TABLE IF NOT EXISTS seen_post(
	id SERIAL PRIMARY KEY,
	uid INT,
	post_id INT,

	CONSTRAINT fk_user
		FOREIGN KEY (uid)
		REFERENCES uws_user(id),
	CONSTRAINT fk_post
		FOREIGN KEY (post_id)
		REFERENCES post(id)
	);

CREATE TABLE IF NOT EXISTS seen_update(
	id SERIAL PRIMARY KEY,
	uid INT,
	update_id INT,

	CONSTRAINT fk_user
		FOREIGN KEY (uid)
		REFERENCES uws_user(id),
	CONSTRAINT fk_update
		FOREIGN KEY (update_id)
		REFERENCES source_update(id)
	);

CREATE TABLE IF NOT EXISTS poll_vote(
	id SERIAL PRIMARY KEY,
	uid INT,
	option_id INT,

	CONSTRAINT fk_user
		FOREIGN KEY (uid)
		REFERENCES uws_user(id),
	CONSTRAINT fk_option
		FOREIGN KEY (option_id)
		REFERENCES poll_option(id)
	);
