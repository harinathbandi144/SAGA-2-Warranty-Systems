
CREATE TABLE dealer_schema.dealers (

                                       dealer_id VARCHAR(20) PRIMARY KEY,

                                       dealer_name VARCHAR(150) NOT NULL,

                                       email VARCHAR(150) NOT NULL UNIQUE,

                                       phone VARCHAR(20) NOT NULL,

                                       city VARCHAR(100) NOT NULL,

                                       status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',

                                       created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

                                       updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

                                       CONSTRAINT chk_dealer_status
                                           CHECK (status IN ('ACTIVE', 'INACTIVE'))
);
