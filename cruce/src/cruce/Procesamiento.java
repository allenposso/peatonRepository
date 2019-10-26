package cruce;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 *
 * @author dasting allan
 */
public class Procesamiento {

	final String separador = ";";
	final String saltoLinea = "\"";
	ArrayList<Cruce> listaCruce;
	SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

	public Procesamiento(ArrayList<Cruce> listaCruce) {
		this.listaCruce = listaCruce;
	}

	public Procesamiento() {
	}

	public void agregarDatos(String fecha, boolean data) {
		listaCruce.add(new Cruce(data, parseDate(fecha)));

	}

	private Date parseDate(String fecha) {
		try {
			return formatter.parse(fecha);
		} catch (Exception e) {
			return null;
		}
	}

	

	public Map<String, Integer> procesarListaYear() {
		Map<String, Integer> nuevaListay = new HashMap<>();
		nuevaListay.put(formatter.format(anotherday(-1)), 0);
		new PromedioFechaAnalisis(listaCruce).filterListByDateYear(listaCruce).forEach(e -> {
			String val = formatter.format(e);
			nuevaListay.put(val, new PromedioFechaAnalisis(listaCruce).countValuesYear(e));
		});
		nuevaListay.put(formatter.format(anotherday(1)), 0);
		return nuevaListay;
	}

	public Map<String, Integer> procesarListaDay() {
		Map<String, Integer> nuevaListad = new HashMap<>();
		nuevaListad.put(formatter.format(anotherday(-1)), 0);
		new PromedioFechaAnalisis(listaCruce).filterListByDateDay(listaCruce).forEach(e -> {
			String val = formatter.format(e);
			nuevaListad.put(val, new PromedioFechaAnalisis(listaCruce).countValuesDay(e));
		});
		nuevaListad.put(formatter.format(anotherday(1)), 0);
		return nuevaListad;
	}
	
	

	public Map<String, Integer> procesarListaMonth() {
		Map<String, Integer> nuevaListam = new HashMap<>();
		nuevaListam.put(formatter.format(anotherday(-1)), 0);
		new PromedioFechaAnalisis(listaCruce).filterListByDateMonth(listaCruce).forEach(e -> {
			String val = formatter.format(e);
			nuevaListam.put(val, new PromedioFechaAnalisis(listaCruce).countValuesMonth(e));
		});
		nuevaListam.put(formatter.format(anotherday(1)), 0);

		return nuevaListam;
	}
	private Date anotherday(Integer value) {
	    final Calendar cal = Calendar.getInstance();
	    cal.add(Calendar.DATE,value);
	    return cal.getTime();
	}
	
	

	public void escribirArchivo(Map<String, Integer> lista) throws IOException {
		FileWriter fichero = null;
		PrintWriter pw = null;
		try {
			fichero = new FileWriter("C:\\opt\\thingspeak\\write\\report.csv");
			pw = new PrintWriter(fichero);
			pw.println("valor promedio Dia: " + new PromedioFechaAnalisis(listaCruce).hallarPasoPromedioDia());
			pw.println("valor promedio Mes: " + new PromedioFechaAnalisis(listaCruce).hallarPasoPromedioMes());
			pw.println("valor promedio Año: " + new PromedioFechaAnalisis(listaCruce).hallarPasoPromedioYear());
			for (Entry<String, Integer> d : lista.entrySet()) {
				pw.print("Fecha : " + d.getKey());
				pw.println("valor : " + d.getValue());
			}
		} catch (IOException e) {
			System.out.println(e.getMessage());
		} finally {
			if (null != fichero) {
				fichero.close();
			}
		}
	}

}
