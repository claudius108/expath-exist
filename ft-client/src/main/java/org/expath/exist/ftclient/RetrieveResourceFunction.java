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

import java.io.InputStream;

import org.apache.log4j.Logger;
import org.exist.dom.QName;
import org.exist.xquery.BasicFunction;
import org.exist.xquery.Cardinality;
import org.exist.xquery.FunctionSignature;
import org.exist.xquery.XPathException;
import org.exist.xquery.XQueryContext;
import org.exist.xquery.value.Base64BinaryValueType;
import org.exist.xquery.value.BinaryValueFromInputStream;
import org.exist.xquery.value.FunctionParameterSequenceType;
import org.exist.xquery.value.FunctionReturnSequenceType;
import org.exist.xquery.value.IntegerValue;
import org.exist.xquery.value.Sequence;
import org.exist.xquery.value.SequenceType;
import org.exist.xquery.value.Type;
import org.exist.xquery.value.ValueSequence;

/**
 * Implements a method for retrieving a remote resource.
 * 
 * @author WStarcev
 * @author Adam Retter <adam@existsolutions.com>
 * @author Claudius Teodorescu <claudius.teodorescu@gmail.com>
 */
public class RetrieveResourceFunction extends BasicFunction {
	private static final Logger log = Logger.getLogger(RetrieveResourceFunction.class);

	public final static FunctionSignature signature = new FunctionSignature(new QName("retrieve-resource",
			ExistExpathFTClientModule.NAMESPACE_URI, ExistExpathFTClientModule.PREFIX),
			"This function retrieves a resource from the remote host.", new SequenceType[] {
					new FunctionParameterSequenceType("connection-handle", Type.LONG,
							Cardinality.EXACTLY_ONE, "The connection handle"),
					new FunctionParameterSequenceType("remote-resource-path", Type.STRING,
							Cardinality.EXACTLY_ONE, "The path for resource to be retrieved.") },
			new FunctionReturnSequenceType(Type.ANY_TYPE, Cardinality.ZERO_OR_ONE,
					"the resource retrieved, wrapped in element(resource)."));

	public RetrieveResourceFunction(XQueryContext context, FunctionSignature signature) {
		super(context, signature);
	}

	@Override
	public Sequence eval(Sequence[] args, Sequence contextSequence) throws XPathException {

		Sequence result = new ValueSequence();

		InputStream resultAsStreamResult = null;

		try {
			resultAsStreamResult = ro.kuberam.libs.java.ftclient.RetrieveResource.retrieveResource(
					ExistExpathFTClientModule.retrieveRemoteConnection(context,
							((IntegerValue) args[0].itemAt(0)).getLong()), args[1].getStringValue());
		} catch (Exception ex) {
			throw new XPathException(ex.getMessage());
		}

		try {
			result.add(BinaryValueFromInputStream.getInstance(context, new Base64BinaryValueType(),
					resultAsStreamResult));
		} catch (final XPathException xpe) {
			throw new XPathException("Unable to add binary value to result:" + xpe.getMessage());
		}

		return result;
	}
}