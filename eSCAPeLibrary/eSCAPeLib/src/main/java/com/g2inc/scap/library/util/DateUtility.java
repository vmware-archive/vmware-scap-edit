package com.g2inc.scap.library.util;

/* ESCAPE Software Copyright 2010 G2, Inc. - All rights reserved.
 *
 * ESCAPE is open source software distributed under GNU General Public License Version 3.  ESCAPE is not in the public domain 
 * and G2, Inc. holds its copyright.  Redistribution and use in source and binary forms, with or without modification, are
 * permitted provided that the following conditions are met:

 * 1. Redistributions of ESCAPE source code must retain the above copyright notice, this list of conditions and the following disclaimer. 
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the ESCAPE Software distribution. 
 * 3. Neither the name of G2, Inc. nor the names of any contributors may be used to endorse or promote products derived from this software without specific prior written permission. 

 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS ``AS IS'' AND ANY EXPRESS OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL G2, INC., THE AUTHORS OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY,
 * OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
 * OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.

 * You should have received a copy of the GNU General Public License Version 3 along with this program. 
 * If not, see http://www.gnu.org/licenses/ for a copy.
 */

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.xml.bind.DatatypeConverter;

/**
 * Contains functions related to date and time processing.
 */
public class DateUtility {
	/**
	 * Take xsd:dateTime format string and return a Date object.
	 * 
	 * from xsd dateTime documentation. string will be of the following form:
	 * 
	 * [-]CCYY-MM-DDThh:mm:ss[Z|(+|-)hh:mm]
	 * 
	 * examples:
	 * 
	 * 2001-10-26T21:32:52 2001-10-26T21:32:52+02:00 2001-10-26T19:32:52Z
	 * 2001-10-26T19:32:52+00:00 -2001-10-26T21:32:52 2001-10-26T21:32:52.12679
	 * 
	 * @param xsdDateTime
	 *            A string in the form of an xsd:datetime
	 * 
	 * @return Date
	 */
	public static Date getDateFromXSDDateTime(String xsdDateTime) {
		Date d = null;
		Calendar c = null;

		try {
			c = DatatypeConverter.parseDateTime(xsdDateTime);
			d = c.getTime();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return d;
	}

	/**
	 * Take xsd:dateTime format string and return a Calendar object.
	 * 
	 * from xsd dateTime documentation. string will be of the following form:
	 * 
	 * [-]CCYY-MM-DDThh:mm:ss[Z|(+|-)hh:mm]
	 * 
	 * examples:
	 * 
	 * 2001-10-26T21:32:52 2001-10-26T21:32:52+02:00 2001-10-26T19:32:52Z
	 * 2001-10-26T19:32:52+00:00 -2001-10-26T21:32:52 2001-10-26T21:32:52.12679
	 * 
	 * @param xsdDateTime
	 *            A string in the form of an xsd:datetime
	 * 
	 * @return Calendar
	 */
	public static Calendar getCalendarFromXSDDateTime(String xsdDateTime) {
		Calendar c = null;

		try {
			c = DatatypeConverter.parseDateTime(xsdDateTime);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return c;
	}

	/**
	 * Take a Calendar object and return an xsd:dateTime format string.
	 * 
	 * @param calTime
	 *            An instance of a Calendar
	 * 
	 * @return String
	 */
	public static String getXSDDateTimeFromCalendar(Calendar calTime) {
		String ret = null;

		try {
			ret = DatatypeConverter.printDateTime(calTime);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return ret;
	}

	/**
	 * Take a Date object and return an xsd:dateTime format string.
	 * 
	 * @param date
	 *            A Date object
	 * 
	 * @return String
	 */
	public static String getXSDDateTimeFromDate(Date date) {
		String ret = null;

		try {
			Calendar c = Calendar.getInstance();
			c.setTime(date);

			ret = DatatypeConverter.printDateTime(c);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return ret;
	}

	/**
	 * Take a value of a cpe dictionary's generator timestamp and return a
	 * Calendar object.
	 * 
	 * @param cpeDateTime
	 *            A string in the form of yyyy-mm-ddThh:mm:ss
	 * 
	 * @return Calendar
	 */
	public static Calendar getCalendarFromCPEGeneratorDateTime(
			String cpeDateTime) {
		Calendar c = null;
		String dateFormatString = "yyyy-MM-dd'T'hh:mm:ss";

		try {
			SimpleDateFormat sdf = new SimpleDateFormat(dateFormatString);

			Date d = sdf.parse(cpeDateTime);
			c = Calendar.getInstance();
			c.setTime(d);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return c;
	}

	/**
	 * Take a value of a oval document's generator timestamp and return a
	 * Calendar object.
	 * 
	 * @param ovalDateTime
	 *            A string in the form of yyyy-mm-ddThh:mm:ss
	 * 
	 * @return Calendar
	 */
	public static Calendar getCalendarFromOVALGeneratorDateTime(
			String ovalDateTime) {
		Calendar c = null;
		String dateFormatString = "yyyy-MM-dd'T'hh:mm:ss";

		try {
			SimpleDateFormat sdf = new SimpleDateFormat(dateFormatString);

			Date d = sdf.parse(ovalDateTime);
			c = Calendar.getInstance();
			c.setTime(d);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return c;
	}

	/**
	 * Take a given Calendar object and return a string in the form of
	 * yyyy-mm-ddThh:mm:ss
	 * 
	 * @param calendar
	 * @return String
	 */
	public static String getOVALGeneratorDateTimeFromCalendar(Calendar calendar) {
		return getOVALGeneratorDateTimeFromDate(calendar.getTime());
	}

	/**
	 * Take a given Date object and return a string in the form of
	 * yyyy-mm-ddThh:mm:ss
	 * 
	 * @param date
	 * @return String
	 */
	public static String getOVALGeneratorDateTimeFromDate(Date date) {
		String ret = null;
		String dateFormatString = "yyyy-MM-dd'T'hh:mm:ss";

		try {
			SimpleDateFormat sdf = new SimpleDateFormat(dateFormatString);

			ret = sdf.format(date);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return ret;
	}

	/**
	 * Take a given Calendar object and return a string in the form of
	 * yyyy-mm-ddThh:mm:ss
	 * 
	 * @param calendar
	 * @return String
	 */
	public static String getCPEGeneratorDateTimeFromCalendar(Calendar calendar) {
		return getCPEGeneratorDateTimeFromDate(calendar.getTime());
	}

	/**
	 * Take a given Date object and return a string in the form of
	 * yyyy-mm-ddThh:mm:ss
	 * 
	 * @param date
	 * @return String
	 */
	public static String getCPEGeneratorDateTimeFromDate(Date date) {
		String ret = null;
		String dateFormatString = "yyyy-MM-dd'T'hh:mm:ss";

		try {
			SimpleDateFormat sdf = new SimpleDateFormat(dateFormatString);

			ret = sdf.format(date);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return ret;
	}
}
