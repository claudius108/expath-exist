/*
 *  eXist Java Cryptographic Extension
 *  Copyright (C) 2010 Claudius Teodorescu at http://kuberam.ro
 *
 *  Released under LGPL License - http://gnu.org/licenses/lgpl.html.
 *
 */

package org.expath.exist.crypto.encrypt;

import org.apache.log4j.Logger;
import org.exist.dom.QName;
import org.exist.xquery.BasicFunction;
import org.exist.xquery.Cardinality;
import org.exist.xquery.FunctionSignature;
import org.exist.xquery.XPathException;
import org.exist.xquery.XQueryContext;
import org.exist.xquery.value.FunctionParameterSequenceType;
import org.exist.xquery.value.Sequence;
import org.exist.xquery.value.SequenceType;
import org.exist.xquery.value.Type;
import org.exist.xquery.value.FunctionReturnSequenceType;
import org.exist.xquery.value.StringValue;
import org.expath.exist.crypto.ExistExpathCryptoModule;

import ro.kuberam.libs.java.crypto.ErrorMessages;
import ro.kuberam.libs.java.crypto.encrypt.AsymmetricEncryption;
import ro.kuberam.libs.java.crypto.encrypt.SymmetricEncryption;

/**
 * @author Claudius Teodorescu <claudius.teodorescu@gmail.com>
 */

public class EncryptionFunctions extends BasicFunction {

	private final static Logger log = Logger.getLogger(EncryptionFunctions.class);

	public final static FunctionSignature signatures[] = {
			new FunctionSignature(
					new QName("encrypt", ExistExpathCryptoModule.NAMESPACE_URI,
							ExistExpathCryptoModule.PREFIX),
					"Encrypts the input string.",
					new SequenceType[] {
							new FunctionParameterSequenceType("data", Type.STRING, Cardinality.EXACTLY_ONE,
									"The data to be encrypted. This parameter can be of type xs:string, xs:base64Binary, or xs:hexBinary."),
							new FunctionParameterSequenceType("encryption-type", Type.STRING,
									Cardinality.EXACTLY_ONE,
									"The type of encryption. Legal values: 'symmetric', and 'asymmetric'."),
							new FunctionParameterSequenceType("secret-key", Type.STRING,
									Cardinality.EXACTLY_ONE,
									"The secret key used for encryption, as string."),
							new FunctionParameterSequenceType("cryptographic-algorithm", Type.STRING,
									Cardinality.EXACTLY_ONE,
									"The cryptographic algorithm used for encryption."),
							new FunctionParameterSequenceType("iv", Type.STRING, Cardinality.ZERO_OR_ONE,
									"The initialization vector."),
							new FunctionParameterSequenceType("provider", Type.STRING,
									Cardinality.ZERO_OR_ONE, "The cryptographic provider.") },
					new FunctionReturnSequenceType(Type.STRING, Cardinality.EXACTLY_ONE,
							"the encrypted data.")),
			new FunctionSignature(new QName("decrypt", ExistExpathCryptoModule.NAMESPACE_URI,
					ExistExpathCryptoModule.PREFIX), "Decrypts the input string.", new SequenceType[] {
					new FunctionParameterSequenceType("data", Type.STRING, Cardinality.EXACTLY_ONE,
							"The data to be decrypted."),
					new FunctionParameterSequenceType("decryption-type", Type.STRING,
							Cardinality.EXACTLY_ONE,
							"The type of decryption. Legal values: 'symmetric', and 'asymmetric'."),
					new FunctionParameterSequenceType("secret-key", Type.STRING, Cardinality.EXACTLY_ONE,
							"The secret key used for decryption, as string."),
					new FunctionParameterSequenceType("cryptographic-algorithm", Type.STRING,
							Cardinality.EXACTLY_ONE, "The cryptographic algorithm used for decryption."),
					new FunctionParameterSequenceType("iv", Type.STRING, Cardinality.ZERO_OR_ONE,
							"The initialization vector."),
					new FunctionParameterSequenceType("provider", Type.STRING, Cardinality.ZERO_OR_ONE,
							"The cryptographic provider.") }, new FunctionReturnSequenceType(Type.STRING,
					Cardinality.EXACTLY_ONE, "the decrypted data.")) };

	public EncryptionFunctions(XQueryContext context, FunctionSignature signature) {
		super(context, signature);
	}

	public Sequence eval(Sequence[] args, Sequence contextSequence) throws XPathException {
		String result = null;
		String functionName = getSignature().getName().getLocalPart();

		try {
			if ("encrypt".equals(functionName)) {
				if ("symmetric".equals(args[1].getStringValue())) {
					result = SymmetricEncryption.encryptString(args[0].getStringValue(),
							args[2].getStringValue(), args[3].getStringValue(), args[4].getStringValue(),
							args[5].getStringValue());
				} else if ("asymmetric".equals(args[1].getStringValue())) {
					result = AsymmetricEncryption.encryptString(args[0].getStringValue(),
							args[2].getStringValue(), args[3].getStringValue());
				} else {
					throw new XPathException(ErrorMessages.error_encType);
				}
			} else if ("decrypt".equals(functionName)) {
				if ("symmetric".equals(args[1].getStringValue())) {
					result = SymmetricEncryption.decryptString(args[0].getStringValue(),
							args[2].getStringValue(), args[3].getStringValue(), args[4].getStringValue(),
							args[5].getStringValue());
				} else if ("asymmetric".equals(args[1].getStringValue())) {

				} else {
					throw new XPathException(ErrorMessages.error_decryptionType);
				}
			}
		} catch (Exception ex) {
			throw new XPathException(ex.getMessage());
		}

		return new StringValue(result);
	}

}
