/*
 *  eXist Java Cryptographic Extension
 *  Copyright (C) 2010 Claudius Teodorescu at http://kuberam.ro
 *  
 *  Released under LGPL License - http://gnu.org/licenses/lgpl.html.
 *  
 */
package org.expath.exist.crypto.digitalSignature;

import java.io.IOException;
import java.io.StringReader;
import java.util.Properties;
import java.util.logging.Level;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;

import org.apache.log4j.Logger;
import org.exist.dom.QName;
import org.exist.storage.serializers.Serializer;
import org.exist.xquery.BasicFunction;
import org.exist.xquery.Cardinality;
import org.exist.xquery.FunctionSignature;
import org.exist.xquery.XPathException;
import org.exist.xquery.XQueryContext;
import org.exist.xquery.value.BooleanValue;
import org.exist.xquery.value.FunctionParameterSequenceType;
import org.exist.xquery.value.FunctionReturnSequenceType;
import org.exist.xquery.value.NodeValue;
import org.exist.xquery.value.Sequence;
import org.exist.xquery.value.SequenceType;
import org.exist.xquery.value.Type;
import org.expath.exist.crypto.ExistExpathCryptoModule;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;

import ro.kuberam.libs.java.crypto.digitalSignature.ValidateXmlSignature;

/**
 * Cryptographic extension functions.
 * 
 * @author Claudius Teodorescu <claudius.teodorescu@gmail.com>
 */
public class ValidateSignatureFunction extends BasicFunction {

	private final static Logger logger = Logger.getLogger(ValidateSignatureFunction.class);

	public final static FunctionSignature signature =
		new FunctionSignature(
			new QName("validate-signature", ExistExpathCryptoModule.NAMESPACE_URI, ExistExpathCryptoModule.PREFIX),
			"This function validates an XML Digital Signature.",
			new SequenceType[] {
                            new FunctionParameterSequenceType("data", Type.NODE, Cardinality.EXACTLY_ONE, "The enveloped, enveloping, or detached signature.")                        },
			new FunctionReturnSequenceType(Type.BOOLEAN, Cardinality.EXACTLY_ONE, "boolean value true() if the signature is valid, otherwise return value false()."));

	public ValidateSignatureFunction(XQueryContext context, FunctionSignature signature) {
		super(context, signature);
	}

	protected final static Properties defaultOutputKeysProperties = new Properties();
	static {
		defaultOutputKeysProperties.setProperty(OutputKeys.INDENT, "no");
		defaultOutputKeysProperties.setProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
		defaultOutputKeysProperties.setProperty(OutputKeys.ENCODING, "UTF-8");
	}

	public Sequence eval(Sequence[] args, Sequence contextSequence) throws XPathException {
            if( args[0].isEmpty() ){
                return Sequence.EMPTY_SEQUENCE;
            }

            //get and process the input document or node to InputStream, in order to be transformed into DOM Document
            Serializer serializer = context.getBroker().getSerializer();
            serializer.reset();
            Properties outputProperties = new Properties( defaultOutputKeysProperties );
            try {
                serializer.setProperties(outputProperties);
            } catch (SAXNotRecognizedException ex) {
            java.util.logging.Logger.getLogger(ValidateSignatureFunction.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SAXNotSupportedException ex) {
            java.util.logging.Logger.getLogger(ValidateSignatureFunction.class.getName()).log(Level.SEVERE, null, ex);
            }

            //initialize the document builder
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(true);
            DocumentBuilder db = null;
            try {
                db = dbf.newDocumentBuilder();
            } catch (ParserConfigurationException ex) {}

            //process the input string to DOM document
            Document inputDOMDoc = null;
            try {
                inputDOMDoc = db.parse(new InputSource(new StringReader(serializer.serialize((NodeValue)args[0].itemAt(0)))));
            } catch (SAXException ex) {
                ex.getMessage();
            } catch (IOException ex) {
                ex.getMessage();
            }

            //validate the signature
            Boolean isValid = false;
            try {
                isValid = ValidateXmlSignature.validate(inputDOMDoc);
//            	isValid = ValidateXmlSignature.validate((Document)args[0].itemAt(0));
            } catch (Exception ex) {
                throw new XPathException(ex.getMessage());
            }

            return new BooleanValue(isValid);
     }
}