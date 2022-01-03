
package uk.ac.nott.cs.g53dia.multiagent;

import java.util.ArrayList;
import java.util.Random;


import uk.ac.nott.cs.g53dia.multilibrary.*;
/**
 * A simple example Tanker
 * 
 * @author Julian Zappala
 */
/*
 * Copyright (c) 2011 Julian Zappala
 * 
 * See the file "license.terms" for information on usage and redistribution of
 * this file, and for a DISCLAIMER OF ALL WARRANTIES.
 */
public class DemoTanker extends Tanker {
	int index,counter;
	final int FUEL_BIAS =6;
	Pair agentPos,goal = null;
	static ArrayList<Pair> stationTask = new ArrayList<Pair>();
	static ArrayList<Pair> allAgentPos = new ArrayList<Pair>();
	static ArrayList<Integer> task = new ArrayList<Integer>();
	ArrayList<Pair> individualTask = new ArrayList<Pair>(); 
	static ArrayList<Pair> fuelPump = new ArrayList<Pair>();
	static ArrayList<Pair> well = new ArrayList<Pair>();
	
	
	public DemoTanker() {
		this(new Random(),0);
	}
	
	/**
	 * The tanker implementation makes random moves. For reproducibility, it
	 * can share the same random number generator as the environment.
	 * 
	 * @param r
	 *            The random number generator.
	 */
	public DemoTanker(Random r, int i) {
		this.r = r;
		index =i;
		stationTask.clear();
		task.clear();
		individualTask.clear();
		fuelPump.clear();
		well.clear();
		if(allAgentPos.size()>index) {
			allAgentPos.clear();
		}
		agentPos = new Pair(index,0,0);
		allAgentPos.add(agentPos);
		counter = i%7;
	}
	
	// update the position of the agent
	public void updatePosition(int dir) {
	final int NORTH = 0, SOUTH = 1, EAST = 2, WEST = 3, NORTHEAST = 4, NORTHWEST = 5, SOUTHEAST = 6, SOUTHWEST = 7;
			switch (dir) {
			case NORTH:
				agentPos.y++;
				break;
			case SOUTH:
				agentPos.y--;
				break;
			case EAST:
				agentPos.x++;
				break;
			case WEST:
				agentPos.x--;
				break;
			case NORTHEAST:
				agentPos.x++;
				agentPos.y++;
				break;
			case NORTHWEST:
				agentPos.x--;
				agentPos.y++;
				break;
			case SOUTHEAST:
				agentPos.x++;
				agentPos.y--;
				break;
			case SOUTHWEST:
				agentPos.x--;
				agentPos.y--;
				break;
			
		}
		allAgentPos.set(index,agentPos);
	}
	
	
	// record every item seen by the agent
	public void RecordItem(Cell[][] view) {
		for(int i=0; i < view.length ; i++) {
			for(int j=0 ; j < view[0].length; j++) {
				if(view[i][j] instanceof Station) {
					Station s = (Station)view[i][j];
					if(s.getTask() != null) {
						if(!stationTask.contains(new Pair(s, i-20+agentPos.x,20-j+agentPos.y)) ) {
								stationTask.add(new Pair(s, i-20+agentPos.x,20-j+agentPos.y));
						}
					}
				}
				if(view[i][j] instanceof FuelPump) {
					if(!fuelPump.contains(new Pair(view[i][j], i-20+agentPos.x,20-j+agentPos.y) ) )  {
						fuelPump.add(  new Pair(view[i][j], i-20+agentPos.x,20-j+agentPos.y) );
					}
				}
				if(view[i][j] instanceof Well) {
					if(!well.contains(new Pair(view[i][j], i-20+agentPos.x,20-j+agentPos.y) ) )  {
						well.add(new Pair(view[i][j], i-20+agentPos.x,20-j+agentPos.y) );
					}
				}
			}
		}
		
		
	}
		
	
	public Action MoveToGoal(Pair goal,Cell[][] view) {
		int dir=0;
		int dx =goal.getx() - this.agentPos.x , dy = goal.gety()- this.agentPos.y;
		if(goal.c.getPoint().equals(getPosition())) {
			
		}
		//north
		else if(dy >0 && dx == 0) {
			dir =0;
		}
		//south
		else if(dy<0 && dx == 0) {
			dir =1;
		}
		//east
		else if(dx >0 && dy ==0) {
			dir =2;
		}
		//west
		else if(dx < 0 && dy ==0 ) {
			dir =3;
		}
		//northeast
		else if (dx > 0 && dy > 0 ) {
			dir = 4;
		}
		//northwest
		else if (dx < 0 && dy > 0) {
			dir =5;
		}
		//southeast
		else if(dx > 0 && dy < 0) {
			dir =6;
		}
		//southwest
		else if(dx < 0 &&  dy < 0) {
			dir  =7;
		}
		updatePosition(dir);
		return new MoveAction(dir);
		
	}

	
	public Pair FindClosestObject(ArrayList<Pair> ObjectList) {
		int distance =-1 ;
		Pair closestObject =null;
		for(Pair z : ObjectList) {
			int dis = Math.max( Math.abs(z.getx()-agentPos.x) , Math.abs(z.gety()-agentPos.y ) );
			if(distance == -1) {
				distance = dis;
				closestObject =z;
			}
			else if(distance > dis) {
				distance = dis;
				closestObject = z;
			}
		}
		return closestObject;
	}
	
