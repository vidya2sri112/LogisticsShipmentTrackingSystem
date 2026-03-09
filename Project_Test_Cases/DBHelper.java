package utils;

import java.sql.*;

/**
 * DBHelper.java
 * Utility class for MySQL database validation queries.
 * Used during DB testing phase to confirm shipment records
 * and status transitions are stored correctly.
 */
public class DBHelper {

    private static final String DB_URL      = "jdbc:mysql://localhost:3306/logistics_db";
    private static final String DB_USER     = "root";
    private static final String DB_PASSWORD = "root@1234";

    private Connection connection;

    /**
     * Open a new DB connection
     */
    public void openConnection() throws SQLException {
        connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        System.out.println("DB connection opened successfully.");
    }

    /**
     * Close the DB connection
     */
    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("DB connection closed.");
            }
        } catch (SQLException e) {
            System.err.println("Error closing DB connection: " + e.getMessage());
        }
    }

    /**
     * Verify a shipment record exists in the database by tracking ID.
     *
     * @param trackingId the shipment tracking ID
     * @return true if record exists, false otherwise
     */
    public boolean verifyShipmentExists(String trackingId) throws SQLException {
        String query = "SELECT COUNT(*) FROM shipments WHERE tracking_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, trackingId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int count = rs.getInt(1);
                System.out.println("Shipment records found for " + trackingId + ": " + count);
                return count > 0;
            }
        }
        return false;
    }

    /**
     * Get the current status of a shipment from the database.
     *
     * @param trackingId the shipment tracking ID
     * @return current shipment status string
     */
    public String getShipmentStatus(String trackingId) throws SQLException {
        String query = "SELECT shipment_status FROM shipments WHERE tracking_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, trackingId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String status = rs.getString("shipment_status");
                System.out.println("DB shipment status for " + trackingId + ": " + status);
                return status;
            }
        }
        return null;
    }

    /**
     * Verify delivery timestamp is NOT NULL for a delivered shipment.
     *
     * @param trackingId the shipment tracking ID
     * @return true if delivery timestamp is stored, false otherwise
     */
    public boolean verifyDeliveryTimestampExists(String trackingId) throws SQLException {
        String query = "SELECT delivery_timestamp FROM shipments WHERE tracking_id = ? AND shipment_status = 'Delivered'";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, trackingId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Timestamp ts = rs.getTimestamp("delivery_timestamp");
                boolean exists = (ts != null);
                System.out.println("Delivery timestamp for " + trackingId + ": " + ts);
                return exists;
            }
        }
        return false;
    }

    /**
     * Count status history transitions recorded for a shipment.
     *
     * @param trackingId the shipment tracking ID
     * @return number of status transitions
     */
    public int countStatusTransitions(String trackingId) throws SQLException {
        String query = "SELECT COUNT(*) FROM shipment_status_history WHERE tracking_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, trackingId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int count = rs.getInt(1);
                System.out.println("Status transitions for " + trackingId + ": " + count);
                return count;
            }
        }
        return 0;
    }

    /**
     * Check if there are any duplicate shipments with the same reference number.
     *
     * @param referenceNumber the shipment reference number
     * @return true if duplicates found, false otherwise
     */
    public boolean hasDuplicateShipments(String referenceNumber) throws SQLException {
        String query = "SELECT COUNT(*) FROM shipments WHERE reference_number = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, referenceNumber);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int count = rs.getInt(1);
                System.out.println("Shipments with reference " + referenceNumber + ": " + count);
                return count > 1;
            }
        }
        return false;
    }

    /**
     * Verify the delivery address stored in DB matches expected values.
     *
     * @param trackingId  the shipment tracking ID
     * @param expectedCity expected city name
     * @param expectedPin  expected pin code
     * @return true if values match, false otherwise
     */
    public boolean verifyDeliveryAddress(String trackingId, String expectedCity, String expectedPin) throws SQLException {
        String query = "SELECT da.city, da.pin_code " +
                       "FROM delivery_addresses da " +
                       "JOIN shipments s ON s.shipment_id = da.shipment_id " +
                       "WHERE s.tracking_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, trackingId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String city = rs.getString("city");
                String pin  = rs.getString("pin_code");
                System.out.println("DB address — City: " + city + ", Pin: " + pin);
                return expectedCity.equalsIgnoreCase(city) && expectedPin.equals(pin);
            }
        }
        return false;
    }
}
