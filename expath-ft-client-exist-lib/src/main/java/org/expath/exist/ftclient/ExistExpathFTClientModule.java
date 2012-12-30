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

import java.util.Map;
import java.util.List;
import org.exist.xquery.AbstractInternalModule;
import org.exist.xquery.FunctionDef;
import org.exist.xquery.XQueryContext;
import org.exist.xquery.modules.ModuleUtils;
import org.expath.ftclient.ExpathFTClientModule;

/**
 * Implements the module definition.
 *
 * @author WStarcev
 * @author Adam Retter <adam@existsolutions.com>
 * @author Claudius Teodorescu <claudius.teodorescu@gmail.com>
 */
public class ExistExpathFTClientModule extends AbstractInternalModule {

    public static String          NAMESPACE_URI                   = "";
	static {
		NAMESPACE_URI = ExpathFTClientModule.NAMESPACE_URI;
	}
    public static String          PREFIX                          = "";
	static {
		PREFIX = ExpathFTClientModule.PREFIX;
	}    
    public final static String          INCLUSION_DATE                  = "2011-03-24";
    public final static String          RELEASED_IN_VERSION             = "eXist-1.5";
    public final static String          CONNECTIONS_CONTEXTVAR          = "_eXist_ftc_connections";

    protected static int CONNECTION_TYPE;

    private final static FunctionDef[] functions = {
        new FunctionDef(ConnectFunction.signatures[0], ConnectFunction.class),
        new FunctionDef(ConnectFunction.signatures[1], ConnectFunction.class),
        new FunctionDef(ListResourcesFunction.signature, ListResourcesFunction.class),
        new FunctionDef(DisconnectFunction.signature, DisconnectFunction.class),
        new FunctionDef(StoreResourceFunction.signature, StoreResourceFunction.class),
        new FunctionDef(RetrieveResourceFunction.signature, RetrieveResourceFunction.class),
        new FunctionDef(GetResourceMetadataFunction.signature, GetResourceMetadataFunction.class),
        new FunctionDef(DeleteResourceFunction.signature, DeleteResourceFunction.class)
    };

    public ExistExpathFTClientModule(Map<String, List<? extends Object>> parameters) throws Exception {
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
        return "A module for performing File Transfer requests as a client.";
    }

    @Override
    public String getReleaseVersion() {
        return RELEASED_IN_VERSION;
    }
    
    /**
     * Stores a remote connection in the Context of an XQuery.
     *
     * @param   context  The Context of the XQuery to store the Connection in
     * @param   con      The connection to store
     *
     * @return  A unique ID representing the connection
     */
    public static synchronized long storeRemoteConnection(XQueryContext context, Object remoteConnection) {
        return ModuleUtils.storeObjectInContextMap(context, ExistExpathFTClientModule.CONNECTIONS_CONTEXTVAR, remoteConnection);
    }

    /**
     * Retrieves a previously stored connection from the Context of an XQuery.
     *
     * @param   context        The Context of the XQuery containing the Connection
     * @param   connectionUID  The UID of the Connection to retrieve from the Context of the XQuery
     *
     * @return  DOCUMENT ME!
     */
    @SuppressWarnings("empty-statement")
    public static Object retrieveRemoteConnection(XQueryContext context, long connectionUID) {
        return ModuleUtils.retrieveObjectFromContextMap(context, ExistExpathFTClientModule.CONNECTIONS_CONTEXTVAR, connectionUID);
    }

    /**
     * Resets the Module Context and closes any connections for the XQueryContext.
     *
     * @param  xqueryContext  The XQueryContext
     */
    @Override
    public void reset(XQueryContext xqueryContext) {
        // reset the module context
        super.reset(xqueryContext);
    }
}