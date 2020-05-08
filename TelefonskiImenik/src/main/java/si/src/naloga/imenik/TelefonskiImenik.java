package si.src.naloga.imenik;

import si.src.naloga.driver.Driver;
import si.src.naloga.kontakt.Kontakt;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class TelefonskiImenik {

    private List<Kontakt> seznamKontaktov;
    private Driver driver = new Driver();

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
    public void dodajKontakt() {
    	
    	Scanner in = new Scanner(System.in);
    	
    	System.out.println("Ime kontakta:");
    	String ime = in.next();
    	
    	System.out.println("Priimek:");
    	String priimek = in.next();
    	
    	System.out.println("Naslov:");
    	String naslov = in.next();
    	
    	System.out.println("Elektronska pošta:");
    	String elektronskaPosta = in.next();
    	
    	System.out.println("Telefon:");    	
    	long telefon;
    	if(in.hasNextLong()) {
    		telefon = in.nextLong();
    	}
    	else {
    		while(true) {
    			try {
    				telefon = Long.parseLong(in.next());
    				break;
    			}
    			catch(NumberFormatException ex){
    			System.out.println("Napačen vnos za telefon (možen vnos: 0-9). Ponovno vpišite telefon:");    
    			}
    		}
    	}    	   	
    	
    	System.out.println("Mobilni telefon:");    	
    	long mobilniTelefon;
    	if(in.hasNextLong()) {
    		mobilniTelefon = in.nextLong();
    	}
    	else {
    		while(true) {
    			try {
    				mobilniTelefon = Long.parseLong(in.next());
    				break;
    			}
    			catch(NumberFormatException ex){
    			System.out.println("Napačen vnos za mobilni telefon (možen vnos: 0-9). Ponovno vpišite mobilni telefon:");    
    			}
    		}
    	}
    	
    	System.out.println("Opomba:");
    	String opomba = in.next();
    	
    	 
    	try {
			seznamKontaktov.add(driver.addKontakt(ime, priimek, naslov, elektronskaPosta, Long.toString(telefon), Long.toString(mobilniTelefon), opomba));
			System.out.println("Kontakt: " + ime + " " + priimek + " uspešno dodan!");
			
		} catch (Exception e) {
			System.out.println("Napaka pri dodajanju kontakta!");
			e.printStackTrace();
		}
    }

    /**
     * Metoda popravi podatke na obstoječem kontaktu
     * ID kontakta ni mogoče spreminjati
     */
    public void urediKontakt() {
        System.out.println("Metoda še ni implementirana");
    }

    /**
     * Brisanje kontakta po ID-ju
     */
    public void izbrisiKontaktPoId() {
        System.out.println("Metoda še ni implementirana");
    }

    /**
     * Izpis kontakta po ID-ju
     */
    public void izpisiKontaktZaId() {
        System.out.println("Metoda še ni implementirana");
    }

    /**
     * Izpis kontakta po ID-ju
     */
    public void izpisiSteviloKontaktov() {
        System.out.println("Metoda še ni implementirana");
    }

    /**
     * Serializiraj seznam kontoktov na disk.
     * Ime datoteke naj bo "kontakti.ser"
     */
    public void serializirajSeznamKontaktov() {
        System.out.println("Metoda še ni implementirana");
    }

    /**
     * Pereberi serializiran seznam kontakotv iz diska
     */
    public void naloziSerializiranSeznamKontakotv() {
        System.out.println("Metoda še ni implementirana");
    }

    /**
     * Izvozi seznam kontakov CSV datoteko.
     * Naj uporabnik sam izbere ime izhodne datoteke.
     */
    public void izvoziPodatkeVCsvDatoteko() {
        System.out.println("Metoda še ni implementirana");
    }
}
