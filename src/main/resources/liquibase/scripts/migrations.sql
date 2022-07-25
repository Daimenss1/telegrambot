--liquibase formatted sql

--changeset daimenss:1
create table notification
(
    id                      serial NOT NULL PRIMARY KEY,
    chat_id                 bigint NOT NULL,
    notification_date       timestamp NOT NULL,
    notification_message    varchar(255) NOT NULL,
    status                  varchar(255) NOT NULL DEFAULT 'SCHEDULED'
);


