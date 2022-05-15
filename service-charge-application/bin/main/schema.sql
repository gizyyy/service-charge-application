CREATE TABLE IF NOT EXISTS charge_history(
  transaction_id BIGINT PRIMARY KEY AUTO_INCREMENT,
  vehicle_id BIGINT,
  start_time TIMESTAMP,
  end_time TIMESTAMP,
  cost DECIMAL
  );
  
 
CREATE INDEX IF NOT EXISTS charge_history_idx_1 ON charge_history(vehicle_id);

  