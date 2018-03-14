import java.util.Scanner;

import javax.xml.xquery.XQConnection;
import javax.xml.xquery.XQDataSource;
import javax.xml.xquery.XQExpression;

public class Main {
	
	static Scanner sc = new Scanner(System.in);
	static XQExpression exp;

	public static void main(String[] args) throws Exception {
		
		connectar("localhost", "8080", "admin2", "root");
		int opcioMenu = -1;
				
		//mostra el menú principal mentre no es trii l'opció de sortir
		while (opcioMenu != 4) {
			System.out.print("\n\n================= MENÚ =================\n"
						   + "   [1] Productes base\n"
						   + "   [2] Productes a granel\n"
						   + "   [3] Productes envasats\n"
						   + "   [4] Sortir\n\n"
						   + "   Tria una opció: ");
			
			opcioMenu = sc.nextInt();
			System.out.println("========================================\n\n");

			switch (opcioMenu) {
				case 1: mostrarOpcionsBase(); break;
				case 2: mostrarOpcionsGranel(); break;
				case 3: mostrarOpcionsEnvas(); break;
				case 4: break; //surt del programa
				default: System.err.println("[ERROR] Opció no vàlida."); break;
			}
		}
	}
	
//-- fi main() ----------------------------------------------------------------------------------------	
	
	public static void mostrarOpcionsBase() throws Exception {
		GestioBase gestioBase = new GestioBase(exp);
		int opcioBase = -1;
		
		while (opcioBase != 8) {
			System.out.print("\n\n=========== PRODUCTES BASE =============\n"
					   + "   [1] Veure productes\n"
					   + "   [2] Veure stock producte\n"
					   + "   [3] Canviar ID producte\n"
					   + "   [4] Canviar nom producte\n"
					   + "   [5] Canviar origen producte\n"
					   + "   [6] Afegir producte nou\n"
					   + "   [7] Eliminar producte\n"
					   + "   [8] Sortir\n\n"
					   + "   Tria una opció: ");
			
			opcioBase = sc.nextInt();
			System.out.println("========================================\n\n");
			
			switch (opcioBase) {
				case 1: gestioBase.veureDisponibles(); break;
				case 2: gestioBase.veureStock(); break;
				case 3: gestioBase.canviarIdProducte(); break;
				case 4: gestioBase.canviarNomProducte(); break;
				case 5: gestioBase.canviarOrigenProducte();break;
				case 6: gestioBase.afegirProducteNou(); break;
				case 7: gestioBase.eliminarProducte(); break;
				case 8: break; //torna al menú principal
				default: System.err.println("[ERROR] Opció no vàlida");
			}
		}		
	}
		
//-----------------------------------------------------------------------------------------------------	
	

	public static void mostrarOpcionsGranel() throws Exception {
		GestioGranel gestioGranel = new GestioGranel(exp);
		int opcioGranel = -1;
		
		while (opcioGranel != 9) {
			System.out.print("\n\n========== PRODUCTES A GRANEL =========\n"
						   + "   [1] Veure magatzem\n"
						   + "   [2] Veure producte\n"
						   + "   [3] Canviar ID del producte\n"
						   + "   [4] Canviar preu/kilo\n"
						   + "   [5] Canviar producte pare\n"
						   + "   [6] Afegir producte nou\n"
						   + "   [7] Afegir producte existent\n"
						   + "   [8] Eliminar producte\n"
						   + "   [9] Sortir\n\n"
						   + "   Tria una opció: ");
			
			opcioGranel = sc.nextInt();
			System.out.println("========================================\n\n");
			
			switch (opcioGranel) {
				case 1: gestioGranel.veureMagatzemGranel(); break;
				case 2: gestioGranel.veureProducteGranel(); break;
				case 3: gestioGranel.canviarIdProducteGranel(); break;
				case 4: gestioGranel.canviarPreuGranel(); break;
				case 5: gestioGranel.canviarProductePareGranel(); break;
				case 6: gestioGranel.afegirProducteNouGranel(); break;
				case 7: gestioGranel.afegirProducteExistentGranel(); break;
				case 8: gestioGranel.eliminarProducteGranel(); break;
				case 9: break; //surt
				default: System.err.println("[ERROR] Opció no vàlida.");
			}		   
		}
	}
	
//-----------------------------------------------------------------------------------------------------	
	
	public static void mostrarOpcionsEnvas() throws Exception {
		GestioEnvas gestioEnvas = new GestioEnvas(exp);
		int opcioEnvas = -1;
		
		while (opcioEnvas != 10) {
			System.out.print("\n\n========== PRODUCTES ENVASATS ==========\n"
						   + "   [1] Veure magatzem\n"
						   + "   [2] Veure producte\n"
						   + "   [3] Canviar ID del producte\n"
						   + "   [4] Canviar preu\n"
						   + "   [5] Canviar producte pare\n"
						   + "   [6] Canviar envàs\n"
						   + "   [7] Afegir producte nou\n"
						   + "   [8] Afegir producte existent\n"
						   + "   [9] Eliminar producte\n"
						   + "   [10] Sortir\n\n"
						   + "   Tria una opció: ");
		
			opcioEnvas = sc.nextInt();
			System.out.println("========================================\n\n");
			
			switch (opcioEnvas) {
				case 1: gestioEnvas.veureMagatzemEnvas(); break;
				case 2: gestioEnvas.veureProducteEnvas(); break;
				case 3: gestioEnvas.canvidIdProducteEnvas(); break;
				case 4: gestioEnvas.canviarPreuProducteEnvas(); break;
				case 5: gestioEnvas.canviarProductePareEnvas(); break;
				case 6: gestioEnvas.canviarEnvasProducte(); break;
				case 7: gestioEnvas.afegirProducteNouEnvas(); break;
				case 8: gestioEnvas.afegirProducteExistentEnvas(); break;
				case 9: gestioEnvas.eliminarProducteEnvas(); break;
				case 10: break; //surt
				default: System.err.println("[ERROR] Opció no vàlida.");
			}
		}
	}
	
	
//--------------------------------------------------------------------------------------------------------

	public static void connectar(String ip, String port, String usuari, String contrasenya) throws Exception {
		XQDataSource xqs = (XQDataSource) Class.forName("net.xqj.exist.ExistXQDataSource").newInstance();
		
		//dades per la connexió
		xqs.setProperty("serverName", ip);
		xqs.setProperty("port", port);
		xqs.setProperty("user", usuari);
		xqs.setProperty("password", contrasenya);
		
		XQConnection conn = xqs.getConnection();
		exp = conn.createExpression();
	}
	
}