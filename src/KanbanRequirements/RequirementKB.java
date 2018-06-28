package KanbanRequirements;

import repast.simphony.space.grid.Grid;

public class RequirementKB {

	private Grid<Object> grid;
	private int effort;


	public RequirementKB(Grid<Object> grid, int effort) {
		this.grid = grid;
		this.effort = effort;
	}


	public int identificador(){
		return this.hashCode();
		// este identificador nos permite saber si el ingeniero estaba trabajando con este requerimiento la última vez
		
	}

	
	// Función que reduce el esfuerzo de convertir un requerimiento en una especificación.
	public boolean Bajaesfuerzo(){
		effort --;
		if(effort==0)
			return true;
		return false;
	}


}
