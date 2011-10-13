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

/**
 * tuProlog Null Term, used only for implementation purpose.
 *
 * currently used only as terminator in Struct
 * representing prolog list.
 *
 *
 */
public class NullTerm extends Term {

	public final static NullTerm NULL_TERM = new NullTerm(); 

    private NullTerm() {
    }

    // checking type and properties of the Term

    /** is this term a prolog numeric term? */
    public boolean isNumber(){
        return false;
    }

    /** is this term a struct  */
    public boolean isStruct(){
        return false;
    }

    /** is this term a variable  */
    public boolean isVar(){
        return false;
    }

    public boolean isEmptyList(){
        return false;
    }

    //

    public boolean isAtomic(){
        return true;
    }

    public boolean isCompound(){
        return false;
    }

    public boolean isAtom(){
        return false;
    }

    public boolean isList(){
        return false;
    }

    public boolean isGround(){
        return true;
    }

    public boolean isGreater(Term t){
        return false;
    }

    public boolean isEqual(Term t){
        return false;
    }

    public Term copy(int idExecCtx){
        return this;
    }

    public Term getTerm(){
        return this;
    }


    int renameOuterVars(int count, int antialias){
        return antialias;
    }

    long resolveTerm(long count){
        return count;
    }

    public void free(){
    }

    Term copy(AbstractMap vMap, int idExecCtx){
        return this;
    }

    Term copy(AbstractMap vMap, AbstractMap substMap){
        return this;
    }

    boolean unify(List varsUnifiedArg1, List varsUnifiedArg2, Term t){
        return false;
    }

}