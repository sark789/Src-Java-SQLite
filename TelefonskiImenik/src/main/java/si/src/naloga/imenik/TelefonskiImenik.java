package si.src.naloga.imenik;

import si.src.naloga.driver.Driver;
import si.src.naloga.kontakt.Kontakt;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;


public class TelefonskiImenik {

    private List<Kontakt> seznamKontaktov;
    private Driver driver = new Driver();
    private int _id = -1;

    public TelefonskiImenik() throws SQLException {
        seznamKontaktov = new ArrayList<>();
        seznamKontaktov = driver.getAllContacts();
    }

    /**
     * Metaoda izpiše vse kontakte
     */
    public void izpisiVseKontakte() {
    	try {
    		seznamKontaktov = driver.getAllContacts();
    		if(!seznamKontaktov.isEmpty()) {
	    		for(Kontakt kontakt : seznamKontaktov) {
	    			System.out.println(kontakt.toString());
    		}
	    		}
    		else {System.out.println("V imeniku nimate nobenega kontakta!");}
	    	
    	}catch(Exception e) {e.printStackTrace();}
    }

    /**
     * Metaoda doda nov kontakt v imenik
     *
     * onemogočimo dodajanje dupliciranega kontakta :TODO
     */
    public void dodajKontakt(String whereFrom) {
    	
    	Scanner in = new Scanner(System.in);

    	if(whereFrom == "3") { //from UPDATE   		
    		System.out.println("Uredi kontakt z ID:");       	
    		_id = izpisIntId();
        	final int innerId = _id;
        	boolean exists = seznamKontaktov.stream().filter(o -> Integer.toString(o.getId()).equals(Integer.toString(innerId))).findFirst().isPresent();
        	if(!exists) {
        		String print = "ID " + innerId + " ne obstaja.";
        		System.out.println(print);
        		return;
        	}        	       	
        	
    	}
    	
    	System.out.println("Ime kontakta:");
    	String ime = in.nextLine();
    	
    	System.out.println("Priimek:");
    	String priimek = in.nextLine();
    	
    	System.out.println("Naslov:");
    	String naslov = in.nextLine();
    	
    	System.out.println("Elektronska pošta:");
    	String elektronskaPosta = in.nextLine();
    	
    	System.out.println("Telefon:");    	
    	String telefon;
    	if(in.hasNextLong()) {
    		telefon = in.nextLine();
    	}
    	else {
    		while(true) {
    			try {
    				telefon = in.nextLine();
    				long telefon2 = Long.parseLong(telefon.replaceAll("\\s+",""));
    				break;
    			}
    			catch(Exception ex){
    			System.out.println("Napačen vnos za telefon (možen vnos so cela števila). Ponovno vpišite telefon:");
    			}
    		}
    	}    	   	
    	
    	System.out.println("Mobilni telefon:");    	
    	String mobilniTelefon;
    	if(in.hasNextLong()) {
    		mobilniTelefon = in.nextLine();
    	}
    	else {
    		while(true) {
    			try {
    				mobilniTelefon = in.nextLine();
    				long mobilniTelefon2 = Long.parseLong(mobilniTelefon.replaceAll("\\s+",""));
    				break;
    			}
    			catch(Exception ex){
    			System.out.println("Napačen vnos za mobilni telefon (možen vnos so cela števila). Ponovno vpišite mobilni telefon:");    
    			}
    		}
    	}
    	
    	System.out.println("Opomba:");
    	String opomba = in.nextLine();
    	
    	 
    	try {
    		if(whereFrom == "2") { //from DODAJ
    			Kontakt kontakt = new Kontakt(0, ime, priimek, naslov, elektronskaPosta, telefon, mobilniTelefon, opomba);
    			for(Kontakt item : seznamKontaktov) {
    				if(item.equals(kontakt)) {
    					System.out.println("Kontakt že obstaja!");
    					return;
    				}
    			}
    			seznamKontaktov.add(driver.addKontakt(ime, priimek, naslov, elektronskaPosta, telefon, mobilniTelefon, opomba));
    			System.out.println("Kontakt: " + ime + " " + priimek + " uspešno dodan!");
    		}else { // from UPDATE
    			if(_id == -1) {throw new Exception("napaka");}
    			Kontakt kontakt = new Kontakt(_id,ime, priimek, naslov, elektronskaPosta, telefon, mobilniTelefon, opomba);
    			driver.updateKontakt(kontakt);
    			for(Kontakt item : seznamKontaktov) {
    				if(item.getId() == _id) {
    					int index = seznamKontaktov.indexOf(item);
    					seznamKontaktov.set(index, kontakt);
    					System.out.println("Kontakt: " + ime + " " + priimek + " uspešno spremenjen!");
    				}
    			}   			
    		}
			
		} catch (Exception e) {
			System.out.println("Napaka pri dodajanju/urejanju kontakta!");
			e.printStackTrace();
		}
    }
    
    

