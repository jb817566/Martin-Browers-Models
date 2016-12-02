/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wcupa.cscclub.projects.martinbrower.reportgenerator.model;

/**
 *
 * @author hhbhagat
 */
public class ProductStack implements Comparable {

    public String Route, WRIN, trailerPosition, Description;
    public int Cases, Stop;

    //arranges MB in ascending order
    @Override
    public int compareTo(Object o) {
        return ((ProductStack) o).Cases - this.Cases;
    }

}
