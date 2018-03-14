import java.util.Scanner;
import javax.xml.xquery.*;

public class GestioEnvas {
	
	private GestioBase gestioBase;
	private XQExpression expressio;
	private Scanner sc;
	
	/**
	 * Constructor
	 * @param expressio
	 */
	public GestioEnvas(XQExpression expressio) {
		this.expressio = expressio;
		gestioBase = new GestioBase(expressio);
		sc = new Scanner(System.in);
	}
	
//-------------------------------------------------------------------------------------------------------
	
	/**
	 * Mostra una llista amb tots els productes envasats que hi ha al magatzem
	 * @throws Exception
	 */
	public void veureMagatzemEnvas() throws Exception {
		System.out.println("------ MAGATZEM PRODUCTES ENVASATS -----\n"
				 	     + String.format("%-5s %-20s %-20s", "ID", "Producte", "Envàs")
				 	     + "\n----------------------------------------");
		
		String query = "doc('/db/xqj/magatzemEnvas.xml')/magatzem/producte/@idProd/string()";
		XQResultSequence result = expressio.executeQuery(query);
		
		//per a cada producte que hi hagi al magatzem, mostrem les seves dades
		while (result.next()) {
			String idProdEnvas = result.getItemAsString(null);
			        	
			//nom i origen del producte
			String idProd = getPareProducteEnvas(idProdEnvas);
			String producte = gestioBase.getNomProducte(idProd);
			String origen = gestioBase.getOrigenProducte(idProd);
			
			//agafa l'ID de l'envàs
			String idEnvas = getIdEnvasProducte(idProdEnvas);
			//dades de l'envàs
			String tipusEnvas = getTipusEnvas(idEnvas);
			String capacitatEnvas = getCapacitatEnvas(idEnvas);
			String unitatEnvas = getUnitatMesuraEnvas(idEnvas);
			
		
			//imprimeix el producte per pantalla
	 	     System.out.println(String.format("%-5s %-20s %-20s", idProdEnvas, producte + " " + origen, tipusEnvas + " " + capacitatEnvas + unitatEnvas));
		}
		
		veureResumMagatzemEnvas();
	}
	
//-------------------------------------------------------------------------------------------------------
	
	public void veureResumMagatzemEnvas() throws Exception {
		System.out.println("\n----------------------- RESUM -----------------------\n"
		 	             + String.format("%-5s %-20s %-20s %-20s", "ID", "Producte", "Envàs", "Stock")
		 	  	         + "\n-----------------------------------------------------");
		
		String query = "doc('/db/xqj/productesEnvas.xml')/productes/producte/@idProdEnvas/string()";
		XQResultSequence result = expressio.executeQuery(query);
		
		//per a cada producte que hi hagi al magatzem, mostrem les seves dades
		while (result.next()) {
			String idProdEnvas = result.getItemAsString(null);
			
			//nom i origen del producte
			String idProd = getPareProducteEnvas(idProdEnvas);
			String nom = gestioBase.getNomProducte(idProd);
			String origen = gestioBase.getOrigenProducte(idProd);
			
			//agafa l'ID de l'envàs
			String idEnvas = getIdEnvasProducte(idProdEnvas);
			//dades de l'envàs
			String tipusEnvas = getTipusEnvas(idEnvas);
			String capacitatEnvas = getCapacitatEnvas(idEnvas);
			String unitatEnvas = getUnitatMesuraEnvas(idEnvas);
			
			//stock
			String stock = getStockProducteEnvas(idProdEnvas);
			
			System.out.println(String.format("%-5s %-20s %-20s %-20s", idProdEnvas, nom + " " + origen, tipusEnvas + " " + capacitatEnvas + unitatEnvas, stock));
		}
		
		System.out.println("-----------------------------------------------------");
	}
	
//-------------------------------------------------------------------------------------------------------	

