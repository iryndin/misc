ComputeTree
===========

Need to create a framework to process a tree. 
Tree consists of nodes. Each node can make a computation, which returns a result.
Based on this result one or another branch of tree is computed. 
Independent nodes should be computed in parallel. 
A result object is filled while computations are run. 

Example of application: passport control. 

First, validity of passport is checked. It passport is not valid, we do not continue. 
If passport is valid, we should check it against terrorists databases, and
against wanted databases. Wanted databases can be domestic/local or international. 
Terrorists and wanted checks may be run independently in parallel. 
