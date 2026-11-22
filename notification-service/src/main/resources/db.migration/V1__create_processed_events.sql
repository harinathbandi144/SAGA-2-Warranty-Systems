CREATE TABLE notification_schema.processed_events (
                                                      event_id VARCHAR(100) PRIMARY KEY,
                                                      status VARCHAR(20) NOT NULL,
                                                      created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                                      updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

                                                      CONSTRAINT chk_event_status
                                                          CHECK (status IN ('PROCESSING', 'SENT'))
);