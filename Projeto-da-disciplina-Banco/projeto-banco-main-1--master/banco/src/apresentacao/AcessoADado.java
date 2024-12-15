package apresentacao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

public class AcessoADado {
  public AcessoADado() {
    // Construtor vazio para inicialização
  }

  /** 
   * Conecta ao banco de dados PostgreSQL.
   * 
   * @return um objeto Connection
   * @throws SQLException se ocorrer algum erro de conexão
  */

  public Connection connect() throws SQLException {
    String url = "jdbc:postgresql://localhost/banco"; // Atualize para seu banco
    Properties props = new Properties();
    props.setProperty("user", "postgres");
    props.setProperty("password", "tone123TONE");
    props.setProperty("ssl", "false");

    return DriverManager.getConnection(url, props);
  }

  /**
   * Cadastra uma nova conta no banco de dados.
   * 
   * @param numero número de conta
   * @param saldo saldo inicial da conta
   * @return mensagem de sucesso ou erro
   */
  
   public String cadastrarConta(String numero, float saldo) {
    String SQL = "INSERT INTO public.conta(numero, saldo) values (?, ?)";
    String mensagem;

    try(Connection conn = connect();
        PreparedStatement pstmt = conn.prepareStatement(SQL)) {
          pstmt.setString(1, numero); // Corrigido: tipo String para o número
          pstmt.setFloat(2, saldo); // Corrigido: tipo Float para o saldo

          int affectedRows = pstmt.executeUpdate();
          if(affectedRows > 0) {
            mensagem = "Cadastro de conta " + numero + " realizado com sucesso.";
          } else {
            mensagem = "Erro ao cadastrar conta.";
          }
    } catch(SQLException ex) {
      mensagem = "Erro de SQL: " + ex.getMessage();
    }

    return mensagem;
   }

   /**
     * Altera o saldo de uma conta.
     *
     * @param numero número da conta
     * @param saldo  novo saldo
     * @return mensagem de sucesso ou erro
    */

    public String alterarConta(String numero, float saldo) {
      String SQL = "UPDATE public.conta SET saldo = ? WHERE numero = ?";
      String mensagem;

      try (Connection conn = connect();
          PreparedStatement pstmt = conn.prepareStatement(SQL)) {

          pstmt.setFloat(1, saldo);  // Corrigido: tipo Float para o saldo
          pstmt.setString(2, numero);

          int affectedRows = pstmt.executeUpdate();
          if (affectedRows > 0) {
              mensagem = "Saldo da conta " + numero + " alterado com sucesso.";
          } else {
              mensagem = "Conta não encontrada.";
          }
      } catch (SQLException ex) {
          mensagem = "Erro de SQL: " + ex.getMessage();
      }

      return mensagem;
    }

   /**
    * Lista todas as contas de banco de dados.
    */
    public void listarContas() {
      String SQL = "SELECT numero, saldo FROM public.conta";

      try(Connection conn = connect();
          PreparedStatement pstmt = conn.prepareStatement(SQL);
          ResultSet rs = pstmt.executeQuery()) {
            while(rs.next()) {
              String numero = rs.getString("numero");
              float saldo = rs.getFloat("saldo");
              System.out.println("Conta: " + numero + " | Saldo: " + saldo);
            }
      } catch(SQLException ex) {
        System.out.println("Erro de SQL: " + ex.getMessage());
      }
    }

    /**
     * Exclui uma conta do banco de dados.
     * 
     * @param numero número da conta
     * @return mensagem de sucesso ou erro
     */

    public String excluirConta(String numero) {
      String SQL = "DELETE FROM public.conta WHERE numero = ?";
      String mensagem;

      try(Connection conn = connect();
        PreparedStatement pstmt = conn.prepareStatement(SQL)) {
          pstmt.setString(1, numero);

          int affectedRows = pstmt.executeUpdate();
          if(affectedRows > 0) {
            mensagem = "Conta " + numero + " excluída com sucesso.";
          } else {
            mensagem = "Conta não encontrada.";
          }
      } catch(SQLException ex) {
        mensagem = "Erro de SQL: " + ex.getMessage();
      }

      return mensagem;
    }

    public static void main(String[] args) {
      AcessoADado acesso = new AcessoADado();

      System.out.println(acesso.cadastrarConta("12345", 1000.0f));
      System.out.println(acesso.alterarConta("12345", 1500.0f));
      acesso.listarContas();
      // System.out.println(acesso.excluirConta("12345"));

      try (Connection conn = acesso.connect()) {
          if (conn != null) {
              System.out.println("Conexão bem-sucedida com o banco de dados!");
          } else {
              System.out.println("Falha ao conectar com o banco de dados.");
          }
      } catch (SQLException e) {
          e.printStackTrace();
      }
    }
}