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

package org.expath.exist.pdf.metadata;

/**
 * Implements the get-content-metadata() function for eXist.
 * 
 * @author W.S. Hager <wshager@gmail.com>
 */

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.xml.transform.stream.StreamResult;

import org.apache.log4j.Logger;
import org.exist.dom.QName;
import org.exist.xquery.modules.ModuleUtils;
import org.exist.xquery.BasicFunction;
import org.exist.xquery.Cardinality;
import org.exist.xquery.FunctionSignature;
import org.exist.xquery.XPathException;
import org.exist.xquery.XQueryContext;
import org.exist.xquery.functions.map.MapType;
import org.exist.xquery.value.Base64BinaryValueType;
import org.exist.xquery.value.BinaryValue;
import org.exist.xquery.value.BinaryValueFromInputStream;
import org.exist.xquery.value.FunctionParameterSequenceType;
import org.exist.xquery.value.FunctionReturnSequenceType;
import org.exist.xquery.value.Sequence;
import org.exist.xquery.value.SequenceType;
import org.exist.xquery.value.StringValue;
import org.exist.xquery.value.Type;
import org.exist.xquery.value.ValueSequence;
import org.exist.xquery.value.NodeValue;
import org.expath.exist.pdf.ExistExpathPdfModule;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import ro.kuberam.libs.java.pdf.metadata.Metadata;

public class GetContentMetadataFunction extends BasicFunction {

	private final static Logger log = Logger.getLogger(GetContentMetadataFunction.class);

	public final static FunctionSignature signatures[] = {
		new FunctionSignature(new QName("get-content-metadata",
			ExistExpathPdfModule.NAMESPACE_URI, ExistExpathPdfModule.PREFIX),
			"Get the XMP metadata from a PDF contents.",
			new SequenceType[] { new FunctionParameterSequenceType("contents", Type.BASE64_BINARY,
					Cardinality.ZERO_OR_ONE, "PDF contents where to get the metadata from.") },
			new FunctionReturnSequenceType(Type.NODE, Cardinality.ZERO_OR_ONE,
					"an XML document containing XMP metadata.")),
		new FunctionSignature(new QName("get-content-metadata",
			ExistExpathPdfModule.NAMESPACE_URI, ExistExpathPdfModule.PREFIX),
			"Get document information metadata attributes from a PDF contents.",
			new SequenceType[] { new FunctionParameterSequenceType("contents", Type.BASE64_BINARY,
					Cardinality.ZERO_OR_ONE, "PDF contents where to get the metadata from.") },
			new SequenceType[] { new FunctionParameterSequenceType("properties", Type.STRING,
					Cardinality.ONE_OR_MORE, "List of properties to retrieve.") },
			new FunctionReturnSequenceType(Type.STRING, Cardinality.ONE_OR_MORE,
					"a map containing document information metadata."))
	};

	public GetContentMetadataFunction(XQueryContext context, FunctionSignature signature) {
		super(context, signature);
	}

	@Override
	public Sequence eval(Sequence[] args, Sequence contextSequence) throws XPathException {
		
		Sequence result = new ValueSequence();
		
		String resultAsString = null;
		
		try {
			byte[] binary = (byte[]) ((BinaryValue) args[0].itemAt(0)).toJavaObject(byte[].class);
			BinaryValue data = BinaryValueFromInputStream.getInstance(context, new Base64BinaryValueType(),
					new ByteArrayInputStream(binary));
		} catch (Exception ex) {
			throw new XPathException(ex.getMessage());
		}
		if (args.length == 1) {
			try {
				resultAsString = Metadata.get-content-xmp(data.getInputStream());
			} catch (Exception ex) {
				throw new XPathException(ex.getMessage());
			}
			try {
				result = (NodeValue) ModuleUtils.stringToXML(context,resultAsString);
			} catch (SAXException saxe) {
				// do nothing, we will default to trying to return a string below
			} catch (IOException ioe) {
				// do nothing, we will default to trying to return a string below
			}
		} else if (args.length == 2) {
			
		}
		
		return result;
	}
}