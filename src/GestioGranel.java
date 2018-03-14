import java.util.Scanner;
import javax.xml.xquery.*;

public class GestioGranel {
	
	private GestioBase gestioBase;
	private XQExpression expressio;
	private Scanner sc;
	
	/**
	 * Constructor
	 * @param expressio
	 */
	public GestioGranel(XQExpression expressio) {
		this.expressio = expressio;
		gestioBase = new GestioBase(expressio);
		sc = new Scanner(System.in);
	}
	
//--------------------------------------------------------------------------------
	
	public void veureMagatzemGranel() throws Exception {
		//capçalera
		System.out.println("---- MAGATZEM PRODUCTES A GRANEL ----\n"
					     + String.format("%-5s %-20s %-15s", "ID", "Producte", "Stock (Kg)")
    				   + "\n-------------------------------------");
		
		String producte, origen, quantitat;
		
        String query = "doc('/db/xqj/magatzemGranel.xml')/magatzem/producte/@idProd/string()";
        XQResultSequence result = expressio.executeQuery(query);

        //per a cada producte que hi hagi al magatzem, mostrem les seves dades
        while (result.next()) {
        	String idProdGranel = result.getItemAsString(null);
        	
        	//agafa la quantitat en kg
        	quantitat = getStockGranel(idProdGranel);
        	
        	//nom i origen del producte
        	String idProd = getPareProducteGranel(idProdGranel);
        	
        	producte = gestioBase.getNomProducte(idProd);
        	origen = gestioBase.getOrigenProducte(idProd);
        	
        	//imprimeix les dades del producte
        	System.out.println(String.format("%-5s %-20s %-15s", idProdGranel, producte + " " + origen, quantitat));
        }
	}
	
//--------------------------------------------------------------------------------

	/**
	 * Retorna com a String l'stock en Kg d'un producte a granel
	 * @param idProdGranel ID del producte a granel
	 * @return Quantitat en Kg d'un producte que hi ha al magatzem
	 * @throws Exception
	 */
	public String getStockGranel(String idProdGranel) throws Exception {
		String query = "doc('/db/xqj/magatzemGranel.xml')/magatzem/producte[@idProd='" + idProdGranel + "']/@qtat/string()";
    	XQResultSequence result = expressio.executeQuery(query);

    	result.next();
    	return  result.getItemAsString(null);
	}
	
//--------------------------------------------------------------------------------
	
	/**
	 * Demana l'ID d'un producte existent, i en mostra totes les dades
	 * @throws Exception
	 */
	public void veureProducteGranel() throws Exception {
		System.out.print("\t- ID del producte a granel: ");
		String idProdGranel = sc.next();
		
		//agafa el codi de producte al que pertany el producte granel
    	String idProd = getPareProducteGranel(idProdGranel);
    	
    	
    	//nom, origen i preu del producte
    	String producte = gestioBase.getNomProducte(idProd);
    	String origen = gestioBase.getOrigenProducte(idProd);
    	String preu = getPreuProducteGranel(idProdGranel);
    	
    	//stock
    	String stock = getStockGranel(idProdGranel);
    	
    	//imprimeix les dades del producte
    	System.out.println("\n\t- ID del producte a granel: " + idProdGranel
    				     + "\n\t- Nom i origen: " + producte + " " + origen
    				     + "\n\t- Preu/Kg: " + preu + "€"
    				     + "\n\t- Stock: " + stock + "Kg");
	}
	
//--------------------------------------------------------------------------------	
	
	/**
	 * Retorna el codi del producte pare d'un producte a granel
	 * @param idProdGranel ID del producte a granel
	 * @return ID del producte pare
	 * @throws Exception
	 */
	private String getPareProducteGranel(String idProdGranel) throws Exception {
		//agafa el codi de producte al que pertany el producte granel
    	String query = "doc('/db/xqj/productesGranel.xml')/productes/producte[@idProdGranel='" + idProdGranel + "']/idProd/string()";
    	XQResultSequence result = expressio.executeQuery(query);
    	
    	result.next();
    	return  result.getItemAsString(null);
	}
	
//--------------------------------------------------------------------------------		
	
	/**
	 * Retorna el preu d'1Kg d'un producte a granel amb ID donat
	 * @param idProd ID del producte
	 * @return Quant costa 1Kg de producte
	 * @throws Exception
	 */
	private String getPreuProducteGranel(String idProd) throws Exception {
		String query = "doc('/db/xqj/productesGranel.xml')/productes/producte[@idProdGranel='" + idProd + "']/preuKilo/string()";
		
		XQResultSequence result = expressio.executeQuery(query);
		result.next();

		return result.getItemAsString(null);
	}
	
//--------------------------------------------------------------------------------			
	
	/**
	 * Demana ID d'un producte a granel existent, seguit la ID que se li vol donar.
	 * Si la primera ID és correcta, la canvia per la segona.
	 * @throws Exception
	 */
	public void canviarIdProducteGranel() throws Exception {
		System.out.print("\t- ID del producte a granel a modificar: ");
		String idProdGranel = sc.next();
		
		System.out.print("\t- Nou ID del producte: ");
		String idProdNou = sc.next();
		
		String queryLlista = "update value doc('/db/xqj/productesGranel.xml')/productes/producte[@idProdGranel='" + idProdGranel + "']/@idProdGranel with " + idProdNou;
		String queryMagatzem = "update value doc('/db/xqj/magatzemGranel.xml')//producte[@idProd='" + idProdGranel + "']/@idProd with " + idProdNou;
	
		expressio.executeCommand(queryLlista);
		expressio.executeCommand(queryMagatzem);
	}
	
//--------------------------------------------------------------------------------	
	
