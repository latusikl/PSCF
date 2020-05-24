package pl.polsl.pscfdemo.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OutputBrokerDto {

	@JsonProperty("dawka")
	Double dose;

	@JsonProperty("bPompa1")
	Boolean pumpOneState;

	@JsonProperty("bPompa2")
	Boolean pumpTwoState;

	@JsonProperty("bAwaria")
	Boolean accident = false;

	@JsonProperty("bStop")
	Boolean emergencyStop = false;

}
