package KanbanRequirements;

import repast.simphony.context.Context;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.parameter.Parameters;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridPoint;
import repast.simphony.util.ContextUtils;

public class SpecificationKB {

	private Grid<Object> grid;
	Parameters params = RunEnvironment.getInstance().getParameters();
	int ProbRS =params.getInteger("ProbRS");
	
	public SpecificationKB(Grid<Object> grid) {
		this.grid = grid;
	}

	// calculate the state for the next time tick for Specifications
	@ScheduledMethod(start = 1, interval = 1, priority = 4)
	public void step1() {
		int prob = RandomHelper.nextIntFromTo(0,ProbRS);
				
		// Lo ponemos aqui durante las pruebas.
		Context context = ContextUtils.getContext(this);
				
		if(prob<1){  
			//se crea un requerimiento sobrevenido desde una especificacion
			GridPoint gpt = grid.getLocation(this);
			//Context context = ContextUtils.getContext(this);
			//borramos la spec
			context.remove(this);
			
			//creamos el requerimiento
			RequirementKB overComingReq = new RequirementKB(grid,1);
			context.add(overComingReq);
			grid.moveTo(overComingReq, gpt.getX(), gpt.getY());

			// para depurar
			/*
			 * System.out.printf("Requerimiento lean sobrevenido (%d %d). Totales:%d%n",
			 * gpt.getX(),gpt.getY(),context.getObjects(RequirementKB.class).size());
			 */
						
		}
		
		/**** Aquí podemos poner si queremos una función de control para que no se vaya al infinito.
		 * System.out.printf("Especificaciones lean %d%n",context.getObjects(SpecificationKB.class).size());
		 * if(context.getObjects(SpecificationKB.class).size() == dimension*dimension) {
		 * RunEnvironment.getInstance().endRun();
		 * }
		 *****/
	}
}
