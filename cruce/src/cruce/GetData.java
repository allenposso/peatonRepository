package cruce;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GetData {

	static final String separador = ";";
	static final String saltoLinea = "\"";
	static SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

	private String leerPaginaWeb() {
		String code = "";
		try {
			StringBuilder codeBuffered = new StringBuilder();

			URL url = new URL("https://thingspeak.com/channels/870220EI/field/2.json");
			BufferedReader read;
			try (InputStream in = url.openStream()) {
				read = new BufferedReader(new InputStreamReader(in));
				String line;
				int i = 0;
				while ((line = read.readLine()) != null) {
					codeBuffered.append(line).append("\n");

				}
				code = codeBuffered.toString();
			}
			read.close();
		} catch (IOException e) {
		}
		return code;
	}

	public ArrayList<Cruce> obtenerParametros() {
		ArrayList<Cruce> listaCruce = new ArrayList<Cruce>();
		try {

			JSONObject obj = new JSONObject(leerPaginaWeb());
			JSONArray arr = obj.getJSONArray("feeds");
			for (int i = 0; i < arr.length(); i++) {
				boolean validate1 = !arr.getJSONObject(i).getString("field2").equals("null") && arr.getJSONObject(i).getString("field2") != null;
				boolean validate2 = !arr.getJSONObject(i).getString("created_at").equals("null") && arr.getJSONObject(i).getString("created_at") != null;
				boolean validate3 = arr.getJSONObject(i) != null;
				
				if (validate3 && validate1 && validate2) {
					String fecha = arr.getJSONObject(i).getString("created_at");
					Boolean data = arr.getJSONObject(i).getString("field2").equals("1");
					listaCruce.add(new Cruce(data, parseDate(fecha)));
				}
			}
		} catch (JSONException e) {
			System.out.println("No se puede leer la p·gina web.");
			return new ArrayList<Cruce>();
		}
		return listaCruce;
	}
	
	private Date parseDate(String fecha) {
		try {
			String f = organice(fecha);
			Date ld = formatter.parse(f);
			return ld;
		} catch (Exception e) {
			return null;
		}

	}

	private String organice(String data) {
		data = data.replace("T", " ");
		data = data.replace("Z", "");
		String[] dar = data.substring(0, 10).split("-");
		String sl = "";
		for (int i = dar.length - 1; i >= 0; i--) {

			String l = (i == 0) ? "" : "-";
			sl = sl + dar[i] + l;

		}
		return sl +" "+ data.split(" ")[1];
	}

	public ArrayList<Cruce> leerArchivo() throws IOException {
		ArrayList<Cruce> listaCruce = new ArrayList<Cruce>();
		BufferedReader br = null;

		try {
			int i = 0;
			br = new BufferedReader(new FileReader("2.csv"));
			String line = br.readLine();
			while (null != line) {
				String[] fields = line.split(separador);
				fields = separarArchivo(fields);
				line = br.readLine();
				listaCruce = recibirParametros(listaCruce, fields[i]);

			}
			return listaCruce;
		} catch (IOException e) {
			System.out.println("No se encontr√≥ el archivo.");
			return null;
		} finally {
			if (null != br) {
				br.close();
			}
		}

	}

	private String[] separarArchivo(String[] fields) {

		String[] resultado = new String[fields.length];

		for (int i = 0; i < resultado.length; i++) {
			resultado[i] = fields[i].replaceAll("^" + saltoLinea, "").replaceAll(saltoLinea + "$", "");
		}
		return resultado;
	}

	private ArrayList<Cruce> recibirParametros(ArrayList<Cruce> listaCruce, String data) {
		if (!data.contains("Timestamp")) {
			String fecha = data.substring(data.indexOf(",") + 1, data.lastIndexOf(","));
			boolean valorData = data.substring(data.lastIndexOf(",") + 1).equals("1");
			listaCruce.add(new Cruce(valorData, parseDate(fecha)));
		}
		return listaCruce;
	}
}
