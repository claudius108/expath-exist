/*
 *  eXist Java Cryptographic Extension
 *  Copyright (C) 2010 Claudius Teodorescu at http://kuberam.ro
 *  
 *  Released under LGPL License - http://gnu.org/licenses/lgpl.html.
 *  
 */
package org.expath.exist.crypto.digitalSignature;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.URISyntaxException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.log4j.Logger;
import org.exist.Namespaces;
import org.exist.dom.QName;
import org.exist.dom.memtree.SAXAdapter;
import org.exist.dom.persistent.BinaryDocument;
import org.exist.dom.persistent.DocumentImpl;
import org.exist.security.PermissionDeniedException;
import org.exist.storage.lock.Lock;
import org.exist.storage.serializers.Serializer;
import org.exist.validation.internal.node.NodeInputStream;
import org.exist.xmldb.XmldbURI;
import org.exist.xquery.BasicFunction;
import org.exist.xquery.Cardinality;
import org.exist.xquery.FunctionSignature;
import org.exist.xquery.XPathException;
import org.exist.xquery.XQueryContext;
import org.exist.xquery.value.FunctionParameterSequenceType;
import org.exist.xquery.value.FunctionReturnSequenceType;
import org.exist.xquery.value.NodeValue;
import org.exist.xquery.value.Sequence;
import org.exist.xquery.value.SequenceType;
import org.exist.xquery.value.Type;
import org.expath.exist.crypto.ExistExpathCryptoModule;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import ro.kuberam.libs.java.crypto.ErrorMessages;
import ro.kuberam.libs.java.crypto.ExpathCryptoModule;
import ro.kuberam.libs.java.crypto.digitalSignature.GenerateXmlSignature;

/**
 * @author Claudius Teodorescu (claudius.teodorescu@gmail.com)
 */
public class GenerateSignatureFunction extends BasicFunction {

	private final static Logger log = Logger.getLogger(GenerateSignatureFunction.class);

	private static String NAMESPACE_URI = ExistExpathCryptoModule.NAMESPACE_URI;
	private static String PREFIX = ExistExpathCryptoModule.PREFIX;
	private static String canonicalization_algorithm = ExpathCryptoModule.canonicalization_algorithm;
	private final static String certificateRootElementName = "digital-certificate";
	private final static String[] certificateChildElementNames = { "keystore-type", "keystore-password",
			"key-alias", "private-key-password", "keystore-uri" };

