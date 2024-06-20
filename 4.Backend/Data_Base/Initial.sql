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

INSERT INTO public.ec_product (ecp_name,ecp_description,ecp_image_path,ecp_price,ecp_available) VALUES('Zapatos Adidas','Zapatos de moda color negro, talla 42','https://test-images-ecommerce.s3.amazonaws.com/1.png',230999.00,100);
INSERT INTO public.ec_product (ecp_name,ecp_description,ecp_image_path,ecp_price,ecp_available) VALUES('Zapatos Nike','Zapatos deportivo color blanco, talla 38','https://test-images-ecommerce.s3.amazonaws.com/2.png',255000.00,100);