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

package org.expath.exist.crypto.digest;

/**
 * Implements the crypto:hash() function for eXist.
 * 
 * @author Claudius Teodorescu <claudius.teodorescu@gmail.com>
 */

import java.io.ByteArrayInputStream;

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
import org.exist.xquery.value.StringValue;
import org.exist.xquery.value.Type;
import org.expath.exist.crypto.ExistExpathCryptoModule;

import ro.kuberam.libs.java.crypto.digest.Hash;

public class HashFunction extends BasicFunction {

	private final static Logger logger = Logger.getLogger(HashFunction.class);

	public final static FunctionSignature signatures[] = {
			new FunctionSignature(new QName("hash", ExistExpathCryptoModule.NAMESPACE_URI,
					ExistExpathCryptoModule.PREFIX), "Digests the input data.", new SequenceType[] {
					new FunctionParameterSequenceType("data", Type.ANY_TYPE, Cardinality.EXACTLY_ONE,
							"The data to be hashed."),
					new FunctionParameterSequenceType("algorithm", Type.STRING, Cardinality.EXACTLY_ONE,
							"The cryptographic hashing algorithm.") }, new FunctionReturnSequenceType(
					Type.BYTE, Cardinality.ONE_OR_MORE, "resulting hash value, as string.")),
			new FunctionSignature(
					new QName("hash", ExistExpathCryptoModule.NAMESPACE_URI, ExistExpathCryptoModule.PREFIX),
					"Digests the input data.",
					new SequenceType[] {
							new FunctionParameterSequenceType("data", Type.ANY_TYPE,
									Cardinality.EXACTLY_ONE, "The data to be hashed."),
							new FunctionParameterSequenceType("algorithm", Type.STRING,
									Cardinality.EXACTLY_ONE, "The cryptographic hashing algorithm."),
							new FunctionParameterSequenceType("format", Type.STRING,
									Cardinality.EXACTLY_ONE,
									"The format of the output. The legal values are \"hex\" and \"base64\". The default value is \"base64\".") },
					new FunctionReturnSequenceType(Type.BYTE, Cardinality.ONE_OR_MORE,
							"resulting hash value, as string.")) };

	public HashFunction(XQueryContext context, FunctionSignature signature) {
		super(context, signature);
	}

	@Override
	public Sequence eval(Sequence[] args, Sequence contextSequence) throws XPathException {
		String resultString = null;

		int inputType = args[0].itemAt(0).getType();
		String hashAlgorithm = args[1].getStringValue();
		String format = "base64";
		if (args.length == 3) {
			format = args[2].getStringValue();
		}

		if (inputType == Type.STRING || inputType == Type.ELEMENT || inputType == Type.DOCUMENT) {
			try {
				resultString = Hash.hashString(args[0].getStringValue(), hashAlgorithm, format);
			} catch (Exception ex) {
				throw new XPathException(ex.getMessage());
			}
		} else if (inputType == Type.BASE64_BINARY || inputType == Type.HEX_BINARY) {
			try {
				byte[] binary = (byte[]) ((BinaryValue) args[0].itemAt(0)).toJavaObject(byte[].class);
				BinaryValue data = BinaryValueFromInputStream.getInstance(context,
						new Base64BinaryValueType(), new ByteArrayInputStream(binary));
				resultString = Hash.hashBinary(data.getInputStream(), hashAlgorithm, format);
			} catch (Exception ex) {
				throw new XPathException(ex.getMessage());
			}
		}
		// ValueSequence result = new ValueSequence();
		// for (int i = 0, il = resultBytes.length; i < il; i++) {
		// result.add(new IntegerValue(resultBytes[i]));
		// }

		return new StringValue(resultString);
	}
}