/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.paris.lutece;

import fr.paris.lutece.business.Directive;
import fr.paris.lutece.service.FreemarkerXdocService;
import java.util.ArrayList;
import java.util.List;


/**
 *
 * @author leridons
 */
public class TestFreemarkerMacroApi {

    
    public static void generateCommons() {
        String path = "/home/seboo/DEV/tests/test/src/main/resources/";
        String pathCommons = "/home/seboo/DEV/lutece-plugins/lutece-core/webapp/WEB-INF/templates/";
        String fileName ="test_macros.html";
        
        
        List<String> commonsFileList = getCommonsFileList( );
        
        try {
            
            // generate commons ref
            String refInputFileName = "commons" ;
            
            ArrayList<Directive> refMacros = new ArrayList<>( );
            ArrayList<Directive> refFunctions = new ArrayList<>( );
           
            FreemarkerXdocService.getBeans( refMacros, refFunctions, pathCommons + refInputFileName + ".html" ) ;
            FreemarkerXdocService.generateXdoc( refMacros, refFunctions, pathCommons + refInputFileName + ".html", refInputFileName + "_api.xml" ) ;

            for ( String inputFileName : commonsFileList )
            {
                ArrayList<Directive> macros = new ArrayList<>( );
                ArrayList<Directive> functions = new ArrayList<>( );

                FreemarkerXdocService.getBeans( macros, functions, pathCommons + inputFileName + ".html" ) ;
                FreemarkerXdocService.generateXdoc( refMacros, refFunctions, pathCommons + inputFileName + ".html", inputFileName + "_api.xml" ) ;
                
                FreemarkerXdocService.compareCommonsApi( refMacros, refFunctions, macros, functions, pathCommons + inputFileName + "_err.log" );
            }
            
            
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
        }  
    }
    
    /**
     * 
     * @return 
     */
    private static List<String> getCommonsFileList( )
    {
        List<String> commonsFileList = new ArrayList<>();
        
        commonsFileList.add( "commons_bulma" );
        commonsFileList.add( "commons_bs4_adminlte" );
        commonsFileList.add( "commons_bs4_backport" );
        commonsFileList.add( "commons_bs4_materialkit" );

        return commonsFileList;
    }

    /**
     * 
     */
    public static void testGenerateCommons() {
        String path = "/home/seboo/DEV/tests/test/src/main/resources/";
        String pathCommons = "/home/seboo/DEV/lutece-plugins/lutece-core/webapp/WEB-INF/templates/";

        try {
            
            // unit test
            String inputFileName = "test_macros" ;
            
            ArrayList<Directive> refMacros = new ArrayList<>( );
            ArrayList<Directive> refFunctions = new ArrayList<>( );
           
            FreemarkerXdocService.getBeans( refMacros, refFunctions, pathCommons + inputFileName + ".html" ) ;
            FreemarkerXdocService.generateXdoc( refMacros, refFunctions, pathCommons + inputFileName + ".html", inputFileName + "_api.xml" ) ;
            
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
        }  
    }
}