	public final static FunctionSignature signatures[] = {
			new FunctionSignature(
					new QName("generate-signature", NAMESPACE_URI, PREFIX),
					"Generate an XML digital signature based on generated key pair. This signature is for the whole document.",
					new SequenceType[] {
							new FunctionParameterSequenceType("data", Type.NODE, Cardinality.EXACTLY_ONE,
									"The document to be signed."),
							new FunctionParameterSequenceType("canonicalization-algorithm", Type.STRING,
									Cardinality.EXACTLY_ONE, canonicalization_algorithm),
							new FunctionParameterSequenceType("digest-algorithm", Type.STRING,
									Cardinality.EXACTLY_ONE, ExpathCryptoModule.DIGEST_ALGORITHM),
							new FunctionParameterSequenceType("signature-algorithm", Type.STRING,
									Cardinality.EXACTLY_ONE, ExpathCryptoModule.SIGNATURE_ALGORITHM),
							new FunctionParameterSequenceType("signature-namespace-prefix", Type.STRING,
									Cardinality.EXACTLY_ONE, "The namespace prefix for signature."),
							new FunctionParameterSequenceType("signature-type", Type.STRING,
									Cardinality.EXACTLY_ONE, ExpathCryptoModule.SIGNATURE_TYPE) },
					new FunctionReturnSequenceType(Type.NODE, Cardinality.EXACTLY_ONE,
							"the signed document (or signature) as node().")),
			new FunctionSignature(
					new QName("generate-signature", NAMESPACE_URI, PREFIX),
					"Generate an XML digital signature based on generated key pair. This signature is for node(s) selected using an XPath expression",
					new SequenceType[] {
							new FunctionParameterSequenceType("data", Type.NODE, Cardinality.EXACTLY_ONE,
									"The document to be signed."),
							new FunctionParameterSequenceType("canonicalization-algorithm", Type.STRING,
									Cardinality.EXACTLY_ONE, canonicalization_algorithm),
							new FunctionParameterSequenceType("digest-algorithm", Type.STRING,
									Cardinality.EXACTLY_ONE, ExpathCryptoModule.DIGEST_ALGORITHM),
							new FunctionParameterSequenceType("signature-algorithm", Type.STRING,
									Cardinality.EXACTLY_ONE, ExpathCryptoModule.SIGNATURE_ALGORITHM),
							new FunctionParameterSequenceType("signature-namespace-prefix", Type.STRING,
									Cardinality.EXACTLY_ONE, "The default namespace prefix for signature."),
							new FunctionParameterSequenceType("signature-type", Type.STRING,
									Cardinality.EXACTLY_ONE, ExpathCryptoModule.SIGNATURE_TYPE),
							new FunctionParameterSequenceType("xpath-expression", Type.ANY_TYPE,
									Cardinality.EXACTLY_ONE,
									"The XPath expression used for selecting the subset to be signed.") },
					new FunctionReturnSequenceType(Type.NODE, Cardinality.EXACTLY_ONE,
							"the signed document (or signature) as node().")),
			new FunctionSignature(
					new QName("generate-signature", NAMESPACE_URI, PREFIX),
					"Generate an XML digital signature based on X.509 certificate.",
					new SequenceType[] {
							new FunctionParameterSequenceType("data", Type.NODE, Cardinality.EXACTLY_ONE,
									"The document to be signed."),
							new FunctionParameterSequenceType("canonicalization-algorithm", Type.STRING,
									Cardinality.EXACTLY_ONE, canonicalization_algorithm),
							new FunctionParameterSequenceType("digest-algorithm", Type.STRING,
									Cardinality.EXACTLY_ONE, ExpathCryptoModule.DIGEST_ALGORITHM),
							new FunctionParameterSequenceType("signature-algorithm", Type.STRING,
									Cardinality.EXACTLY_ONE, ExpathCryptoModule.SIGNATURE_ALGORITHM),
							new FunctionParameterSequenceType("signature-namespace-prefix", Type.STRING,
									Cardinality.EXACTLY_ONE, "The default namespace prefix for signature."),
							new FunctionParameterSequenceType("signature-type", Type.STRING,
									Cardinality.EXACTLY_ONE, ExpathCryptoModule.SIGNATURE_TYPE),
							new FunctionParameterSequenceType("digital-certificate", Type.ANY_TYPE,
									Cardinality.ONE,
									ExpathCryptoModule.digitalCertificateDetailsDescription) },
					new FunctionReturnSequenceType(Type.NODE, Cardinality.EXACTLY_ONE,
							"the signed document (or signature) as node().")),
			new FunctionSignature(
					new QName("generate-signature", NAMESPACE_URI, PREFIX),
					"Generate an XML digital signature based on generated key pair. This signature is for node(s) selected using an XPath expression",
					new SequenceType[] {
							new FunctionParameterSequenceType("data", Type.NODE, Cardinality.EXACTLY_ONE,
									"The document to be signed."),
							new FunctionParameterSequenceType("canonicalization-algorithm", Type.STRING,
									Cardinality.EXACTLY_ONE, canonicalization_algorithm),
							new FunctionParameterSequenceType("digest-algorithm", Type.STRING,
									Cardinality.EXACTLY_ONE, ExpathCryptoModule.DIGEST_ALGORITHM),
							new FunctionParameterSequenceType("signature-algorithm", Type.STRING,
									Cardinality.EXACTLY_ONE, ExpathCryptoModule.SIGNATURE_ALGORITHM),
							new FunctionParameterSequenceType("signature-namespace-prefix", Type.STRING,
									Cardinality.EXACTLY_ONE, "The default namespace prefix for signature."),
							new FunctionParameterSequenceType("signature-type", Type.STRING,
									Cardinality.EXACTLY_ONE, ExpathCryptoModule.SIGNATURE_TYPE),
							new FunctionParameterSequenceType("xpath-expression", Type.ANY_TYPE,
									Cardinality.EXACTLY_ONE,
									"The XPath expression used for selecting the node(s) to be signed."),
							new FunctionParameterSequenceType("digital-certificate", Type.ANY_TYPE,
									Cardinality.ONE,
									ExpathCryptoModule.digitalCertificateDetailsDescription) },
					new FunctionReturnSequenceType(Type.NODE, Cardinality.EXACTLY_ONE,
							"the signed document (or detached signature) as node().")) };

	public GenerateSignatureFunction(XQueryContext context, FunctionSignature signature) {
		super(context, signature);
	}

