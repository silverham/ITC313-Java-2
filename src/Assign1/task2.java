package Assign1;
/*	Joshua Graham
	 *  ID: 11490893
	 *  ITC-313
	 * 	Date:26/08/2013
	 *  Assignment 1
	 *  Task 2
	 */
class rectangle{
		private int base;
		private int height;

		public rectangle(){	//no-arg constructor 
			base = 0;
			height = 0;
		}
	void setValues(int base, int height){ //Set the person's first name, accessing the private fields
		this.height = height;
		this.base = base;
		}

	int calcArea(){ //Display the area
		int area = this.height * this.base;
		return area;
		}
}
class square 
	extends rectangle{
	//overload setValues in rectangle
	void setValues(int side){ 
		super.setValues(side, side); //side is used twice as the sides are the same.
		}
}

public class task2 {

	public static void main(String[] args) throws Exception {
		System.out.println("Task2 - Part1:");
		rectangle myRectangle = new rectangle(); //create rectangle
		myRectangle.setValues(8, 6);
		System.out.println("The Area of myrectange is:" + myRectangle.calcArea());
		System.out.println("Task2 - Part2:");
		square mySquare = new square(); //create square
		mySquare.setValues(8);
		System.out.println("The Area of mysquare is:" + mySquare.calcArea());

	}

}
