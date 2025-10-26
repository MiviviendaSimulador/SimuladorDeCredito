CREATE TABLE IF NOT EXISTS simulations (
  id UUID PRIMARY KEY,
  user_id TEXT NOT NULL,
  title TEXT,
  created_at TIMESTAMPTZ NOT NULL,
  tcea NUMERIC(18,8),
  van NUMERIC(18,8),
  tir NUMERIC(18,8),
  monthly_payment NUMERIC(18,8),
  inputs_json JSONB,
  outputs_json JSONB
);
CREATE INDEX IF NOT EXISTS idx_simulations_user_created
  ON simulations(user_id, created_at DESC);
