/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package alice.tuprolog.scriptengine;

import alice.tuprolog.InvalidLibraryException;
import alice.tuprolog.InvalidTheoryException;
import alice.tuprolog.MalformedGoalException;
import alice.tuprolog.NoMoreSolutionException;
import alice.tuprolog.NoSolutionException;
import alice.tuprolog.Prolog;
import alice.tuprolog.SolveInfo;
import alice.tuprolog.Struct;
import alice.tuprolog.Theory;
import alice.tuprolog.Var;
import alice.tuprolog.event.ExceptionEvent;
import alice.tuprolog.event.ExceptionListener;
import alice.tuprolog.lib.InvalidObjectIdException;
import alice.tuprolog.lib.JavaLibrary;
import alice.tuprolog.lib.SimpleIOLibrary;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.script.Bindings;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptException;


/**
 * Implementation of the interface ScriptEngine for tuProlog
 *
 * @author Andrea Bucaletti
 */
public class PrologScriptEngine implements ScriptEngine, ExceptionListener {
  
    public static final String CONTEXT = "context";
    public static final String THEORY = "theory";
    public static final String IS_SUCCESS =  "isSuccess";
    public static final String IS_HALTED = "isHalted";
    public static final String HAS_OPEN_ALTERNATIVES = "hasOpenAlternatives";
    
    protected List<Var> solveVars;
    protected String previousScript;
    protected boolean useSolveNext;
   
    protected ScriptContext defaultContext;
    protected Prolog prolog;
    
    protected SimpleIOLibrary simpleIOLib; /* Test IO */
    
    public PrologScriptEngine() {
        prolog = new Prolog();

        
        defaultContext = new PrologScriptContext(prolog);
        
        /* Test IO */
        try {
            // prolog.unloadLibrary("alice.tuprolog.lib.ISOIOLibrary");
            simpleIOLib = new SimpleIOLibrary();
            prolog.loadLibrary(simpleIOLib);
        }
        catch(InvalidLibraryException ex) {
            ex.printStackTrace();
        }
        /* Fine Test IO */        
        
        useSolveNext = false;
        previousScript = null;
        solveVars = new ArrayList<Var>();
    } 

    @Override
    public Object eval(String string) throws ScriptException {
        return eval(string, getContext());
    }

    @Override
    public Object eval(Reader reader) throws ScriptException {
        return eval(reader, getContext());
    }
    
    @Override
    public Object eval(String script, ScriptContext sc) throws ScriptException {
        /*
        As the jsr-223 part SCR.4.3.4.1.2 Script Execution :
        "In all cases, the ScriptContext used during a script execution must be
        a value in the Engine Scope of the ScriptEngine whose key is the
        String "context"     
         */
        
        /* IO Test */
        simpleIOLib.setStandardInput(sc.getReader());
        simpleIOLib.setStandardOutput(sc.getWriter());
        /* IO Test */
        
        sc.getBindings(ScriptContext.ENGINE_SCOPE).put(CONTEXT, sc);
        return eval(script, sc.getBindings(ScriptContext.ENGINE_SCOPE));
    }

    @Override
    public Object eval(Reader reader, ScriptContext sc) throws ScriptException {
        /*
        As the jsr-223 part SCR.4.3.4.1.2 Script Execution :
        "In all cases, the ScriptContext used during a script execution must be
        a value in the Engine Scope of the ScriptEngine whose key is the
        String "context"     
         */        

       /* IO Test */
        simpleIOLib.setStandardInput(sc.getReader());
        simpleIOLib.setStandardOutput(sc.getWriter());
        /* IO Test */        
        
        sc.getBindings(ScriptContext.ENGINE_SCOPE).put(CONTEXT, sc);
        return eval(reader, sc.getBindings(ScriptContext.ENGINE_SCOPE));
    }    
    
    @Override
    public Object eval(String script, Bindings bndngs) throws ScriptException {
        Theory theory = (Theory)bndngs.get(THEORY);
        SolveInfo info = null;
         
        /*
        As the jsr-223 part SCR.4.2.6 Bindings :
        "Each Java Script Engine has a Bindings known as its Engine Scope
        containing the mappings of script variables to their values. The
        values are often Objects in the host application. Reading the value of
        a key in the Engine Scope of a ScriptEngine returns the value of the
        corresponding script variable. Adding an Object as a value in the
        scope usually makes the object available in scripts using the specified
        key as a variable name. 
        
        So, all the objects in the engine scope are registered using the JavaLibrary,
        if available. This is done using the method 
            boolean register(Struct id, Object obj)
        of the JavaLibrary class. Any exception raised by this method will be
        forwarded, and the Object won't be registered.
         */
        
        JavaLibrary javaLibrary = (JavaLibrary) prolog.getLibrary("alice.tuprolog.lib.JavaLibrary");
        
        if(javaLibrary != null) {
            for(Map.Entry<String, Object> keyPair: bndngs.entrySet()) {
                try {
                    javaLibrary.register(new Struct(keyPair.getKey()), keyPair.getValue());
                }
                catch(InvalidObjectIdException ex) {
                    throw new ScriptException("Could not register object(" + keyPair.getKey() + "): " + ex.getMessage());
                }
            }
        }
        
        try {
            
            if(!script.equals(previousScript))
                useSolveNext = false;
            
            if(theory != null)
                prolog.setTheory(theory);
            
            if(useSolveNext)
                info = prolog.solveNext();
            else
                info = prolog.solve(script);
           
            previousScript = script;

            for(Var v : solveVars) 
                bndngs.remove(v.getName());

            solveVars.clear();

            bndngs.put(IS_SUCCESS, info.isSuccess());
            bndngs.put(IS_HALTED, info.isHalted());
            bndngs.put(HAS_OPEN_ALTERNATIVES, info.hasOpenAlternatives());
            
            if(info.isSuccess()) {
                solveVars = info.getBindingVars();
                for(Var v : solveVars)            
                    bndngs.put(v.getName(), v.getTerm().toString());             
            }
            
            useSolveNext = info.hasOpenAlternatives();
            
            return true;
        }
        catch(NoSolutionException | InvalidTheoryException | 
                MalformedGoalException | NoMoreSolutionException ex) {
            throw new ScriptException(ex);
        }
    }

    @Override
    public Object eval(Reader reader, Bindings bndngs) throws ScriptException {
        BufferedReader bReader = new BufferedReader(reader);
        String script = new String();
        try {
            while(bReader.ready()) {
                script += bReader.readLine();
            }
        }
        catch(IOException ex) {
            throw new ScriptException(ex);
        }
        return eval(script, bndngs);
    }

    @Override
    public Bindings createBindings() {
        return new PrologBindings(prolog);
    }

    @Override
    public ScriptEngineFactory getFactory() {
        return PrologScriptEngineFactory.DEFAULT_FACTORY;
    }

    @Override
    public void put(String key, Object o) {
       getBindings(ScriptContext.ENGINE_SCOPE).put(key, o);
    }

    @Override
    public Object get(String key) {
        return getBindings(ScriptContext.ENGINE_SCOPE).get(key);
    }

    @Override
    public Bindings getBindings(int i) {
        return getContext().getBindings(i);
    }

    @Override
    public void setBindings(Bindings bndngs, int i) {
        getContext().setBindings(bndngs, i);
    }

    @Override
    public ScriptContext getContext() {
        return defaultContext;
    }

    @Override
    public void setContext(ScriptContext sc) {
        defaultContext = sc;
    }

    @Override
    public void onException(ExceptionEvent ee) {
        System.out.println(ee.getMsg());
    }
    
}
