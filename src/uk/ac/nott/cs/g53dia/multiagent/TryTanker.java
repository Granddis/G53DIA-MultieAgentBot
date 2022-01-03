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
public class TryTanker extends Tanker {
	int index;
	int x=0,y=0;
	int counter;
	Pair goal = null, actualGoal= null;
	static ArrayList<Pair> goals = new ArrayList<Pair>();
	static ArrayList<Pair> stationTask = new ArrayList<Pair>();
	ArrayList<Pair> stationT; 
	static ArrayList<Pair> fuelPump = new ArrayList<Pair>();
	static ArrayList<Pair> well = new ArrayList<Pair>();
	
	
	
	
	public TryTanker() {
		this(new Random(),0);
	}
	
	/**
	 * The tanker implementation makes random moves. For reproducibility, it
	 * can share the same random number generator as the environment.
	 * 
	 * @param r
	 *            The random number generator.
	 */
	public TryTanker(Random r, int i) {
		this.r = r;
		index =i;
		counter = i%7;
	}
	
	// update the position of the agent
	public void updatePosition(int dir) {
			switch (dir) {
			case 0:
				y++;
				break;
			case 1:
				y--;
				break;
			case 2:
				x++;
				break;
			case 3:
				x--;
				break;
			case 4:
				x++;
				y++;
				break;
			case 5:
				x--;
				y++;
				break;
			case 6:
				x++;
				y--;
				break;
			case 7:
				x--;
				y--;
				break;
			
		}
	}
	
	
	// record every item seen by the agent
	public void RecordItem(Cell[][] view) {
		for(int i=0; i < view.length ; i++) {
			for(int j=0 ; j < view[0].length; j++) {
				if(view[i][j] instanceof Station) {
					Station s = (Station)view[i][j];
					if(s.getTask() != null) {
						if(!stationTask.contains(new Pair(s, i-20+x,20-j+y)) ) {
								stationTask.add(new Pair(s, i-20+x,20-j+y));
						}
					}
				}
				if(view[i][j] instanceof FuelPump) {
					if(!fuelPump.contains(new Pair(view[i][j], i-20+x,20-j+y) ) )  {
						fuelPump.add(  new Pair(view[i][j], i-20+x,20-j+y) );
					}
				}
				if(view[i][j] instanceof Well) {
					if(!well.contains(new Pair(view[i][j], i-20+x,20-j+y) ) )  {
						well.add(new Pair(view[i][j], i-20+x,20-j+y) );
					}
				}
			}
		}
		
		
	}
		
	
	public Action MoveToGoal(Pair goal,Cell[][] view) {
		int dir=-1;
		if(index==0)
		System.out.println("Tanker "+index+" heading from x:"+x+"y:"+y +"to x:"+goal.getx()+"y"+goal.gety() + "Goal type"+goal.c);
		int dx =goal.getx() - this.x , dy = goal.gety()- this.y;
		if(goal.c.getPoint().equals(getPosition())) {

			
		}
		//up
		else if(dy >0 && dx == 0) {
			dir =0;
		}
		//down
		else if(dy<0 && dx == 0) {
			dir =1;
		}
		//right
		else if(dx >0 && dy ==0) {
			dir =2;
		}
		//left
		else if(dx < 0 && dy ==0 ) {
			dir =3;
		}
		else if (dx > 0 && dy > 0 ) {
			dir = 4;
		}
		else if (dx < 0 && dy > 0) {
			dir =5;
		}
		else if(dx > 0 && dy < 0) {
			dir =6;
		}
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
			int dis = Math.max( Math.abs(z.getx()-x) , Math.abs(z.gety()-y ) );
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
		return Math.max( Math.abs(o.getx() - x) , Math.abs(o.gety() - y ) );
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
	

	/*
	 * The following is a simple demonstration of how to write a tanker. The
	 * code below is very stupid and simply moves the tanker randomly until the
	 * fuel tank is half full, at which point it returns to a fuel pump.
	 */
	public Action senseAndAct(Cell[][] view, boolean actionFailed, long timestep) {
		stationT = new ArrayList<Pair>();
		for(Pair s:stationTask) {
			stationT.add(s);
		}
		if(goals != null) {
			if(goals.contains(actualGoal)) {
				goals.remove(actualGoal);
			}
			for(Pair s:goals) {
				if(stationT.contains(s)) {
					stationT.remove(s);
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
		// if the current position is a Station and the station has waste then collect waste
		if((getCurrentCell(view)) instanceof Station) {
			// if station doesn't have a Task just random move
			if(((Station)getCurrentCell(view)).getTask() != null && getWasteLevel() < getWasteCapacity()) {
				goal =null;
				stationTask.remove(new Pair(((Station)getCurrentCell(view)),x,y));
				return new LoadWasteAction(((Station)getCurrentCell(view)).getTask());
			}
			
		}
		// if the current position is a FuelPump then refuel and always change the random move direction
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
		if(CalculateDistance(fuelStation)*2 +6 > getFuelLevel()) {
			goal = fuelStation;
			return MoveToGoal(goal,view);
		}
		
		// there are station with task
		if(!stationT.isEmpty()) {
			// if current waste level is 0 , go to the nearest station with Task
			if(getWasteLevel() == 0) {
					Pair closestStation= FindClosestObject(stationT), closestF= FindClosestObject(fuelPump,closestStation);
					int distanceS =CalculateDistance(closestStation), distanceF =CalculateDistance(closestStation,closestF);
					goals.add(closestStation);
					actualGoal = closestStation;
					// if not enough to reach the Station and then to fuelPump
					if((distanceS+distanceF)*2+6 > getFuelLevel()) {
						// if the agent fuel is not full or is not at a fuel station
						if(getFuelLevel() != 200) {
							// if there is not enough fuel to reach the refueling point that is closer to the station
								goal = closestF;
								return MoveToGoal(goal,view);
							
						}
						// if the agent fuel is full or is at a fuel station
						else {
							// if the agent is at the refueling point closer to the station
							if(closestF.c.getPoint().equals(getCurrentCell(view).getPoint())){
								System.out.println("Tanker"+index+"hihi");
								stationTask.remove(closestStation);
								return Explore();
							}
							// if not keep moving to the refueling point
							else {
								System.out.println("Tanker "+index + "how");
								goal = closestF;
								return MoveToGoal(goal,view);
							}
						}
					}
					goal = closestStation;
					return MoveToGoal(goal,view);
					
				
			}
			// When the agent is holding maximum capacity of waste
			else if(getWasteLevel() == 1000) {
				// there is a well in memory
				if(!well.isEmpty()) {
					Pair closestWell=FindClosestObject(well), closestF=FindClosestObject(fuelPump,closestWell);
					int distanceW =CalculateDistance(closestWell), distanceF =CalculateDistance(closestWell,closestF);
					// if not enough to reach the well and then to fuelPump, finding the closest FuelPump
					if((distanceW+distanceF)*2  +6 > getFuelLevel() && getFuelLevel()!= 200) {
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
					
					Pair closestStation= FindClosestObject(stationT) ,closestFS = FindClosestObject(fuelPump,closestStation);
					int distanceS =CalculateDistance(closestStation), distanceFS =CalculateDistance(closestStation,closestFS);
					
					// if the agent can still contain waste from the nearest station
					if(((Station)closestStation.c).getTask().getWasteAmount() + getWasteLevel() <= getWasteCapacity() && isNotWell() ) {
						
						// if the agent doesn't has enough Fuel to reach that that station
						if((distanceS+distanceFS)*2+6 > getFuelLevel() ) {
							// if the agent fuel level is not full or not at the fuel station
							if(getFuelLevel() != 200) {
								// if the agent has enough fuel to reach the nearest well and then to the fuel station and the current waste level is more than 300
								if((distanceW+distanceFW)*2  + 6 < getFuelLevel() && getWasteLevel() > 300) {
									goal = closestWell;
									return MoveToGoal(goal,view);
								}
								// if the agent dont have enough fuel to reach the other fueling point or the well
								else {
									goals.add(closestStation);
									actualGoal = closestStation;
									goal = closestFS;
									return MoveToGoal(goal,view);
								}
							}
							// if the agent fuel is full
							else {
								// and if the agent is at the fuel point then remove
								if(closestFS.c.getPoint().equals(getCurrentCell(view).getPoint())){
									System.out.println("Tanker"+index+"hihi");
									stationTask.remove(closestStation);
									return Explore();
								}
								// else head to the fuel station
								else {
									System.out.println("Tanker "+index + "why");
									goal = closestFS;
									return MoveToGoal(goal,view);
								}
							}
						}
						// if has enough fuel to reach the closest station, just go to it
						else {
							goals.add(closestStation);
							actualGoal = closestStation;
							goal= closestStation;
							return MoveToGoal(goal,view);
						}
					}
					// if the agent couldn't take anymore waste from the nearest station, then go to the nearest well
					else {
						// if the agent doesn't have enough fuel to go to the nearest well and then to the nearest fuel station, return to the fuel station
						if((distanceW+distanceFW)*2  + 6 > getFuelLevel() && getFuelLevel()!= 200) {
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
					if((distanceW+distanceF)*2  + 6 > getFuelLevel() && getFuelLevel()!= 200) {
						goal = FindClosestObject(fuelPump);
						return MoveToGoal(goal,view);
						
					}
					// if got enough fuel just straight go to the Well
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

//class Pair{
//	Cell c;
//	int x;
//	int y;
//	Pair(Cell c, int x, int y){
//		if(c instanceof FuelPump) {
//			this.c = (FuelPump)c;
//		}
//		else if(c instanceof Well) {
//			this.c = (Well)c;
//		}
//		else if(c instanceof Station) {
//			this.c = (Station)c;
//		}
//		this.x = x;
//		this.y =y;
//	}
//	
//	public int getx() {
//		return this.x;
//	}
//	public int gety() {
//		return this.y;
//	}
//	@Override
//	public boolean equals(Object o) {
//		if(!(o instanceof Pair)){
//			return false;
//		}
//		else {
//			Pair a = (Pair)o;
//			if(this.x == a.getx() && this.y == a.gety()) {
//			return true;
//			}
//			else {
//				return false;
//			}
//		}
//	}
//}