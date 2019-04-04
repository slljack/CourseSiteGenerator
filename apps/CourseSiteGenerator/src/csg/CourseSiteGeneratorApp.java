package csg;

import csg.data.CourseSiteGeneratorData;
import csg.files.CourseSiteGeneratorFiles;
import csg.workspace.CourseSiteGeneratorWorkspace;
import djf.AppTemplate;
import djf.components.AppDataComponent;
import djf.components.AppFileComponent;
import djf.components.AppWorkspaceComponent;
import java.io.IOException;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import static javafx.application.Application.launch;


public class CourseSiteGeneratorApp extends AppTemplate {
    
         public static void main(String[] args) {
	Locale.setDefault(Locale.US);
	launch(args);
    }
         
          @Override
    public AppDataComponent buildDataComponent(AppTemplate app) {
        return new CourseSiteGeneratorData(this);
    }

    @Override
    public AppFileComponent buildFileComponent() {
        return new CourseSiteGeneratorFiles(this);
    }

    @Override
    public AppWorkspaceComponent buildWorkspaceComponent(AppTemplate app) {
             try {        
                 return new CourseSiteGeneratorWorkspace(this);
             } catch (IOException ex) {
                 Logger.getLogger(CourseSiteGeneratorApp.class.getName()).log(Level.SEVERE, null, ex);
             }
             return null;
    }
}
