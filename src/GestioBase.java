import java.util.Scanner;

import javax.xml.xquery.*;

public class GestioBase {
	private Scanner sc;
	private XQExpression expressio;
	
	/**
	 * Constructor
	 * @param expressio
	 */
	public GestioBase(XQExpression expressio) {
		this.expressio = expressio;
		sc = new Scanner(System.in);
	}
	
//------------------------------------------------------------------------------------------------------	
	
	/**
	 * Mostra per pantalla una llista amb els productes base que existeixen
	 * @throws Exception
	 */
	public void veureDisponibles() throws Exception {
		//capçalera
		System.out.println(String.format("%-5s %-15s %-15s", "ID", "Nom", "Origen")
						+ "\n----------------------------------------");
		
		String idProd, nom, origen;
		String query = "doc('/db/xqj/productes.xml')/productes/producte/@id/string()";
		
		XQResultSequence result = expressio.executeQuery(query);
		
		while (result.next()) {
			//id del producte
			idProd = result.getItemAsString(null);
			
			//nom  i origen del producte
			nom = getNomProducte(idProd);
			origen = getOrigenProducte(idProd);
			
			System.out.println(String.format("%-5s %-15s %-15s", idProd, nom, origen));
		}
	}
	
//------------------------------------------------------------------------------------------------------	

	/**
	 * Demana per teclat l'ID d'un producte i un nom i, si l'ID és vàlid,
	 * canvia el nom del producte per l'especificat.
	 * @throws Exception
	 */
	public void canviarNomProducte() throws Exception {
		//introducció de les dades
		System.out.print("\t- ID del producte: ");
		String idProd = sc.next();
		
		System.out.print("\t- Nom nou: ");
		String nomNou = sc.next();
		
		String query = "update value doc('/db/xqj/productes.xml')/productes/producte[@id='" + idProd + "']/nom with '" + nomNou + "'";
		expressio.executeCommand(query);
	}
	
//------------------------------------------------------------------------------------------------------
	
	/**
	 * Demana per teclat l'ID d'un producte i un origen i, si l'ID és vàlid,
	 * canvia l'origen del producte per l'especificat.
	 * @throws Exception
	 */
	public void canviarOrigenProducte() throws Exception {
		//introducció de les dades
		System.out.print("\t- ID del producte: ");
		String idProd = sc.next();
		
		System.out.print("\t- Origen nou: ");
		String origenNou = sc.next();
		
		String query = "update value doc('/db/xqj/productes.xml')/productes/producte[@id='" + idProd + "']/origen with '" + origenNou + "'";
		expressio.executeCommand(query);
	}
	
//------------------------------------------------------------------------------------------------------
	
	/**
	 * Demana er teclat l'ID d'un producte, seguit per un altre ID nou.
	 * Si el primer ID és vàlid, el canvia per el segón.
	 * @throws Exception
	 */
	public void canviarIdProducte() throws Exception {
		//introducció de les dades
		System.out.print("\t- ID del producte: ");
		String idProd = sc.next();
		
		System.out.print("\t- Nou ID del producte: ");
		String idProdNou = sc.next();
		
		//canvia l'ID del producte a productes.xml
		String query = "update value doc('/db/xqj/productes.xml')/productes/producte[@id='" + idProd + "']/@id with " + idProdNou;
		expressio.executeCommand(query);
		
		//canvia l'ID dels productes envasats que tinguin com a pare aquest producte
		query = "update value doc('/db/xqj/productesEnvas.xml')/productes/producte[./idProd='" + idProd + "']/idProd with " + idProdNou;
		expressio.executeCommand(query);
		
		//canvia l'ID dels productes a granel que tinguin com a pare aquest producte
		query = "update value doc('/db/xqj/productesGranel.xml')/productes/producte[./idProd='" + idProd + "']/idProd with " + idProdNou;
		expressio.executeCommand(query);
	}

//------------------------------------------------------------------------------------------------------
	
	/**
	 * Demana per pantalla ID, nom i origen d'un producte nou, i l'afegeix a la llista de productes
	 * @throws Exception
	 */
	public void afegirProducteNou() throws Exception {
		//introducció de les dades
		System.out.print("\t- ID del producte nou: ");
		String idProdNou = sc.next();
		
		System.out.print("\t- Nom del producte nou: ");
		String nomNou = sc.next();
		
		System.out.print("\t- Origen nou: ");
		String origenNou = sc.next();
		
		String query = "update insert <producte id='" + idProdNou + "'>"
				     + "<nom>" + nomNou + "</nom>"
				     + "<origen>" + origenNou + "</origen>"
				     + "</producte>"
				     + " preceding doc('/db/xqj/productes.xml')/productes/producte[1]";
		expressio.executeCommand(query);
	}
	
//------------------------------------------------------------------------------------------------------
	
