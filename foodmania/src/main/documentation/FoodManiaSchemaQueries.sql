CREATE TABLE `restaurant`
(
    id               BIGINT auto_increment PRIMARY KEY,
    name             VARCHAR(255) NOT NULL,
    rating           DOUBLE DEFAULT 0,
    max_capacity     INT NOT NULL,
    current_capacity INT DEFAULT 0,
    added_on         TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_on       TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP

);

CREATE TABLE `menu_item`
(
    id            BIGINT auto_increment PRIMARY KEY,
    name          VARCHAR(255) NOT NULL,
    price         DOUBLE NOT NULL,
    restaurant_id BIGINT NOT NULL,
    preparation_time int NOT NULL,
    added_on         TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_on       TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE `order_data`
(
    id          BIGINT auto_increment PRIMARY KEY,
    deliverable BOOLEAN DEFAULT true,
    added_on         TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_on       TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE `order_item`
(
    id        BIGINT auto_increment PRIMARY KEY,
    item_name VARCHAR(255) NOT NULL,
    quantity  INT NOT NULL,
    order_id  BIGINT,
    added_on         TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_on       TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE config
(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    `key` VARCHAR(255) NOT NULL,
    `value` VARCHAR(255),
    is_active BOOLEAN NOT NULL,
    added_on timestamp NULL DEFAULT CURRENT_TIMESTAMP,
    updated_on timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