	/**
	 * Mostra per pantalla totes les dades d'un producte envasat
	 * @throws Exception
	 */
	public void veureProducteEnvas() throws Exception {
		System.out.print("ID del producte envasat: ");
		String idProdEnvas = sc.next();
		
		
		String idProd = getPareProducteEnvas(idProdEnvas);
    	
    	//nom, origen i preu del producte
    	String producte = gestioBase.getNomProducte(idProd);
    	String origen = gestioBase.getOrigenProducte(idProd);
    	String preu = getPreuProducteEnvas(idProdEnvas);
    	
    	//tot el referent a l'envàs
    	String idEnvas = getIdEnvasProducte(idProdEnvas);
    	String tipusEnvas = getTipusEnvas(idEnvas);
    	String capacitatEnvas = getCapacitatEnvas(idEnvas);
    	String unitatEnvas = getUnitatMesuraEnvas(idEnvas);
    	
    	//stock
    	String stock = getStockProducteEnvas(idProdEnvas);
    	
    	//imprimeix les dades del producte
    	System.out.println("\n\t- ID del producte envasat: " + idProdEnvas
    					 + "\n\t- Nom i origen: " + producte + " " + origen
    					 + "\n\t- Preu unitat: " + preu + "€"
    					 + "\n\t- Envàs: " + tipusEnvas + " " + capacitatEnvas + unitatEnvas
    					 + "\n\t- Stock: " + stock);
	}
	
//-------------------------------------------------------------------------------------------------------	
	
	/**
	 * Retorna el preu d'un producte envasat amb ID donat
	 * @param idProd ID del producte
	 * @return Quant costa el producte
	 * @throws Exception
	 */
	public String getPreuProducteEnvas(String idProd) throws Exception {
		String query = "doc('/db/xqj/productesEnvas.xml')/productes/producte[@idProdEnvas='" + idProd + "']/preuUnitat/string()";
		
		XQResultSequence result = expressio.executeQuery(query);
		result.next();

		return result.getItemAsString(null);
	}
	
//-------------------------------------------------------------------------------------------------------	

	/**
	 * Retorna el codi de l'envàs que té un producte envasat
	 * @param idProdEnvas ID del producte envasat
	 * @return ID de l'envàs
	 * @throws Exception
	 */
	public String getIdEnvasProducte(String idProdEnvas) throws Exception {
		String query = "doc('/db/xqj/productesEnvas.xml')/productes/producte[@idProdEnvas='" + idProdEnvas + "']/envas/string()";
		XQResultSequence result = expressio.executeQuery(query);
		
		result.next();
		return result.getItemAsString(null);
	}
	
//-------------------------------------------------------------------------------------------------------	

	
	/**
	 * Retorna el codi del producte pare d'un producte envasat
	 * @param idProdEnvas ID del producte envasat
	 * @return ID del producte pare
	 * @throws Exception
	 */
	public String getPareProducteEnvas(String idProdEnvas) throws Exception {
		//agafa el codi de producte al que pertany el producte granel
    	String query = "doc('/db/xqj/productesEnvas.xml')/productes/producte[@idProdEnvas='" + idProdEnvas + "']/idProd/string()";
    	XQResultSequence result = expressio.executeQuery(query);
    	
    	result.next();
    	return  result.getItemAsString(null);
	}
	
//-------------------------------------------------------------------------------------------------------		
	
	/**
	 * Retorna el tipus d'envas (tetrabrick, llauna...) que és un envàs determinat
	 * @param id ID de l'envàs
	 * @return Tipus d'envàs
	 * @throws Exception
	 */
	public String getTipusEnvas(String id) throws Exception {
		String query = "doc('/db/xqj/envasos.xml')/envasos/envas[@id='" + id + "']/tipus/string()";
		
		XQResultSequence result = expressio.executeQuery(query);
		result.next();

		return result.getItemAsString(null);
	}
	
//------------------------------------------------------------------------------------------------------------------------
	
	/**
	 * Retorna la capacitat que té un envàs, la quantitat de producte que emmagatzema (sense unitats de mesura)
	 * @param id ID de l'envàs
	 * @return Capacitat d'un envàs sense unitats de mesura
	 * @throws Exception
	 */
	public String getCapacitatEnvas(String id) throws Exception {
		String query = "doc('/db/xqj/envasos.xml')/envasos/envas[@id='" + id + "']/capacitat/string()";
		
		XQResultSequence result = expressio.executeQuery(query);
		result.next();

		return result.getItemAsString(null);
	}
	
//------------------------------------------------------------------------------------------------------------------------	
	
