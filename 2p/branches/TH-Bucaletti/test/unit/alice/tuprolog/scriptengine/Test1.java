/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package alice.tuprolog.scriptengine;

import alice.tuprolog.InvalidTheoryException;
import alice.tuprolog.SolveInfo;
import alice.tuprolog.Theory;
import alice.tuprolog.scriptengine.PrologScriptEngine;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.swing.JFileChooser;

/**
 *
 * @author Andrea
 */
public class Test1 {
    public static void main(String[] args) throws ScriptException,InvalidTheoryException, IOException {
        //ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = new PrologScriptEngine();
        JFileChooser fileChooser = new JFileChooser();
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        Theory theory;
        
        if(fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            theory = new Theory(new FileInputStream(fileChooser.getSelectedFile()));  
            engine.put(PrologScriptEngine.THEORY, theory);
        }
       
        
        while(true) {
            System.out.print("Goal: ");
            String goal = reader.readLine();
            engine.eval(goal);
            System.out.println(engine.getBindings(ScriptContext.ENGINE_SCOPE));
        }  
    }
}
