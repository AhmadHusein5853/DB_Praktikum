package de.unidue.inf.is.stores;

import java.io.Closeable;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import de.unidue.inf.is.utils.DBUtil;


public final class EinschreibenStore implements Closeable {

    private Connection connection;
    private boolean complete;
    private String schema = "dbp213";

    public EinschreibenStore() throws StoreException {
        try {
            connection = DBUtil.getExternalConnection();
            connection.setAutoCommit(false);
        } catch (SQLException e) {
            throw new StoreException(e);
        }
    }

    public Boolean authenticated(int kid, String user_einschreibeschluessel) {
        try {
            PreparedStatement pstm = connection.
                    prepareStatement("SELECT einschreibeschluessel,name FROM " + schema + ".kurs WHERE kid=?");

            pstm.setInt(1, kid);
            ResultSet rs = pstm.executeQuery();
            if (rs.next()) {
                String einschreibeschluessel = rs.getString("einschreibeschluessel");
                // if there is no password
                if (user_einschreibeschluessel.equals("") && einschreibeschluessel == null) {
                    return true;
                }
                // if there is an password
                if (einschreibeschluessel != null && einschreibeschluessel.equals(user_einschreibeschluessel)) {
                    return true;
                } else {
                    return false;
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }


    public Boolean hasPassword(int kid) throws StoreException {
        try {
            PreparedStatement pstm = connection
                    .prepareStatement("SELECT * FROM " + schema + ".Kurs WHERE kid=?");
            pstm.setInt(1, kid);
            ResultSet rs = pstm.executeQuery();
            if (rs.next()) {
                String einschreibeschluessel = rs.getString("einschreibeschluessel");
                if (einschreibeschluessel == null) {
                    return false;
                }

            }

        } catch (Exception e) {
            throw new StoreException(e);
        }
        return true;
    }


    public Boolean isEnrolled(int bnummer, int kid) throws StoreException {
        try {
            PreparedStatement pstm = connection
                    .prepareStatement("SELECT * FROM " + schema + ".einschreiben WHERE kid=? AND bnummer=?");
            pstm.setInt(1, kid);
            pstm.setInt(2, bnummer);

            ResultSet rs = pstm.executeQuery();
            if (rs.next()) {
                return true;
            }

        } catch (Exception e) {
            throw new StoreException(e);
        }
        return false;
    }

    public void einschreiben(int bnummer, int kid) throws StoreException {
        // TRANSAKTION ////////////////

        try {
            PreparedStatement pstm = connection
                    .prepareStatement("INSERT INTO " + schema + ".einschreiben(bnummer,kid) VALUES(?,?)");
            pstm.setInt(1, bnummer);
            pstm.setInt(2, kid);


            pstm.executeUpdate();

            // reduce the freiePlaetze for the course

            PreparedStatement pstm2 = connection.prepareStatement("UPDATE " + schema + ".kurs SET freiePlaetze=freiePlaetze -1 WHERE kid=? ");

            pstm2.setInt(1, kid);
            pstm2.executeUpdate();

            complete();

        } catch (Exception e) {
            throw new StoreException(e);
        }
    }


    public void complete() {
        complete = true;
    }


    @Override
    public void close() throws IOException {
        if (connection != null) {
            try {
                if (complete) {
                    connection.commit();
                } else {
                    connection.rollback();
                }
            } catch (SQLException e) {
                throw new StoreException(e);
            } finally {
                try {
                    connection.close();
                } catch (SQLException e) {
                    throw new StoreException(e);
                }
            }
        }
    }

}