	public Sequence eval(Sequence[] args, Sequence contextSequence) throws XPathException {
		Serializer serializer = context.getBroker().getSerializer();
		NodeValue inputNode = (NodeValue) args[0].itemAt(0);
		InputStream inputNodeStream = new NodeInputStream(serializer, inputNode);
		Document inputDOMDoc = inputStreamToDocument(inputNodeStream);

		String canonicalizationAlgorithm = args[1].getStringValue();
		String digestAlgorithm = args[2].getStringValue();
		String signatureAlgorithm = args[3].getStringValue();
		String signatureNamespacePrefix = args[4].getStringValue();
		String signatureType = args[5].getStringValue();

		String signatureString = null;
		Document signatureDocument = null;

		// get the XPath expression and/or the certificate's details
		String xpathExprString = null;
		String[] certificateDetails = new String[5];
		certificateDetails[0] = "";
		InputStream keyStoreInputStream = null;

		// function with 7 arguments
		if (args.length == 7) {
			if (args[6].itemAt(0).getType() == 22) {
				xpathExprString = args[6].getStringValue();
			} else if (args[6].itemAt(0).getType() == 1) {
				Node certificateDetailsNode = ((NodeValue) args[6].itemAt(0)).getNode();
				// get the certificate details
				certificateDetails = getDigitalCertificateDetails(certificateDetails,
						certificateDetailsNode);
				// get the keystore InputStream
				keyStoreInputStream = getKeyStoreInputStream(keyStoreInputStream, certificateDetails[4]);
			}
		}

		// function with 8 arguments
		if (args.length == 8) {
			xpathExprString = args[6].getStringValue();
			Node certificateDetailsNode = ((NodeValue) args[7].itemAt(0)).getNode();
			// get the certificate details
			certificateDetails = getDigitalCertificateDetails(certificateDetails, certificateDetailsNode);
			// get the keystore InputStream
			keyStoreInputStream = getKeyStoreInputStream(keyStoreInputStream, certificateDetails[4]);
		}

		try {
			signatureString = GenerateXmlSignature.generate(inputDOMDoc, canonicalizationAlgorithm,
					digestAlgorithm, signatureAlgorithm, signatureNamespacePrefix, signatureType,
					xpathExprString, certificateDetails, keyStoreInputStream);
		} catch (Exception ex) {
			throw new XPathException(ex.getMessage());
		}

		try {
			signatureDocument = stringToDocument(signatureString);
		} catch (Exception ex) {
			throw new XPathException(ex.getMessage());
		}

		return (Sequence) (DocumentImpl) signatureDocument;
	}

	private Document stringToDocument(String signatureString) throws Exception {
		// process the output (signed) document from string to node()
		SAXAdapter adapter = null;
		try {
			SAXParserFactory factory = SAXParserFactory.newInstance();
			factory.setNamespaceAware(true);
			SAXParser parser = factory.newSAXParser();
			XMLReader xr = parser.getXMLReader();
			adapter = new SAXAdapter(context);
			xr.setContentHandler(adapter);
			xr.setProperty(Namespaces.SAX_LEXICAL_HANDLER, adapter);
			xr.parse(new InputSource(new StringReader(signatureString)));
		} catch (ParserConfigurationException e) {
			throw new XPathException("Error while constructing XML parser: " + e.getMessage());
		} catch (SAXException e) {
			throw new XPathException("Error while parsing XML: " + e.getMessage());
		} catch (IOException e) {
			throw new XPathException("Error while parsing XML: " + e.getMessage());
		}

		return adapter.getDocument();
	}

	private String[] getDigitalCertificateDetails(String[] certificateDetails, Node certificateDetailsNode)
			throws XPathException {
		if (!certificateDetailsNode.getNodeName().equals(certificateRootElementName)) {
			throw new XPathException(ErrorMessages.error_sigElem);
			// TODO: here was err:CX05 The root element of argument
			// $digital-certificate must have the name 'digital-certificate'.
		}
		NodeList certificateDetailsNodeList = certificateDetailsNode.getChildNodes();
		for (int i = 0, il = certificateDetailsNodeList.getLength(); i < il; i++) {
			Node child = certificateDetailsNodeList.item(i);
			if (child.getNodeName().equals(certificateChildElementNames[i])) {
				certificateDetails[i] = child.getFirstChild().getNodeValue();
			} else {
				throw new XPathException(ErrorMessages.error_sigElem);
				// TODO: here was err:CX05 The root element of argument
				// $digital-certificate must have the name
				// 'digital-certificate'.
			}
		}
		return certificateDetails;
	}

	private InputStream getKeyStoreInputStream(InputStream keyStoreInputStream, String keystoreURI)
			throws XPathException {
		// get the keystore as InputStream
		DocumentImpl keyStoreDoc = null;
		try {
			try {
				keyStoreDoc = context.getBroker().getXMLResource(XmldbURI.xmldbUriFor(keystoreURI),
						Lock.READ_LOCK);
				if (keyStoreDoc == null) {
					throw new XPathException(ErrorMessages.error_readKeystore);
					// TODO: here was err:CX07 The keystore is null.
				}
				BinaryDocument keyStoreBinaryDoc = (BinaryDocument) keyStoreDoc;
				try {
					keyStoreInputStream = context.getBroker().getBinaryResource(keyStoreBinaryDoc);
				} catch (IOException ex) {
					throw new XPathException(ErrorMessages.error_readKeystore);
				}
			} catch (PermissionDeniedException ex) {
				log.info(ErrorMessages.error_deniedKeystore);
			}
		} catch (URISyntaxException ex) {
			log.error(ErrorMessages.error_keystoreUrl);
		}
		return keyStoreInputStream;
	}

	private Document inputStreamToDocument(InputStream inputStream) {
		// initialize the document builder
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setNamespaceAware(true);
		DocumentBuilder db = null;
		try {
			db = dbf.newDocumentBuilder();
		} catch (ParserConfigurationException ex) {
		}

		// convert data to DOM document
		Document document = null;
		try {
			document = db.parse(inputStream);
		} catch (SAXException ex) {
			ex.getMessage();
		} catch (IOException ex) {
			ex.getMessage();
		}

		return document;
	}
}