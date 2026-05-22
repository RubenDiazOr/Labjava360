package com.axity.dinosaurpark.persistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

import liquibase.Contexts;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;

public class DatabaseService {
    private final Connection connection;
   

    public DatabaseService(String dbPath) throws Exception{
        connection = DriverManager.getConnection("jdbc:h2:" + dbPath, "sa", "");
        runLiquibase();
    }

    private void runLiquibase() throws Exception {
        Database db = DatabaseFactory.getInstance()
            .findCorrectDatabaseImplementation(new JdbcConnection(connection));

        new Liquibase("db/changelog/db.changelog-master.xml", new ClassLoaderResourceAccessor(), db)
        .update(new Contexts());
    }

    // Mismos nombres que CsvWriter para no cambiar el código de las zonas
    public void appendRevenue(RevenueRecord r) throws SQLException {
        String sql = "INSERT INTO revenues (type, amount, tourist_id, zone, timestamp) VALUES (?,?,?,?,?)";
        try(PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setString(1, r.type());
            ps.setDouble(2, r.amount());
            ps.setLong(3, r.touristId());
            ps.setString(4, r.zone());
            ps.setTimestamp(5, Timestamp.valueOf(r.timestamp()));
            ps.executeUpdate();
        }
        // INSERT INTO revenues (type, amount, tourist_id, zone, timestamp) VALUES (?,?,?,?,?)
        // Usar PreparedStatement — NUNCA concatenar SQL con strings
    }
    public void appendExpense(ExpenseRecord e) throws SQLException {
        String sql = "INSERT INTO expenses (type, amount, description, timestamp) VALUES (?,?,?,?)";
        try(PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setString(1, e.type());
            ps.setDouble(2, e.amount());
            ps.setString(3, e.description());
            ps.setTimestamp(4, Timestamp.valueOf(e.timestamp()));
            ps.executeUpdate();
        }
    }
    public void appendEvent(EventRecord ev) throws SQLException {
        String sql = "INSERT INTO events (step, event_name, description, affected_entities, timestamp) VALUES (?,?,?,?,?)";
        try(PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setLong(1, ev.step());
            ps.setString(2, ev.eventName());
            ps.setString(3, ev.description());
            ps.setString(4, ev.affectedEntities());
            ps.setTimestamp(5, Timestamp.valueOf(ev.timestamp()));
            ps.executeUpdate();
        }
    }

}
