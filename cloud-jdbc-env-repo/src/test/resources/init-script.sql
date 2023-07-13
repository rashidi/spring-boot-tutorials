CREATE TABLE `PROPERTIES` (
                              `KEY` VARCHAR(128),
                              `VALUE` VARCHAR(128),
                              `APPLICATION` VARCHAR(128),
                              `PROFILE` VARCHAR(128),
                              `LABEL` VARCHAR(128),
                              PRIMARY KEY (`KEY`, `APPLICATION`, `LABEL`)
);

INSERT INTO PROPERTIES (`APPLICATION`, `PROFILE`, `LABEL`, `KEY`, `VALUE`) VALUES ('demo', 'default', 'master', 'app.greet.name', 'Demo');
