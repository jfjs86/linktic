CREATE TABLE public.ec_order
(
 eco_id serial  PRIMARY KEY NOT NULL,
 eco_total_price numeric(18, 2) NOT NULL,
 eco_active boolean NOT NULL
);

CREATE TABLE public.ec_product
(
 ecp_id serial  PRIMARY KEY NOT NULL,
 ecp_name varchar(100) NOT NULL,
 ecp_description text NOT NULL,
 ecp_image_path varchar(500) NOT NULL,
 ecp_price numeric(18, 2) NOT NULL,
 ecp_available SMALLINT
);


CREATE TABLE ec_order_product (
    order_id INT NOT NULL,
    product_id INT NOT NULL,
    product_quantity INT,
    product_total numeric(18, 2),
    PRIMARY KEY (order_id, product_id),
    FOREIGN KEY (order_id) REFERENCES ec_order (eco_id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES ec_product (ecp_id) ON DELETE CASCADE
);