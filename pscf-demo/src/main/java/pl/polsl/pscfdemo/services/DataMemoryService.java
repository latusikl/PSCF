package pl.polsl.pscfdemo.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.polsl.pscfdemo.dto.InputBrokerDto;

import java.time.Instant;
import java.util.LinkedList;

/**
 * Example service responsible for storing received measurements.
 * If list size is less than max size it only adds to the end of list.
 * If list size is equal to max size. It removes the oldest object from top of lists and adds new one at the end.
 * During adding timestamp is added to element.
 */
@Service
@Slf4j
public class DataMemoryService {

	protected static Integer MAX_LIST_SIZE = 60;

	protected static Integer currentListSize = 0;

	protected static LinkedList<InputBrokerDto> measurementList = new LinkedList<>();

	public void addMeasurement(final InputBrokerDto newMeasurement) {
		newMeasurement.setTimestamp(Instant.now());
		if (MAX_LIST_SIZE > currentListSize) {
			measurementList.add(newMeasurement);
			currentListSize++;
			log.info("New object added. List size: {}", currentListSize);
		} else {
			measurementList.removeFirst();
			measurementList.add(newMeasurement);
			log.info("New object added. List full removing the oldest.");
		}

	}

	public LinkedList<InputBrokerDto> getAllMeasurements() {
		return measurementList;
	}

}
