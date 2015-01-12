/*
 *  Copyright (C) 2011 Claudius Teodorescu
 *
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public License
 *  as published by the Free Software Foundation; either version 2
 *  of the License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public
 *  License along with this library; if not, write to the Free Software
 *  Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 *
 *  $Id$
 */

package org.expath.exist.pdf.stamp;

/**
 * Implements the pdf:stamp() function for eXist.
 * 
 * @author Claudius Teodorescu <claudius.teodorescu@gmail.com>
 */

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import org.apache.log4j.Logger;
import org.exist.dom.QName;
import org.exist.xquery.BasicFunction;
import org.exist.xquery.Cardinality;
import org.exist.xquery.FunctionSignature;
import org.exist.xquery.XPathException;
import org.exist.xquery.XQueryContext;
import org.exist.xquery.value.Base64BinaryValueType;
import org.exist.xquery.value.BinaryValue;
import org.exist.xquery.value.BinaryValueFromInputStream;
import org.exist.xquery.value.FunctionParameterSequenceType;
import org.exist.xquery.value.FunctionReturnSequenceType;
import org.exist.xquery.value.Sequence;
import org.exist.xquery.value.SequenceType;
import org.exist.xquery.value.Type;
import org.exist.xquery.value.ValueSequence;
import org.expath.exist.pdf.ExistExpathPdfModule;

import ro.kuberam.libs.java.pdf.stamp.StringStamper;

public class StampFunction extends BasicFunction {

	private final static Logger log = Logger.getLogger(StampFunction.class);

	public final static FunctionSignature signatures[] = {
			new FunctionSignature(new QName("stamp", ExistExpathPdfModule.NAMESPACE_URI,
					ExistExpathPdfModule.PREFIX), "Stamps PDF contents.", new SequenceType[] {
					new FunctionParameterSequenceType("contents", Type.BASE64_BINARY,
							Cardinality.ZERO_OR_ONE, "The PDF contents whose pages are to be stamped."),
					new FunctionParameterSequenceType("stamp", Type.ITEM, Cardinality.EXACTLY_ONE,
							"The stamp to be applied to the PDF contents."),
					new FunctionParameterSequenceType("stamp-styling", Type.STRING,
							Cardinality.EXACTLY_ONE, "The CSS styling for the current stamp.") },
					new FunctionReturnSequenceType(Type.BASE64_BINARY, Cardinality.ZERO_OR_ONE,
							"stamped PDF contents.")),
			new FunctionSignature(new QName("stamp", ExistExpathPdfModule.NAMESPACE_URI,
					ExistExpathPdfModule.PREFIX), "Stamps PDF contents.", new SequenceType[] {
					new FunctionParameterSequenceType("contents", Type.BASE64_BINARY,
							Cardinality.ZERO_OR_ONE, "The PDF contents whose pages are to be stamped."),
					new FunctionParameterSequenceType("stamp", Type.ITEM, Cardinality.EXACTLY_ONE,
							"The stamp to be applied to the PDF contents."),
					new FunctionParameterSequenceType("stamp-selector", Type.STRING,
							Cardinality.EXACTLY_ONE, "The CSS selector used to match the current stamp."),
					new FunctionParameterSequenceType("stamp-styling", Type.STRING,
							Cardinality.EXACTLY_ONE, "The CSS styling for the current stamp.") },
					new FunctionReturnSequenceType(Type.BASE64_BINARY, Cardinality.ZERO_OR_ONE,
							"stamped PDF contents.")) };

	public StampFunction(XQueryContext context, FunctionSignature signature) {
		super(context, signature);
	}

	@Override
	public Sequence eval(Sequence[] args, Sequence contextSequence) throws XPathException {
		Sequence result = new ValueSequence();

		byte[] pdfContentsAsBytes = (byte[]) ((BinaryValue) args[0].itemAt(0)).toJavaObject(byte[].class);
		InputStream pdfContentsAsIs = BinaryValueFromInputStream.getInstance(context,
				new Base64BinaryValueType(), new ByteArrayInputStream(pdfContentsAsBytes)).getInputStream();
		String stamp = args[1].getStringValue();
		String stampStyling = "";

		StringStamper stamper = null;
		ByteArrayOutputStream resultAsStreamResult = new ByteArrayOutputStream();

		if (args.length == 3) {
			stampStyling = args[2].getStringValue();
			stamper = new StringStamper(pdfContentsAsIs, stamp, stampStyling);
		} else {
			String stampSelector = args[2].getStringValue();
			stampStyling = args[3].getStringValue();
			stamper = new StringStamper(pdfContentsAsIs, stamp, stampSelector, stampStyling);
		}

		try {
			resultAsStreamResult = stamper.stamp();
		} catch (Exception ex) {
			throw new XPathException(ex.getMessage());
		}

		result.add(BinaryValueFromInputStream.getInstance(context, new Base64BinaryValueType(),
				new ByteArrayInputStream(resultAsStreamResult.toByteArray())));

		return result;
	}
}