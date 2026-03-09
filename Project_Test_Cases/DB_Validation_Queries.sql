-- =====================================================================
-- DB_Validation_Queries.sql
-- Logistics Shipment Tracking System - Database Validation Queries
-- Tool: MySQL
-- =====================================================================

-- -------------------------------------------------------
-- 1. VERIFY SHIPMENT RECORD EXISTS AFTER CREATION
-- -------------------------------------------------------
SELECT 
    shipment_id,
    tracking_id,
    sender_name,
    receiver_name,
    shipment_status,
    created_at
FROM shipments
WHERE tracking_id = 'TRK001234'
ORDER BY created_at DESC
LIMIT 1;

-- Expected: 1 row returned with status = 'Created'


-- -------------------------------------------------------
-- 2. VERIFY SHIPMENT STATUS = 'CREATED' AFTER SUBMISSION
-- -------------------------------------------------------
SELECT 
    tracking_id,
    shipment_status
FROM shipments
WHERE tracking_id = 'TRK001234'
  AND shipment_status = 'Created';

-- Expected: 1 row returned


-- -------------------------------------------------------
-- 3. VERIFY STATUS TRANSITIONS ARE STORED CORRECTLY
--    Lifecycle: Created → Picked Up → In Transit → Out for Delivery → Delivered
-- -------------------------------------------------------
SELECT 
    tracking_id,
    old_status,
    new_status,
    changed_at,
    changed_by
FROM shipment_status_history
WHERE tracking_id = 'TRK001234'
ORDER BY changed_at ASC;

-- Expected rows in this order:
-- Created → Picked Up
-- Picked Up → In Transit
-- In Transit → Out for Delivery
-- Out for Delivery → Delivered


-- -------------------------------------------------------
-- 4. VERIFY DELIVERY TIMESTAMP IS STORED AFTER DELIVERY
-- -------------------------------------------------------
SELECT 
    tracking_id,
    shipment_status,
    delivery_timestamp,
    delivered_to
FROM shipments
WHERE tracking_id = 'TRK001234'
  AND shipment_status = 'Delivered';

-- Expected: delivery_timestamp is NOT NULL and matches delivery time


-- -------------------------------------------------------
-- 5. VERIFY NO DUPLICATE SHIPMENTS EXIST
-- -------------------------------------------------------
SELECT 
    reference_number,
    COUNT(*) AS occurrence_count
FROM shipments
GROUP BY reference_number
HAVING COUNT(*) > 1;

-- Expected: 0 rows (no duplicates)


-- -------------------------------------------------------
-- 6. VERIFY SHIPMENT WEIGHT IS STORED CORRECTLY
-- -------------------------------------------------------
SELECT 
    tracking_id,
    shipment_weight_kg,
    shipment_type
FROM shipments
WHERE tracking_id = 'TRK001234';

-- Expected: weight = 2.5, type = 'Standard'


-- -------------------------------------------------------
-- 7. VERIFY DELIVERY ADDRESS IS STORED IN DB
-- -------------------------------------------------------
SELECT 
    s.tracking_id,
    da.recipient_name,
    da.street_address,
    da.city,
    da.state,
    da.pin_code,
    da.contact_number
FROM shipments s
JOIN delivery_addresses da ON s.shipment_id = da.shipment_id
WHERE s.tracking_id = 'TRK001234';

-- Expected: Address fields match what was entered during checkout


-- -------------------------------------------------------
-- 8. VERIFY INVALID TRACKING ID RETURNS NO RECORDS
-- -------------------------------------------------------
SELECT COUNT(*) AS record_count
FROM shipments
WHERE tracking_id = 'INVALID_XYZ';

-- Expected: record_count = 0


-- -------------------------------------------------------
-- 9. CHECK ALL SHIPMENTS IN 'IN TRANSIT' STATUS
-- -------------------------------------------------------
SELECT 
    shipment_id,
    tracking_id,
    sender_name,
    receiver_name,
    shipment_status,
    last_updated
FROM shipments
WHERE shipment_status = 'In Transit'
ORDER BY last_updated DESC;


-- -------------------------------------------------------
-- 10. VERIFY USER LOGIN AUDIT LOGS (FAILED LOGINS)
-- -------------------------------------------------------
SELECT 
    user_email,
    login_status,
    attempted_at,
    ip_address
FROM login_audit_logs
WHERE login_status = 'FAILED'
  AND attempted_at >= NOW() - INTERVAL 1 DAY
ORDER BY attempted_at DESC;

-- Expected: Failed login attempts are properly logged


-- -------------------------------------------------------
-- 11. FULL LIFECYCLE VALIDATION FOR A SPECIFIC SHIPMENT
-- -------------------------------------------------------
SELECT 
    sh.tracking_id,
    sh.shipment_status AS current_status,
    sh.created_at,
    sh.delivery_timestamp,
    TIMESTAMPDIFF(HOUR, sh.created_at, sh.delivery_timestamp) AS delivery_time_hours,
    hist.old_status,
    hist.new_status,
    hist.changed_at
FROM shipments sh
LEFT JOIN shipment_status_history hist ON sh.shipment_id = hist.shipment_id
WHERE sh.tracking_id = 'TRK001234'
ORDER BY hist.changed_at ASC;


-- -------------------------------------------------------
-- 12. VERIFY TOTAL SHIPMENTS CREATED TODAY
-- -------------------------------------------------------
SELECT 
    COUNT(*) AS total_shipments_today
FROM shipments
WHERE DATE(created_at) = CURDATE();


-- -------------------------------------------------------
-- 13. CLEANUP - DELETE TEST SHIPMENT DATA (USE WITH CAUTION)
-- -------------------------------------------------------
-- DELETE FROM shipment_status_history WHERE tracking_id IN ('TESTTRK001','TESTTRK002');
-- DELETE FROM delivery_addresses WHERE shipment_id IN (SELECT shipment_id FROM shipments WHERE tracking_id IN ('TESTTRK001','TESTTRK002'));
-- DELETE FROM shipments WHERE tracking_id IN ('TESTTRK001','TESTTRK002');
