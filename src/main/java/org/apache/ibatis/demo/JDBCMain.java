package org.apache.ibatis.demo;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * <p>文件名称：JDBCMain. </p>
 * <p>描述：JDBC demo入口</p>
 * <p>创建时间: 2024/06/04 </p>
 *
 * @author 徐林
 * @since v1.0
 */
public class JDBCMain {

  private static final String JDBC_URL = "jdbc:mysql://localhost:3306/employees";
  private static final String JDBC_USER = "root";
  private static final String JDBC_PASSWORD = "root";
  public static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";

  public static void main(String[] args) {
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;

    try {
      Class.forName(JDBC_DRIVER);

      connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
      preparedStatement = connection.prepareStatement(
        "SELECT emp_no, birth_date, first_name, last_name, gender, hire_date FROM employees WHERE first_name like ? LIMIT ?");

      preparedStatement.setString(1, "%%a%%");
      preparedStatement.setInt(2, 10);

      resultSet = preparedStatement.executeQuery();
      while (resultSet.next()) {
        int empNo = resultSet.getInt("emp_no");
        Date birthDate = resultSet.getDate("birth_date");
        String firstName = resultSet.getString("first_name");
        String lastName = resultSet.getString("last_name");
        String gender = resultSet.getString("gender");
        Date hireDate = resultSet.getDate("hire_date");

        System.out.printf(
          "empNo = %d, birthDate = %s, firstName = %s, lastName = %s, gender = %s, hireDate = %s%n",
          empNo, birthDate, firstName, lastName, gender, hireDate);
      }
    } catch (ClassNotFoundException | SQLException e) {
      throw new RuntimeException(e);
    } finally {
      try {
        if (resultSet != null) {
          resultSet.close();
        }
      } catch (SQLException e1) {
        System.err.println("关闭resultSet异常" + e1);
      } finally {
        try {
          if (preparedStatement != null) {
            preparedStatement.close();
          }
        } catch (SQLException e2) {
          System.err.println("关闭preparedStatement异常" + e2);
        } finally {
          try {
            if (connection != null) {
              connection.close();
            }
          } catch (SQLException e3) {
            System.err.println("关闭connection异常" + e3);
          }
        }
      }
    }
  }
}
