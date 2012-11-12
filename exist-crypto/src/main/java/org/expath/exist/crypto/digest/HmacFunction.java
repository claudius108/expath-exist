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

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.Arrays;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.log4j.Logger;
import org.exist.dom.QName;
import org.exist.util.Base64Encoder;
import org.exist.xquery.BasicFunction;
import org.exist.xquery.Cardinality;
import org.exist.xquery.FunctionSignature;
import org.exist.xquery.XPathException;
import org.exist.xquery.XQueryContext;
import org.exist.xquery.value.FunctionParameterSequenceType;
import org.exist.xquery.value.FunctionReturnSequenceType;
import org.exist.xquery.value.Sequence;
import org.exist.xquery.value.SequenceType;
import org.exist.xquery.value.StringValue;
import org.exist.xquery.value.Type;
import org.expath.crypto.ErrorMessages;
import org.expath.crypto.digest.Hmac;
import org.expath.exist.crypto.ExistExpathCryptoModule;

public class HmacFunction extends BasicFunction {

	private static final String[] supportedHashAlgorithms = { "HmacMD5",
			"HmacSHA1", "HmacSHA256", "HmacSHA384", "HmacSHA512" };

	private final static Logger log = Logger.getLogger(HmacFunction.class);

	public final static FunctionSignature signature = new FunctionSignature(
			new QName("hmac", ExistExpathCryptoModule.NAMESPACE_URI,
					ExistExpathCryptoModule.PREFIX),
			"Hashes the input message.",
			new SequenceType[] {
					new FunctionParameterSequenceType(
							"data",
							Type.STRING,
							Cardinality.EXACTLY_ONE,
							"The data to be authenticated. This parameter can be of type xs:string, xs:base64Binary, or xs:hexBinary."),
					new FunctionParameterSequenceType(
							"secret-key",
							Type.STRING,
							Cardinality.EXACTLY_ONE,
							"The secret key used for calculating the authentication code. This parameter can be of type xs:string, xs:base64Binary, or xs:hexBinary."),
					new FunctionParameterSequenceType("algorithm", Type.STRING,
							Cardinality.EXACTLY_ONE,
							"The cryptographic hashing algorithm."),
					new FunctionParameterSequenceType("provider", Type.STRING,
							Cardinality.EXACTLY_ONE,
							"The cryptographic provider's name."), },
			new FunctionReturnSequenceType(Type.STRING,
					Cardinality.EXACTLY_ONE,
					"hash-based message authentication code."));

	public HmacFunction(XQueryContext context, FunctionSignature signature) {
		super(context, signature);
	}

	@Override
	public Sequence eval(Sequence[] args, Sequence contextSequence)
			throws XPathException {
		String result = null;

		String data = args[0].getStringValue();
		String secretKey = args[1].getStringValue();
		String algorithm = args[2].getStringValue();
		String provider = args[3].getStringValue();

		try {
			result = Hmac.hmac(data, secretKey, algorithm, provider);
		} catch (Exception ex) {
			throw new XPathException(ex.getMessage());
		}

		return new StringValue(result);
	}
}
