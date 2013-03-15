package org.knoesis.utils;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Commons {

		public static String formatDate(Calendar date, String format) {
			DateFormat dateFormat = new SimpleDateFormat(format);
			return dateFormat.format(date.getTime());
		}
		
		public static String formatDouble(Double number, String format) {
			DecimalFormat df = new DecimalFormat(format);
			return df.format(number);
		}
}