	/**
	 * Demana l'ID d'un producte existent i elimina en cascada aquest producte
	 * @throws Exception
	 */
	public void eliminarProducte() throws Exception {
		//introducció de les dades
		System.out.print("\t- ID del producte: ");
		String idProd = sc.next();
		
		//elimina el producte de la llista de productes base
		String query = "update delete doc('/db/xqj/productes.xml')/productes/producte[@id='" + idProd + "']";
		expressio.executeCommand(query);
		
		//elimina del magatzem de productes envasats i de la llista de productes envasats
		//tots els productes "fills" del que s'està eliminant
		query = "doc('/db/xqj/productesEnvas.xml')//producte[./idProd='" + idProd + "']/@idProdEnvas/string()";
		XQResultSequence result = expressio.executeQuery(query);
		
		while (result.next()) {
			String idProdEnvas = result.getItemAsString(null);
		
			query = "update delete doc('/db/xqj/magatzemEnvas.xml')//producte[@idProd='" + idProdEnvas + "']";
			expressio.executeCommand(query);
			
			query = "update delete doc('/db/xqj/productesEnvas.xml')//producte[@idProdEnvas='" + idProdEnvas + "']";
			expressio.executeCommand(query);
		}
		
		//elimina del magatzem de productes a granel i de la llista de productes a granel
		//tots els productes "fills" del que s'està eliminant
		query = "doc('/db/xqj/productesGranel.xml')//producte[./idProd='" + idProd + "']/@idProdGranel/string()";
		result = expressio.executeQuery(query);
		
		while (result.next()) {
			String idProdGranel = result.getItemAsString(null);
		
			query = "update delete doc('/db/xqj/magatzemGranel.xml')//producte[@idProd='" + idProdGranel + "']";
			expressio.executeCommand(query);
			
			query = "update delete doc('/db/xqj/productesGranel.xml')//producte[@idProdGranel='" + idProdGranel + "']";
			expressio.executeCommand(query);
		}
	}
	
//------------------------------------------------------------------------------------------------------
	
	/**
	 * Demana l'ID d'un producte existent i mostra el seu stock detallat
	 * @throws Exception
	 */
	public void veureStock() throws Exception {
		System.out.print("\t- ID del producte: ");
		String idProd = sc.next();
		
		String nom = getNomProducte(idProd);
		String origen = getOrigenProducte(idProd);
				
		//-- stock granel ---------------------------------------------------------
		
		String query = "doc('/db/xqj/productesGranel.xml')/productes/producte[./idProd='" + idProd + "']/@idProdGranel/string()";
		XQResultSequence result = expressio.executeQuery(query); result.next();
		
		String idProdGranel = result.getItemAsString(null);
		query = "doc('/db/xqj/magatzemGranel.xml')/magatzem/producte[@idProd='" + idProdGranel + "']/@qtat/string()";
		result = expressio.executeQuery(query); result.next();
		double stockGranel = Double.parseDouble(result.getItemAsString(null));
		
		System.out.println("\n------------- STOCK GRANEL -------------\n"
					     + "TOTAL A GRANEL: " + String.format("%.2f", stockGranel) + "Kg.");
		
		//-- stock envas ----------------------------------------------------------
		System.out.println("\n\n------- STOCK PRODUCTES ENVASATS -------");
		
		GestioEnvas gestioEnvas = new GestioEnvas(expressio);
		double stockEnvas = 0;
		
		query = "doc('/db/xqj/productesEnvas.xml')/productes/producte[./idProd='" + idProd + "']/@idProdEnvas/string()";
		result = expressio.executeQuery(query);
		
		while (result.next()) {
			String idProdEnvas = result.getItemAsString(null);
			
			//agafa l'ID de l'envàs
			String idEnvas = gestioEnvas.getIdEnvasProducte(idProdEnvas);
			//dades de l'envàs
			String tipusEnvas = gestioEnvas.getTipusEnvas(idEnvas);
			String capacitatEnvas = gestioEnvas.getCapacitatEnvas(idEnvas);
			String unitatEnvas = gestioEnvas.getUnitatMesuraEnvas(idEnvas);
			
			//stock en unitats
			String stock = gestioEnvas.getStockProducteEnvas(idProdEnvas);
			
			//càlcul de l'stock en Kg
			if (unitatEnvas.equals("g")) stockEnvas += Double.parseDouble(capacitatEnvas)/1000 * Integer.parseInt(stock);
			else stockEnvas += Double.parseDouble(capacitatEnvas) * Integer.parseInt(stock);
			
			System.out.println(String.format("%-20s %-20s", tipusEnvas + " " + capacitatEnvas + unitatEnvas, stock + " unitats"));
		}
		
		System.out.println("TOTAL PRODUCTES ENVASATS: " + String.format("%.2f", stockEnvas) + "Kg.");
		
		System.out.println("\n----------------------------------------"
					     + "\n[ TOTAL STOCK: " + String.format("%.2f", (stockEnvas+stockGranel)) + "Kg. ]");
	
	}

//------------------------------------------------------------------------------------------------------------------------	
		
	/**
	 * Retorna el nom d'un producte amb ID donat
	 * @param idProd ID del producte
	 * @return Nom del producte com String
	 * @throws Exception
	 */
	public String getNomProducte(String idProd) throws Exception {
		String query = "doc('/db/xqj/productes.xml')/productes/producte[@id='" + idProd + "']/nom/string()";
		
		XQResultSequence result = expressio.executeQuery(query);
		result.next();

		return result.getItemAsString(null);
	}

//------------------------------------------------------------------------------------------------------------------------	

	/**
	 * Retorna l'origen d'un producte amb ID donat
	 * @param idProd ID del producte
	 * @return Origen del producte com String
	 * @throws Exception
	 */
	public String getOrigenProducte(String idProd) throws Exception {
		String query = "doc('/db/xqj/productes.xml')/productes/producte[@id='" + idProd + "']/origen/string()";
		
		XQResultSequence result = expressio.executeQuery(query);
		result.next();

		return result.getItemAsString(null);
	}
}
