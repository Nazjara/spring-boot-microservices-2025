CREATE TABLE IF NOT EXISTS loans (
    loan_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    mobile_number VARCHAR(255),
    loan_number VARCHAR(255),
    loan_type VARCHAR(255),
    total_loan INT,
    amount_paid INT,
    outstanding_amount INT,
    created_at DATETIME,
    created_by VARCHAR(255),
    updated_at DATETIME,
    updated_by VARCHAR(255)
);