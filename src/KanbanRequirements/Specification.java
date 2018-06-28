package KanbanRequirements;

import repast.simphony.context.Context;

import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.parameter.Parameters;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridPoint;
import repast.simphony.util.ContextUtils;



public class Specification {

	private Grid<Object> grid;
	Parameters params = RunEnvironment.getInstance().getParameters();
	int ProbRS =params.getInteger("ProbRS");

	
	
	public Specification(Grid<Object> grid) {
		this.grid = grid;
	}

	// calculate the state for the next time tick for Specifications
	@ScheduledMethod(start = 1, interval = 1, priority = 4)
	public void step1() {
		int prob = RandomHelper.nextIntFromTo(0,ProbRS);
		
		if(prob<1){
			//se crea un requerimiento sobrevenido desde una especificacion
			GridPoint gpt = grid.getLocation(this);
			Context context = ContextUtils.getContext(this);
			//borramos la spec
			context.remove(this);
			
			//creamos el requerimiento
			Requirement overComingReq = new Requirement(grid,1);
			context.add(overComingReq);
			grid.moveTo(overComingReq, gpt.getX(), gpt.getY());
			
			/*
			System.out.printf("Requerimiento waterfall sobrevenido (%d %d). Totales:%d%n",gpt.getX(), 
								gpt.getY(),context.getObjects(Requirement.class).size());	
			*/
			
		}
	}
}
