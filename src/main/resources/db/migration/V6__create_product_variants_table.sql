CREATE TABLE product_variants (
                                  id BIGSERIAL PRIMARY KEY,
                                  product_id BIGINT NOT NULL,
                                  label VARCHAR(255) NOT NULL,
                                  price DECIMAL(10, 2),
                                  stock_quantity INT NOT NULL DEFAULT 0,
                                  CONSTRAINT fk_product_variants_product FOREIGN KEY (product_id) REFERENCES product(id) ON DELETE CASCADE
);

CREATE INDEX idx_product_variants_product_id ON product_variants(product_id);