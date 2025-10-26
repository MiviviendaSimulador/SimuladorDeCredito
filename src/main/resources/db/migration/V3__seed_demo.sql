INSERT INTO simulations (id, user_id, title, created_at, tcea, van, tir, monthly_payment, inputs_json, outputs_json)
VALUES ('11111111-1111-1111-1111-111111111111','user','Caso A', NOW(),
        0.1215, 8500, 0.1452, 1450.32, '{"PV":120000}', '{"BBP":27400}');
INSERT INTO payment_schedule (simulation_id, period, due_date, installment, interest, principal, balance, insurances)
VALUES ('11111111-1111-1111-1111-111111111111', 1, CURRENT_DATE + INTERVAL '30 days',
        1450.32, 1150.00, 300.32, 119699.68, 35.00);
