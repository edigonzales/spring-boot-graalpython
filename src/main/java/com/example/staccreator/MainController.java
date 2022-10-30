package com.example.staccreator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.Set;

import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Source;
import org.graalvm.polyglot.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
public class MainController {

    @GetMapping("/ping")
    public ResponseEntity<String> ping() {
        return new ResponseEntity<String>("staccreator", HttpStatus.OK);
    }
    
    @GetMapping("/foo")
    public String foo() throws FileNotFoundException {
        
        String PYTHON = "python";
        String VENV_EXECUTABLE = MainController.class.getClassLoader().getResource(Paths.get("venv", "bin", "graalpy").toString()).getPath();
        String SOURCE_FILE_NAME = "staccreator.py";
                
        Context context = Context.newBuilder(PYTHON).
                allowAllAccess(true).
                option("python.Executable", VENV_EXECUTABLE).
                option("python.ForceImportSite", "true").
                build();
        
        context.eval("python", "print(\"Hello World\")");
        
        InputStreamReader code = new InputStreamReader(MainController.class.getClassLoader().getResourceAsStream(SOURCE_FILE_NAME));               
        Source source;
        try {
            source = Source.newBuilder(PYTHON, code, SOURCE_FILE_NAME).build();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        
        System.out.println(source);
        
        context.eval(source);
              
        Set<String> keys = context.getPolyglotBindings().getMemberKeys();
        System.out.println(keys.size());

//        Iterator<String> it = keys.iterator();
//        while(it.hasNext()) {
//            System.out.println(it.next());
//        }

                
//        Value pystacCreatorClass = context.getPolyglotBindings().getMember("StacCreator");
//        Value pystacCreator = pystacCreatorClass.newInstance();
        
//        StacCreator stacCreator = pystacCreator.as(StacCreator.class);
//        stacCreator.create("dummy");

        
        context.close(); 

        
        return "foo";
    }

}
