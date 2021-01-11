/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.paris.lutece.business;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author seboo
 */
public class Directive {
    
    private String _strName ;
    private String _strType ;
    private ArrayList<Attribute> _attributes = new  ArrayList<>( ) ;

    /**
     * 
     * @return 
     */
    public String getName() {
        return _strName;
    }

    /**
     * 
     * @param strName 
     */
    public void setName(String strName) {
        this._strName = strName;
    }

    /**
     * 
     * @return 
     */
    public String getType() {
        return _strType;
    }

    /**
     * 
     * @param strType 
     */
    public void setType(String strType) {
        this._strType = strType;
    }

    /**
     * 
     * @return 
     */
    public List<Attribute> getAttributes() {
        return (ArrayList<Attribute>) _attributes.clone( );
    }

    /**
     * 
     * @param attributes 
     */
    public void setAttributes( List<Attribute> attributes) {
        this._attributes.clear( );
        this._attributes.addAll( attributes );
    }
    
    /**
     * 
     * @return 
     */
    public void addAttribute( Attribute atr ) {
        _attributes.add( atr );
    }

    /**
     * 
     * @return true if exists
     */
    public boolean existsMandatoryAttribute( )
    {
        for (Attribute attr : _attributes)
        {
            if (attr.isMandatory()) return true ;
        } 
        
        return false;
    }
    
    /**
     * 
     * @return true if exists
     */
    public boolean existsOptionnalAttribute( )
    {
        for (Attribute attr : _attributes)
        {
            if (!attr.isMandatory()) return true ;
        } 
        
        return false;
    }
    
}
