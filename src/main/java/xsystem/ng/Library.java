/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package xsystem.ng;

import org.apache.log4j.BasicConfigurator;
import org.javatuples.Pair;

import xsystem.ng.Learning.LabelAssignment;

public class Library {
    public boolean someLibraryMethod() {
        new LabelAssignment();
        BasicConfigurator.configure();
        for(Pair<XStructure, String> p : LabelAssignment.learnedXwithLabel){
            System.out.println(p.getValue0().toString() + "  " + p.getValue1());
        }
        return true;
    }
}
