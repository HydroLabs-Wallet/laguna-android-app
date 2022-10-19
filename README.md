Features are split by   android modules *api and *impl. 
Each module has interact class which is not and should not be accessible in other modules (by original nova code). 
Thus code is duplicated in some usecases classes.
Particular  blockchain  operations are performed in runtime module

Ui is following MVP pattern with use of Moxy and Ciceron libraries. 