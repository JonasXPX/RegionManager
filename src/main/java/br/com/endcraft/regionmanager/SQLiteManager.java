package br.com.endcraft.regionmanager;

import java.sql.*;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class SQLiteManager {

    private Connection connection = null;
    public static final String SQLITE_FILENAME = "data.sql";
    public String parentLocation = "";
    private static final String TABLES = "" +
            "CREATE TABLE IF NOT EXISTS blocked_items( " +
            "item_id BIGINT, " +
            "region varchar(255)," +
            "world varchar(255))";
    private static final String CREATE_TABLE_INDEX_UNIQUE = "CREATE UNIQUE INDEX IF NOT EXISTS blocked_items_index ON blocked_items(item_id, region, world)";

    public SQLiteManager(String parentLocation) {
        this.parentLocation = parentLocation;
        connection = createConnection();
    }

    private Connection createConnection() {
        try {
            Connection con = DriverManager.getConnection("jdbc:sqlite:" + parentLocation + "/" + SQLITE_FILENAME);
            try (PreparedStatement statement = con.prepareStatement(TABLES)) {
                statement.execute();
            }
            try (PreparedStatement statement = con.prepareStatement(CREATE_TABLE_INDEX_UNIQUE)) {
                statement.execute();
            }
            return con;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public Set<Long> getBlockedItemsByRegion(String regionName, String worldName) {
        Set<Long> blockedItems = new HashSet<>();
        try (PreparedStatement statement = getConnection().prepareStatement("SELECT item_id FROM blocked_items WHERE region = ? and world = ?")) {
            statement.setString(1, regionName);
            statement.setString(2, worldName);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    blockedItems.add(resultSet.getLong("item_id"));
                }
            }
            return blockedItems;
        } catch (Exception ex) {
            ErrorMessage.getInstance().addErrorMessage(ex.getMessage());
        }
        return Collections.emptySet();
    }

    public void setItemIdBlocked(Long itemId, String regionName, String worldName) {
        try (PreparedStatement statement = getConnection().prepareStatement("INSERT INTO blocked_items(item_id, region, world) VALUES(?, ?, ?)")) {
            statement.setLong(1, itemId);
            statement.setString(2, regionName);
            statement.setString(3, worldName);
            statement.executeUpdate();
        } catch (Exception ex) {
            ErrorMessage.getInstance().addErrorMessage(ex.getMessage());
        }
    }

    public void removeBlockedItemFromRegion(Long itemId, String regionName, String worldName) {
        try (PreparedStatement statement = getConnection().prepareStatement("DELETE FROM blocked_items WHERE region = ? AND world = ? AND item_id = ?")) {
            statement.setString(1, regionName);
            statement.setString(2, worldName);
            statement.setLong(3, itemId);
            if(statement.executeUpdate() == 0) {
                throw new SQLException("No item to delete");
            }
        } catch (SQLException ex) {
            ErrorMessage.getInstance().addErrorMessage(ex.getMessage());
        }
    }


    private void verifyConnection() throws SQLException {

        if (connection == null || connection.isClosed()) {
            connection = createConnection();
        }

    }

    private Connection getConnection() throws SQLException {
        verifyConnection();
        return connection;
    }
}