	/**
	 * Modifica el preu per kilo (en euros) d'un producte a granel
	 * @param idProdGranel ID del producte a granel
	 * @param preuKiloNou Preu (en euros) del kilo nou
	 * @throws Exception
	 */
	public void canviarPreuGranel() throws Exception {
		System.out.print("\t- ID del producte a granel a modificar: ");
		String idProdGranel = sc.next();
		
		System.out.print("\t- Preu(€)/Kg nou: ");
		String preuKiloNou = sc.next();
		
		String query = "update value doc('/db/xqj/productesGranel.xml')/productes/producte[@idProdGranel='" + idProdGranel + "']/preuKilo with " + preuKiloNou;
		expressio.executeCommand(query);
	}

//--------------------------------------------------------------------------------	
	
	public void canviarProductePareGranel() throws Exception {
		System.out.print("\t- ID del producte a granel a modificar: ");
		String idProdGranel = sc.next();
		
		System.out.print("\t- ID del producte pare nou: ");
		String idPareNou = sc.next();
		
		String query = "update value doc('/db/xqj/productesGranel.xml')/productes/producte[@idProdGranel='" + idProdGranel + "']/idProd with " + idPareNou;
		expressio.executeCommand(query);
	}
	
//--------------------------------------------------------------------------------	
	
	/**
	 * Afegeix un producte a granel nou al llistat de productes i al magatzem
	 * @param idProdGranel ID del producte a granel nou
	 * @param idProd ID del producte pare
	 * @param preuKilo Quant costa 1 Kilo de producte
	 * @param quantitatInicial Quants kilos s'afegeixen
	 * @throws Exception
	 */
	public void afegirProducteNouGranel() throws Exception {
		System.out.print("\t- Nou ID del producte a granel: ");
		String idProdGranel = sc.next();
		
		System.out.print("\t- ID del producte pare: ");
		String idProd = sc.next();
		
		System.out.print("\t- Preu/Kg: ");
		String preuKilo = sc.next();
		
		System.out.print("\t- Quantitat inicial a afegir (Kg): ");
		String quantitatInicial = sc.next();
		
		//-- afegir un producte existent és el mateix que afegirProducteGranelMagatzem(), per això no té funció pròpia
		
		//afegeix el producte a la llista de productes a granel
		String query = "update insert <producte idProdGranel='" + idProdGranel + "'>"
				+ "<idProd>" + idProd + "</idProd>"
				+ "<preuKilo>" + preuKilo + "</preuKilo>"
				+ "</producte> preceding doc('/db/xqj/productesGranel.xml')/productes/producte[1]";
		expressio.executeCommand(query);

		//crea una instància del producte al magatzem
		query = "update insert <producte idProd='" + idProdGranel + "' qtat='0' />"
			  + "preceding doc('/db/xqj/magatzemGranel.xml')/magatzem/producte[1]";
		expressio.executeCommand(query);
		
		//afegeix el producte al magatzem
		afegirProducteGranelMagatzem(idProdGranel, quantitatInicial);	
	}
	
//------------------------------------------------------------------------------------------------------------------------

	/**
	 * Demana per pantalla ID d'un produce a granel existent i la quantitat en Kg
	 * que es vol afegir al magatzem d'aquest producte
	 * @throws Exception
	 */
	public void afegirProducteExistentGranel() throws Exception {
		System.out.print("\t- ID del producte a granel: ");
		String idProdGranel = sc.next();
		
		System.out.print("\t- Quantitat a afegir (Kg): ");
		String quantitatAfegir = sc.next();
		
		afegirProducteGranelMagatzem(idProdGranel, quantitatAfegir);
	}
	
//------------------------------------------------------------------------------------------------------------------------

	/**
	 * Elimina un producte a granel de la llista de productes a granel i del magatzem (en cascada)
	 * @param idProdGranel ID del producte a granel a eliminar
	 * @throws Exception
	 */
	public void eliminarProducteGranel() throws Exception {
		System.out.print("\t- ID del producte a granel a eliminar: ");
		String idProdGranel = sc.next();
		
		String query = "update delete doc('/db/xqj/magatzemGranel.xml')/magatzem/producte[@idProd='" + idProdGranel + "']";
		expressio.executeCommand(query);
		
		query = "update delete doc('/db/xqj/productesGranel.xml')/productes/producte[@idProdGranel='" + idProdGranel + "']";
		expressio.executeCommand(query);
	}
	
//------------------------------------------------------------------------------------------------------------------------
	
	/**
	 * Afegeix una quantitat donada de productes a granel existents al magatzem
	 * @param idProdGranel ID del producte a granel que es vol afegir
	 * @param quantitat Quantitat (en Kilos) de producte que es vol afegir
	 * @throws Exception
	 */
	private void afegirProducteGranelMagatzem(String idProdGranel, String quantitat) throws Exception {
		//afegeix el producte al magatzem
		String query = "declare variable $qtatActual := doc('/db/xqj/magatzemGranel.xml')/magatzem/producte[@idProd='" + idProdGranel + "']/@qtat/string();"
		             + "declare variable $qtatNova := sum((xs:integer($qtatActual)+" + quantitat + "));"
		             + "update value doc('/db/xqj/magatzemGranel.xml')/magatzem/producte[@idProd='" + idProdGranel + "']/@qtat with $qtatNova";
		
		expressio.executeCommand(query);
	}
	

}
