package fr.ciadlab.labmanager.entities.project;

/**
 * 
 * @author $Author: bperrat-dit-janton$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public enum ProjectType {
	
	THEORICAL_RESEARCH {
		@Override
		public String toString() {
			return "Recherche théorique";
		}
	},

	APPLIED_RESEARCH {
		@Override
		public String toString() {
			return "Recherche appliqué";
		}
	},

	EXPERIMENTAL_DEVELOPMENT {
		@Override
		public String toString() {
			return "Développement expérimental";
		}
	},
}
