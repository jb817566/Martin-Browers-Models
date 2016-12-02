/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wcupa.cscclub.projects.martinbrower.reportgenerator.model;

import java.util.ArrayList;

/**
 *
 * @author hhbhagat
 */
public class ReportGenerationStore {

    private ArrayList<Page> pages;
    private final int STACK_TARGET = 21;

    public ReportGenerationStore(ArrayList<Page> pages) {
        this.pages = pages;
    }

    public void GenReport() {
        //BestTargetOfN(STACK_TARGET, null);
    }

    //Target is 21 or lower
    public ArrayList<Integer> BestTargetOfN(int target, ArrayList<Integer> list) {
        int sum = 0;
        for (int num : list) {
            sum += num;
        }
        if (sum > 3 * STACK_TARGET) { //4 stacks
            
        } else if (sum > 2 * STACK_TARGET) { //3 stacks

        } else if (sum > STACK_TARGET) { //2 stacks

        } else {
            return list;
        }
        return null;
    }
}
