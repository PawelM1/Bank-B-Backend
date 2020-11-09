CREATE TABLE TRANSACTION
(
    `transaction_id`                       BIGINT         NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `account_id`                           BIGINT         NOT NULL,
    `transfer_type`                        varchar(20)    NOT NULL,
    `recipient_or_sender_address_and_name` varchar(250)   NOT NULL,
    `recipient_or_sender_number`           varchar(26),
    `title`                                varchar(100)   NOT NULL,
    `transfer_amount`                      DECIMAL(10, 2) NOT NULL,
    `transaction_type`                     varchar(20)    NOT NULL,
    `transfer_status`                      varchar(20)    NOT NULL,
    `transfer_date`                        TIMESTAMP      NOT NULL
);
