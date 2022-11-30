# XSystem-NG

[![Generic badge](https://img.shields.io/badge/gradle-6.3-<COLOR>.svg)](https://shields.io/)  [![Version](https://badge.fury.io/gh/tterb%2FHyde.svg)](https://badge.fury.io/gh/tterb%2FHyde)

**XSystem** is a method to learn and represent syntactic patterns in datasets as data structures called **XStructures**. Once **XSystem** learns a collection of patterns, it can be used to perform several tasks like: **_automatic label assignment_**, where data items are assigned a class by comparing them to a library of known classes (written as **Regexes** or **XStructures**); finding syntactically similar content, where learned **XStructures** are compared to see if they are similar, and **_outlier detection_**, where a learned **XStructure** for a single item is compared to other **XStructures** to check that its structure is different.

See the associated research paper [here](https://ieeexplore.ieee.org/abstract/document/8509235?casa_token=5yDC4o3mpNwAAAAA:ZnOFDnD0aoOIJLBsttKuBCC_VAUWCJ27OGQxZS0xszg5vo2vGVmg-_FT8jY6sWCNrcsaK671vLg).

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.

### Prerequisites
1. Java
2. (Optional) Gradle.

### Installing

#### Downloading source code
Enter the following command in your console to clone the repository.
```bash
$> git clone https://github.com/UCHI-DB/xsystem-ng.git
```
#### Adding the Wrapper JAR
Enter the following command in your console to add Gradle Wrapper JAR
```bash
$> gradle wrapper
```

#### Compiling and running tests
Since XSystem is a gradle project and you have already included a gradle wrapper, you do not
need to install gradle to build and test the project. Instead, you can just:
```bash
$> ./gradlew build
```

## Running Tests
And now to run the unit tests, run the following command!
```bash
$> ./gradlew test
```

## Functionality of XSystem
The XSystem Inteface has been implemented by `XSystemImplementation` class. To use the methods, first create an instance of the `XSystemImplementation` class.

```Java

XSystemImplementation impl = new XSystemImplementation();

```

The methods available are as follows - 

* **`build` method**

Given `lines`(an ArrayList of String), the build method learns the line into a XStructure, and returns the XStructure.

```Java

XStructure xstruct = impl.build(lines); 

```

* **`generate` method**

Given a XStructure `pattern`, and an integer `n`, the generate method returns an ArrayList of n random Strings generated from the pattern.

```Java

ArrayList<String> randomString = impl.generate(pattern, n); 

```

* **`similarity` method**

Given two XStructures, `x1` and `x2` or given a XStructure `pattern` and a String `str`, this method returns the similarity score between the two.

```Java

double similarity = impl.similarity(x1, x2); 

```

***OR***

```Java

double similarity = impl.similarity(pattern, str); 

```

* **`match` method**

This method returns a boolean indicating whether XStructure `pattern` and regular expression Pattern `regex` match with each other or not.

```Java

boolean match = impl.match(pattern, regex); 

```


* **`computeOutlierScore` method**

Computes the outlier score for a given string `str`, in the given XStructure `pattern`

```Java

double outlierScore = impl.computeOutlierScore(pattern, str); 

```

* **`mergetwoXStructs` method**

Merges two XStructures `x1` and `x2`.

```Java

XStructure merged = impl.mergetwoXStructs(x1, x2); 

```

* **`mergeMultipleXStructs` method**

Merges a list (ArrayList) of XStructures `xstructList`

```Java

XStructure merged = impl.mergetwoXStructs(xstructList); 

```


* **`learnXStructs` method**

Given an input CSV file/folder with path - `inputPath`, this method learns the XStructs for each column and stores in a specified JSON file path, `outFile`.

```Java

impl.learnXStructs(inputPath, outFile); 

```

* **`readXStructswthType` method**

Given a JSON file/folder path `inputJSONfolder`, this method returns the list of pair XStructures and their corresponding datatype contained in it.

```Java

ArrayList<Pair<XStructure, String>> list = impl.readXStructswthType(inputJSONfolder); 

```


* **`labelAssignwthRegex` method**

Given a regex-type CSV file/folder path `sampleRegexFolderPath`, assigns labels to a JSON file/folder of learned XStructs (path `learnedXStructsJSONfolderpath`) and outputs a CSV file whose path can be specified by the user - `outFile`

```Java

impl.labelAssignwthRegex(sampleRegexFolderPath, learnedXStructsJSONfolderpath, outFile); 

```

* **`labelAssignmentwthXStruct` method**

Given a ArrayList of String inputs - `lines`, returns the computed label from a given JSON file/folder (path `referenceFilePath`) of learned XStructs

```Java

String label = impl.labelAssignmentwthXStruct(lines, referenceFilePath); 

```


## Authors

* **Ipsita Mohanty**
* **Raul Castro Fernandez**

This XSystem implementation is based on [this
paper](https://ieeexplore.ieee.org/abstract/document/8509235?casa_token=5yDC4o3mpNwAAAAA:ZnOFDnD0aoOIJLBsttKuBCC_VAUWCJ27OGQxZS0xszg5vo2vGVmg-_FT8jY6sWCNrcsaK671vLg).

