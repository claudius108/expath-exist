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
package org.expath.exist.location;

import java.util.List;
import java.util.Map;

import org.exist.xquery.AbstractInternalModule;
import org.exist.xquery.FunctionDef;

/**
 * Implements the module definition.
 * 
 * @author Claudius Teodorescu <claudius.teodorescu@gmail.com>
 */
public class ModuleDescription extends AbstractInternalModule {

	public static String NAMESPACE_URI = "";
	static {
		NAMESPACE_URI = org.expath.location.ModuleDescription.NAMESPACE_URI;
	}
	public static String PREFIX = "";
	static {
		PREFIX = org.expath.location.ModuleDescription.PREFIX;
	}
	public final static String INCLUSION_DATE = "2011-03-24";
	public final static String RELEASED_IN_VERSION = "eXist-1.5";

	private final static FunctionDef[] functions = { new FunctionDef(HashFunction.signature, HashFunction.class),
			new FunctionDef(HmacFunction.signature, HmacFunction.class), new FunctionDef(GenerateSignatureFunction.signatures[0], GenerateSignatureFunction.class),
			new FunctionDef(GenerateSignatureFunction.signatures[1], GenerateSignatureFunction.class),
			new FunctionDef(GenerateSignatureFunction.signatures[2], GenerateSignatureFunction.class),
			new FunctionDef(GenerateSignatureFunction.signatures[3], GenerateSignatureFunction.class),
			new FunctionDef(ValidateSignatureFunction.signature, ValidateSignatureFunction.class),
			new FunctionDef(EncryptionFunctions.signatures[0], EncryptionFunctions.class), new FunctionDef(EncryptionFunctions.signatures[1], EncryptionFunctions.class) };

	public ModuleDescription(Map<String, List<? extends Object>> parameters) throws Exception {
		super(functions, parameters);
	}

	@Override
	public String getNamespaceURI() {
		return NAMESPACE_URI;
	}

	@Override
	public String getDefaultPrefix() {
		return PREFIX;
	}

	@Override
	public String getDescription() {
		return org.expath.location.ModuleDescription.MODULE_DESCRIPTION;
	}

	@Override
	public String getReleaseVersion() {
		return RELEASED_IN_VERSION;
	}
}