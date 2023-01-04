/**
 * 
 */
package fr.ciadlab.labmanager.repository.project;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.ciadlab.labmanager.entities.project.Project;

/**
 * @author baptiste
 *
 */
public interface ProjectRepository extends JpaRepository<Project, Integer> {

	/**
	 * 
	 * @param name the name to search for, with case insensitive test.
	 * @return the set of projects with the given name.
	 */
	List<Project> findAllByNameIgnoreCase(String name);
}
