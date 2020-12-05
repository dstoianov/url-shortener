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


-- DROP TABLE IF EXISTS "locations";
-- CREATE TABLE "locations"
-- (
--     "id"      LONG         NOT NULL AUTO_INCREMENT,
--     "city"    varchar(100) NOT NULL,
--     "country" varchar(100),
--     PRIMARY KEY ("id")
-- );
--
-- DROP TABLE IF EXISTS "companies";
-- CREATE TABLE "companies"
-- (
--     "id"         LONG         NOT NULL AUTO_INCREMENT,
--     "name"       varchar(255) NOT NULL,
--     "url"        varchar(255),
--     "active"     bool         NULL        DEFAULT true,
--     "created_on" timestamp with time zone DEFAULT now() NOT NULL,
--     PRIMARY KEY ("id")
-- );
--
-- DROP TABLE IF EXISTS "tags";
-- CREATE TABLE "tags"
-- (
--     "id"         LONG                                   NOT NULL AUTO_INCREMENT,
--     "tag_name"   varchar(255)                           NOT NULL,
--     "created_on" timestamp with time zone DEFAULT now() NOT NULL,
--     PRIMARY KEY ("id")
-- );
--
-- -- test table
--
-- CREATE TABLE touristinfo
-- (
--     TOURISTINFO_ID LONG NOT NULL AUTO_INCREMENT PRIMARY KEY,
--     NAME           VARCHAR(25) NOT NULL,
--     NATIONALITY    VARCHAR(15) NOT NULL
-- );
--
-- CREATE TABLE PLANETICKETS
-- (
--     DESTINATION    VARCHAR(10)   NOT NULL,
--     TICKETPRICE    NUMERIC(8, 2) NOT NULL,
--     TOURISTINFO_ID LONG,
--     foreign key (TOURISTINFO_ID) references touristinfo (TOURISTINFO_ID)
-- );
--
--
-- DROP TABLE IF EXISTS "locations2";
--
-- CREATE TABLE "locations2"
-- (
--     "id"      SERIAL NOT NULL,
--     "city"    varchar(100) NOT NULL,
--     "country" varchar(100),
--     PRIMARY KEY ("id")
-- );
