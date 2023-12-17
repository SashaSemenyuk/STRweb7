package com.example.servlets.connection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.locks.ReentrantLock;

public class ConnectionPool {
    private static final Logger logger = LogManager.getLogger(ConnectionPool.class);
    private static final String PROPERTY_PATH = "db";
    private static final int INITIAL_CAPACITY = 32;
    private ArrayBlockingQueue<Connection> freeConnections = new ArrayBlockingQueue<>(INITIAL_CAPACITY);
    private ArrayBlockingQueue<Connection> releaseConnections = new ArrayBlockingQueue<>(INITIAL_CAPACITY);
    private static ReentrantLock lock = new ReentrantLock();
    private volatile static ConnectionPool connectionPool;

    public static ConnectionPool getInstance() {
        try {
            lock.lock();
            if (connectionPool == null) {
                connectionPool = new ConnectionPool();
            }
        } catch (Exception e) {
            logger.error("Can not get Instance", e);
            throw new RuntimeException("Can not get Instance", e);
        } finally {
            lock.unlock();
        }
        return connectionPool;
    }

    private ConnectionPool() throws SQLException {
        try {
            lock.lock();
            if (connectionPool != null) {
                throw new UnsupportedOperationException();
            } else {
                DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver());
                init();
            }
        } finally {
            lock.unlock();
        }
    }

    private void init() {
        Properties properties = new Properties();
        ResourceBundle resource = ResourceBundle.getBundle(PROPERTY_PATH, Locale.getDefault());
        if (resource == null) {
            logger.error("Error while reading properties");
        } else {
            String connectionURL = resource.getString("db.url");
            String initialCapacityString = resource.getString("db.poolsize");
            String user = resource.getString("db.user");
            String pass = resource.getString("db.password");
            Integer initialCapacity = Integer.valueOf(initialCapacityString);
            for (int i = 0; i < initialCapacity; i++) {
                try {
                    Connection connection = DriverManager.getConnection(connectionURL, user, pass);
                    freeConnections.add(connection);
                } catch (SQLException e) {
                    logger.error("Pool can not initialize", e);
                    throw new RuntimeException("Pool can not initialize", e);
                }
            }
        }
    }

    public Connection getConnection() {
        try {
            Connection connection = freeConnections.take();
            releaseConnections.offer(connection);
            logger.info("Connection was taken, the are free connection " + freeConnections.size());
            return connection;
        } catch (InterruptedException e) {
            throw new RuntimeException("Can not get database", e);
        }
    }

    public void releaseConnection(Connection connection) {
        releaseConnections.remove(connection);
        freeConnections.offer(connection);
        logger.info("Connection was released, the are free connection " + freeConnections.size());
    }

    public void destroy() {
        for (int i = 0; i < freeConnections.size(); i++) {
            try {
                Connection connection = (Connection) freeConnections.take();
                connection.close();
            } catch (InterruptedException e) {
                logger.error("Connection close exception", e);
            } catch (SQLException e) {
                logger.error("database is not closed", e);
                throw new RuntimeException("database is not closed", e);
            }
        }
        try {
            Enumeration<Driver> drivers = DriverManager.getDrivers();
            while (drivers.hasMoreElements()) {
                java.sql.Driver driver = drivers.nextElement();
                DriverManager.deregisterDriver(driver);
            }
        } catch (SQLException e) {
            logger.error("Drivers were not deregistrated", e);
        }
    }
}
