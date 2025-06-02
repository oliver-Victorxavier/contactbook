CREATE TABLE IF NOT EXISTS contact_book_tb (
                                               id BIGSERIAL PRIMARY KEY,
                                               name VARCHAR(255),
    phone VARCHAR(255),
    cep VARCHAR(255),
    logradouro VARCHAR(255),
    numero INTEGER,
    complemento VARCHAR(255),
    bairro VARCHAR(255),
    cidade VARCHAR(255),
    estado VARCHAR(255)
    );