package cruce;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.stream.Collectors;

public class PromedioFechaAnalisis {
	ArrayList<Cruce> listaCruce;
	public PromedioFechaAnalisis(ArrayList<Cruce> listaCruce) {
		this.listaCruce = listaCruce;
	}
	public double hallarPasoPromedioFecha(LocalDate fechaInicio, LocalDate fechaFin) {
		return listaCruce.stream().filter(e -> {
			return dateToLocalDate(e.getMomento()).isAfter(fechaInicio)
					&& dateToLocalDate(e.getMomento()).isBefore(fechaFin);
		}).filter(e -> e.getCruzo()).collect(Collectors.toList()).size() - 1;
	}

	public double hallarPasoPromedioDia() {
		int val = (filterListByDateDay(listaCruce).size() >= 2) ? filterListByDateDay(listaCruce).size() - 1
				: filterListByDateDay(listaCruce).size();
		return filterListByDateDay(listaCruce).stream().map(this::countValuesDay).mapToDouble(value -> value).sum() / val;
	}

	public double hallarPasoPromedioMes() {
		int val = (filterListByDateMonth(listaCruce).size() >= 2) ? filterListByDateMonth(listaCruce).size() - 1
				: filterListByDateMonth(listaCruce).size();
		return filterListByDateMonth(listaCruce).stream().map(this::countValuesMonth).mapToDouble(value -> value).sum()
				/ val;
	}

	public double hallarPasoPromedioYear() {
		int val = (filterListByDateYear(listaCruce).size() >= 2) ? filterListByDateYear(listaCruce).size() - 1
				: filterListByDateYear(listaCruce).size();

		return filterListByDateYear(listaCruce).stream().map(this::countValuesYear).mapToDouble(value -> value).sum() / val;
	}
	
	public ArrayList<Date> filterListByDateDay(ArrayList<Cruce> list) {
		ArrayList<Date> newl = new ArrayList<>();
		ArrayList<Integer> prl = new ArrayList<>();
		list.stream().filter((element) -> !prl.contains(dateToLocalDate(element.getMomento()).getYear())).forEach(e -> {
			prl.add(dateToLocalDate(e.getMomento()).getYear());
			newl.add(e.getMomento());
		});
		return newl;
	}

	public ArrayList<Date> filterListByDateYear(ArrayList<Cruce> list) {
		ArrayList<Date> newl = new ArrayList<>();
		ArrayList<Integer> prl = new ArrayList<>();
		list.stream().filter((element) -> !prl.contains(dateToLocalDate(element.getMomento()).getYear())).forEach(e -> {
			prl.add(dateToLocalDate(e.getMomento()).getYear());
			newl.add(e.getMomento());
		});
		return newl;
	}

	public ArrayList<Date> filterListByDateMonth(ArrayList<Cruce> list) {
		ArrayList<Date> newl = new ArrayList<>();
		ArrayList<Integer> prl = new ArrayList<>();
		list.stream().filter((element) -> !prl.contains(dateToLocalDate(element.getMomento()).getMonthValue())).forEach(e -> {
			prl.add(dateToLocalDate(e.getMomento()).getMonthValue());
			newl.add(e.getMomento());
		});
		return newl;
	}
	
	public int countValuesDay(Date stamp) {
		return listaCruce.stream()
				.filter(e -> dateToLocalDate(e.getMomento()).getDayOfMonth() == dateToLocalDate(stamp).getDayOfMonth())
				.filter(e -> e.getCruzo()).collect(Collectors.toList()).size() - 1;
	}

	public int countValuesMonth(Date stamp) {
		return listaCruce.stream()
				.filter(e -> dateToLocalDate(e.getMomento()).getMonth() == dateToLocalDate(stamp).getMonth())
				.filter(e -> e.getCruzo()).collect(Collectors.toList()).size() - 1;
	}

	public int countValuesYear(Date stamp) {
		return listaCruce.stream()
				.filter(e -> dateToLocalDate(e.getMomento()).getYear() == dateToLocalDate(stamp).getYear())
				.filter(e -> e.getCruzo()).collect(Collectors.toList()).size() - 1;
	}

	public LocalDate dateToLocalDate(Date dateToConvert) {
		return dateToConvert.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
	}
}
