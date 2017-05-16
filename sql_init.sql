CREATE TABLE help_desk_roles (
  role_id   VARCHAR(20) NOT NULL,
  PRIMARY KEY (role_id)
);

CREATE TABLE help_desk_states (
  state_id    INT(10) NOT NULL,
  state_title VARCHAR(20) NOT NULL,
  PRIMARY KEY (state_id)
);

CREATE TABLE help_desk_category (
  category_id   VARCHAR(20) NOT NULL,
  PRIMARY KEY (category_id)
);

CREATE TABLE help_desk_maintenance (
  maintenance_message VARCHAR(512)
);

CREATE TABLE help_desk_sub_category (
  sub_category_id   VARCHAR(20) NOT NULL,
  PRIMARY KEY (sub_category_id)
);

/*_____________________DONE ________________*/
CREATE TABLE help_desk_users (
  user_username     VARCHAR(50) NOT NULL,
  role_id           VARCHAR(20) NOT NULL,
  user_password     VARCHAR(50) NOT NULL,
  user_firstname    VARCHAR(50) NOT NULL,
  user_lastname     VARCHAR(50) NOT NULL,
  user_emailaddress VARCHAR(50) NOT NULL,
  user_phonenumber  INT(10)     NOT NULL DEFAULT '0123456789',
  PRIMARY KEY (user_username),
  FOREIGN KEY (role_id) REFERENCES help_desk_roles(role_id)
);



CREATE TABLE help_desk_issues (
  issue_id          INT(10)     UNSIGNED NOT NULL AUTO_INCREMENT,
  user_username     VARCHAR(50) NOT NULL,
  state_id          INT(10)     NOT NULL,
  category_id       VARCHAR(20) NOT NULL,
  sub_category_id   VARCHAR(20),
  issue_title       TINYTEXT    NOT NULL,
  issue_description TEXT        NOT NULL,
  issue_resolution  TEXT,
  issue_tags        TEXT,
  issue_date_time_reported  DATETIME  NOT NULL,
  issue_date_time_resolved  DATETIME  ,
  issue_anonymous_user BIT      NOT NULL DEFAULT 0,
  PRIMARY KEY (issue_id),
  FOREIGN KEY (user_username) REFERENCES help_desk_users(user_username),
  FOREIGN KEY (state_id) REFERENCES help_desk_states(state_id),
  FOREIGN KEY (category_id) REFERENCES help_desk_category(category_id),
  FOREIGN KEY (sub_category_id) REFERENCES help_desk_sub_category(sub_category_id)
);

CREATE TABLE help_desk_watched_issues (
  user_username     VARCHAR(50) NOT NULL,
  issue_id          INT(10)     UNSIGNED NOT NULL,
  FOREIGN KEY (user_username) REFERENCES help_desk_users (user_username),
  FOREIGN KEY (issue_id) REFERENCES help_desk_issues (issue_id)
);

CREATE TABLE help_desk_knowledge_base (
  knowledge_base_id          INT(10)     UNSIGNED NOT NULL AUTO_INCREMENT,
  user_username     VARCHAR(50) NOT NULL,
  state_id          INT(10) NOT NULL,
  category_id       VARCHAR(20) NOT NULL,
  sub_category_id   VARCHAR(20),
  knowledge_base_title       TINYTEXT    NOT NULL,
  knowledge_base_description TEXT        NOT NULL,
  knowledge_base_resolution  TEXT        NOT NULL,
  knowledge_base_tags        TEXT  ,
  knowledge_base_date_time_reported  DATETIME  NOT NULL,
  knowledge_base_date_time_resolved  DATETIME  NOT NULL,
  PRIMARY KEY (knowledge_base_id),
  FOREIGN KEY (user_username) REFERENCES help_desk_users(user_username),
  FOREIGN KEY (state_id) REFERENCES help_desk_states(state_id),
  FOREIGN KEY (category_id) REFERENCES help_desk_category(category_id),
  FOREIGN KEY (sub_category_id) REFERENCES help_desk_sub_category(sub_category_id)
);

CREATE TABLE help_desk_comments (
  comment_id        INT(10)     UNSIGNED NOT NULL AUTO_INCREMENT,
  parent_comment_id INT(10)     UNSIGNED,
  issue_id          INT(10)     UNSIGNED,
  knowledge_base_id INT(10)     UNSIGNED,
  user_username     VARCHAR(50) NOT NULL,
  comment_body      TEXT        NOT NULL,
  comment_date_time DATETIME    NOT NULL,
  PRIMARY KEY (comment_id),
  FOREIGN KEY (issue_id) REFERENCES help_desk_issues(issue_id),
  FOREIGN KEY (user_username) REFERENCES help_desk_users(user_username),
  FOREIGN KEY (knowledge_base_id) REFERENCES help_desk_knowledge_base(knowledge_base_id)
);



