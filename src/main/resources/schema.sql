DROP TABLE IF EXISTS url_shortener;
CREATE TABLE url_shortener
(
    id                 LONG                                   NOT NULL AUTO_INCREMENT,
    long_url           VARCHAR(2000)                          NOT NULL,
    shorten_key        VARCHAR(32),
    count              INTEGER                  DEFAULT 0,
    creation_date_time timestamp with time zone DEFAULT now() NOT NULL,
    last_modified_date timestamp with time zone,
--     created timestamp with time zone DEFAULT now() NOT NULL,
--     updated timestamp with time zone,
    PRIMARY KEY (id)
);



