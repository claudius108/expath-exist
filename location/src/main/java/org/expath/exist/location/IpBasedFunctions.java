/*
 *  eXist Java Geo Data Extension
 *  Copyright (C) 2010 Claudius Teodorescu at http://kuberam.ro
 *
 *  Released under LGPL License - http://gnu.org/licenses/lgpl.html.
 *
 */
package org.expath.exist.location;

import java.io.IOException;
import java.util.logging.Level;
import org.apache.log4j.Logger;
import org.exist.dom.QName;
import org.exist.xquery.BasicFunction;
import org.exist.xquery.Cardinality;
import org.exist.xquery.FunctionDef;
import org.exist.xquery.FunctionSignature;
import org.exist.xquery.XPathException;
import org.exist.xquery.XQueryContext;
import org.exist.xquery.value.FunctionParameterSequenceType;
import org.exist.xquery.value.FunctionReturnSequenceType;
import org.exist.xquery.value.Sequence;
import org.exist.xquery.value.SequenceType;
import org.exist.xquery.value.Type;
import org.exist.xquery.value.StringValue;
/**
 *
 * @author Claudius Teodorescu (claud108@yahoo.com)
 */
public class IpBasedFunctions extends BasicFunction {

    @SuppressWarnings("unused")
	private final static Logger logger = Logger.getLogger( IpBasedFunctions.class );

         private final static String getCountryCodeFunctionDescription =
                 "Returns contry code based on an IP address, in a form as defined by function's argument, according to ISO 3166-1.\n" +
                 "The default value for argument is 'A2' (two letters code).\n" +
                 "Available forms for country codes are: 'A2' (two letters code). ";//, and 'A3' (three letters code)

         private final static String getCountryNameFunctionDescription =
                 "Returns contry name based on an IP address, in a language as defined by function's argument, according to ISO 3166-1.\n" +
                 "The default value for argument is 'EN'.\n" +
                 "Available languages for country names are: English (EN).";//, and French (FR)

	public final static FunctionSignature signatures[] = {
		new FunctionSignature(
			new QName("get-country-code", org.expath.location.ModuleDescription.NAMESPACE_URI, org.expath.location.ModuleDescription.PREFIX), getCountryCodeFunctionDescription,
			new SequenceType[] {
                            new FunctionParameterSequenceType("ip-address", Type.STRING, Cardinality.ZERO_OR_ONE, "The IP address.")
                        },
			new FunctionReturnSequenceType(Type.STRING, Cardinality.EXACTLY_ONE, "the country code, as string.")
                ),
		new FunctionSignature(
			new QName("get-country-name", org.expath.location.ModuleDescription.NAMESPACE_URI, org.expath.location.ModuleDescription.PREFIX), getCountryNameFunctionDescription,
			new SequenceType[] {
                            new FunctionParameterSequenceType("ip-address", Type.STRING, Cardinality.ZERO_OR_ONE, "The IP address.")
                        },
			new FunctionReturnSequenceType(Type.STRING, Cardinality.EXACTLY_ONE, "the country name, as string.")
                ),
		new FunctionSignature(
			new QName("get-city", org.expath.location.ModuleDescription.NAMESPACE_URI, org.expath.location.ModuleDescription.PREFIX), "Returns city name based on an IP address.",
			new SequenceType[] {
                            new FunctionParameterSequenceType("ip-address", Type.STRING, Cardinality.ZERO_OR_ONE, "The IP address.")
                        },
			new FunctionReturnSequenceType(Type.STRING, Cardinality.EXACTLY_ONE, "the city name, as string.")
                ),
		new FunctionSignature(
			new QName("get-time-zone", org.expath.location.ModuleDescription.NAMESPACE_URI, org.expath.location.ModuleDescription.PREFIX), "Returns time zone based on an IP address.",
			new SequenceType[] {
                            new FunctionParameterSequenceType("ip-address", Type.STRING, Cardinality.ZERO_OR_ONE, "The IP address.")
                        },
			new FunctionReturnSequenceType(Type.STRING, Cardinality.EXACTLY_ONE, "the time zone, as string.")
                ),
		new FunctionSignature(
			new QName("get-distance", org.expath.location.ModuleDescription.NAMESPACE_URI, org.expath.location.ModuleDescription.PREFIX), "Returns distance between two points, based on their IP addresses.",
			new SequenceType[] {
                            new FunctionParameterSequenceType("first-ip-address", Type.STRING, Cardinality.EXACTLY_ONE, "First IP address."),
                            new FunctionParameterSequenceType("second-ip-address", Type.STRING, Cardinality.EXACTLY_ONE, "Second IP address.")
                        },
			new FunctionReturnSequenceType(Type.STRING, Cardinality.EXACTLY_ONE, "the distance, in kilometers.")
                )

        };

	public IpBasedFunctions(XQueryContext context, FunctionSignature signature) {
		super(context, signature);
	}
	
    private final static FunctionDef[] functions = {
    	new FunctionDef(HashFunction.signature, HashFunction.class),
        new FunctionDef(HmacFunction.signature, HmacFunction.class),   	
		new FunctionDef(GenerateSignatureFunction.signatures[0], GenerateSignatureFunction.class),
        new FunctionDef(GenerateSignatureFunction.signatures[1], GenerateSignatureFunction.class),
        new FunctionDef(GenerateSignatureFunction.signatures[2], GenerateSignatureFunction.class),
        new FunctionDef(GenerateSignatureFunction.signatures[3], GenerateSignatureFunction.class),
        new FunctionDef(ValidateSignatureFunction.signature, ValidateSignatureFunction.class),
        new FunctionDef(EncryptionFunctions.signatures[0], EncryptionFunctions.class),
        new FunctionDef(EncryptionFunctions.signatures[1], EncryptionFunctions.class)
    };

	public Sequence eval(Sequence[] args, Sequence contextSequence) throws XPathException {
            String result = null;
            String functionName = getSignature().getName().getLocalName();
            String sep = System.getProperty("file.separator");
            String eXistHome = System.getProperty("exist.home");
            String dbfilePath = eXistHome + sep + "lib" + sep + "extensions" + sep + "keo" + sep + "GeoIP" + sep + "GeoLiteCity.dat";

            try {
                LookupService cl = new LookupService( dbfilePath, LookupService.GEOIP_MEMORY_CACHE);
                Location l1 = cl.getLocation( args[0].getStringValue() );
                    if("get-country-code".equals(functionName)) {
                        result = l1.countryCode;
                    } else if("get-country-name".equals(functionName)) {
                        result = l1.countryName;
                    } else if("get-city".equals(functionName)) {
                        result = l1.city;
                    } else if("get-time-zone".equals(functionName)) {
                        result = timeZone.timeZoneByCountryAndRegion(l1.countryCode, l1.region);
                    } else if("get-distance".equals(functionName)) {
                        Location l2 = cl.getLocation( args[1].getStringValue() );
                        result = Double.toString( l2.distance( l1 ) );
                    }
                    cl.close();
                } catch (IOException ex) {
                    java.util.logging.Logger.getLogger(IpBasedFunctions.class.getName()).log(Level.SEVERE, null, ex);
                }

            return new StringValue(result);
     }
}
