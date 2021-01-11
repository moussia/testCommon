/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.paris.lutece;

import fr.paris.lutece.business.DemandType;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.paris.lutece.business.Directive;
import fr.paris.lutece.service.FreemarkerXdocService;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collector;
import java.util.stream.Collectors;


/**
 *
 * @author leridons
 */
public class TestRun {

    
    public static void main(String[] args)  {
        
        TestFreemarkerMacroApi.generateCommons( );
    }
    
   
}
