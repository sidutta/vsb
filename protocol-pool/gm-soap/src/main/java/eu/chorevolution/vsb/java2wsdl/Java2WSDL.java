package eu.chorevolution.vsb.java2wsdl;
/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;

import org.apache.axis2.description.java2wsdl.Java2WSDLConstants;
import org.apache.axis2.util.CommandLineOptionConstants;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ws.java2wsdl.Java2WSDLCodegenEngine;
import org.apache.ws.java2wsdl.jaxws.JAXWS2WSDLCodegenEngine;
import org.apache.ws.java2wsdl.utils.Java2WSDLCommandLineOption;
import org.apache.ws.java2wsdl.utils.Java2WSDLCommandLineOptionParser;
import org.apache.ws.java2wsdl.utils.Java2WSDLOptionsValidator;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import eu.chorevolution.vsb.gmdl.utils.Constants;

public class Java2WSDL {
  private static Log log = LogFactory.getLog(Java2WSDL.class);

  public static void main(String[] args) throws Exception {
    String[] arguments = {"-o", "/home/siddhartha/Downloads/chor/evolution-service-bus/vsb-manager/src/test/java", 
        "-of", "test", 
        "-sn", "BindingComponent", 
        "-tn", "generated.bindingcomponent.vsb.chorevolution.eu", 
        "-cn", "eu.chorevolution.vsb.bindingcomponent.generated.BindingComponent", 
        "-l", "http://localhost:8888/BindingComponent", 
        "-ptn", "BindingComponent",
        "-st", "document",
        "-u", "literal",
        "-dlb",
        "-soap11BindingName", "BindingComponentPortBinding",
        "-disableSOAP12", "-disableREST"
    };//new String[1];


    Java2WSDLCommandLineOptionParser commandLineOptionParser = new Java2WSDLCommandLineOptionParser(
        arguments);
    if (isJwsOptionEnabled(commandLineOptionParser)){
      JAXWS2WSDLCodegenEngine engine = new JAXWS2WSDLCodegenEngine(commandLineOptionParser.getAllOptions(), arguments);
      engine.generate();
      return;
    }        
    //  validate the arguments
    validateCommandLineOptions(commandLineOptionParser);
    Java2WSDLCodegenEngine engine = new Java2WSDLCodegenEngine(commandLineOptionParser.getAllOptions());
    engine.generate();
    log.info("WSDL created at "+ engine.getOutputFile());
  }

