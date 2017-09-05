package net.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
 
public class MySQL {
 
	public interface Runner {  
	    void run(Throwable throwable);
	}

	
    private ExecutorService executor;
    private Plugin plugin;
    private privateMySQL sql;

    public MySQL(Plugin plugin, Runner runner , String host, int port, String user, String password, String database) {
        try {
            sql = new privateMySQL(runner, host, port, user, password, database);
            executor = Executors.newCachedThreadPool();
            this.plugin = plugin;
        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void update(PreparedStatement statement) {
        executor.execute(() -> sql.queryUpdate(statement));
    }

    public void update(String statement) {
        executor.execute(() -> sql.queryUpdate(statement));
    }

    public void query(PreparedStatement statement, Consumer<ResultSet> consumer) {
        executor.execute(() -> {
            ResultSet result = sql.query(statement);
            Bukkit.getScheduler().runTask(plugin, () -> consumer.accept(result));
        });
    }

    public void query(String statement, Consumer<ResultSet> consumer) {
        executor.execute(() -> {
            ResultSet result = sql.query(statement);
            Bukkit.getScheduler().runTask(plugin, () -> consumer.accept(result));
        });
    }

    public PreparedStatement prepare(String query) {
        try {
            return sql.getConnection().prepareStatement(query);
        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public privateMySQL Connection() {
        return sql;
    }

    public static class privateMySQL {

        private String host, user, password, database;
        private int port;

        private Runner runner;
        private Connection conn;

        public privateMySQL(Runner runner, String host, int port, String user, String password, String database) throws Exception {
            this.host = host;
            this.port = port;
            this.user = user;
            this.password = password;
            this.database = database;

            this.runner = runner;
            this.openConnection(runner);
        }

        public Boolean isConnected() {
            return conn != null;
        }

        public void queryUpdate(String query) {
            checkConnection();
            try (PreparedStatement statement = conn.prepareStatement(query)) {
                queryUpdate(statement);
            } catch (Exception ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            }
        }

        public void queryUpdate(PreparedStatement statement) {
            checkConnection();
            try {
                statement.executeUpdate();
            } catch (Exception ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    statement.close();
                } catch (Exception ex) {
                    Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        public ResultSet query(String query) {
            checkConnection();
            try {
                return query(conn.prepareStatement(query));
            } catch (Exception ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            }
            return null;
        }

        public ResultSet query(PreparedStatement statement) {
            checkConnection();
            try {
                return statement.executeQuery();
            } catch (Exception ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            }
            return null;
        }

        public Connection getConnection() {
            return this.conn;
        }

        private void checkConnection() {
            try {
                if (this.conn == null || !this.conn.isValid(10) || this.conn.isClosed())
                    openConnection(runner);
            } catch (Exception ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            }
        }

        public Connection openConnection(Runner runner) {
            try {
                try {
                    Class.forName("com.mysql.jdbc.Driver");
                } catch (ClassNotFoundException ex) {
                    runner.run(ex);
                }
                return this.conn = DriverManager.getConnection(
                        "jdbc:mysql://" + this.host + ":" + this.port + "/" + this.database, this.user, this.password);
            } catch (SQLException ex) {
                runner.run(ex);
            }
            return this.conn = null;
        }

        public void closeConnection(Runner runner) {
            try {
                this.conn.close();
                this.conn = null;
            } catch(SQLException ex) {
                runner.run(ex);
            }
        }

    }
}