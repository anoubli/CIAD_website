package fr.ciadlab.labmanager.entities.project;

public enum TRLGrade {
	
	UN(1),
	DEUX(2),
	TROIS(3),
	QUATRE(4),
	CINQ(5),
	SIX(6),
	SEPT(7),
	HUIT(8),
	NEUF(9);
	
	private int value;

	  private TRLGrade(int value) {
	    this.value = value;
	  }

	  public int getValue() {
	    return this.value;
	  }
	  
	  public String toString() {
		  return Integer.toString(this.value);
	  }
}
