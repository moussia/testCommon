/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.paris.lutece.business;

/**
 *
 * @author seboo
 */
public class Attribute {
    private String _strName ;
    private String _strType ;
    private String _strDefault ;

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
    public boolean isMandatory() {
        return ( _strDefault == null || "".equals( _strDefault ) );
    }


    /**
     * 
     * @return 
     */
    public String getDefault() {
        return _strDefault;
    }

    /**
     * 
     * @param strDefault 
     */
    public void setDefault(String strDefault) {
        this._strDefault = strDefault;
    }
}
