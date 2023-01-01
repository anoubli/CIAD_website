package fr.ciadlab.labmanager.repository.conference;

import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.ciadlab.labmanager.entities.conference.Conference;



public interface ConferenceRepository extends JpaRepository<Conference, Integer> {
	/** Find the conference by their name.
	 *
	 * @param name the name of the conference to search for.
	 * @return the conferences with the given name.
	 */
	Set<Conference> findDistinctByConferenceName(String name);

	/** Find a conference by their name.
	 *
	 * @param name the name of the conference to search for.
	 * @return the conference with the given name.
	 */
	Optional<Conference> findByConferenceName(String name); 
}
