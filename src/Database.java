import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {

	private static final String driver = "org.postgresql.Driver";
	private static final String url = "jdbc:postgresql:test";
	private static final String user = "postgres";
	private static final String password = "1735ranger";
	
	private Connection connection = null;
	private Statement statement = null;
	private String sql = "Insert into table_a (column_a, column_b) values (?,?)";
	private PreparedStatement preparedStatement = null;
	private static final int batch = 1000;
	
	public Database() {
	}
	
	public void write(int param, int num) {
		try {
			for(int i = 0; i < num; i++) {
				preparedStatement.setInt(1, param);
				preparedStatement.setString(2, String.valueOf(param));
				
				preparedStatement.addBatch();
			}
			
			int[] temp = preparedStatement.executeBatch();
			
			System.out.println(temp.length+"");
		} catch(SQLException e) {
			printException(e);
		}
	}
	
	public void clear() {
		String sql = "delete from table_a;";
		
		try {
			statement.executeUpdate(sql);
		} catch (SQLException e) {
			printException(e);
		}
	}
	
	public void init() {
		try {
			Class.forName(driver);
			
			connection = DriverManager.getConnection(url, user, password);
			statement = connection.createStatement();
			preparedStatement = connection.prepareStatement(sql);
		} catch(ClassNotFoundException e) {
			printException(e);
		} catch(SQLException e) {
			printException(e);
		}
	}
	
	public void release() {
		try {
			preparedStatement.close();
			statement.close();
			connection.close();
		} catch(SQLException e) {
			printException(e);
		}
	}
	
	public void printException(Exception e) {
		System.out.println("Exception : " + e.getMessage());
	}
	
}
