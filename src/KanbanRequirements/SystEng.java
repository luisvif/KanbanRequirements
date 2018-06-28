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
public class SystEng {

	//Variables
	private Grid<Object> grid;
	private Requirement requerimiento; //Requerimiento en el que estamos trabajando
	private Requirement requerimientoAnt; // El último memorizado

	//Constructor
	public SystEng(Grid<Object> grid) {
		// System engineer will transform requirements into specifications
		this.grid = grid;
		if(!BuscaSiguiente()){
			System.err.println("Error: no ha encontrado ningún requerimiento inicial");
			RunEnvironment.getInstance().endRun();	
		}
		System.out.printf("Primer requerimiento encontrado %d %n",requerimiento.hashCode());
		requerimientoAnt=requerimiento;
	}
	
	//Busca siguiente requerimiento
	private boolean BuscaSiguiente(){
		for (int x = 0; x < 30 ; x++) {
			for (int y = 0; y < 30 ; y++) {
				Object o =grid.getObjectAt(x,y);
				// usamos el doble && luego es seguro que es un requerimiento
				// Si buscamos que no se esté reprocesando:  && !((Requirement) o).Procesandose() 
				if (o instanceof Requirement){
					//((Requirement) o).EmpiezoProceso();
					requerimiento = (Requirement) o;
					//System.out.printf("requerimiento encontrado %d %n",requerimiento.hashCode());
					return true;
				}
			}
		}
		requerimiento = null;
		return false;
	}
		
	// 
	@ScheduledMethod(start = 1, interval = 1, priority = 1)
	public void step() {

		if(BuscaSiguiente()){
			if(requerimiento==requerimientoAnt){	
				if(requerimiento.Bajaesfuerzo()){  // Hemos realizado todo el esfuerzo	
					// Desaparece el requerimiento
					GridPoint gpt = grid.getLocation(requerimiento); // lo guardamos para la futura spec que lo sustituye
					Context<Object> context = ContextUtils.getContext(requerimiento);
					context.remove(requerimiento);
					// Lo sustituimos por una especificación
					Specification overComingSpec = new Specification(grid);
					context.add(overComingSpec);
					grid.moveTo(overComingSpec, gpt.getX(), gpt.getY());
					//System.out.printf("Nueva spec en waterfall (%d %d).%n",gpt.getX(),gpt.getY());
					requerimiento = null;
					return;
				}
				// No hay que hacer nada, porque ya hemos llamado a la función Bajaesfuerzo en la condición
			}
			requerimientoAnt = requerimiento;
			//System.out.printf("%d %d %n",requerimientoAnt.hashCode(),requerimiento.hashCode());
		}
		else{
			System.err.println("Hemos terminado, no encuentra requerimiento WF");
			RunEnvironment.getInstance().endRun();	
		}
			
	}
}
