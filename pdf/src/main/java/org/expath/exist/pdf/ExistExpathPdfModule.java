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
package org.expath.exist.pdf;

import java.util.List;
import java.util.Map;

import org.exist.xquery.AbstractInternalModule;
import org.exist.xquery.FunctionDef;
import org.expath.exist.pdf.formControls.GetTextFieldsFunction;
import org.expath.exist.pdf.formControls.SetTextFieldsFunction;
import org.expath.exist.pdf.metadata.GetMetadataFunction;
import org.expath.exist.pdf.stamp.StampFunction;

import ro.kuberam.libs.java.pdf.ModuleDescription;

/**
 * Implements the module definition.
 * 
 * @author Claudius Teodorescu <claudius.teodorescu@gmail.com>
 */
public class ExistExpathPdfModule extends AbstractInternalModule {

	public static String NAMESPACE_URI = ModuleDescription.NAMESPACE_URI;
	public static String PREFIX = ModuleDescription.PREFIX;
	public final static String INCLUSION_DATE = "2014-07-12";
	public final static String RELEASED_IN_VERSION = "eXist-2.2";

	private final static FunctionDef[] functions = {
			new FunctionDef(GetTextFieldsFunction.signature, GetTextFieldsFunction.class),
			new FunctionDef(SetTextFieldsFunction.signature, SetTextFieldsFunction.class),
			new FunctionDef(StampFunction.signatures[0], StampFunction.class),
			new FunctionDef(StampFunction.signatures[1], StampFunction.class),
			new FunctionDef(GetMetadataFunction.signature, GetMetadataFunction.class) };

	public ExistExpathPdfModule(Map<String, List<? extends Object>> parameters) throws Exception {
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
		return ModuleDescription.MODULE_DESCRIPTION;
	}

	@Override
	public String getReleaseVersion() {
		return RELEASED_IN_VERSION;
	}
}