INSERT INTO help_desk_states (state_id,state_title)
VALUES (1,"NEW"),
       (2,"IN_PROGRESS"),
       (3,"COMPLETED"),
       (4,"RESOLVED");



INSERT INTO help_desk_roles (role_id)
VALUES ("USER"),
       ("IT");
       


INSERT INTO help_desk_category (category_id)
VALUES ("NETWORK"),
       ("SOFTWARE"),
       ("HARDWARE"),
       ("EMAIL"),
       ("ACCOUNT");
       





INSERT INTO help_desk_sub_category (sub_category_id)
VALUES
("CANT_CONNECT"),
("SPEED"),
("DROPOUTS"),
("LOAD_SLOW"),
("NO_LOAD"),
("WONT_BOOT"),
("BSOD"),
("HDD"),
("PERIPHERAL"),
("CANT_SEND"),
("CANT_RECEIVE"),
("SPAM"),
("RESET_PASSWORD"),
("WRONG_DETAILS");



INSERT INTO help_desk_users (user_username,
 role_id,
 user_password,
 user_firstname,
 user_lastname,
 user_emailaddress)
VALUES ("chris","USER","sun4heat","Chris","ODonnell","c3165328@uon.edu.au");

INSERT INTO help_desk_users (user_username,
 role_id,
 user_password,
 user_firstname,
 user_lastname,
 user_emailaddress)
VALUES ("chris_admin","IT","cake2eat","Chris","ODonnell","c3165328@uon.edu.au");

INSERT INTO help_desk_users (user_username,
 role_id,
 user_password,
 user_firstname,
 user_lastname,
 user_emailaddress)
VALUES ("george","USER","sun4heat","George","Edwards","c3167656@uon.edu.au");

INSERT INTO help_desk_users (user_username,
 role_id,
 user_password,
 user_firstname,
 user_lastname,
 user_emailaddress)
VALUES ("george_admin","IT","cake2eat","George","Edwards","c3167656@uon.edu.au");

INSERT INTO help_desk_users (user_username,
 role_id,
 user_password,
 user_firstname,
 user_lastname,
 user_emailaddress)
VALUES ("jacob","USER","sun4heat","Jacob","Clulow","c3164461@uon.edu.au");

INSERT INTO help_desk_users (user_username,
 role_id,
 user_password,
 user_firstname,
 user_lastname,
 user_emailaddress)
VALUES ("jacob_admin","IT","cake2eat","Jacob","Clulow","c3164461@uon.edu.au");




INSERT INTO help_desk_issues (
 state_id,
 user_username,
 category_id,
 sub_category_id,
 issue_title,
 issue_description,
 issue_tags,
 issue_date_time_reported
 )
VALUES (
  "2",
  "chris",
  "ACCOUNT",
  "RESET_PASSWORD",
  "LAN password reset.",
  "I forgot my LAN password.",
  "LAN, passwordreset",
  "20161028081000"
  );







INSERT INTO help_desk_comments
 (
 parent_comment_id,
 issue_id,
 user_username,
 comment_body,
 comment_date_time
 )
VALUES (
        "1",
        "1",
        "jacob_admin",
        "Please ignore the above comment. The outgoing SMTP server settings are obtained automagically and SHOULD NOT be changed",
        "161101100805"
        );


INSERT INTO help_desk_knowledge_base (user_username, 
 state_id, 
 category_id, 
 sub_category_id, 
 knowledge_base_title, 
 knowledge_base_description, 
 knowledge_base_resolution, 
 knowledge_base_tags, 
 knowledge_base_date_time_reported, 
 knowledge_base_date_time_resolved) 
VALUES ("jacob",
 4,
 "SOFTWARE",
 "NO_LOAD",
 "Outlook will not open",
 "Outlook does not open. Upon attempting it gives me an error message about the Navigation Pane",
 "The fix was to start Outlook with the following command: Outlook /resetnavpane. This reset the nav pane. Outlook opens again. Setting resolved and placing in knowledge base.",
 "Outlook, software, navigation pane",
 "20161101080000",
 "20161102081543"
  );

INSERT INTO help_desk_maintenance (maintenance_message)
VALUES ("Maintenance is scheduled for the coming weekend.")
