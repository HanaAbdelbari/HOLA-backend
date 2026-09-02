CREATE TABLE product_variant (
                                 id BIGSERIAL PRIMARY KEY,
                                 product_id BIGINT NOT NULL,
                                 label VARCHAR(100) NOT NULL,
                                 price NUMERIC(10, 2),
                                 stock_quantity INT NOT NULL DEFAULT 0,
                                 CONSTRAINT fk_product_variant_product FOREIGN KEY (product_id) REFERENCES product (id) ON DELETE CASCADE
);

CREATE INDEX idx_product_variant_product_id ON product_variant(product_id);