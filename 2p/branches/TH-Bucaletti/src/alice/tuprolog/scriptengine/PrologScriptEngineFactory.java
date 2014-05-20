/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package alice.tuprolog.scriptengine;

import java.util.Arrays;
import java.util.List;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;

import alice.util.VersionInfo;

/**
* Implementation of the ScriptEngineFactory interface
* @author Andrea Bucaletti
*/
public class PrologScriptEngineFactory implements ScriptEngineFactory {
    
    public static final PrologScriptEngineFactory DEFAULT_FACTORY = new PrologScriptEngineFactory();
    
    private static final String ENGINE_NAME = "tuProlog";
    
    private static final String LANGUAGE_NAME = "Prolog";
    
    private static final List<String> EXTENSIONS = Arrays.asList("pro", "pl", "2p");
    private static final List<String> MIME_TYPES = Arrays.asList("text/plain");
    private static final List<String> NAMES = Arrays.asList("tuProlog", "prolog");
    

    @Override
    public String getEngineName() {
        return ENGINE_NAME;
    }

    @Override
    public String getEngineVersion() {
        return VersionInfo.getCompleteVersion();
    }

    @Override
    public List<String> getExtensions() {
        return EXTENSIONS;
    }

    @Override
    public List<String> getMimeTypes() {
        return MIME_TYPES;
    }

    @Override
    public List<String> getNames() {
        return NAMES;
    }

    @Override
    public String getLanguageName() {
        return LANGUAGE_NAME;
    }

    @Override
    public String getLanguageVersion() {
    	return VersionInfo.getCompleteVersion();
    }

    @Override
    public Object getParameter(String string) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getMethodCallSyntax(String obj, String methodName, String... args) {
        String result = new String();
        
        result += obj + " <- " + methodName;
        
        if(args.length > 0) {
            result += "(";
            
            for(int i = 0; i < args.length; i++) {
                result += args[i] + (i == args.length - 1 ? ")" : ", ");
            }
        }
        
        return result;
    }

    @Override
    public String getOutputStatement(String string) {
        return "write('" + string + "')";
    }

    @Override
    public String getProgram(String... strings) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ScriptEngine getScriptEngine() {
        return new PrologScriptEngine();
    }
}
