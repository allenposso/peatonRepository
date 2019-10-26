package cruce;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

/**
 *
 * @author dasting allan
 */
public class Grafica extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	ArrayList<Cruce> lista;
	ChartPanel chartActual;
	JLabel layoutActual;
	JButton year = new JButton();
	JButton dia = new JButton();
	JButton mes = new JButton();
	JButton guardar = new JButton();
	JButton reload = new JButton();
	Map<String, Integer> listaActual;
	SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

	public Grafica() {
		setTitle("Gráfica Cruce");
		setSize(720, 600);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
		setResizable(false);
		JPanel panel = new JPanel();
		panel.setBackground(new java.awt.Color(51, 51, 51));
		getContentPane().add(panel);
		lista = new GetData().obtenerParametros();
		Double value = new PromedioFechaAnalisis(lista).hallarPasoPromedioDia();
		graficarDatos(panel, procesarLista(), "total", value);
	}

	private Map<String, Integer> procesarLista() {
		Map<String, Integer> nuevaLista = new HashMap<>();
		lista.forEach(e -> {
			String val = formatter.format(e.getMomento());
			nuevaLista.put(val, e.getCruzo() ? 1 : 0);
		});

		listaActual = nuevaLista;
		return nuevaLista;
	}

	private void setButtons(JPanel panel) {

		guardar.setBackground(new java.awt.Color(51, 153, 255));
		guardar.setForeground(new java.awt.Color(255, 255, 255));
		guardar.setText("guardar archivo");
		guardar.setBorderPainted(false);
		Dimension d = new Dimension();
		d.setSize(150, 40);
		guardar.setPreferredSize(d);

		guardar.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				guardarAction();
			}
		});

		panel.add(guardar);

		dia.setBackground(new java.awt.Color(51, 153, 255));
		dia.setForeground(new java.awt.Color(255, 255, 255));
		dia.setText("Cruces por Dia");
		dia.setBorderPainted(false);
		Dimension d1 = new Dimension();
		d1.setSize(130, 40);
		dia.setPreferredSize(d1);

		dia.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				diaAction(panel);
			}
		});
		panel.add(dia);

		mes.setBackground(new java.awt.Color(51, 153, 255));
		mes.setForeground(new java.awt.Color(255, 255, 255));
		mes.setText("Cruces por Mes");
		mes.setBorderPainted(false);
		mes.setPreferredSize(d1);

		dia.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				mesesAction(panel);
			}
		});
		panel.add(mes);

		year.setBackground(new java.awt.Color(51, 153, 255));
		year.setForeground(new java.awt.Color(255, 255, 255));
		year.setText("Cruces por Año");
		year.setBorderPainted(false);
		year.setPreferredSize(d1);

		year.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				yearAction(panel);
			}
		});
		panel.add(year);

		reload.setBackground(new java.awt.Color(51, 153, 255));
		reload.setForeground(new java.awt.Color(255, 255, 255));
		reload.setText("reload");
		reload.setBorderPainted(false);
		Dimension d2 = new Dimension();
		d2.setSize(100, 40);
		reload.setPreferredSize(d2);

		reload.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				reloadAction(panel);
			}
		});
		panel.add(reload);
	}

	private void guardarAction() {
		try {
			new Procesamiento(lista).escribirArchivo(listaActual);
			setMsg("el guardado ha sido exitoso", true);
		} catch (Exception e) {
			setMsg("ha ocurrido un error al guardar", false);
			System.out.println(e.getMessage());
		}

	}

	private void reloadAction(JPanel panel) {
		lista = new GetData().obtenerParametros();
		double value = new PromedioFechaAnalisis(lista).hallarPasoPromedioDia();
		graficarDatos(panel, procesarLista(), "total", value);
	}

	private void diaAction(JPanel panel) {
		Map<String, Integer> ld = new Procesamiento(lista).procesarListaDay();
		double value = new PromedioFechaAnalisis(lista).hallarPasoPromedioDia();
		graficarDatos(panel, ld, "dias", value);
	}

	private void mesesAction(JPanel panel) {
		Map<String, Integer> lm = new Procesamiento(lista).procesarListaMonth();
		lm.keySet().forEach(System.out::println);
		double value = new PromedioFechaAnalisis(lista).hallarPasoPromedioMes();
		graficarDatos(panel, lm, "meses", value);
	}

	private void yearAction(JPanel panel) {
		Map<String, Integer> ly = new Procesamiento(lista).procesarListaYear();
		double value = new PromedioFechaAnalisis(lista).hallarPasoPromedioYear();
		graficarDatos(panel, ly, "años", value);
	}

	private void graficarDatos(JPanel panel, Map<String, Integer> data, String info, Double value) {
		if (chartActual != null) {
			panel.remove(chartActual);
			panel.remove(layoutActual);
			panel.remove(year);
			panel.remove(dia);
			panel.remove(mes);
			panel.remove(guardar);
			panel.remove(reload);

			year = new JButton();
			dia = new JButton();
			mes = new JButton();
			guardar = new JButton();
			reload = new JButton();
		}
		DefaultCategoryDataset line_chart_dataset = new DefaultCategoryDataset();
		line_chart_dataset.clear();
		data.entrySet().forEach(d -> {
			String fecha = d.getKey();
			int valores = d.getValue();
			line_chart_dataset.addValue(valores, "cruze", fecha);
		});

		JFreeChart chart = ChartFactory.createLineChart("cruces leidos de sensor ULTRASONIDO", "Fecha DD/MM/YYYY",
				"valor cruzo? 1/true , 0/false", line_chart_dataset, PlotOrientation.VERTICAL, true, true, false);

		// Mostrar Grafico
		ChartPanel chartPanel = new ChartPanel(chart);
		chartActual = chartPanel;
		JLabel lay = new JLabel();
		lay.setText("Promedio " + info + " : " + value);
		lay.setForeground(new java.awt.Color(255, 255, 255));
		layoutActual = lay;
		panel.add(lay);
		panel.add(chartPanel);

		setButtons(panel);
		this.setVisible(true);
	}

	public static void setMsg(String text, boolean exitoso) {
		Toolkit.getDefaultToolkit().beep();
		JOptionPane optionPane = new JOptionPane(text,
				exitoso ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE);
		JDialog dialog = optionPane.createDialog("guardado");
		dialog.setAlwaysOnTop(true);
		dialog.setVisible(true);
	}

	public static void main(String[] args) {
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				new Grafica();
			}
		});
	}
}
