import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DS {
	Properties p;

	public DS() {
		p = new Properties();
		File file = new File("./config.prop");
		try {
			p.load(new FileInputStream(file));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public Connection getConnection() {
		String url = p.getProperty("url");
		String nom = p.getProperty("nom");

		String mdp = p.getProperty("mdp");
		System.out.println(nom);
		try {
			return DriverManager.getConnection(url, nom, mdp);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}
}
