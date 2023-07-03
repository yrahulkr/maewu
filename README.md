# maewu
Mutation analysis framework for End-to-end web UI test suites

DataSet available at https://doi.org/10.5281/zenodo.4737083

Here is the [Published paper](https://yrahulkr.github.io/resources/papers/maewu.pdf). 

To cite the paper, use this bib
```
@INPROCEEDINGS{9609146,
  author={Yandrapally, Rahulkrishna and Mesbah, Ali},
  booktitle={2021 IEEE International Conference on Software Maintenance and Evolution (ICSME)}, 
  title={Mutation Analysis for Assessing End-to-End Web Tests}, 
  year={2021},
  volume={},
  number={},
  pages={183-194},
  doi={10.1109/ICSME52107.2021.00023}}
```

## Instructions

**murun** is the main project which depends on **webmutator** and **jOOX-java-8** and [**crawljax**](https://github.com/crawljax/crawljax). The project has not been tested with the latest version of crawljax but it is the best option to get it working again. Latest version of crawljax requires Java-11, and therefore the whole project will need to be updated to Java-11. However, if that is not something that you want to do, you can try to build the older version of crawljax included in this repository. However, you may have to update selenium and other related libraries for it to work with latest versions of browsers. 

### Requirements

- JDK-8
- maven
- AspectJ

### Running

- [Main file](murun/src/main/java/com/runner/Stub.java) has the code to start a mutation analysis.
- The project **murun** uses AspectJ instrumentation to capture test case execution traces. The tests have to be part of the project. The tests used in paper evaluation are available in [tests](murun/src/main/java/tests).
- The open source web apps used in the evaluation are available in the [dockerApps](dockerApps) folder, each of the app can be started by running the corresponding **run-docker.sh** script. [This](murun/src/main/java/utils/TestCaseExecutor.java) Java code  automatically does the same while performing mutation analysis.
- [PythonScripts](murun/PythonScripts) contains some python scripts that analyze tool outputs. So looking at those scripts might help understand the tool's output.
