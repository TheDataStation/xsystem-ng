/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package xsystem;

import xsystem.learning.LearnXStruct;

// import xsystem.learning.LabelAssignment;

public class Library {
    public boolean someLibraryMethod() {
        String path = "src/main/resources/LearningData";
        String outputFile = "src/main/resources/Learned/LearnedXStructs.json";
        LearnXStruct l =  new LearnXStruct();
        l.learnStructs(path, outputFile);
        return true;
    }
}
