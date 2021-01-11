/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.paris.lutece.service;

import fr.paris.lutece.business.Attribute;
import fr.paris.lutece.business.Directive;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 *
 * @author leridons
 */
public class FreemarkerXdocService {

    /**
     * get directives definition from template
     *  
     * @param macros
     * @param functions
     * @param fileName
     */
    public static void getBeans( List<Directive> macros,  List<Directive> functions, String fileName ) throws FileNotFoundException 
    {
        
        try (Scanner scanner = new Scanner( new File(fileName), "UTF-8" )) {
            String text = scanner.useDelimiter("\\A").next();
            
            getBeansForDirective( text, "macro", macros );
            getBeansForDirective( text, "function", functions );
            
        } 

    }
    
    /**
     * 
     * @param text
     * @param directiveName
     * @param macros 
     */
    private static void getBeansForDirective( String text, String directiveName, List<Directive> macros  )
    {
        String START_TAG = "<#" ;
        String END_TAG = ">";
        
        
        int pos = text.indexOf( START_TAG + directiveName + " " ) ;
        boolean hasMacros = pos > -1 ;

        while ( hasMacros ) 
        {
            int endpos = text.indexOf( END_TAG , pos );
            Directive dir = new Directive();

            String dirTxt = text.substring( pos, endpos) ;
            String dirArgs[] = dirTxt.split(" ");

            dir.setName( dirArgs[1] );

            for ( int i = 2 ; i < dirArgs.length ; i++ )
            {
                Attribute attr = new Attribute();
                String arg = dirArgs[i];

                if ("".equals( arg ) ) continue;
                
                if ( arg.indexOf("=") > -1 )
                {
                    attr.setName( arg.substring(0, arg.indexOf("=") ) );
                    String defaultVal = arg.substring(arg.indexOf("=") + 1  ) ;

                    if ( defaultVal.startsWith("\"") )
                    {
                        while ( defaultVal.indexOf( "\"", 1 ) < 0 && i < dirArgs.length )
                        {
                            i++;
                            defaultVal += " " + dirArgs[i];
                        }
                        
                        attr.setType("String");
                        attr.setDefault( "\"" + defaultVal.substring( 1, defaultVal.length() -1 ) + "\"" );
                    }
                    else if ( defaultVal.startsWith("'") )
                    {
                        while ( defaultVal.indexOf( "'", 1 ) < 0 && i < dirArgs.length )
                        {
                            i++;
                            defaultVal += " " + dirArgs[i];
                        }
                        
                        attr.setType("String");
                        attr.setDefault( "'" + defaultVal.substring( 1, defaultVal.length() -1 ) + "'" );
                    }
                    else if ( defaultVal.matches("^-?\\d+$") )
                    {
                        attr.setType("Numeric");
                        attr.setDefault( defaultVal );
                    }
                    else if ( defaultVal.equals("[]") )
                    {
                        attr.setType("List");
                        attr.setDefault( defaultVal );
                    }
                    else if ( ("true".equals( defaultVal ) )  || ( "false".equals( defaultVal ) ) )
                    {
                        attr.setType("Boolean");
                        attr.setDefault( defaultVal );
                    }
                    else
                    {
                        attr.setType("Var");
                        attr.setDefault( defaultVal );
                    }
                }
                else
                {
                    attr.setName( arg );
                }

                if (!"/".equals(attr.getName( ) ) ) 
                {
                    dir.addAttribute( attr );
                }
            }

            macros.add( dir );

            // next directive
            pos = text.indexOf( START_TAG + directiveName + " ", endpos ) ;
            hasMacros = pos > -1 ;
        }
    }
    
    
    /**
     * generate xdoc
     *  
     * @param macros
     * @param functions
     * @param outputFileName
     */
    public static void generateXdoc( List<Directive> macros,  List<Directive> functions, String inputFileName, String outputFileName ) throws FileNotFoundException, UnsupportedEncodingException 
    {
        PrintWriter output = new PrintWriter( outputFileName, "UTF-8");
        
        output.println ( "<?xml version=\"1.0\" encoding=\"UTF-8\" ?> ");
        output.println ( "<document>");
        output.println ( "    <properties>");
        output.println ( "        <title>Freemarker XDoc for : " + inputFileName + "</title>");
        output.println ( "    </properties>");
        output.println ( "    <body>");
        
        output.println ( "        <section name=\"Macros\">");        
        for ( Directive dir : macros.stream().sorted( (at1, at2) -> at1.getName().compareToIgnoreCase( at2.getName() ) ).collect(Collectors.toList()) )
        {
            printSubSection( dir, output );
        }
        output.println ( "        </section>");
        
        
        output.println ( "        <section name=\"Functions\">");
        for ( Directive dir : functions.stream().sorted( (at1, at2) -> at1.getName().compareToIgnoreCase( at2.getName() ) ).collect(Collectors.toList()) )
        {
            printSubSection( dir, output );
        }
        output.println ( "        </section>");
        
        
        
        
        output.println ( "    </body>");
        output.println ( "</document>");
        output.close( );
        
    }
    
