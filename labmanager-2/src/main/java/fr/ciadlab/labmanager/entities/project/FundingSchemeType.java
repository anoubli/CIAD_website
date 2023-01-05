package fr.ciadlab.labmanager.entities.project;

/**
 * 
 * @author baptiste
 *
 */
public enum FundingSchemeType {
	H2020 {
		@Override 
		public String toString() {
			return "H2020";
		}
	},
	
	HorizonEurope {
		@Override 
		public String toString() {
			return "Horizon Europe";
		}
	},
	
	EDIH {
		@Override 
		public String toString() {
			return "EDIH";
		}
	},
	
	JPI_EU {
		@Override 
		public String toString() {
			return "JPI-EU";
		}
	},
	
	FITEC {
		@Override 
		public String toString() {
			return "FITEC";
		}
	},
	PHC {
		@Override 
		public String toString() {
			return "PHC";
		}
	},
	
	COST_ACTION {
		@Override 
		public String toString() {
			return "COST ACTION";
		}
	},
	
	ANR {
		@Override 
		public String toString() {
			return "ANR";
		}
	},
	FEDER {
		@Override 
		public String toString() {
			return "FEDER";
		}
	},
	
	CPER {
		@Override 
		public String toString() {
			return "CPER";
		}
	},
	
	I_SITE {
		@Override 
		public String toString() {
			return "I-SITE";
		}
	},
	
	PIC {
		@Override 
		public String toString() {
			return "PIC";
		}
	},

	CARNOT {
		@Override 
		public String toString() {
			return "CARNOT";
		}
	},
	
	RBFC {
		@Override 
		public String toString() {
			return "RBFC";
		}
	},
	
	SUPERVISORY_UNIVERSITY {
		@Override 
		public String toString() {
			return "Université de tutelle";
		}
	},

	CIFRE {
		@Override 
		public String toString() {
			return "CIFRE";
		}
	},
	

	BPI{
		@Override 
		public String toString() {
			return "BPI";
		}
	},

	EURÉKA {
		@Override 
		public String toString() {
			return "Euréka";
		}
	},

	COMPANY_SERVICE {
		@Override 
		public String toString() {
			return "Prestation entreprise";
		}
	},

	RESEARCH_CONTRACT {
		@Override 
		public String toString() {
			return "Contrat de recherche";
		}
	},

	OTHER_INTERNATIONAL {
		@Override 
		public String toString() {
			return "Autre financement international";
		}
	},

	OTHER_NATIONAL {
		@Override 
		public String toString() {
			return "Autre financement national";
		}
	},
}
