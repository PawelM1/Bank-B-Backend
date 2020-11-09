CREATE TABLE ACCOUNT
(
    `ACCOUNT_ID`     BIGINT         NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `ACCOUNT_NUMBER` varchar(26)    NOT NULL,
    `BALANCE`        DECIMAL(10, 2) NOT NULL,
    `CLIENT_ID`      BIGINT         NOT NULL
);