    /**
     * 
     * @param dir
     * @param output 
     */
    private static void printSubSection( Directive dir, PrintWriter output ) 
    {
            
        
            output.println ( "            <subsection name=\"" + dir.getName( ) +"\">");
            
            if ( dir.existsMandatoryAttribute() )
            {
                output.println ( "                <p>Mandatory attributes : ");
                output.println ( "                    <ul>");

                for ( Attribute attr : dir.getAttributes( ).stream().sorted( (at1, at2) -> at1.getName().compareToIgnoreCase( at2.getName() ) ).collect(Collectors.toList()) )
                {
                        if (!attr.isMandatory()) continue;

                        output.print ( "                        <li>");
                        output.print( attr.getName( ) );
                        if ( attr.getType( ) != null ) output.print( " :" + attr.getType( ) );
                        if ( !attr.isMandatory( ) )
                        {
                            output.print( " (default = " + attr.getDefault( ) +")");
                        }
                        output.println ( "</li>");
                }

                output.println ( "                    </ul>");
                output.println ( "                </p>");
            }
            
            if ( dir.existsOptionnalAttribute() )
            {
                output.println ( "                <p>Optionnal attributes : ");
                output.println ( "                    <ul>");

                for ( Attribute attr : dir.getAttributes( ).stream().sorted( (at1, at2) -> at1.getName().compareToIgnoreCase( at2.getName() ) ).collect(Collectors.toList()) )
                {
                        if (attr.isMandatory()) continue;

                        output.print ( "                        <li>");
                        output.print( attr.getName( ) );
                        if ( attr.getType( ) != null ) output.print( " :" + attr.getType( ) );
                        if ( !attr.isMandatory( ) )
                        {
                            output.print( " (default = " + attr.getDefault( ) +")");
                        }
                        output.println ( "</li>");
                }

                output.println ( "                    </ul>");
                output.println ( "                </p>");
            }
            
            output.println ( "            </subsection>");
    }
    
    /**
     * compare api
     *  
     * @param refMacros
     * @param refFunctions
     * @param macros
     * @param functions
     * @param outputFileName
     * 
     * @throws java.io.FileNotFoundException
     * @throws java.io.UnsupportedEncodingException
     */
    public static void compareCommonsApi( List<Directive> refMacros,  List<Directive> refFunctions, List<Directive> macros,  List<Directive> functions, String outputFileName ) throws FileNotFoundException, UnsupportedEncodingException 
    {
        List<String> mErrorList = compare( refMacros, macros );
        List<String> fErrorList = compare( refFunctions, functions );
        
        // output
        PrintWriter output = new PrintWriter( outputFileName, "UTF-8");
        
        output.println ("Freemarker API macros compare : " + outputFileName );
        output.println ("----------------------------------------------------" );
        for ( String err : mErrorList )
        {
            output.println ( err );
        }
        output.println ("----------------------------------------------------" );
        output.println ("Freemarker API functions compare : " + outputFileName );
        output.println ("----------------------------------------------------" );
        for ( String err : fErrorList )
        {
            output.println ( err );
        }
        output.println ("----------------------------------------------------" );
        
        output.close( );
        
    }
    
    
    /**
     * compare api
     *  
     * @param refMacros
     * @param refFunctions
     * @param macros
     * @param functions
     * @param outputFileName
     * 
     * @throws java.io.FileNotFoundException
     * @throws java.io.UnsupportedEncodingException
     */
    public static List<String> compare( List<Directive> listDir1,  List<Directive> listDir2 ) 
    {
        List<String> listErr = new ArrayList<>();
        
        // ...
        
        return listErr;
    }
}