    /**
     * Metoda popravi podatke na obstoječem kontaktu
     * ID kontakta ni mogoče spreminjati
     * METODA IMPLEMENTIRANA V METODI dodajKontakt
     */
    public void urediKontakt() {
        System.out.println("Metoda še ni implementirana");
    }

    /**
     * Brisanje kontakta po ID-ju
     */
    public void izbrisiKontaktPoId() throws SQLException {
    	Scanner in = new Scanner(System.in);
    	System.out.println("Izbriši kontakt z ID:");
    	int id = izpisIntId();
    	final int innerId = id;
    	driver.deleteKontakt(id);
    	boolean deleted = (seznamKontaktov.removeIf(o -> o.getId() == innerId));
    	String print = deleted ? "ID " + innerId + " uspešno zbrisan!" : "ID " + innerId + " ni bil izbrisan, saj ne obstaja."; 
    	System.out.println(print);
    	   	   	
    }

    /**
     * Izpis kontakta po ID-ju
     */
    public void izpisiKontaktZaId() {
    	boolean exists = false;
    	System.out.println("Izpiši kontakt z ID:");
    	int id = izpisIntId();
    	for(Kontakt item : seznamKontaktov) {
			if(item.getId() == id) {				
				System.out.println(item.toString());
				exists = true;
				break;
			}
    	}
		if(!exists) {System.out.println("ID " + id + " ne obstaja.");}
    	 	
    }

    /**
     * Izpis števila kontaktov
     */
    public void izpisiSteviloKontaktov() {
        System.out.println("Število kontaktov: " + seznamKontaktov.size());
    }

    /**
     * Serializiraj seznam kontoktov na disk.
     * Ime datoteke naj bo "kontakti.ser"
     */
    public void serializirajSeznamKontaktov() {
    	try {
	        FileOutputStream fileOut = new FileOutputStream("kontakti.ser"); // if file does not exist it creates it automatically
	        ObjectOutputStream out = new ObjectOutputStream(fileOut); 
	        
	        out.writeObject(seznamKontaktov);
	        
	        out.close();
	        fileOut.close();
	        System.out.println("Serializirani kontakti so bili shranjeni!");
    	}catch(Exception e) {
    		System.out.println("Napaka pri shranjevanju serializiranih kontaktov");
    		}
    }
    

    /**
     * Pereberi serializiran seznam kontakotv iz diska
     */
    public void naloziSerializiranSeznamKontakotv() {
    	List<Kontakt> seznamSerKont = new ArrayList<Kontakt>();
    	
    	try {
	    	FileInputStream fileIn = new FileInputStream("kontakti.ser");
	        ObjectInputStream in = new ObjectInputStream(fileIn);
	        	    
	        seznamSerKont = (List<Kontakt>) in.readObject();
	        
	        in.close();
	        fileIn.close();
	        
    	}catch(Exception FileNotFoundException) {
    		System.out.println("Datoteka ne obstaja!");
    		return;
    	}
    	if(seznamSerKont.isEmpty()) {
    		System.out.println("Datoteka ne vsebuje kontaktov");
    	}else {
    		for(Kontakt item : seznamSerKont) {
        		System.out.println(item);
        	}
    	}   	   	
    	   	
        
    }

    /**
     * Izvozi seznam kontakov CSV datoteko.
     * Naj uporabnik sam izbere ime izhodne datoteke.
     */
    public void izvoziPodatkeVCsvDatoteko() {
    	Scanner in = new Scanner(System.in);
    	System.out.println("Ime izhodne CSV datoteke:");
    	String ime = in.nextLine();
    	
    	PrintWriter pw = null;
        try {
            pw = new PrintWriter(new File(ime + ".csv"));
        } catch (Exception e) {
            System.out.println("Napala pri ustvarjanju CSV datoteke.");
        }
        StringBuilder builder = new StringBuilder();
        if(!seznamKontaktov.isEmpty()) {
        	for(Kontakt kontakt : seznamKontaktov) {
            	builder.append(kontakt.getId()+ ", ");
            	builder.append(kontakt.getIme()+ ", ");
            	builder.append(kontakt.getPriimek()+ ", ");
            	builder.append(kontakt.getNaslov()+ ", ");
            	builder.append(kontakt.getElektronskaPosta()+ ", ");
            	builder.append(kontakt.getTelefon()+ ", ");
            	builder.append(kontakt.getMobilniTelefon()+ ", ");
            	builder.append(kontakt.getOpomba());       	
            	builder.append("\n");
            }
            pw.write(builder.toString());
            pw.close();
            System.out.println("CSV datoteka je bila ustvarjena!");
        }else {
        	System.out.println("V imeniku ni kontaktov!");
        }
        
    }
    
