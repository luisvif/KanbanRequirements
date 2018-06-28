package KanbanRequirements;

import repast.simphony.space.grid.Grid;

public class Requirement {

	private Grid<Object> grid;
	private int effort;
	private boolean procesandose;


	public Requirement(Grid<Object> grid, int effort) {
		this.grid = grid;
		this.effort = effort;
		// en 
		procesandose = false;
	}
	
		
	public int identificador(){
			/* este identificador nos permite saber si el 
			 * ingeniero estaba trabajando con este requerimiento la última vez
			 */
			return this.hashCode();
	}
	
	boolean Procesandose(){
		return procesandose;
	}

	void EmpiezoProceso(){
		procesandose =true;
	}
	
	
	public boolean Bajaesfuerzo(){
		effort --;
		if(effort==0)
			return true;
		return false;
	}
}