	/**
	 * Retorna la unitat de mesura que té un envàs (Kilograms, litres...)
	 * @param id ID de l'envàs
	 * @return Unitat de mesura
	 * @throws Exception
	 */
	public String getUnitatMesuraEnvas(String id) throws Exception {
		String query = "doc('/db/xqj/envasos.xml')/envasos/envas[@id='" + id + "']/unitatMesura/string()";
		
		XQResultSequence result = expressio.executeQuery(query);
		result.next();

		return result.getItemAsString(null);
	}
	
//------------------------------------------------------------------------------------------------------------------------	
	
	/**
	 * Demana ID d'un producte envasat existent, seguit la ID que se li vol donar.
	 * Si la primera ID és correcta, la canvia per la segona.
	 * @throws Exception
	 */
	public void canvidIdProducteEnvas() throws Exception {
		System.out.print("\t- ID del producte envasat a modificar: ");
		String idProdEnvas = sc.next();
		
		System.out.print("\t- Nou ID del producte: ");
		String idProdEnvasNou = sc.next();
		
		String queryLlista = "update value doc('/db/xqj/productesEnvas.xml')/productes/producte[@idProdEnvas='" + idProdEnvas + "']/@idProdEnvas with " + idProdEnvasNou;
		String queryMagatzem = "update value doc('/db/xqj/magatzemEnvas.xml')//producte[@idProd='" + idProdEnvas + "']/@idProd with " + idProdEnvasNou;
	
		expressio.executeCommand(queryLlista);
		expressio.executeCommand(queryMagatzem);
	}
	
//------------------------------------------------------------------------------------------------------------------------	

	/**
	 * Canvia el preu per unitat d'un producte envasat.
	 * L'ID del producte envasat i el preu que se li vol donar es demanen per pantalla.
	 * @throws Exception
	 */
	public void canviarPreuProducteEnvas() throws Exception {
		System.out.print("\t- ID del producte envasat a modificar: ");
		String idProdEnvas = sc.next();
		
		System.out.print("\t- Nou preu(€) del producte: ");
		String preuNou = sc.next();
		
		String query = "update value doc('/db/xqj/productesEnvas.xml')/productes/producte[@idProdEnvas='" + idProdEnvas + "']/preuUnitat with " + preuNou;
		expressio.executeCommand(query);

	}
	
//------------------------------------------------------------------------------------------------------------------------	

	/**
	 * Assigna un producte base a un producte envasat.
	 * L'ID del producte envasat i del nou producte base es demanen per pantalla.
	 * @throws Exception
	 */
	public void canviarProductePareEnvas() throws Exception {
		System.out.print("\t- ID del producte envasat a modificar: ");
		String idProdEnvas = sc.next();
		
		System.out.print("\t- ID del producte pare nou: ");
		String idPareNou = sc.next();
		
		String query = "update value doc('/db/xqj/productesEnvas.xml')/productes/producte[@idProdEnvas='" + idProdEnvas + "']/idProd with " + idPareNou;
		expressio.executeCommand(query);
	}
	
//------------------------------------------------------------------------------------------------------------------------	

	/**
	 * Canvia l'envàs que té un producte envasat.
	 * L'ID del producte envasat i l'ID de l'envàs es demanen per pantalla.
	 * @throws Exception
	 */
	public void canviarEnvasProducte() throws Exception {
		System.out.print("\t- ID del producte envasat a modificar: ");
		String idProdEnvas = sc.next();
		
		System.out.print("\t- ID de l'envàs nou: ");
		String idEnvasNou = sc.next();
		
		String query = "update value doc('/db/xqj/productesEnvas.xml')/productes/producte[@idProdEnvas='" + idProdEnvas + "']/envas with " + idEnvasNou;
		expressio.executeCommand(query);
	}
	
//------------------------------------------------------------------------------------------------------------------------	
	
