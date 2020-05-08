package si.src.naloga.driver;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import si.src.naloga.kontakt.Kontakt;

public class Driver {
	
	private static Connection con;
	private static boolean hasData = false;
	
	public List<Kontakt> getAllContacts() throws SQLException {

		getConnection();

		
		List<Kontakt> seznamKontaktov = new ArrayList<>();
		Statement statement = con.createStatement();
		ResultSet res = statement.executeQuery("SELECT id, ime, priimek, naslov, elektronskaPosta, "
												+ "telefon, mobilniTelefon, opomba FROM kontakti");	
		while(res.next()) {
    		//System.out.println(rs.getString("id"));
			Kontakt kontakt = new Kontakt(res.getInt("id"), res.getString("ime"), res.getString("priimek"), res.getString("naslov"),
										  res.getString("elektronskaPosta"), res.getString("telefon"), res.getString("mobilniTelefon"), res.getString("opomba"));
			seznamKontaktov.add(kontakt);
    	}
		con.close();
		return seznamKontaktov;
	}

	
	
	public Kontakt addKontakt(String ime, String priimek, String naslov, String elektronskaPosta, String telefon, String mobilniTelefon, String opomba) throws SQLException {

		getConnection();


		//inserting sample data
		PreparedStatement prep = con.prepareStatement("INSERT INTO kontakti(ime, priimek, naslov, elektronskaPosta,telefon, mobilniTelefon, opomba) values(?,?,?,?,?,?,?);");
		prep.setString(1, ime);
		prep.setString(2, priimek);
		prep.setString(3, naslov);
		prep.setString(4, elektronskaPosta);
		prep.setString(5, telefon);
		prep.setString(6, mobilniTelefon);
		prep.setString(7, opomba);
		prep.execute();
		
		
		Statement statement = con.createStatement();
		ResultSet res = statement.executeQuery("SELECT * FROM kontakti ORDER BY rowid DESC LIMIT 1;"); // gets the last row
		
		Kontakt kontakt = new Kontakt(res.getInt("id"), res.getString("ime"), res.getString("priimek"), res.getString("naslov"),
				  res.getString("elektronskaPosta"), res.getString("telefon"), res.getString("mobilniTelefon"), res.getString("opomba"));
		con.close();
		return kontakt; 
		
	}
	
	
	private void getConnection() throws SQLException {
		con = DriverManager.getConnection("jdbc:sqlite:ImenikDB.db"); //if there is no such DB, it gets created in the root folder of the project!
		initialize();
	}

	private void initialize() throws SQLException {
		//if there is no table in the DB create one
		if(!hasData) {
			hasData = true;
			Statement statement = con.createStatement();
			ResultSet res = statement.executeQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='kontakti'");
			
			//if res has not a single next, that means we got nothing in res
			if(!res.next()) {
				Statement statement2 = con.createStatement();
				statement2.execute("CREATE TABLE kontakti(id integer,"
						+ "ime varchar(25)," + "priimek varchar(25), "
						+ "naslov varchar(25)," + "elektronskaPosta varchar(25), "
						+ "telefon varchar(25)," + "mobilniTelefon varchar(25), " + "opomba varchar(25)," + "primary key(id));"
						);								
			}
		}
		
	}
	

}
