package br.gov.caixa.discovery.ws.handlers;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class CalendarAdapter extends XmlAdapter<String, Calendar> {

	private SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

	@Override
	public String marshal(Calendar v) throws Exception {
		return dateFormat.format(v.getTime());
	}

	@Override
	public Calendar unmarshal(String v) throws Exception {
		Calendar cal = Calendar.getInstance();
		cal.setTime(dateFormat.parse(v));
		return cal;
	}

}