  //shrani kontakte v poljubno bazo (baze išče v datoteki OstaleBaze)
    public void shraniVBazo()  {
    	try {
	    	String path = System.getProperty("user.dir")+ "\\OstaleBaze\\";
	    	File folder = new File(path);
	    	List<File> filesList = Arrays.asList(folder.listFiles());
	    	List<String> baze = new ArrayList<String>();
	    	
	    	String name = "";
	    	String extension;
	    	for(File item : filesList) {
	    		name = item.getName();
	    		if(name.substring(name.length() - 2).equals("db")) {
	    			baze.add(name.substring(0, name.length()-3));
	    		}
	    	}
	    	System.out.print("Napisite ime baze v katero naj se shranijo kontakti. Baze, ki so na voljo:");
	    	for(String item : baze) {
	    		System.out.print(" " + item);
	    	}
	    	System.out.println();
	    	Scanner in = new Scanner(System.in);
	    	String odg = in.nextLine();
	    	
	    	if(baze.contains(odg)) {
	    		driver.addAllContacts(seznamKontaktov, path + odg +".db");
	    		System.out.println("Podatki so bili shranjeni v bazo z imenom: " + odg + ".db");
	    		
	    	}else {System.out.println("Baza z imenom " + odg + " ne obstaja! Vse baze, ki so na voljo so v direktoriju OstaleBaze!");}
    	}catch(Exception e) {
    		System.out.println("Napaka pri shranjevanju podatkov v poljubno podatkovno bazo");
    	}    	
    }
    
    //prebere kontakte iz poljubne baze (baze išče v datoteki OstaleBaze)
    public void preberiIzBaze() {
    	try {
	    	String path = System.getProperty("user.dir")+ "\\OstaleBaze\\";
	    	File folder = new File(path);
	    	List<File> filesList = Arrays.asList(folder.listFiles());
	    	List<String> baze = new ArrayList<String>();
	    	
	    	String name = "";
	    	String extension;
	    	for(File item : filesList) {
	    		name = item.getName();
	    		if(name.substring(name.length() - 2).equals("db")) {
	    			baze.add(name.substring(0, name.length()-3));
	    		}
	    	}
	    	System.out.print("Napisite ime baze iz katere naj se preberejo kontakti. Baze, ki so na voljo:");
	    	for(String item : baze) {
	    		System.out.print(" " + item);
	    	}
	    	System.out.println();
	    	Scanner in = new Scanner(System.in);
	    	String odg = in.nextLine();
	    	
	    	if(baze.contains(odg)) {
	    		List<Kontakt> list = driver.getAllContacts(path + odg +".db");
	    		for(Kontakt item : list) {
	    			System.out.println(item);
	    		}	    		
	    		
	    	}else {System.out.println("Baza z imenom " + odg + " ne obstaja! Vse baze, ki so na voljo so v direktoriju OstaleBaze!");}
    	}catch(Exception e) {
    		System.out.println("Napaka pri branju podatkov iz poljubne podatkovne baze");
    	} 
    }
    
    
    public void iskanjeKontaktov() {
    	Scanner in = new Scanner(System.in);
    	System.out.println("Iskalni niz:");
    	String niz = in.next().toLowerCase();
    	int i = 0;
    	
    	for(Kontakt kontakt : seznamKontaktov) {
    		if(kontakt.getIme().toLowerCase().contains(niz) || kontakt.getPriimek().toLowerCase().contains(niz)) {
    			System.out.println(kontakt);
    			i++;
    		}    	
    	}
    	if(i==0) {
    		System.out.println("Ni zadetkov!");
    	}
    	
    }
    
       
    
    
    
    
    private int izpisIntId() {
    	Scanner in = new Scanner(System.in);
    	int id;
    	if(in.hasNextInt()) {
    		id = in.nextInt();
    	}
    	else {
    		while(true) {
    			try {
    				id = Integer.parseInt(in.next());
    				break;
    			}
    			catch(NumberFormatException ex){
    			System.out.println("Napačen vnos za ID (možen vnos so cela števila). Ponovno vpišite ID:");    
    			}
    		}
    	}
    	return id;
    }
}
