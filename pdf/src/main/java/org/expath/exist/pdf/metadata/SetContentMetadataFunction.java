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
 * Implements the pdf:set-text-fields() function for eXist.
 * 
 * @author Claudius Teodorescu <claudius.teodorescu@gmail.com>
 */

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.exist.dom.QName;
import org.exist.xquery.BasicFunction;
import org.exist.xquery.Cardinality;
import org.exist.xquery.FunctionSignature;
import org.exist.xquery.XPathException;
import org.exist.xquery.XQueryContext;
import org.exist.xquery.functions.map.AbstractMapType;
import org.exist.xquery.value.AtomicValue;
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

import org.exist.storage.serializers.Serializer;
import org.exist.validation.internal.node.NodeInputStream;
import org.exist.xquery.value.NodeValue;

import ro.kuberam.libs.java.pdf.metadata.Metadata;

public class SetContentMetadataFunction extends BasicFunction {

	private final static Logger log = Logger.getLogger(SetContentMetadataFunction.class);

	public final static FunctionSignature signature = new FunctionSignature(new QName("set-content-metadata",
			ExistExpathPdfModule.NAMESPACE_URI, ExistExpathPdfModule.PREFIX),
			"Set the XMP metadata of a PDF contents.", new SequenceType[] {
					new FunctionParameterSequenceType("contents", Type.BASE64_BINARY,
							Cardinality.ZERO_OR_ONE, "the PDF contents where to set the metadata to."),
					new FunctionParameterSequenceType("text-fields", Type.NODE, Cardinality.ZERO_OR_ONE,
							"the information sets about the text fields, namely a map containing pairs of fully qualified name"
									+ "and value for each text field to be set.") },
			new FunctionReturnSequenceType(Type.BASE64_BINARY, Cardinality.ZERO_OR_MORE,
					"A new PDF file with the new metadata inserted."));

	public SetContentMetadataFunction(XQueryContext context, FunctionSignature signature) {
		super(context, signature);
	}

	@Override
	public Sequence eval(Sequence[] args, Sequence contextSequence) throws XPathException {
		byte[] binary = (byte[]) ((BinaryValue) args[0].itemAt(0)).toJavaObject(byte[].class);
		Serializer serializer = context.getBroker().getSerializer();
		NodeValue inputNode = (NodeValue) args[1].itemAt(0);
		InputStream inputNodeStream = new NodeInputStream(serializer, inputNode);

		Sequence result = new ValueSequence();

		ByteArrayOutputStream resultAsStreamResult = new ByteArrayOutputStream();

		try {
			BinaryValue data = BinaryValueFromInputStream.getInstance(context, new Base64BinaryValueType(),
					new ByteArrayInputStream(binary));

			resultAsStreamResult = Metadata.run(data.getInputStream(), inputNodeStream);

			result.add(BinaryValueFromInputStream.getInstance(context, new Base64BinaryValueType(),
					new ByteArrayInputStream(resultAsStreamResult.toByteArray())));

		} catch (Exception ex) {
			throw new XPathException(ex.getMessage());
		}

		return result;
	}
}