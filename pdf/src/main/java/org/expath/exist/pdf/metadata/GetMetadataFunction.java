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
 * Implements the pdf:get-metadata() function for eXist.
 * 
 * @author Claudius Teodorescu <claudius.teodorescu@gmail.com>
 */

import java.io.ByteArrayInputStream;
import java.util.Map;

import org.apache.log4j.Logger;
import org.exist.dom.QName;
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
import org.exist.xquery.value.IntegerValue;
import org.exist.xquery.value.Sequence;
import org.exist.xquery.value.SequenceType;
import org.exist.xquery.value.StringValue;
import org.exist.xquery.value.Type;
import org.exist.xquery.value.ValueSequence;
import org.expath.exist.pdf.ExistExpathPdfModule;

import ro.kuberam.libs.java.pdf.metadata.GetMetadata;

public class GetMetadataFunction extends BasicFunction {

	private final static Logger log = Logger.getLogger(GetMetadataFunction.class);

	public final static FunctionSignature signature = new FunctionSignature(new QName("get-metadata",
			ExistExpathPdfModule.NAMESPACE_URI, ExistExpathPdfModule.PREFIX),
			"Get all the metadata of a PDF contents.",
			new SequenceType[] { new FunctionParameterSequenceType("contents", Type.BASE64_BINARY,
					Cardinality.ZERO_OR_ONE, "PDF contents where to get the metadata from.") },
			new FunctionReturnSequenceType(Type.MAP, Cardinality.ZERO_OR_ONE,
					"a map containing pairs of fully qualified name and value for each metadata."));

	public GetMetadataFunction(XQueryContext context, FunctionSignature signature) {
		super(context, signature);
	}

	@Override
	public Sequence eval(Sequence[] args, Sequence contextSequence) throws XPathException {
		Sequence result = new ValueSequence();

		try {
			Map<String, Object> resultMap = null;
			MapType resultXqueryMap = new MapType(this.context, null);

			byte[] binary = (byte[]) ((BinaryValue) args[0].itemAt(0)).toJavaObject(byte[].class);
			BinaryValue data = BinaryValueFromInputStream.getInstance(context, new Base64BinaryValueType(),
					new ByteArrayInputStream(binary));
			resultMap = GetMetadata.documentProperties(data.getInputStream());

			log.debug("The EXPath PDF module got the following text fields: " + resultMap);

			for (Map.Entry<String, Object> resultEntry : resultMap.entrySet()) {
				StringValue key = new StringValue(resultEntry.getKey());
				Object value = resultEntry.getValue();

				if (value instanceof String) {
					resultXqueryMap.add(key, new StringValue((String) value));
				}
				
				if (value instanceof Integer) {
					resultXqueryMap.add(key, new IntegerValue((Integer) value));
				}
			}

			result.add(resultXqueryMap);
		} catch (Exception ex) {
			throw new XPathException(ex.getMessage());
		}

		return result;
	}
}