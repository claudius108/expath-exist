/*
 *  eXist Java Cryptographic Extension
 *  Copyright (C) 2010 Claudius Teodorescu at http://kuberam.ro
 *
 *  Released under LGPL License - http://gnu.org/licenses/lgpl.html.
 *
 */

package org.expath.exist.crypto.digest;

/**
 * Implements the module definition.
 * 
 * @author Claudius Teodorescu <claudius.teodorescu@gmail.com>
 */

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

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
import ro.kuberam.libs.java.crypto.digest.Hmac;

public class HmacFunction extends BasicFunction {

	private final static Logger logger = Logger.getLogger(HmacFunction.class);

	public final static FunctionSignature signatures[] = {
			new FunctionSignature(
					new QName("hmac", ExistExpathCryptoModule.NAMESPACE_URI, ExistExpathCryptoModule.PREFIX),
					"Hashes the input message.",
					new SequenceType[] {
							new FunctionParameterSequenceType("data", Type.STRING, Cardinality.EXACTLY_ONE,
									"The data to be authenticated. This parameter can be of type xs:string, xs:base64Binary, or xs:hexBinary."),
							new FunctionParameterSequenceType(
									"secret-key",
									Type.STRING,
									Cardinality.EXACTLY_ONE,
									"The secret key used for calculating the authentication code. This parameter can be of type xs:string, xs:base64Binary, or xs:hexBinary."),
							new FunctionParameterSequenceType("algorithm", Type.STRING,
									Cardinality.EXACTLY_ONE, "The cryptographic hashing algorithm.") },
					new FunctionReturnSequenceType(Type.STRING, Cardinality.EXACTLY_ONE,
							"hash-based message authentication code, as string.")),
			new FunctionSignature(
					new QName("hmac", ExistExpathCryptoModule.NAMESPACE_URI, ExistExpathCryptoModule.PREFIX),
					"Hashes the input message.",
					new SequenceType[] {
							new FunctionParameterSequenceType("data", Type.STRING, Cardinality.EXACTLY_ONE,
									"The data to be authenticated. This parameter can be of type xs:string, xs:base64Binary, or xs:hexBinary."),
							new FunctionParameterSequenceType(
									"secret-key",
									Type.STRING,
									Cardinality.EXACTLY_ONE,
									"The secret key used for calculating the authentication code. This parameter can be of type xs:string, xs:base64Binary, or xs:hexBinary."),
							new FunctionParameterSequenceType("algorithm", Type.STRING,
									Cardinality.EXACTLY_ONE, "The cryptographic hashing algorithm."),
							new FunctionParameterSequenceType("format", Type.STRING,
									Cardinality.EXACTLY_ONE,
									"The format of the output. The legal values are \"hex\" and \"base64\". The default value is \"base64\".") },
					new FunctionReturnSequenceType(Type.BASE64_BINARY, Cardinality.EXACTLY_ONE,
							"hash-based message authentication code, as string.")) };

	public HmacFunction(XQueryContext context, FunctionSignature signature) {
		super(context, signature);
	}

	@Override
	public Sequence eval(Sequence[] args, Sequence contextSequence) throws XPathException {
		String result = null;

		Sequence data = args[0];
		logger.debug("data = " + data.getStringValue());
		int dataType = args[0].itemAt(0).getType();
		logger.debug("dataType = " + dataType);
		byte[] processedData = string2byte(data, dataType);
		logger.debug("processedData = " + processedData);

		Sequence secretKey = args[1];
		logger.debug("secretKey = " + secretKey.getStringValue());
		int secretKeyType = args[1].itemAt(0).getType();
		logger.debug("secretKeyType = " + secretKeyType);
		byte[] processedSecretKey = string2byte(secretKey, secretKeyType);
		logger.debug("processedSecretKey = " + processedSecretKey);

		String algorithm = args[2].getStringValue();
		logger.debug("algorithm = " + algorithm);
		String format = "base64";
		if (args.length == 4) {
			format = args[3].getStringValue();
		}
		logger.debug("format = " + format);

		try {
			result = Hmac.hmac(processedData, processedSecretKey, algorithm, format);
		} catch (Exception ex) {
			throw new XPathException(ex.getMessage());
		}

		return new StringValue(result);
	}

	private byte[] string2byte(Sequence data, int datatype) throws XPathException {
		byte[] processedData = null;
		
		try {
			switch (datatype) {
			case Type.STRING:
			case Type.ELEMENT:
			case Type.DOCUMENT:

				processedData = data.getStringValue().getBytes(StandardCharsets.UTF_8);

				break;
			case Type.BASE64_BINARY:
				processedData = (byte[]) ((BinaryValue) data.itemAt(0)).toJavaObject(byte[].class);
				break;
			}
		} catch (Exception ex) {
			throw new XPathException(ex.getMessage());
		}
		
		return processedData;
	}
}
