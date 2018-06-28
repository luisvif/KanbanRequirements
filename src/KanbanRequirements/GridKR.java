package KanbanRequirements;

import repast.simphony.context.Context;
import repast.simphony.context.space.grid.GridFactory;
import repast.simphony.context.space.grid.GridFactoryFinder;
import repast.simphony.dataLoader.ContextBuilder;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.parameter.Parameters;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridBuilderParameters;
import repast.simphony.space.grid.SimpleGridAdder;
import repast.simphony.space.grid.WrapAroundBorders;


public class GridKR implements ContextBuilder<Object> {
	
	
	public Context build(Context<Object> context) {
		
		context.setId("KanbanRequirements");
		
		//Cargamos la tabla de parámetros de la simulación
		Parameters params = RunEnvironment.getInstance().getParameters();
		int dimension =params.getInteger("Dimensiones");
		int esfuerzoMax = params.getInteger("EsfuerzoMax");
						
		
		GridFactory gridFactory = GridFactoryFinder.createGridFactory(null);
		
		//Grid para la presentación de tratamiento waterfall
		Grid<Object> grid = gridFactory.createGrid("grid", context,
				new GridBuilderParameters<Object>(new WrapAroundBorders(),
						new SimpleGridAdder<Object>(), false, dimension, dimension));
		//Grid para la presentación de tratamiento agil
		Grid<Object> grid_agil = gridFactory.createGrid("grid_agil", context,
				new GridBuilderParameters<Object>(new WrapAroundBorders(),
						new SimpleGridAdder<Object>(), false, dimension, dimension));
		
		
		int esfuerzo;
		
		//Creamos el campo de requerimientos
		for (int x = 0; x < dimension ; x++) {
			for (int y = 0; y < dimension ; y++) {
				 esfuerzo = RandomHelper.nextIntFromTo(2,esfuerzoMax);
				
				// Requerimientos para waterfall
				Requirement requirement = new Requirement(grid,esfuerzo);
				context.add(requirement);
				grid.moveTo(requirement, x, y);
				//Requerimientos para ágil.
				RequirementKB requirementKB = new RequirementKB(grid_agil,esfuerzo);
				context.add(requirementKB);
				grid_agil.moveTo(requirementKB, x, y);					
			 }
			}
		
		//Creamos los ingenieros de sistemas.		
		SystEng systemEngineer = new SystEng(grid);
		context.add(systemEngineer);
		/*
		 * Por ahora solamente vamos a meter un ingeniero
		 *
		 *systemEngineer = new SystEng(grid);
		 *context.add(systemEngineer);
		 */
		
		// El ingeniero lean lo ponemos en el segundo grid.
		SystEngKB systemEngineerAgil = new SystEngKB(grid_agil);
		context.add(systemEngineerAgil);
				
		/*
		 * Por ahora no vamos a meter nada más que un ingeniero. 
		 *
		 *systemEngineerAgil = new SystEngKB(grid_agil);
		 *context.add(systemEngineerAgil);
		 */
		
		//Si estamos en modo lotes terminamos al alcanzar cierto valor
		if( RunEnvironment.getInstance().isBatch()){
			int termina = (int)params.getValue("NumIteraciones");
			RunEnvironment.getInstance().endAt(termina);
		}
						
		return context;
	}

	public int devuelve10_prueba(){
		return (int) 10;
	}
	
}
