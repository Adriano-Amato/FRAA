package classes.model.dao;

import classes.model.DriverManagerConnectionPool;
import classes.model.bean.entity.OperazioneBean;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.LinkedList;

/**
 * Model per collegare la tabella "Operazione" al backend.
 */
public class OperazioneModel implements Model<OperazioneBean> {

  private static final String nomeTabella = "operazione";

  /**
   * Prelevamento singola operazione.
   *
   * @param code chiave primaria dell'operazione
   * @return Operazione avente quell'id
   * @throws SQLException per problemi di esecuzione della query
   */
  @Override
  public OperazioneBean doRetrieveByKey(String code) throws SQLException {
    Connection con = null;
    PreparedStatement ps = null;

    OperazioneBean tmp = new OperazioneBean();

    String selectSql = "SELECT * FROM " + nomeTabella + " WHERE id = ?";

    try {
      con = DriverManagerConnectionPool.getConnection();
      ps = con.prepareStatement(selectSql);
      ps.setInt(1, Integer.parseInt(code));

      ResultSet rs = ps.executeQuery();

      while (rs.next()) {
        tmp.setId(rs.getInt("id"));
        tmp.setTipoOperazione(rs.getString("tipoOperazione"));
        tmp.setDescrizione(rs.getString("descrizione"));
      }
    } catch (SQLException e) {
      e.printStackTrace();
    } finally {
      try {
        if (ps != null) {
          ps.close();
        }
      } finally {
        DriverManagerConnectionPool.releaseConnection(con);
      }
    }
    return tmp;
  }

  /**
   * Prelevamento di tutte le operazioni presenti nel DB.
   *
   * @param order Ordine per la visualizzazione della collezione
   * @return Collezione di tutte le operazioni
   * @throws SQLException per problemi di esecuzione della query
   */
  @Override
  public Collection<OperazioneBean> doRetrieveAll(String order) throws SQLException {
    Connection con = null;
    PreparedStatement ps = null;

    Collection<OperazioneBean> result = new LinkedList<OperazioneBean>();

    String selectSql = "SELECT * FROM " + nomeTabella;

    if (order != null && !order.equals("")) {
      selectSql += " ORDER BY " + order;
    }

    try {

      con = DriverManagerConnectionPool.getConnection();
      ps = con.prepareStatement(selectSql);

      ResultSet rs = ps.executeQuery();
      OperazioneBean tmp = new OperazioneBean();

      while (rs.next()) {

        tmp.setId(rs.getInt("id"));
        tmp.setTipoOperazione(rs.getString("tipoOperazione"));
        tmp.setDescrizione(rs.getString("descrizione"));
        result.add(tmp);
      }

    } catch (SQLException e) {
      e.printStackTrace();
    } finally {
      try {
        if (ps != null) {
          ps.close();
        }
      } finally {
        DriverManagerConnectionPool.releaseConnection(con);
      }
    }

    return result;
  }

  /**
   * Inserimento di una nuova operazione nel DB.
   *
   * @param param Nuova operazione
   * @throws SQLException per problemi di esecuzione della query
   */
  @Override
  public void doSave(OperazioneBean param) throws SQLException {
    Connection connection = null;
    PreparedStatement preparedStatement = null;

    String insertSql = "INSERT INTO " + nomeTabella + " VALUES (?, ?)";

    try {
      connection = DriverManagerConnectionPool.getConnection();
      preparedStatement = connection.prepareStatement(insertSql);
      preparedStatement.setString(1, param.getTipoOperazione());
      preparedStatement.setString(2, param.getDescrizione());

      preparedStatement.executeUpdate();
    } finally {
      try {
        if (preparedStatement != null) {
          preparedStatement.close();
        }
      } finally {
        DriverManagerConnectionPool.releaseConnection(connection);
      }
    }
  }

  /**
   * Aggiornamento di un operazione sul DB.
   *
   * @param param Operazione da aggiornare
   * @throws SQLException per problemi di esecuzione della query
   */
  @Override
  public void doUpdate(OperazioneBean param) throws SQLException {
    Connection connection = null;
    PreparedStatement preparedStatement = null;

    String deleteSql =
        "UPDATE "
            + nomeTabella
            + " SET tipoOperazione = ?, descrizione = ? WHERE id = ?";

    try {
      connection = DriverManagerConnectionPool.getConnection();
      preparedStatement = connection.prepareStatement(deleteSql);
      preparedStatement.setString(1, param.getTipoOperazione());
      preparedStatement.setString(2, param.getDescrizione());
      preparedStatement.setInt(3, param.getId());

      preparedStatement.executeUpdate();

    } finally {
      try {
        if (preparedStatement != null) {
          preparedStatement.close();
        }
      } finally {
        DriverManagerConnectionPool.releaseConnection(connection);
      }
    }
    return;
  }

  /**
   * Rimozione di un operazione presente sul DB.
   *
   * @param param Operazione da rimuovere
   * @throws SQLException per problemi di esecuzione della query
   */
  @Override
  public void doDelete(OperazioneBean param) throws SQLException {
    Connection connection = null;
    PreparedStatement preparedStatement = null;

    String deleteSql = "DELETE FROM " + nomeTabella + " WHERE id = ?";

    try {
      connection = DriverManagerConnectionPool.getConnection();
      preparedStatement = connection.prepareStatement(deleteSql);
      preparedStatement.setInt(1, param.getId());

      preparedStatement.executeUpdate();

    } finally {
      try {
        if (preparedStatement != null) {
          preparedStatement.close();
        }
      } finally {
        DriverManagerConnectionPool.releaseConnection(connection);
      }
    }
    return;
  }
}
