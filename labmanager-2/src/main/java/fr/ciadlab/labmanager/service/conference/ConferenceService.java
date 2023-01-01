package fr.ciadlab.labmanager.service.conference;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import fr.ciadlab.labmanager.entities.conference.ConferenceQualityAnnualIndicators;
import fr.ciadlab.labmanager.entities.journal.Journal;
import fr.ciadlab.labmanager.entities.journal.JournalQualityAnnualIndicators;
import fr.ciadlab.labmanager.utils.ranking.CoreRanking;
import fr.ciadlab.labmanager.utils.ranking.QuartileRanking;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Service;

import com.google.common.base.Strings;

import fr.ciadlab.labmanager.configuration.Constants;
import fr.ciadlab.labmanager.entities.conference.Conference;
import fr.ciadlab.labmanager.repository.conference.ConferenceQualityAnnualIndicatorsRepository;
import fr.ciadlab.labmanager.repository.conference.ConferenceRepository;
import fr.ciadlab.labmanager.service.AbstractService;
import fr.ciadlab.labmanager.utils.net.NetConnection;

@Service 
public class ConferenceService extends AbstractService{
	

	public static final String URL_PREFIX = "";
	
	private final ConferenceRepository conferenceRepository;
	
	private final ConferenceQualityAnnualIndicatorsRepository indicatorRepository;
	
	private final NetConnection netConnection;  
	
	public ConferenceService(
			@Autowired MessageSourceAccessor messages,
			@Autowired Constants constants,
			@Autowired ConferenceRepository conferenceRepository, 
			@Autowired ConferenceQualityAnnualIndicatorsRepository indicatorRepository,
			@Autowired NetConnection netConnection) {
		super(messages, constants);
		this.conferenceRepository = conferenceRepository;
		this.indicatorRepository = indicatorRepository;
		this.netConnection = netConnection;
	}
	
	public List<Conference> getAllConferences(){
		return this.conferenceRepository.findAll();		
	}
	
	public Conference getConferenceById(int identifier) {
		final Optional<Conference> res = this.conferenceRepository.findById(Integer.valueOf(identifier));
		if (res.isPresent()) {
			return res.get();
		}
		return null;
	}
	
	public Conference getConferenceByName(String name) {
		final Set<Conference> res = getConferencesByName(name);
		if (res.isEmpty()) {
			return null;
		}
		return res.iterator().next();
	}
	
	public Set<Conference> getConferencesByName(String name) {
		return this.conferenceRepository.findDistinctByConferenceName(name);
	}
	
	public int getConferenceIdByName(String name) {
		final Set<Conference> res = this.conferenceRepository.findDistinctByConferenceName(name);
		if (!res.isEmpty()) {
			final Conference Conference = res.iterator().next();
			if (Conference != null) {
				return Conference.getId();
			}
		}
		return 0;
	}
	
	public Conference createConference(String name, String acronym,String conferenceUrl, String publisher, String issn, String coreIdentifier) {
		final Conference res = new Conference();
		res.setName(name);
		res.setAcronym(acronym);
		res.setConferenceUrl(conferenceUrl);
		res.setPublisher(publisher);
		res.setIssn(issn);
		res.setCoreIdentifier(coreIdentifier);
		this.conferenceRepository.save(res);
		return res;
	}
	
	public void removeConference(int identifier) {
		final Integer id = Integer.valueOf(identifier);
		final Optional<Conference> conferenceRef = this.conferenceRepository.findById(id);
		if (conferenceRef.isPresent()) {
			this.conferenceRepository.deleteById(id);
		}
	}
	
	public Conference updateConference(int identifier, String name, String acronym,String conferenceUrl, String publisher, String issn, String coreIdentifier) {
		final Optional<Conference> res = this.conferenceRepository.findById(Integer.valueOf(identifier));
		if (res.isPresent()) {
			final Conference conference = res.get();
			if (conference != null) {
				if (!Strings.isNullOrEmpty(name)) {
					conference.setName(name);
				}
				conference.setName(Strings.emptyToNull(name));
				conference.setPublisher(Strings.emptyToNull(publisher));
				conference.setIssn(Strings.emptyToNull(issn));
				conference.setAcronym(Strings.emptyToNull(acronym));
				conference.setConferenceUrl(Strings.emptyToNull(conferenceUrl));
				conference.setCoreIdentifier(Strings.emptyToNull(coreIdentifier));
				this.conferenceRepository.save(conference);
				return conference; 
			}
		}
		return null;
		
	}

	public ConferenceQualityAnnualIndicators setQualityIndicators(Conference conference, int year, CoreRanking ranking) {
		final ConferenceQualityAnnualIndicators indicators = conference.setQualityIndicatorsByYear(year, ranking);
		this.indicatorRepository.save(indicators);
		this.conferenceRepository.save(conference);
		return indicators;
	}

	public void deleteQualityIndicators(Conference conference, int year) {
		final ConferenceQualityAnnualIndicators indicators = conference.getQualityIndicators().remove(Integer.valueOf(year));
		if (indicators != null) {
			this.indicatorRepository.delete(indicators);
			this.conferenceRepository.save(conference);
		}
	}
	
	
	
	
	
	

}
