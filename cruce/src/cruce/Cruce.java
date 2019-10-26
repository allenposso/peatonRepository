package cruce;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.Date;

/**
 *
 * @author martavelasquez
 */
public class Cruce {

	private Boolean cruzo;
	private Date momento;


	public Cruce(Boolean cruzo, Date momento) {
		this.cruzo = cruzo;
		this.momento = momento;
	}

	

	public Boolean getCruzo() {
		return cruzo;
	}

	public void setCruzo(Boolean cruzo) {
		this.cruzo = cruzo;
	}

	public Date getMomento() {
		return momento;
	}

	public void setMomento(Date momento) {
		this.momento = momento;
	}

}
