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

import org.apache.log4j.Logger;
import org.exist.dom.QName;
import org.exist.xquery.BasicFunction;
import org.exist.xquery.Cardinality;
import org.exist.xquery.FunctionSignature;
import org.exist.xquery.XPathException;
import org.exist.xquery.XQueryContext;
import org.exist.xquery.value.BooleanValue;
import org.exist.xquery.value.FunctionParameterSequenceType;
import org.exist.xquery.value.FunctionReturnSequenceType;
import org.exist.xquery.value.IntegerValue;
import org.exist.xquery.value.Sequence;
import org.exist.xquery.value.SequenceType;
import org.exist.xquery.value.Type;

/**
 * Implements a method for closing a remote connection.
 * 
 * @author Claudius Teodorescu <claudius.teodorescu@gmail.com>
 */
public class DisconnectFunction extends BasicFunction {

    private static final Logger log = Logger.getLogger(DisconnectFunction.class);
    
    public final static FunctionSignature signature = new FunctionSignature(
        new QName("disconnect", ExistExpathFTClientModule.NAMESPACE_URI, ExistExpathFTClientModule.PREFIX),
        "Closes a remote connection.",
        new SequenceType[] {
            new FunctionParameterSequenceType("connection-handle", Type.LONG, Cardinality.EXACTLY_ONE, "The connection handle")
        },
        new FunctionReturnSequenceType(Type.BOOLEAN, Cardinality.EXACTLY_ONE, "true or false indicating the success of closing the connection.")
    );

    public DisconnectFunction(XQueryContext context) {
        super(context, signature);
    }

    @Override
    public Sequence eval(Sequence[] args, Sequence contextSequence) throws XPathException {
        
        Boolean result = true;
        
        long connectionUID = ((IntegerValue)args[0].itemAt(0)).getLong();

        try {
            result = (Boolean) org.expath.ftclient.Disconnect.disconnect(ExistExpathFTClientModule.retrieveRemoteConnection(context, connectionUID));
        } catch (Exception ex) {
        	throw new XPathException(ex.getMessage());
        }

        
        /*
        switch (FTClientModule.CONNECTION_TYPE) {
            //FTP
            case 101730:
                FTPClient FTPconnection = (FTPClient) FTClientModule.retrieveRemoteConnection(context, connectionUID);
                if (FTPconnection == null) {throw new XPathException("err:FTC002: The connection was closed by server.");}
                ModuleUtils.modifyContextMap(context, FTClientModule.CONNECTIONS_CONTEXTVAR, new ContextMapEntryModifier<FTPClient>(){

                    @Override
                    public void modify(Map<Long, FTPClient> map) {
                        super.modify(map);
                        //empty the map
                        map.clear();
                    }

                    @Override
                    public void modify(Entry<Long, FTPClient> entry) {

                        final FTPClient FTPconnection = entry.getValue();

                        try {
                            // close the Connection
                            FTPconnection.logout();
                        } catch(IOException ioe) {
                            log.error(ioe.getMessage(), ioe);
                            result = false;
                        } finally {
                            if(FTPconnection.isConnected()) {
                                try {
                                    FTPconnection.disconnect();
                                } catch(IOException ioe) {
                                    log.error(ioe.getMessage(), ioe);
                                    result = false;
                                }
                            }
                        }
                    }
                });
            break;
            //SFTP
            case 3527695:
                Session session = (Session) FTClientModule.retrieveRemoteConnection(context, connectionUID);
                if (session == null) {throw new XPathException("err:FTC002: The connection was closed by server.");}
                ModuleUtils.modifyContextMap(context, FTClientModule.CONNECTIONS_CONTEXTVAR, new ContextMapEntryModifier<Session>(){

                    @Override
                    public void modify(Map<Long, Session> map) {
                        super.modify(map);

                        //empty the map
                        map.clear();
                    }

                    @Override
                    public void modify(Entry<Long, Session> entry) {

                        final Session SFTPconnection = entry.getValue();

                        try {
                            // close the Connection
                            SFTPconnection.disconnect();
                        } catch(Exception ex) {
                            log.error(ex.getMessage(), ex);
                            result = false;
                        } finally {
                            if(SFTPconnection.isConnected()) {
                                try {
                                    SFTPconnection.disconnect();
                                } catch(Exception ioe) {
                                    log.error(ioe.getMessage(), ioe);
                                    result = false;
                                }
                            }
                        }
                    }
                });
            break;
        }*/
        return BooleanValue.valueOf(result);
    }
}