/*
 *  eXist Open Source Native XML Database
 *  Copyright (C) 2011 The eXist Project
 *  http://exist-db.org
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
package org.expath.exist.ftclient;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;

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
import org.exist.xquery.value.BooleanValue;
import org.exist.xquery.value.FunctionParameterSequenceType;
import org.exist.xquery.value.FunctionReturnSequenceType;
import org.exist.xquery.value.IntegerValue;
import org.exist.xquery.value.Sequence;
import org.exist.xquery.value.SequenceType;
import org.exist.xquery.value.Type;

/**
 * Implements a method for storing a resource to a remote directory.
 * 
 * @author WStarcev
 * @author Adam Retter <adam@existsolutions.com>
 * @author Claudius Teodorescu <claudius.teodorescu@gmail.com>
 */
public class StoreResourceFunction extends BasicFunction {
	private static final Logger log = Logger.getLogger(StoreResourceFunction.class);

	private static final String NAMESPACE_URI = ExistExpathFTClientModule.NAMESPACE_URI;

	public final static FunctionSignature signature = new FunctionSignature(
			new QName("store-resource", NAMESPACE_URI, ExistExpathFTClientModule.PREFIX),
			"This function stores a resource to the remote path. It returns true or false indicating the success of the resource storage.",
			new SequenceType[] {
					new FunctionParameterSequenceType("connection-handle", Type.LONG,
							Cardinality.EXACTLY_ONE, "The connection handle."),
					new FunctionParameterSequenceType("remote-resource-path", Type.STRING,
							Cardinality.EXACTLY_ONE, "The path for resource to be stored."),
					new FunctionParameterSequenceType("resource-contents", Type.ANY_TYPE,
							Cardinality.ZERO_OR_ONE, "The contents of the resource.") },
			new FunctionReturnSequenceType(Type.BOOLEAN, Cardinality.EXACTLY_ONE,
					"It returns true if successfully completed, false if not."));

	public StoreResourceFunction(XQueryContext context) {
		super(context, signature);
	}

	@Override
	public Sequence eval(Sequence[] args, Sequence contextSequence) throws XPathException {

		Boolean result = true;

		int resourceType = (args[2].isEmpty()) ? 10 : args[2].itemAt(0).getType();

		BinaryValue data = null;

		switch (resourceType) {
		// xs:base64Binary
		case Type.BASE64_BINARY:
			// xs:hexBinary
		case Type.HEX_BINARY:
			byte[] binary = (byte[]) ((BinaryValue) args[2].itemAt(0)).toJavaObject(byte[].class);
			data = BinaryValueFromInputStream.getInstance(context, new Base64BinaryValueType(),
					new ByteArrayInputStream(binary));

			break;
		// xs:string
		case Type.STRING:
			String dataString = args[2].itemAt(0).getStringValue();
			try {
				data = BinaryValueFromInputStream.getInstance(context, new Base64BinaryValueType(),
						new ByteArrayInputStream(dataString.getBytes("UTF-8")));
			} catch (UnsupportedEncodingException e) {
			}
			break;
		// ()
		case Type.EMPTY:
			try {
				data = BinaryValueFromInputStream.getInstance(context, new Base64BinaryValueType(),
						new ByteArrayInputStream("".getBytes("UTF-8")));
			} catch (UnsupportedEncodingException e) {
			}
			break;
		}
		// store the resource
		try {
			result = ro.kuberam.libs.java.ftclient.StoreResource.storeResource(
					ExistExpathFTClientModule.retrieveRemoteConnection(context,
							((IntegerValue) args[0].itemAt(0)).getLong()), args[1].getStringValue(),
					data.getInputStream());
		} catch (Exception ex) {
			throw new XPathException(ex.getMessage());
		}

		return BooleanValue.valueOf(result);
	}
}