CREATE TABLE IF NOT EXISTS payment_schedule (
  simulation_id UUID NOT NULL,
  period INT NOT NULL,
  due_date DATE,
  installment NUMERIC(18,8),
  interest NUMERIC(18,8),
  principal NUMERIC(18,8),
  balance NUMERIC(18,8),
  insurances NUMERIC(18,8),
  PRIMARY KEY (simulation_id, period),
  CONSTRAINT fk_payment_sim
    FOREIGN KEY (simulation_id) REFERENCES simulations(id) ON DELETE CASCADE
);
CREATE INDEX IF NOT EXISTS idx_payment_sim ON payment_schedule(simulation_id);
