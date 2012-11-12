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
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Date;

import javax.xml.transform.stream.StreamResult;

import org.apache.commons.io.input.CloseShieldInputStream;
import org.apache.log4j.Logger;
import org.exist.dom.QName;
import org.exist.memtree.DocumentBuilderReceiver;
import org.exist.memtree.MemTreeBuilder;
import org.exist.xquery.BasicFunction;
import org.exist.xquery.Cardinality;
import org.exist.xquery.FunctionSignature;
import org.exist.xquery.XPathException;
import org.exist.xquery.XQueryContext;
import org.exist.xquery.value.BooleanValue;
import org.exist.xquery.value.FunctionParameterSequenceType;
import org.exist.xquery.value.FunctionReturnSequenceType;
import org.exist.xquery.value.IntegerValue;
import org.exist.xquery.value.NodeValue;
import org.exist.xquery.value.Sequence;
import org.exist.xquery.value.SequenceType;
import org.exist.xquery.value.Type;
import org.exist.xquery.value.ValueSequence;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

/**
 * Implements a method for getting a resource metadata.
 * 
 * @author Claudius Teodorescu <claudius.teodorescu@gmail.com>
 */
public class GetResourceMetadataFunction extends BasicFunction {

    private static final Logger log = Logger.getLogger(GetResourceMetadataFunction.class);
    
    public final static FunctionSignature signature = new FunctionSignature(
        new QName("get-resource-metadata", ExistExpathFTClientModule.NAMESPACE_URI, ExistExpathFTClientModule.PREFIX),
        "Get metadata of a resource.",
        new SequenceType[] {
				new FunctionParameterSequenceType("connection-handle", Type.LONG, Cardinality.EXACTLY_ONE,
						"The connection handle"),
				new FunctionParameterSequenceType("remote-resource-path", Type.STRING, Cardinality.EXACTLY_ONE,
						"The path for resource to be retrieved.") }, new FunctionReturnSequenceType(Type.ANY_TYPE,
				Cardinality.ZERO_OR_ONE, "the metadata retrieved, wrapped in element(resource).")
    );

    public GetResourceMetadataFunction(XQueryContext context) {
        super(context, signature);
    }

    @Override
    public Sequence eval(Sequence[] args, Sequence contextSequence) throws XPathException {
        
		Sequence result = new ValueSequence();
		
		StreamResult resultAsStreamResult = null;

		try {
			resultAsStreamResult = org.expath.ftclient.GetResourceMetadata.getResourceMetadata(
					ExistExpathFTClientModule.retrieveRemoteConnection(context, ((IntegerValue) args[0].itemAt(0)).getLong()),
					args[1].getStringValue());
		} catch (Exception ex) {
			throw new XPathException(ex.getMessage());
		}
		
	      ByteArrayInputStream resultDocAsInputStream = null;
	      try {
				resultDocAsInputStream = new ByteArrayInputStream(resultAsStreamResult.getWriter().toString().getBytes("UTF-8"));
			} catch (UnsupportedEncodingException ex) {
				throw new XPathException(ex.getMessage());
			}
	        
	        XMLReader reader = null;

	        context.pushDocumentContext();
	        try {
	            InputSource src = new InputSource(new CloseShieldInputStream(resultDocAsInputStream));

	            reader = context.getBroker().getBrokerPool().getParserPool().borrowXMLReader();
	            MemTreeBuilder builder = context.getDocumentBuilder();
	            DocumentBuilderReceiver receiver = new DocumentBuilderReceiver(builder, true);
	            reader.setContentHandler(receiver);
	            reader.parse(src);
	            Document doc = receiver.getDocument();

	            result = (NodeValue)doc;
	        } catch(SAXException saxe) {
	            //do nothing, we will default to trying to return a string below
	        } catch(IOException ioe) {
	            //do nothing, we will default to trying to return a string below
	        } finally {
	            context.popDocumentContext();

	            if(reader != null) {
	                context.getBroker().getBrokerPool().getParserPool().returnXMLReader(reader);
	            }
	        }
	        
	        return result;
    }
}