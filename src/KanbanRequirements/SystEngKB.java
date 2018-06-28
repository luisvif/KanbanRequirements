/**
 * 
 */
package KanbanRequirements;

import repast.simphony.context.Context;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridPoint;
import repast.simphony.util.ContextUtils;

/**
 * @author Luis Villar Fidalgo
 *
 */
public class SystEngKB {

 //Variables
 private Grid<Object> grid;
 private int coordx, coordy;
 private RequirementKB requerimiento; // Requerimiento en el que estamos trabajando
 private RequirementKB requerimientoAnt; // El último memorizado
 
 
 //Constructor
 public SystEngKB(Grid<Object> grid) {
		// System engineer will transform requirements into specifications
		this.grid = grid;
		//El ingeniero por defecto arranca desde la esquina inferior.
		coordx = 0; 
		coordy = 0; 
		if(!BuscaSiguiente()){
			System.err.println("Error: no ha encontrado ningún requerimiento KB inicial");
			RunEnvironment.getInstance().endRun();	
		}
		System.out.printf("Primer requerimiento KB encontrado %d %n",requerimiento.hashCode());
		requerimientoAnt=requerimiento;

	}
	
 // Busca siguiente requerimiento
 private boolean BuscaSiguiente(){
	 
	 /* 
	  * Context<Object> context =ContextUtils.getContext(grid);
	  *	if(context.getObjects(RequirementKB.class).size() == 0)
	  *	return false;
	  */
	 
	int cont=0;  // contador para evitar bucle infinito si no hay requerimientos (Doble check)
	while(cont < 900){
		cont++;
		coordy++;
		if(coordy==30) {
			coordy=0;
			coordx++;
		}
		if(coordx==30)
			coordx=0;
			
		Object o =grid.getObjectAt(coordx,coordy);
		if (o instanceof RequirementKB) {
			requerimiento = (RequirementKB)o;
			return true;
		}
	}
	requerimiento = null;
	System.out.println("Hemos terminado, no encuentra ningún requerimiento KB");
	RunEnvironment.getInstance().endRun(); // Hemos acabado
	return false;
 }
 
 
 
 @ScheduledMethod(start = 1, interval = 1, priority = 1)
 public void step() {
	 if(requerimiento== null)
		 if(!BuscaSiguiente())
			 return;
	 if(requerimiento==requerimientoAnt){
		 if(requerimiento.Bajaesfuerzo()){	
			 // Desaparece el requerimiento
			 GridPoint gpt = grid.getLocation(requerimiento); // lo guardamos para la futura spec que lo sustituye
			 Context<Object> context = ContextUtils.getContext(requerimiento);
			 context.remove(requerimiento);
			 // Lo sustituimos por una especificación
			 SpecificationKB overComingSpec = new SpecificationKB(grid);
			 context.add(overComingSpec);
			 grid.moveTo(overComingSpec, gpt.getX(), gpt.getY());
			 //System.out.printf("Nueva spec en kanban (%d %d).%n",gpt.getX(),gpt.getY());
			 BuscaSiguiente(); 
			 requerimientoAnt = null;
			 return;
		 }
		 //Do nothing. we call Bajaesfuerzo in the conditional clause.
	 }
	 // this tick will be lost due to change of requirement.
	 requerimientoAnt = requerimiento;
 }
 
 
} // Fin definición de la clase
