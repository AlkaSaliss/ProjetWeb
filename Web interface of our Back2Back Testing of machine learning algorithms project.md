# Web interface of our Back2Back Testing of machine learning algorithms project

## A quick introduction to Vaadin Framework
![](/uploads/upload_23d080cdd8dee557abe3f8528e31b699.png)

Vaadin is an open-source web framework for developing web applications in Java. This framework was introduced in 2002 and became a commercial product in 2006. Vaadin is an easy-to-use library that claims to "prioritize ease of development and uncompromised end user experience". 

Vaadin comes with a collection of *JAR* files that can be downloaded or integrated via a Maven project. Besides, Eclipse provides Vaadin plugins (available in the Eclipse marketplace).

Vaadin is built on top of Google Web Toolkit (GWT). It has a set of components precompiled in GWT that could also be customized to adjust our own needs.

The framework is server driven. The components have two parts : client and server. The client simply handles a "view" of the components. When we interact with the components on the client side, it sends a message to the server (e.g. something has been written or a button has been pressed). Then, the server runs the functionality related to the actions made on the client. 

Vaadin appears to us to be a very interesting tool to have a web interface built on our previous Java application without too much effort. 

## User experience
- We aim to build a simple interface, allowing to launch ML algorithms comparison procedures for any user, by hiding the internal complexity of each library.
- Dashboard-like
- The user just uploads some data (*csv* files), set some parameters, choose what kind of ML algorithms to test, and then launch the comparisons. The results will be directly displayed in the application.

Below, we provide a "view" of each component:

## 1. Home page: 
![](/uploads/upload_a3c40db34e4d9988aa8b50936c639541.png)

## 2. Data view: 
The user specifies parameters needed to upload a generic dataset.
The application can handle continuous and categorical target variable as well as continuous or categorical features.
![](/uploads/upload_88984a9c8ef766cfd4777cfd345b88f6.png)

Once the csv is uploaded, the user gets a summary of the paramaters he typed.
![](/uploads/upload_783a2bc8a59536026eac69a10dbe8604.png)
## 3. Decision Tree view:
If the user clicked on the Decision Tree button on the previous page, he will get this view where he can choose the hyperparameters of the model:
![](/uploads/upload_598ba9e85bff431efdfe275c987d21f4.png)
Then, the user gets a summary of the hyparameters and can finally launch the comparison of our ML libaries.
![](/uploads/upload_9e66ab224728dde76708d75c90a81a96.png)
## 4. Final view with the comparison:
![](/uploads/upload_1763ab547c7258cea09e2aee382ba94d.png)





## A few comments on our web application:

- Everything is connected to the MyUI class. It's the heart of the application and is designed by a singleton pattern. The MyUI class controls the views/pages of the application and can be seen as a "main" of the web part.
- On our application, we have a home page and 3 other tabs : one for uploading the dataset and then two for the implemented ML algorithms. We wanted with those tabs to recreate the graphical interface of dashboards.
- All those tabs are called views in Vaadin and are managed by the MyUI class. 
- The tabs are all communicating and are not completly independent from each other. Actually, by default in Vaadin when we are switching from one tab to another, the page refreshes and we loose the information entered on the previous tab. For example, if the user is uploading a file and then run a Decision Tree we won't be able to keep the paramaters about the uploaded dataset. In our implementation, we avoided this behaviour by storing any information typed by the user in the MyUI class (as attributes).
- We added a few controls specially when uploading a file. We specified some fields as required (marked with a red asterisk). If at least one of the required fields isn't filled by the user, he will get a warning and the application won't be able to upload the file.

![](/uploads/upload_04d7c7d14aa3adbf1b867440faa1115f.png)


- Moreover, once the csv file is updloaded, it is stored in the project repository (on local) and can be deleted when the user click on the restore button. We could improve the storage of the file by handling sessions : a user would have a session and when the session is closed all the temporary data (dataset, models results) created during the session would be deleted. 
- We also provided default values for the others views. The views DecisionTree and RandomForest have default values like R or other libraries would provide. We don't necessarly need to fix all hyperparameters to run a model (we just need a dataset).
- Finally, Vaadin provides a high level of abstraction allowing us not to code any Servlet. Vaadin makes the translation from its own language to Servlet. The only time a Servlet appears in the code are in the class WebConfig, a class that is automatically generated. 

### Authors
ABOUBACAR ALKA Mahamadou Salissou
FORMAL Thibault
GIRARD Naomi
KEITA Seydou