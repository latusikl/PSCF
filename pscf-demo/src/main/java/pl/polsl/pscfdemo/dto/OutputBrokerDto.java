package pl.polsl.pscfdemo.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class OutputBrokerDto {

	@JsonProperty("dawka")
	private Double dose;

	@JsonProperty("bPompa1")
	private Boolean pumpOneState;

	@JsonProperty("bPompa2")
	private Boolean pumpTwoState;

	@JsonProperty("bAwaria")
	private Boolean accident;

	@JsonProperty("bStop")
	private Boolean emergencyStop;

}
