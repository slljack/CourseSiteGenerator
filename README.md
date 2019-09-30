![](App.png)

CourseSiteGenerator
========
Desktop application that builds a custom webpage. This app require no programming knowledge to its users. It will prompt the user to input information about the webpage. The user can choose the style of the page, and things they want to import such as charts and images. Unfinished work will be saved as a json file and user can load and work on their unfinished work. 

### Transactions
All transactions within the app are saved in a stack. Users can undo or redo their transactions if they wish so.

***
## Page Design Tab

The tool bar on the top includes create new, open, close, save, generate web Page, Exit, Undo, Redo, Help, Language, and About.
![](PageDesign.png)

## Language Changed
![](Language.png)
## Generated WebPage 
![](FirstPage.png)

*** 
## Event Design Tab
![](Event.png)
## Generated WebPage
![](Schedule.png)

***
## Office Hour Tab
Choose a name and click on the Office Hours table, one can toggle the time slot for the chosen person.
##### Notice the email address imput is invalid, so the add TA tab is disabled. This is one of the fool-proof functions that prevents mistakes.
![](OfficeHourApp.png)

## generated WebPage
![](OfficeHour.png)

### Build order
Property Manager -> jTPS -> DesktopJavaFramework -> CourseSiteGenerator
Make sure also include the javax.json-1.0.4.jar in the library.