	public Pair FindClosestObject(ArrayList<Pair> ObjectList, Pair object) {
		int distance =-1 ;
		Pair closestObject =null;
		for(Pair z : ObjectList) {
			int dis = CalculateDistance(z,object);
			if(distance == -1) {
				distance = dis;
				closestObject =z;
			}
			else if(distance > dis) {
				distance = dis;
				closestObject = z;
			}
		}
		return closestObject;
	}
	
	// calculate distance form the object to the agent
	public int CalculateDistance(Pair o) {
		return Math.max( Math.abs(o.getx() - agentPos.x) , Math.abs(o.gety() - agentPos.y ) );
	}
	
	// calculate distance between two object
	public int CalculateDistance(Pair o1,Pair o2) {
		return Math.max( Math.abs(o1.getx() - o2.getx()) , Math.abs(o1.gety() - o2.gety() ) );
	}
	
	// check if the agent goal is a well
	public boolean isNotWell() {

		if(goal == null) {
			return true;
		}
		else {
			if(goal.c instanceof Well)
				return false;
			return true;
		}
	}
	
	// random move in one direction
	public Action Explore() {
		updatePosition(counter);
		return new MoveAction(counter);
	}
	
	// assign task to each agent
	public void assignTask(ArrayList<Pair> stationList) {
		task.clear();
		for(Pair s:stationList) {
			int distance =-1 ;
			int closestAgent =-1;
			for(Pair z : allAgentPos) {
				int dis = CalculateDistance(z,s);
				if(distance == -1) {
					distance = dis;
					closestAgent =z.index;
				}
				else if(distance > dis) {
					distance = dis;
					closestAgent = z.index;
				}
			}
			task.add(closestAgent);
		}
		
	}
	

