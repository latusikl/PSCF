package pl.polsl.demodatasender;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class InputBrokerDto {

	@JsonProperty("pH")
	Integer phValue;

	@JsonProperty("temp")
	Double temperature;

	@JsonProperty("proc")
	Double percentageOfChemicals;

	@JsonProperty("dawka")
	Double dose;

	@JsonProperty("bOdwroconaOsmoza")
	Boolean reverseOsmosis;

	@JsonProperty("bFiltrWeglowyOk")
	Boolean carbonFilter;

	@JsonProperty("bFiltrZwirowyOk")
	Boolean gravelFilter;

	@JsonProperty("bPompa1")
	Boolean pumpOneState;

	@JsonProperty("bPompa2")
	Boolean pumpTwoState;

	@JsonProperty("bAwaria")
	Boolean accident;

	@JsonIgnore
	Instant timestamp;

}