  public void generateWSDL() {

    String configPath = Constants.configFilePath;

    JSONParser parser = new JSONParser();
    JSONObject jsonObject = null;

    try {
      jsonObject = (JSONObject) parser.parse(new FileReader(configPath));
    } catch (IOException | ParseException e) {
      e.printStackTrace();
    }

    String generatedCodePath = Constants.generatedCodePath;
    String target_namespace = Constants.target_namespace;
    String service_name = Constants.soap_service_name;


    String[] arguments = {"-o", generatedCodePath, 
        "-of", "service.wsdl", 
        "-sn", service_name, 
        "-stn", "eu.chorevolution.vsb.bindingcomponent.generated", //"bc.dtsgoogle.bcs.vsb.chorevolution.eu", eu.chorevolution.vsb.bindingcomponent.generated
        "-tn", "eu.chorevolution.vsb.bindingcomponent.generated",
        //"-tp", "eu",

        "-cp", new File("src" + File.separator + "main" + File.separator + "java").getAbsolutePath(),//"../../../../.",//"/home/siddhartha/Downloads/chor" + "/evolution-service-bus/vsb-manager/src/main/java/.",
            "-cn",target_namespace + "." +  service_name, //
            "-l", "http://localhost:8888/" + service_name, 
            "-ptn", service_name,
            "-p2n", "[all, eu.chorevolution.vsb.bindingcomponent.generated]",
            "-st", "document",
            "-u", "literal",
            "-dlb",
            "-soap11BindingName", service_name+"PortBinding",
            "-disableSOAP12", "-disableREST",
            "-efd", "unqualified",
            "-afd", "unqualified",
    };

    Java2WSDLCommandLineOptionParser commandLineOptionParser = new Java2WSDLCommandLineOptionParser(
        arguments);
    if (isJwsOptionEnabled(commandLineOptionParser)){
      JAXWS2WSDLCodegenEngine engine = new JAXWS2WSDLCodegenEngine(commandLineOptionParser.getAllOptions(), arguments);
      try {
        engine.generate();
      } catch (Exception e) {
        e.printStackTrace();
      }
      return;
    }        
    //  validate the arguments
    validateCommandLineOptions(commandLineOptionParser);
    Java2WSDLCodegenEngine engine;
    try {
      engine = new Java2WSDLCodegenEngine(commandLineOptionParser.getAllOptions());
      engine.generate();
      log.info("WSDL created at "+ engine.getOutputFile());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static void printUsage() {
    System.out.println("Usage: java2wsdl [options] -cn <fully qualified class name>");
    System.out.println("\nwhere [options] include:");
    System.out.println("  -o <output location>                    output directory");
    System.out.println("  -of <output file name>                  output file name for the WSDL");
    System.out.println("  -sn <service name>                      service name");
    System.out.println("  -l <one or more soap addresses>         location URIs, comma-delimited");
    System.out.println("  -cp <class path uri>                    list of classpath entries - (urls)");
    System.out.println("  -tn <target namespace>                  target namespace for service");
    System.out.println("  -tp <target namespace prefix>           target namespace prefix for service");
    System.out.println("  -stn <schema target namespace>          target namespace for schema");
    System.out.println("  -stp <schema target namespace prefix>   target namespace prefix for schema");
    System.out.println("  -st <binding style>                     style for the WSDL");
    System.out.println("  -u <binding use>                        use for the WSDL");
    System.out.println("  -nsg <class name>                       fully qualified name of a class that implements NamespaceGenerator");
    System.out.println("  -sg <class name>                        fully qualified name of a class that implements SchemaGenerator");
    System.out.println("  -p2n [<java package>,<namespace] [<java package>,<namespace]... ");
    System.out.println("                                          java package to namespace mapping for argument and return types");
    System.out.println("  -p2n [all, <namespace>]                 to assign all types to a single namespace");
    System.out.println("  -efd <qualified/unqualified>            setting for elementFormDefault (defaults to qualified)");
    System.out.println("  -afd <qualified/unqualified>            setting for attributeFormDefault (defaults to qualified)");
    System.out.println("  -xc class1 -xc class2...                extra class(es) for which schematype must be generated.  ");
    System.out.println("  -wv <1.1/2.0>                           wsdl version - defaults to 1.1 if not specified");
    System.out.println("  -dlb                                    generate schemas conforming to doc/lit/bare style");
    System.out.println("  -dne                                    disallow nillable elements in the generated schema");
    System.out.println("  -doe                                    disallow optional elements in the generated schema");
    System.out.println("  -disableSOAP11                          disable binding generation for SOAP 1.1");
    System.out.println("  -disableSOAP12                          disable binding generation for SOAP 1.2");
    System.out.println("  -disableREST                            disable binding generation for REST");
    System.out.println("  -mpn <messagePartName>                  change the part name of the generated wsdl messages");
    System.out.println("  -ptn <portTypeName>                     port Type name of the WSDL");
    System.out.println("  -soap11BindingName <SOAP 1.1 name>      SOAP 1.1 binding name");
    System.out.println("  -soap12BindingName <SOAP 1.2 name>      SOAP 1.2 binding name");
    System.out.println("  -restBindingName <REST name>            REST binding name");
    System.out.println("  -res <requestElementSuffix>             Adds a suffix to the request elemment");
    System.out.println("  -dat <disallowAnonymousTypes>           Creates a named complex type for the annonymous complex type");
    System.exit(0);
  }


  private static void validateCommandLineOptions(
      Java2WSDLCommandLineOptionParser parser) {
    if (parser.getAllOptions().size() == 0) {
      printUsage();
    } else if (parser.getInvalidOptions(new Java2WSDLOptionsValidator()).size() > 0) {
      printUsage();
    }

  }

  private static boolean isJwsOptionEnabled(Java2WSDLCommandLineOptionParser parser) {
    Map allOptions = parser.getAllOptions();       
    Java2WSDLCommandLineOption option = (Java2WSDLCommandLineOption) allOptions
        .get(Java2WSDLConstants.JAX_WS_SERVICE_OPTION);
    if( option == null){
      return false;
    }
    return true;
  }

}

