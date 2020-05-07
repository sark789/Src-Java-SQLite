package si.src.naloga.driver;

import java.sql.*;

public class Driver {
	
	private static Connection con;
	private static boolean hasData = false;
	
	public ResultSet getAllContacts() throws SQLException {
		if(con == null) {
			getConnection();
		}
		
		Statement statement = con.createStatement();
		ResultSet res = statement.executeQuery("SELECT id, ime, priimek, naslov, elektronskaPosta, "
												+ "telefon, mobilniTelefon, opomba FROM kontakti");
		return res;
	}

	private void getConnection() throws SQLException {
		con = DriverManager.getConnection("jdbc:sqlite:ImenikDB.db"); //if there is no such DB, it gets created
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
						+ "telefon varchar(25)," + "mobilniTelefon varchar(25), " + "opomba varchar(60)," + "primary key(id));"
						);
				
				//inserting sample data
				PreparedStatement prep = con.prepareStatement("INSERT INTO kontakti(ime, priimek, naslov, elektronskaPosta,telefon, mobilniTelefon, opomba) values(?,?,?,?,?,?,?);");
				prep.setString(1, "SampleIme");
				prep.setString(2, "SamplePriimek");
				prep.setString(3, "SampleNaslov");
				prep.setString(4, "SampleElPosta");
				prep.setString(5, "SampleTel");
				prep.setString(6, "SampleMobTel");
				prep.setString(7, "SampleOpomba");
				prep.execute();
				
			}
		}
		
	}
	
	public void addKontakt() throws SQLException {
		if(con == null) {
			getConnection();
		}
	}
	

}
