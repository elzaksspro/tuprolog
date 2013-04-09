/*
 * tuProlog - Copyright (C) 2001-2002  aliCE team at deis.unibo.it
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package alice.tuprolog;

import java.util.*;

import alice.tuprolog.interfaces.IEngine;

/**
 * @author Alex Benini
 */
public class Engine /*Castagna 06/2011*/implements IEngine/**/{    

	//PrintStream log;
	State  nextState;
	Term   query;
	Struct startGoal;
	Collection<Var> goalVars;
	int    nDemoSteps;
	ExecutionContext currentContext; 
	//ClauseStore clauseSelector;
	ChoicePointContext currentAlternative;
	ChoicePointStore choicePointSelector;
	boolean mustStop;
	EngineRunner manager;


	public Engine(EngineRunner manager, Term query) {
		this.manager = manager;        
		this.nextState = manager.INIT;
		this.query = query;
		this.mustStop = false;
		
		/*long l = Thread.currentThread().getId();
		String s= "Esecuzione"+l+"_"+Integer.toHexString(this.hashCode())+".txt";
		File file= new File(s);
		try {
			file.createNewFile();
			log=new PrintStream(file);
		} catch (Exception e) {
			System.err.println(s);
			e.printStackTrace();
		} */
	}


	public String toString() {
		try {
			return
					"ExecutionStack: \n"+currentContext+"\n"+
					"ChoicePointStore: \n"+choicePointSelector+"\n\n";
		} catch(Exception ex) { return ""; }
	}

	void mustStop() {
		mustStop = true;
	}

	/**
	 * Core of engine. Finite State Machine
	 */
	StateEnd run() {
		String action;
		//log.println("Siamo in engine.run, thread "+Thread.currentThread().getId());
		//log.println(currentContext);
		
		do {
			if (mustStop) {
				nextState = manager.END_FALSE;
				break;
			}
			//log.println(Thread.currentThread().getId()+" - IL PROSSIMO STATO :");
			action = nextState.toString();
			//log.println(nextState);

			nextState.doJob(this);
			//log.println("Abbiamo eseguito l'azione dello stato, il contesto di esecuzione");
			//log.println(currentContext);

			manager.spy(action, this);
			

		} while (!(nextState instanceof StateEnd));
		nextState.doJob(this);
		//log.println("Abbiamo eseguito l'azione dello stato, il contesto di esecuzione");
		//log.println(currentContext);
		return (StateEnd)(nextState);
	}


	/*
	 * Methods for spyListeners
	 */

	public Term getQuery() {
		return query;
	}

	public int getNumDemoSteps() {
		return nDemoSteps;
	}

	public List<ExecutionContext> getExecutionStack() {
		ArrayList<ExecutionContext> l = new ArrayList<ExecutionContext>();
		ExecutionContext t = currentContext;
		while (t != null) {
			l.add(t);
			t = t.fatherCtx;
		}
		return l;
	}

	public ChoicePointStore getChoicePointStore() {
		return choicePointSelector;
	}

	void prepareGoal() {
		LinkedHashMap<Var,Var> goalVars = new LinkedHashMap<Var, Var>();
		startGoal = (Struct)(query).copyGoal(goalVars,0);
		this.goalVars = goalVars.values();
	}

	//    void cut() {
		//        choicePointSelector.cut(currentContext.depth -1);
		//    }

	void initialize(ExecutionContext eCtx) {
		currentContext = eCtx;
		choicePointSelector = new ChoicePointStore();
		nDemoSteps = 1;
		currentAlternative = null;
	}

}