	/**
	 * Afegeix un nou producte envasat a la llista de productes envasats i al magatzem.
	 * Totes les dades del producte nou, i les unitats inicials que es volen afegir al magatzem
	 * es demanen per pantalla.
	 * @throws Exception
	 */
	public void afegirProducteNouEnvas() throws Exception {
		System.out.print("\n\t- Nou ID del producte envasat: ");
		String idProdEnvas = sc.next();
		
		System.out.print("\t- ID del producte pare: ");
		String idProd = sc.next();
		
		System.out.print("\t- ID de l'envàs: ");
		String idEnvas = sc.next();
		
		System.out.print("\t- Preu de l'unitat: ");
		String preuUnitat = sc.next();
		
		System.out.print("\t- Unitats a afegir: ");
		String unitatsAfegir = sc.next();
		
		//-- afegir un producte existent és el mateix que afegirProducteEnvasMagatzem(), per això no té funció pròpia
		
		//afegeix el producte al magatzem
		afegirProducteEnvasMagatzem(idProdEnvas, unitatsAfegir);
		
		//afegeix el producte a la llista de productes envasats
		String query = "update insert <producte idProdEnvas='" + idProdEnvas + "'>"
				+ "<idProd>" + idProd + "</idProd>"
				+ "<preuUnitat>" + preuUnitat + "</preuUnitat>"
				+ "<envas>" + idEnvas + "</envas>"
				+ "</producte> preceding doc('/db/xqj/productesEnvas.xml')/productes/producte[1]";
		
		expressio.executeCommand(query);
	}
	
//------------------------------------------------------------------------------------------------------------------------

	/**
	 * Afegeix una quantitat donada de productes envasats existents al magatzem
	 * @param idProdEnvas ID del producte envasat que es vol afegir
	 * @param unitatsAfegir Número d'unitats del producte es volen afegir
	 * @throws Exception
	 */
	public void afegirProducteEnvasMagatzem(String idProdEnvas, String unitatsAfegir) throws Exception {
		//afegeix el producte al magatzem
		for (int i=0; i<Integer.parseInt(unitatsAfegir); i++) {
			String query = "update insert <producte idProd='" + idProdEnvas + "'/> preceding doc('/db/xqj/magatzemEnvas.xml')/magatzem/producte[1]";
			expressio.executeCommand(query);
		}
	}
	
//------------------------------------------------------------------------------------------------------------------------
	
	/**
	 * Demana per pantalla l'ID d'un producte envasat i la quantitat d'unitats
	 * que es volen afegir al magatzem.
	 * @throws Exception
	 */
	public void afegirProducteExistentEnvas()throws Exception {
		System.out.print("\t- ID del producte envasat: ");
		String idProdEnvas = sc.next();
		
		System.out.print("\t- Unitats a afegir: ");
		String unitatsAfegir = sc.next();
		
		afegirProducteEnvasMagatzem(idProdEnvas, unitatsAfegir);
	}
	
//------------------------------------------------------------------------------------------------------------------------

	/**
	 * 
	 * Elimina en cascada un producte envasat.
	 * L'ID del producte envasat es demana per pantalla.
	 * @throws Exception
	 */
	public void eliminarProducteEnvas() throws Exception {
		System.out.print("\t- ID del producte envasat a eliminar: ");
		String idProdEnvas = sc.next();
		
		String query = "update delete doc('/db/xqj/magatzemEnvas.xml')//producte[@idProd='" + idProdEnvas + "']";
		expressio.executeCommand(query);
		
		query = "update delete doc('/db/xqj/productesEnvas.xml')/productes/producte[@idProdEnvas='" + idProdEnvas + "']";
		expressio.executeCommand(query);
	}
	
//------------------------------------------------------------------------------------------------------------------------
	
	/**
	 * Retorna com a String l'stock d'un producte envasat
	 * @param idProdEnvas ID del producte envasat
	 * @return Quantitat d'unitats del producte envasat que hi ha al magatzem
	 */
	public String getStockProducteEnvas(String idProdEnvas) throws Exception {
		String query = "count(doc('/db/xqj/magatzemEnvas.xml')//producte[@idProd='" + idProdEnvas + "'])";
		XQResultSequence result = expressio.executeQuery(query);
		result.next();
		
		return result.getItemAsString(null);
	}
	
}