	/*
	 * The following is a simple demonstration of how to write a tanker. The
	 * code below is very stupid and simply moves the tanker randomly until the
	 * fuel tank is half full, at which point it returns to a fuel pump.
	 */
	public Action senseAndAct(Cell[][] view, boolean actionFailed, long timestep) {

		individualTask = new ArrayList<Pair>();
		// when the list of task is not empty
		if(!stationTask.isEmpty()) {
			assignTask(stationTask);
			for(int t=0; t<task.size();t++) {
				if(task.get(t)==index) {
					individualTask.add(stationTask.get(t));
				}
			}
		}

		// add everything sees in the view include position of the station well and fuel station into a memory
		RecordItem(view);
		//if current position is a Well and the tanker is holding waste
		if((getCurrentCell(view)) instanceof Well && getWasteLevel() != 0) {
			goal = null;
			return new DisposeWasteAction();
			
		}
		// if the current position is a Station and the station has a task then collect waste
		if((getCurrentCell(view)) instanceof Station) {
			if(((Station)getCurrentCell(view)).getTask() != null && getWasteLevel() < getWasteCapacity()) {
				goal =null;
				stationTask.remove(new Pair(((Station)getCurrentCell(view)),agentPos.x,agentPos.y));
				return new LoadWasteAction(((Station)getCurrentCell(view)).getTask());
			}
			
		}
		// if the current position is a FuelPump then refuel and change the random move direction
		if((getFuelLevel() != MAX_FUEL) && (getCurrentCell(view)) instanceof FuelPump) {
			counter++;
			if(counter>7) {
				counter =0;
			}
			goal =null;
			return new RefuelAction();
		}
		
		// always keep track of the nearest fuel pump
		Pair fuelStation = FindClosestObject(fuelPump);
		// if current position is too far away from the nearest fuel pump , then head to the nearest fuel pump
		if(CalculateDistance(fuelStation)*2 +FUEL_BIAS > getFuelLevel()) {
			goal = fuelStation;
			return MoveToGoal(goal,view);
		}
		
		// there are station with task
		if(!individualTask.isEmpty()) {
			// if current waste level is 0 , go to the nearest station with Task
			if(getWasteLevel() == 0) {
					Pair closestStation= FindClosestObject(individualTask), closestF= FindClosestObject(fuelPump,closestStation);
					int distanceS =CalculateDistance(closestStation), distanceF =CalculateDistance(closestStation,closestF);
					// if the agent dont have enough fuel to reach the Station and then to fuelPump
					if((distanceS+distanceF)*2+FUEL_BIAS > getFuelLevel()) {
						// if the agent tank is not full
						if(getFuelLevel() < MAX_FUEL) {
							// return to the nearest station when the fuel tank is less than half
							if(getFuelLevel()  < MAX_FUEL/2 ) {
								goal=fuelStation;
								return MoveToGoal(goal,view);
							}
							goal = closestF;
							return MoveToGoal(goal,view);
							
						}
						// if the agent fuel is full or is at a fuel station
						else {
							// if the agent is at the refueling point closer to the station
							if(closestF.c.getPoint().equals(getCurrentCell(view).getPoint())){
								stationTask.remove(closestStation);
								return Explore();
							}
							// if not keep moving to the refueling point
							else {
								goal = closestF;
								return MoveToGoal(goal,view);
							}
						}
					}
					goal = closestStation;
					return MoveToGoal(goal,view);
					
				
			}
			// When the agent is holding maximum capacity of waste
			else if(getWasteLevel() == getWasteCapacity()) {
				// there is a well in memory
				if(!well.isEmpty()) {
					Pair closestWell=FindClosestObject(well), closestF=FindClosestObject(fuelPump,closestWell);
					int distanceW =CalculateDistance(closestWell), distanceF =CalculateDistance(closestWell,closestF);
					// if not enough to reach the well and then to fuelPump, finding the closest FuelPump
					if((distanceW+distanceF)*2  +FUEL_BIAS > getFuelLevel() && getFuelLevel()!= MAX_FUEL) {
						goal = FindClosestObject(fuelPump);
						return MoveToGoal(goal,view);
						
					}
					// if got enough fuel just straight go to the Well
					else {
						goal = closestWell;
						return MoveToGoal(goal,view);
					}
					
				}
				// if no memory about the well then explore
				else {
					return Explore();
				}
				
			}
			else {
				// if there is well in the memory
				if(!well.isEmpty()) {
					Pair closestWell=FindClosestObject(well),closestFW=FindClosestObject(fuelPump,closestWell) ;
					int distanceW =CalculateDistance(closestWell), distanceFW =CalculateDistance(closestWell,closestFW);
					
					Pair closestStation= FindClosestObject(individualTask) ,closestFS = FindClosestObject(fuelPump,closestStation);
					int distanceS =CalculateDistance(closestStation), distanceFS =CalculateDistance(closestStation,closestFS);
					
					// if the agent can still contain waste from the nearest station
					if(((Station)closestStation.c).getTask().getWasteAmount() + getWasteLevel() <= getWasteCapacity() && isNotWell() ) {
						
						// if the agent doesn't has enough Fuel to reach that that station
						if((distanceS+distanceFS)*2+FUEL_BIAS > getFuelLevel() ) {
							// if the agent fuel level is not full or not at the fuel station
							if(getFuelLevel() < MAX_FUEL) {
								// if the agent has enough fuel to reach the nearest well and then to the fuel station and the current waste level is more than 300
								if((distanceW+distanceFW)*2  + FUEL_BIAS < getFuelLevel() && getWasteLevel() > getWasteCapacity()*0.3) {
									goal = closestWell;
									return MoveToGoal(goal,view);
								}
								else if(getFuelLevel()  < MAX_FUEL/2 ) {
									goal=fuelStation;
									return MoveToGoal(goal,view);
								}
								else {
									goal = closestFS;
									return MoveToGoal(goal,view);
								}
							}
							// if the agent fuel is full
							else {
								// and if the agent is at the fuel point then remove
								if(closestFS.c.getPoint().equals(getCurrentCell(view).getPoint())){
									stationTask.remove(closestStation);
									return Explore();
								}
								// else head to the fuel station
								else {
									goal = closestFS;
									return MoveToGoal(goal,view);
								}
							}
						}
						// if has enough fuel to reach the closest station, just go to it
						else {
							goal= closestStation;
							return MoveToGoal(goal,view);
						}
					}
					// if the agent couldn't take anymore waste from the nearest station, then go to the nearest well
					else {
						// if the agent doesn't have enough fuel to go to the nearest well and then to the nearest fuel station, return to the fuel station
						if((distanceW+distanceFW)*2  + FUEL_BIAS > getFuelLevel() && getFuelLevel()!= MAX_FUEL) {
							goal =fuelStation;
							return MoveToGoal(goal,view);
						}
						// else just go to the closest well
						else {
							goal = closestWell;
							return MoveToGoal(goal,view);
						}	
					}
				}
				return Explore();
			}
			
		}
		
		//if no more station with task
		else {
			// if the agent is currently holding waste
			if(getWasteLevel() != 0 ) {
				// there is a well in memory
				if(!well.isEmpty()) {
					Pair closestWell=FindClosestObject(well), closestF=FindClosestObject(fuelPump,closestWell);
					int distanceW =CalculateDistance(closestWell), distanceF =CalculateDistance(closestWell,closestF);
					// if not enough to reach the well and then to fuelPump, finding the closest FuelPump
					if((distanceW+distanceF)*2  + FUEL_BIAS > getFuelLevel() && getFuelLevel()!= MAX_FUEL) {
						goal = FindClosestObject(fuelPump);
						return MoveToGoal(goal,view);
						
					}
					// if got enough fuel to the well, just straight go to the Well
					else {
						goal = closestWell;
						return MoveToGoal(goal,view);
					}
				}
			}
			return Explore();
		}
		
	}

}

class Pair{
	Cell c;
	int index;
	int x;
	int y;
	Pair(int index,int x,int y){
		this.index =index;
		this.x =x;
		this.y= y;
	}
	
	Pair(Cell c, int x, int y){
		if(c instanceof FuelPump) {
			this.c = (FuelPump)c;
		}
		else if(c instanceof Well) {
			this.c = (Well)c;
		}
		else if(c instanceof Station) {
			this.c = (Station)c;
		}
		this.x = x;
		this.y =y;
	}
	
	public int getx() {
		return this.x;
	}
	public int gety() {
		return this.y;
	}
	@Override
	public boolean equals(Object o) {
		if(!(o instanceof Pair)){
			return false;
		}
		else {
			Pair a = (Pair)o;
			if(this.x == a.getx() && this.y == a.gety()) {
			return true;
			}
			else {
				return false;
			}
		}
	}
}




